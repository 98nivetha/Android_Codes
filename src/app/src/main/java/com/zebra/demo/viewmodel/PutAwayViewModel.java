package com.zebra.demo.viewmodel;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.repository.IBA_REPOSITORY.PutAwayRepository;
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
import com.zebra.demo.data.remote.responsemodels.RackDetails;
import com.zebra.demo.view.listener.ApiResponseListener;
import java.util.List;

public class PutAwayViewModel extends ViewModel {
    public MutableLiveData<GetAllRackDatasResponse> getAllRackDetailMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<GetAllRackDatasResponse> metaDetailMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<RackDetails> selectedRackDetailObject = new MutableLiveData<>();

    public MutableLiveData<List<RackDetails>> rackDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    public MutableLiveData<String> selectedRackDetail = new MutableLiveData<>();


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
}
