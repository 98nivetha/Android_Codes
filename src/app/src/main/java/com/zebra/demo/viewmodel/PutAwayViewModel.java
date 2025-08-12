package com.zebra.demo.viewmodel;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.repository.IBA_REPOSITORY.PutAwayRepository;
import com.zebra.demo.data.remote.repository.IBA_REPOSITORY.RockWiseInfoRepository;
import com.zebra.demo.data.remote.requestmodels.PutAwayScanningRequest;
import com.zebra.demo.data.remote.requestmodels.StockInwardRequest;
import com.zebra.demo.data.remote.responsemodels.DisplayItem;
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayInsertResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayScanningResponse;
import com.zebra.demo.data.remote.responsemodels.RackDetails;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsValidInfoResponse;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.listener.ApiResponseListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PutAwayViewModel extends ViewModel {
    private int currentQuantity = 0;
    public MutableLiveData<List<RackDetails>> rackDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    public MutableLiveData<String> selectedRackDetail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showClearButton = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> showSubmitButton = new MutableLiveData<>(false);

    public LiveData<Boolean> getShowClearButton() {
        return showClearButton;
    }

    public LiveData<Boolean> getShowSubmitButton() {
        return showSubmitButton;
    }

    private MutableLiveData<Integer> quantityLiveData = new MutableLiveData<>();
    public RackDetails selectedRack;
    public final MutableLiveData<RackDetails> selectedRackLiveData = new MutableLiveData<>();


    public void setQuantity(int quantity) {
        this.currentQuantity = quantity;
        quantityLiveData.setValue(quantity);
    }
    private Integer loggedInUserId;

    public void setLoggedInUserId(Integer id) {
        this.loggedInUserId = id;
    }

    public Integer getLoggedInUserId() {
        return loggedInUserId;
    }

    public LiveData<Integer> getQuantity() {
        return quantityLiveData;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void fetchMetaDetails(ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) {
            return;
        }
        isDataLoading.setValue(true);
        RockWiseInfoRepository rockWiseInfoRepository = RockWiseInfoRepository.getInstance();
        rockWiseInfoRepository.getAllRocks("", new ResponseListener<GetAllRackDatasResponse>() {
            @Override
            public void onDataReceived(GetAllRackDatasResponse response) {
                isDataLoading.setValue(false);
                if (response != null && response.Data != null && response.Data.D_RackDetails != null) {
                    List<RackDetails> rackDetailsList = response.Data.D_RackDetails;
                    for (RackDetails rack : rackDetailsList) {
                        Log.d("RACK_ITEM", "Rack Name: " + rack.getRackcode());
                    }

                    rackDetailsDDListLiveData.setValue(rackDetailsList);
                } else {
                }
            }

            @Override
            public void onError(String e) {
                isDataLoading.setValue(false);
                Log.e("fetchRackDataList", "API Error: " + e);
            }

            @Override
            public void onFailure(Throwable t) {
                isDataLoading.setValue(false);
                Log.e("fetchRackDataList", "API Failure: " + t.getMessage(), t);
            }
        });

    }

    public void sendPutAwayScanning(PutAwayScanningRequest request, ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null || request == null) {
            return;
        }
        isDataLoading.setValue(true);
        PutAwayRepository putAwayRepository = PutAwayRepository.getInstance();
        putAwayRepository.scanPutAwayPockets(request, new ResponseListener<List<DisplayItem>>() {
            @Override
            public void onDataReceived(List<DisplayItem> data) {
                isDataLoading.setValue(false);
                Log.d("API_PUT_AWAY", "onDataReceived: " + new Gson().toJson(data));
                showClearButton.setValue(true);
                showSubmitButton.setValue(true);
                apiResponseListener.onSuccess(data);
            }

            @Override
            public void onError(String e) {
                isDataLoading.setValue(false);
                apiResponseListener.onFailed(e);
            }

            @Override
            public void onFailure(Throwable t) {
                isDataLoading.setValue(false);
                apiResponseListener.onFailed(t);
            }
        });
    }

    public void insertPutAwayScanningData(List<DisplayItem> scannedItems, ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) return;
        isDataLoading.setValue(true);
        Set<String> seenRfids = new HashSet<>();
        List<StockInwardRequest.IbaIvmInvStockinwardPackDetail> detailList = new ArrayList<>();
        for (DisplayItem item : scannedItems) {
            PutAwayScanningResponse.MaterialItem m = item.getMaterialItem();
            if (m == null || m.getRfid() == null) continue;
            if (!seenRfids.add(m.getRfid())) {
                Log.w("RFID_DUPLICATE", "Duplicate RFID skipped: " + m.getRfid());
                continue;
            }

            StockInwardRequest.IbaIvmInvStockinwardPackDetail detail = new StockInwardRequest.IbaIvmInvStockinwardPackDetail();
            detail.setCreatedon(String.valueOf(LocalDateTime.now()));
            detail.setPackid(m.getPackid());
            detail.setDetailid(m.getDetailid());
            detail.setInwardid(m.getInwardid());
            detail.setModelid(m.getModelid());
            detail.setMaterialid(m.getMaterialid());
            detail.setMaterialname(m.getMaterialname());
            detail.setQuantity(m.getQuantity());
            detail.setRfid(m.getRfid());
            detail.setStatus(m.getStatus());
            detail.setModifiedon(m.getModifiedon());
            detail.setRackid(m.getRackid());
            Integer userId = getLoggedInUserId();
            detail.setCreateby(userId != null ? userId : 0);
            detail.setModifiedby(userId != null ? userId : 0);

            detailList.add(detail);
        }
        if (detailList.isEmpty()) {
            isDataLoading.setValue(false);
            return;
        }
        StockInwardRequest request = new StockInwardRequest(detailList);
        PutAwayRepository.getInstance().insertPutAway(request, new ResponseListener<PutAwayInsertResponse>() {
            @Override
            public void onDataReceived(PutAwayInsertResponse root) {
                isDataLoading.setValue(false);
                apiResponseListener.onSuccess(root);
            }
            @Override
            public void onError(String e) {
                isDataLoading.setValue(false);
                apiResponseListener.onFailed(e);
            }
            @Override
            public void onFailure(Throwable t) {
                isDataLoading.setValue(false);
                apiResponseListener.onFailed(t.getMessage());
            }
        });
    }

    public void insertPutAwayScanningDataForReallocate(List<ReturnPartsValidInfoResponse.ValidItem> scannedItems, ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) return;
        isDataLoading.setValue(true);
        List<StockInwardRequest.IbaIvmInvStockinwardPackDetail> detailList = new ArrayList<>();
        for (ReturnPartsValidInfoResponse.ValidItem item : scannedItems) {
            StockInwardRequest.IbaIvmInvStockinwardPackDetail detail = new StockInwardRequest.IbaIvmInvStockinwardPackDetail();
            detail.setPackid(item.getPackid());
            detail.setDetailid(item.getDetailid());
            detail.setInwardid(item.getInwardid());
            detail.setModelid(item.getModelid());
            detail.setModelname("MODEL-1998");
            detail.setMaterialid(item.getMaterialid());
            detail.setMaterialname(item.getMaterialname());
            detail.setQuantity(getCurrentQuantity());
            detail.setRfid(item.getRfid());
            detail.setRackid(item.getRackid());
            detail.setStatus(item.getStatus());
            detail.setCreateby(1);
            detail.setModifiedby(1);
            detail.setCreatedon(Utility.getCurrentDateTimeString());
            detail.setModifiedon(Utility.getCurrentDateTimeString());
            detailList.add(detail);
        }
        if (detailList.isEmpty()) {
            isDataLoading.setValue(false);
            return;
        }
        StockInwardRequest request = new StockInwardRequest(detailList);
        request.setIbaIvmInvStockinwardPackDetail(detailList);
        PutAwayRepository.getInstance().insertPutAway(request, new ResponseListener<PutAwayInsertResponse>() {
            @Override
            public void onDataReceived(PutAwayInsertResponse response) {
                isDataLoading.setValue(false);
                apiResponseListener.onSuccess(response);
            }

            @Override
            public void onError(String e) {
                isDataLoading.setValue(false);
                apiResponseListener.onFailed(e);
            }

            @Override
            public void onFailure(Throwable t) {
                isDataLoading.setValue(false);
                apiResponseListener.onFailed(t.getMessage());
            }
        });
    }
}
