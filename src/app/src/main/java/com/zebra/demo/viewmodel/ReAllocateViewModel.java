package com.zebra.demo.viewmodel;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.repository.IBA_REPOSITORY.ReAllocateRepository;
import com.zebra.demo.data.remote.requestmodels.ReturnPartsValidInfoRequest;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsValidInfoResponse;
import com.zebra.demo.view.listener.ApiResponseListener;

public class ReAllocateViewModel extends ViewModel {
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();

    public void checkValidPartsByRFIDForReallocate(ReturnPartsValidInfoRequest request, ApiResponseListener apiResponseListener) {
        if (apiResponseListener == null) {
            return;
        }
        isDataLoading.setValue(true);
        ReAllocateRepository reAllocateRepository = ReAllocateRepository.getInstance();
        Log.d("API_REQUEST_RAW", "Request: " + new Gson().toJson(request));
        reAllocateRepository.getAllPartsByRFID(request, new ResponseListener<ReturnPartsValidInfoResponse>() {
            @Override
            public void onDataReceived(ReturnPartsValidInfoResponse returnPartsValidInfoResponse) {
                isDataLoading.setValue(false);
                try {
                    Log.d("API_RESPONSE_RAW", "Response: " + new Gson().toJson(returnPartsValidInfoResponse));
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


}
