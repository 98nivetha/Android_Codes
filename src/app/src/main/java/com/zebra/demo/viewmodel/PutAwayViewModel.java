package com.zebra.demo.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.repository.IBA_REPOSITORY.PutAwayRepository;
import com.zebra.demo.data.remote.requestmodels.PutAwayScanningRequest;
import com.zebra.demo.data.remote.requestmodels.StockInwardRequest;
import com.zebra.demo.data.remote.responsemodels.DisplayItem;
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayInsertResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayScanningResponse;
import com.zebra.demo.data.remote.responsemodels.RackDetails;
import com.zebra.demo.view.listener.ApiResponseListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PutAwayViewModel extends ViewModel {
    public MutableLiveData<GetAllRackDatasResponse> getAllRackDetailMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<GetAllRackDatasResponse> metaDetailMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<RackDetails> selectedRackDetailObject = new MutableLiveData<>();

    public MutableLiveData<List<RackDetails>> rackDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    public MutableLiveData<String> selectedRackDetail = new MutableLiveData<>();
    public MutableLiveData<Integer> selectedRackID = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDataValid = new MutableLiveData<>(false); // default false
    public MutableLiveData<Boolean> clearInvalidButton = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showClearButton = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> showSubmitButton = new MutableLiveData<>(false);

    public LiveData<Boolean> getShowClearButton() {
        return showClearButton;
    }


    public LiveData<Boolean> getShowSubmitButton() {
        return showSubmitButton;
    }

    public void fetchMetaDetails(ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) {
            return;
        }

        isDataLoading.setValue(true);
        PutAwayRepository putAwayRepository = PutAwayRepository.getInstance();
        putAwayRepository.getRacks("", new ResponseListener<GetAllRackDatasResponse>() {
            @Override
            public void onDataReceived(GetAllRackDatasResponse response) {
                if (response != null && response.Data != null) {
                    List<RackDetails> rackList = response.Data.D_RackDetails;
                    if (rackList != null) {
                        rackDetailsDDListLiveData.postValue(rackList);
                    } else {
                        Log.d("RackDetails", "rackList is null");
                    }
                } else {
                    apiResponseListener.onFailed("No data found");
                }
                isDataLoading.setValue(false);
            }

            @Override
            public void onError(String e) {
                apiResponseListener.onFailed(e);
                isDataLoading.setValue(false);
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

            // Skip duplicate RFID
            if (seenRfids.contains(m.getRfid())) {
                Log.w("RFID_DUPLICATE", "Duplicate RFID skipped: " + m.getRfid());
                continue;
            }

            seenRfids.add(m.getRfid());

            StockInwardRequest.IbaIvmInvStockinwardPackDetail detail = new StockInwardRequest.IbaIvmInvStockinwardPackDetail();
            detail.setPackid(m.getPackid());
            detail.setDetailid(m.getDetailid());
            detail.setInwardid(m.getInwardid());
            detail.setModelid(m.getModelid());
            detail.setMaterialid(m.getMaterialid());
            detail.setMaterialname(m.getMaterialname());
            detail.setQuantity(m.getQuantity());
            detail.setRfid(m.getRfid());
            detail.setStatus(m.getStatus());
            detail.setCreatedon(m.getCreatedon());
            detail.setModifiedon(m.getModifiedon());
            detail.setRackid(m.getRackid());

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
}
