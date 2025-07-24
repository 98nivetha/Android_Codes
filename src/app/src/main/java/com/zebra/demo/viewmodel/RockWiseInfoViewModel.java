package com.zebra.demo.viewmodel;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.repository.IBA_REPOSITORY.RockWiseInfoRepository;
import com.zebra.demo.data.remote.responsemodels.GetAllMaterialInfoMainResponse;
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
import com.zebra.demo.data.remote.responsemodels.MaterialDetail;
import com.zebra.demo.data.remote.responsemodels.RackDetails;
import com.zebra.demo.view.listener.ApiResponseListener;
import java.util.ArrayList;
import java.util.List;

public class RockWiseInfoViewModel extends ViewModel {
    public final MutableLiveData<List<RackDetails>> rackDetailMutableLiveData = new MutableLiveData<>();
    public final MutableLiveData<RackDetails> selectedRackLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<MaterialDetail>> materialDetailsLiveData = new MutableLiveData<>();
    public MutableLiveData<String> rackDDValidationText = new MutableLiveData<>();
    public RackDetails selectedRack;
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    public LiveData<List<RackDetails>> getRackDetailLiveData() {
        return rackDetailMutableLiveData;
    }

    public LiveData<RackDetails> getSelectedRackLiveData() {
        return selectedRackLiveData;
    }

    public LiveData<List<MaterialDetail>> getMaterialDetailsLiveData() {
        return materialDetailsLiveData;
    }

    public void fetchRackDataList(ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) {
            return;
        }
        isDataLoading.setValue(true);
        String rawJson = "{ \"Rackcode\": \"\" }";
        Log.d("VIEW_MODEL", "getAllRocks Request JSON: " + rawJson);

        RockWiseInfoRepository rockWiseInfoRepository = RockWiseInfoRepository.getInstance();

        rockWiseInfoRepository.getAllRocks("", new ResponseListener<GetAllRackDatasResponse>() {
            @Override
            public void onDataReceived(GetAllRackDatasResponse response) {
                isDataLoading.setValue(false);
                if (response != null && response.Data != null && response.Data.D_RackDetails != null) {
                    List<RackDetails> rackDetailsList = response.Data.D_RackDetails;
                    for (RackDetails rack : rackDetailsList) {
                        Log.d("RACK_ITEM", "Rack Name: " + rack.getRackname());
                    }

                    rackDetailMutableLiveData.setValue(rackDetailsList);
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

    public void fetchAllMaterialsByRack(ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) {
            return;
        }

        if (selectedRack == null || selectedRack.getRackcode() == null) {
            Log.w("VIEW_MODEL", "No selected rack to fetch materials. Rack code: " +
                    (selectedRack != null ? selectedRack.getRackcode() : "null"));
            return;
        }


        isDataLoading.setValue(true);

        String qrCode = selectedRack.getRackcode();
        String rawJson = "{ \"Qrcode\": \"" + qrCode + "\" }";
        Log.d("VIEW_MODEL", "fetchAllMaterialsByRack JSON: " + rawJson);

        RockWiseInfoRepository rockWiseInfoRepository = RockWiseInfoRepository.getInstance();

        rockWiseInfoRepository.getAllMaterialDatas(qrCode, new ResponseListener<GetAllMaterialInfoMainResponse>() {
            @Override
            public void onDataReceived(GetAllMaterialInfoMainResponse response) {
                isDataLoading.setValue(false);

                if (response != null && response.Data != null) {
                    List<MaterialDetail> materials = response.Data.materialDetails;

                    if (materials != null && !materials.isEmpty()) {
                        Log.d("VIEW_MODEL", "Total Materials: " + materials.size());
                        for (MaterialDetail m : materials) {
                            Log.d("VIEW_MODEL", "Material: " + m.getMaterialname());
                        }

                        materialDetailsLiveData.setValue(materials);
                    } else {
                        Log.w("VIEW_MODEL", "Material list is empty or null");
                        materialDetailsLiveData.setValue(new ArrayList<>()); // clear old list
                    }
                } else {
                    Log.w("VIEW_MODEL", "Null response or data");
                    materialDetailsLiveData.setValue(new ArrayList<>()); // clear old list
                }
            }

            @Override
            public void onError(String e) {
                isDataLoading.setValue(false);
                Log.e("VIEW_MODEL", "API Error: " + e);
            }

            @Override
            public void onFailure(Throwable t) {
                isDataLoading.setValue(false);
                Log.e("VIEW_MODEL", "API Failure", t);
            }
        });

    }

}
