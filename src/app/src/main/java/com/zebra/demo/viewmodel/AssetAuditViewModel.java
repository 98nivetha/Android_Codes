package com.zebra.demo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zebra.demo.data.remote.exception.ErrorResource;
import com.zebra.demo.data.remote.exception.ExceptionHandler;
import com.zebra.demo.data.remote.listener.ExceptionHandlerListener;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.AssetAudit;
import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.repository.AssetAuditRepository;
import com.zebra.demo.utility.AssetAuditType;
import com.zebra.demo.view.listener.ApiResponseListener;

import java.util.ArrayList;
import java.util.List;

public class AssetAuditViewModel extends ViewModel {
    public MutableLiveData<List<AssetAudit>> assetAuditListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> networkErrorMutableData = new MutableLiveData<>();

    private AssetAuditType selectedAssetAuditType = AssetAuditType.IN_PROGRESS;

    /**
     * Sending Asset Audit In Progress Request to API
     *
     * @param apiResponseListener
     */
    public void fetchAssetAuditInProgressList(ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }
            isDataLoading.setValue(true);
            AssetAuditRepository assetAuditRepository = AssetAuditRepository.getInstance();
            assetAuditRepository.fetchAssetAuditInProgressList(new ResponseListener<FilterAssetRFIDMappingResponse>() {
                @Override
                public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                    if(assetDetails != null && assetDetails.Result && assetDetails.Data != null
                    && assetDetails.Data.D_InprocessDetails != null && !assetDetails.Data.D_InprocessDetails.isEmpty()) {
                        assetAuditListMutableLiveData.setValue(assetDetails.Data.D_InprocessDetails);
                    } else {
                        assetAuditListMutableLiveData.setValue(new ArrayList<>());
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

    /**
     * Sending Asset Audit Upcoming Request to API
     *
     * @param apiResponseListener
     */
    public void fetchAssetAuditUpcomingList(ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }
        isDataLoading.setValue(true);
        AssetAuditRepository assetAuditRepository = AssetAuditRepository.getInstance();
        assetAuditRepository.fetchAssetAuditUpcomingList(new ResponseListener<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                if(assetDetails != null && assetDetails.Result && assetDetails.Data != null
                        && assetDetails.Data.D_UpcomingDetails != null && !assetDetails.Data.D_UpcomingDetails.isEmpty()) {
                    assetAuditListMutableLiveData.setValue(assetDetails.Data.D_UpcomingDetails);
                } else {
                    assetAuditListMutableLiveData.setValue(new ArrayList<>());
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

    /**
     * Sending Asset Audit Completed Request to API
     *
     * @param apiResponseListener
     */
    public void fetchAssetAuditCompletedList(ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }
        isDataLoading.setValue(true);
        AssetAuditRepository assetAuditRepository = AssetAuditRepository.getInstance();
        assetAuditRepository.fetchAssetAuditCompletedList(new ResponseListener<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                if(assetDetails != null && assetDetails.Result && assetDetails.Data != null
                        && assetDetails.Data.D_CompletedDetails != null && !assetDetails.Data.D_CompletedDetails.isEmpty()) {
                    assetAuditListMutableLiveData.setValue(assetDetails.Data.D_CompletedDetails);
                } else {
                    assetAuditListMutableLiveData.setValue(new ArrayList<>());
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

    public AssetAuditType getSelectedAssetAuditType() {
        return selectedAssetAuditType;
    }

    public void setSelectedAssetAuditType(AssetAuditType selectedAssetAuditType) {
        this.selectedAssetAuditType = selectedAssetAuditType;
    }

    public String getSelectedAssetAuditTypeName() {
        if(this.selectedAssetAuditType != null) {
            return this.selectedAssetAuditType.getAssetAuditTypeName();
        }
        return  "";
    }
}
