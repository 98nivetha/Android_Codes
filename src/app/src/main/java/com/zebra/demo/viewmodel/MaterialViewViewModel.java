package com.zebra.demo.viewmodel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.repository.IBA_REPOSITORY.MaterialViewRepository;
import com.zebra.demo.data.remote.responsemodels.GetSingleMaterialDataResponse;
import com.zebra.demo.view.listener.ApiResponseListener;

public class MaterialViewViewModel extends ViewModel {
    public MutableLiveData<GetSingleMaterialDataResponse> meterialDetailMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    public void fetchMeterialDetails(ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) {
            return;
        }
        isDataLoading.setValue(true);
        MaterialViewRepository materialViewRepository = MaterialViewRepository.getInstance();
        materialViewRepository.getMaterialInfo("BF", new ResponseListener<GetSingleMaterialDataResponse>() {
            @Override
            public void onDataReceived(GetSingleMaterialDataResponse getSingleMaterialDataResponse) {
                isDataLoading.setValue(false);
                if (getSingleMaterialDataResponse != null && getSingleMaterialDataResponse.getData() != null) {
                    meterialDetailMutableLiveData.postValue(getSingleMaterialDataResponse);
                    GetSingleMaterialDataResponse.V_RfidDetails details = getSingleMaterialDataResponse.getData().getV_RfidDetails();
                }
            }
            @Override
            public void onError(String e) {
                isDataLoading.setValue(false);
            }
            @Override
            public void onFailure(Throwable t) {
                isDataLoading.setValue(false);
            }
        });
    }
}
