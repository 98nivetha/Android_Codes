package com.zebra.demo.viewmodel;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.repository.IBA_REPOSITORY.ReAllocateRepository;
import com.zebra.demo.data.remote.requestmodels.ReturnPartsValidInfoRequest;
import com.zebra.demo.data.remote.responsemodels.ReturnDefectiveTypeResponse;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsInsertUpdateResponse;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsValidInfoResponse;
import com.zebra.demo.view.listener.ApiResponseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReAllocateViewModel extends ViewModel {
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    public MutableLiveData<String> selectedMaterialDetail = new MutableLiveData<>();
    public MutableLiveData<Integer> selectedMaterialID = new MutableLiveData<>();
    public MutableLiveData<ReturnDefectiveTypeResponse.MaterialDetail> selectedMaterialDetailObject = new MutableLiveData<>();
    public MutableLiveData<List<ReturnDefectiveTypeResponse.MaterialDetail>> materialDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<ReturnDefectiveTypeResponse.RackDetail>> rackDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<String> selectedRackDetail = new MutableLiveData<>();
    public MutableLiveData<Integer> selectedRackID = new MutableLiveData<>();
    public MutableLiveData<ReturnDefectiveTypeResponse.MaterialDetail> selectedRackDetailObject = new MutableLiveData<>();

    public void metaDetailsForDefectiveDropdown(ApiResponseListener apiResponseListener) {
        isDataLoading.setValue(true);
        ReAllocateRepository reAllocateRepository = ReAllocateRepository.getInstance();
        reAllocateRepository.MetaDetailsForDefecriveParts(0, new ResponseListener<ReturnDefectiveTypeResponse>() {
            @Override
            public void onDataReceived(ReturnDefectiveTypeResponse response) {
                isDataLoading.setValue(false);
                if (response != null && response.getData() != null) {
                    List<ReturnDefectiveTypeResponse.MaterialDetail> materialList = response.getData().getMaterialDetails();
                    List<ReturnDefectiveTypeResponse.RackDetail> rackList = response.getData().getRackDetails();
                    if ((materialList != null && !materialList.isEmpty()) ||
                            (rackList != null && !rackList.isEmpty())) {

                        materialDetailsDDListLiveData.postValue(materialList != null ? materialList : new ArrayList<>());
                        rackDetailsDDListLiveData.postValue(rackList != null ? rackList : new ArrayList<>());
                        apiResponseListener.onSuccess(response);
                    } else {
                        apiResponseListener.onFailed("No data found");
                    }

                } else {
                    apiResponseListener.onFailed("No data found");
                }
                isDataLoading.setValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("metaDetailsForDefectiveDropdown", "Error: " + errorMessage);
                apiResponseListener.onFailed(errorMessage);
                isDataLoading.setValue(false);
            }

            @Override
            public void onFailure(Throwable t) {
                apiResponseListener.onFailed(t);
                isDataLoading.setValue(false);
            }
        });
    }

    public void checkValidPartsByRFIDForReallocate(ReturnPartsValidInfoRequest request, ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) {
            return;
        }
        isDataLoading.setValue(true);
        ReAllocateRepository reAllocateRepository = ReAllocateRepository.getInstance();
        Log.d("API_REQUEST_RAW", "Request: " + new Gson().toJson(request));
        reAllocateRepository.returnScan(request, new ResponseListener<ReturnPartsValidInfoResponse>() {
            @Override
            public void onDataReceived(ReturnPartsValidInfoResponse returnPartsValidInfoResponse) {
                isDataLoading.setValue(false);
                try {
                    Log.d("API_RESPONSE_RAW", "Response for ReAllocate: " + new Gson().toJson(returnPartsValidInfoResponse));
                    apiResponseListener.onSuccess(returnPartsValidInfoResponse);
                } catch (Exception e) {
                    Log.e("API_RESPONSE_ERROR", "Exception during response handling: " + e.getMessage());
                }
            }

            @Override
            public void onError(String e) {
                isDataLoading.setValue(false);
                Log.e("API_ERROR", "onError: " + e);
                apiResponseListener.onFailed(e);
            }

            @Override
            public void onFailure(Throwable t) {
                isDataLoading.setValue(false);
                Log.e("API_FAILURE", "onFailure: ", t);
                apiResponseListener.onFailed(t);
            }
        });
    }

    public void insertAndUpdateReturnStack(Map<String, Object> requestMap, ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) {
            return;
        }
        isDataLoading.setValue(true);
        ReAllocateRepository reAllocateRepository = ReAllocateRepository.getInstance();
        reAllocateRepository.insertUpdateRetunStock(requestMap, new ResponseListener<ReturnPartsInsertUpdateResponse>() {
            @Override
            public void onDataReceived(ReturnPartsInsertUpdateResponse response) {
                Log.e("PickListInsert", "Success: " + response);
                apiResponseListener.onFailed(response);
                isDataLoading.setValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("PickListInsert", "Error: " + errorMessage);
                apiResponseListener.onFailed(errorMessage);
                isDataLoading.setValue(false);

            }

            @Override
            public void onFailure(Throwable t) {
                apiResponseListener.onFailed(t);
                isDataLoading.setValue(false);
            }
        });
    }
}
