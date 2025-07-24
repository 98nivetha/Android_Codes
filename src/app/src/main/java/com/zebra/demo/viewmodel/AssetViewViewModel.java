package com.zebra.demo.viewmodel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zebra.demo.data.remote.exception.ErrorResource;
import com.zebra.demo.data.remote.exception.ExceptionHandler;
import com.zebra.demo.data.remote.listener.ExceptionHandlerListener;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.AssetViewRequest;
import com.zebra.demo.data.remote.model.DAssetResgiterDetail;
import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.repository.AssetRepository;
import com.zebra.demo.view.listener.ApiResponseListener;

public class AssetViewViewModel extends ViewModel {
    public MutableLiveData<DAssetResgiterDetail> assetFilterDetailMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> networkErrorMutableData = new MutableLiveData<>();

    /**
     * Sending Asset View Request to API
     *
     * @param apiResponseListener
     */
    public void sendAssetViewRequest(String tagId, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        if(tagId == null || tagId.trim().isEmpty()) {
            apiResponseListener.onFailed("No Tag Id Found");
        }

        AssetViewRequest assetViewRequest = new AssetViewRequest(tagId);

            isDataLoading.setValue(true);
            AssetRepository assetRepository = AssetRepository.getInstance();

            assetRepository.fetchAssetFilterDetails(assetViewRequest, new ResponseListener<FilterAssetRFIDMappingResponse>() {
                @Override
                public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                    if(assetDetails != null && assetDetails.Data != null && assetDetails.Data.D_RFIDDetails != null && !assetDetails.Data.D_RFIDDetails.isEmpty()) {
                        assetFilterDetailMutableLiveData.setValue(assetDetails.Data.D_RFIDDetails.get(0));
                    } else {
                        apiResponseListener.onFailed("No data found");
                    }
                    isDataLoading.setValue(false);
                }



                @Override
                public void onError(String e) {
                    if (e != null) {
                        apiResponseListener.onFailed(e);
                        //ExceptionHandler.handleResponseException(e, getExceptionHandler());
                        isDataLoading.setValue(false);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t != null) {
                        //apiResponseListener.onFailed(t.getMessage());
                        ExceptionHandler.handleListenerException(apiResponseListener,t, getExceptionHandler());
                        isDataLoading.setValue(false);
                    }
                }
            });

    }



    private ExceptionHandlerListener getExceptionHandler() {
        return new ExceptionHandlerListener() {
            @Override
            public void onInternetUnavailable() {
                networkErrorMutableData.setValue(true);
            }

            @Override
            public void onInvalidLogin(ErrorResource errorResource) {

            }

            @Override
            public void onForbidden(ErrorResource errorResource) {

            }

            @Override
            public void onResourceNotFound(ErrorResource errorResource) {

            }

            @Override
            public void onInternalError(ErrorResource errorResource) {

            }

            @Override
            public void onUnknownException(ErrorResource errorResource) {

            }

            @Override
            public void onResourceConflict(ErrorResource errorResource) {

            }
        };
    }

    public MutableLiveData<Boolean> getNetworkErrorMutableData() {
        return networkErrorMutableData;
    }

    public void setNetworkErrorMutableData(MutableLiveData<Boolean> networkErrorMutableData) {
        this.networkErrorMutableData = networkErrorMutableData;
    }
}
