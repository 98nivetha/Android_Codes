package com.zebra.demo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.repository.IBA_REPOSITORY.RfidMappingRepository;
import com.zebra.demo.data.remote.responsemodels.InwardDetail;
import com.zebra.demo.data.remote.responsemodels.RfidMappingResponse;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RfidMappingViewModel extends ViewModel {
    public MutableLiveData<RfidMappingResponse> getInwardDetailMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public MutableLiveData<List<InwardDetail>> inwardDetailListLiveData = new MutableLiveData<>();
    public MutableLiveData<String> inwardCodeValidationText = new MutableLiveData<>();
    public InwardDetail selectedDInward;

    public void fetchInwardDetailsForDropdown() {
        isLoading.setValue(true);
        String rawJson = "{ \"Rfidid\": 0 }";
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                rawJson
        );
        RfidMappingRepository.getInstance().fetchInwardDetails(0, new ResponseListener<RfidMappingResponse>() {
            @Override
            public void onDataReceived(RfidMappingResponse response) {
                isLoading.postValue(false);
                getInwardDetailMutableLiveData.postValue(response);
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                errorMessage.postValue(error);
            }

            @Override
            public void onFailure(Throwable t) {
                isLoading.postValue(false);
                errorMessage.postValue(t.getMessage());
            }
        });
    }
}

