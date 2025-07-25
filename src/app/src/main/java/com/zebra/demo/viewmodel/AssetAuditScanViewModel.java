package com.zebra.demo.viewmodel;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.data.remote.exception.ErrorResource;
import com.zebra.demo.data.remote.exception.ExceptionHandler;
import com.zebra.demo.data.remote.listener.ExceptionHandlerListener;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.AssetAudit;
import com.zebra.demo.data.remote.model.AssetAuditRequest;
import com.zebra.demo.data.remote.model.AssetAuditResponse;
import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.model.LocationDetail;
import com.zebra.demo.data.remote.repository.AssetAuditRepository;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.zebralib.application.Application;
import java.util.ArrayList;
import java.util.List;

public class AssetAuditScanViewModel extends ViewModel {
    public MutableLiveData<FilterAssetRFIDMappingResponse> assetAuditDetailMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<AssetAudit> assetAuditMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> networkErrorMutableData = new MutableLiveData<>();
    private LocationDetail selectedLocationDetail;
    private String selectedAuditId;

    /**
     * Fetch Audit Details
     *
     * @param apiResponseListener
     */
    public void fetchAuditIdDetails( ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        if(getSelectedAuditId() == null || getSelectedAuditId().isEmpty()) {
            apiResponseListener.onFailed("No Audit Id present");
            return;
        }
            isDataLoading.setValue(true);
            AssetAuditRepository assetAuditRepository = AssetAuditRepository.getInstance();
            assetAuditRepository.fetchAuditIdDetails(getSelectedAuditId(), new ResponseListener<FilterAssetRFIDMappingResponse>() {
                @Override
                public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                    if(assetDetails != null) {
                        assetAuditDetailMutableLiveData.setValue(assetDetails);
                    } else {
                        apiResponseListener.onFailed("No data found");
                    }
                    isDataLoading.setValue(false);
                }

                @Override
                public void onError(String e) {
                    if (e != null) {
                        apiResponseListener.onFailed(e);
                        isDataLoading.setValue(false);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    if (t != null) {
                        ExceptionHandler.handleListenerException(apiResponseListener, t, getExceptionHandler());
                        isDataLoading.setValue(false);
                    }
                }
            });

    }


    /**
     * Insert Audit Scan Data
     * @param tagList
     * @param apiResponseListener
     */
    public void insertAuditScanData(List<String> tagList, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null || tagList == null || tagList.isEmpty()) {
            return;
        }

        if(selectedAuditId == null || selectedAuditId.isEmpty()) {
            apiResponseListener.onFailed("No Audit Selected");
        }

        List<AssetAuditRequest> assetAuditRequestList = getAssetAuditRequests(tagList);

        isDataLoading.setValue(true);
        AssetAuditRepository assetAuditRepository = AssetAuditRepository.getInstance();
        assetAuditRepository.insertAuditScanData(assetAuditRequestList, new ResponseListener<AssetAuditResponse>() {
            @Override
            public void onDataReceived(AssetAuditResponse assetDetails) {
                if(assetDetails != null && assetDetails.Result && assetDetails.Data != null && !assetDetails.Data.isEmpty()) {
                    assetAuditMutableLiveData.setValue(assetDetails.Data.get(0));
                } else {

                    apiResponseListener.onFailed("Insert Failed");
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

    @NonNull
    private List<AssetAuditRequest> getAssetAuditRequests(List<String> tagList) {
        List<AssetAuditRequest> assetAuditRequestList = new ArrayList<>();

        for (String tag : tagList) {
            AssetAuditRequest assetAuditRequest = new AssetAuditRequest();
            assetAuditRequest.setAuditcode(selectedAuditId);
            assetAuditRequest.setAuditby(new SharedPreference(Application.getAppContext()).getUserId());
            assetAuditRequest.setRfidtag(tag);
            assetAuditRequestList.add(assetAuditRequest);
        }
        return assetAuditRequestList;
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

    public LocationDetail getSelectedLocationDetail() {
        return selectedLocationDetail;
    }

    public void setSelectedLocationDetail(LocationDetail selectedLocationDetail) {
        this.selectedLocationDetail = selectedLocationDetail;
    }

    public String getSelectedAuditId() {
        return selectedAuditId;
    }

    public void setSelectedAuditId(String selectedAuditId) {
        this.selectedAuditId = selectedAuditId;
    }
}
