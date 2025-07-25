package com.zebra.demo.viewmodel;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.data.remote.exception.ErrorResource;
import com.zebra.demo.data.remote.exception.ExceptionHandler;
import com.zebra.demo.data.remote.listener.ExceptionHandlerListener;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.AssetInward;
import com.zebra.demo.data.remote.model.AssetInwardAutoRequest;
import com.zebra.demo.data.remote.model.AssetInwardAutoResponse;
import com.zebra.demo.data.remote.model.AssetInwardManualRequest;
import com.zebra.demo.data.remote.model.AssetInwardManualResponse;
import com.zebra.demo.data.remote.repository.AssetInwardRepository;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.zebralib.application.Application;
import java.util.ArrayList;
import java.util.List;

public class AssetInwardViewModel extends ViewModel {



    //Manual Ok NotOk Response
    public MutableLiveData<AssetInwardManualResponse> manualAssetInwardResponseMutableLiveData = new MutableLiveData<>();

    //Automatic Response
    public MutableLiveData<List<AssetInwardAutoResponse>> autoAssetInwardResponseMutableLiveData = new MutableLiveData<>();

    //Manual Filter Response
    public MutableLiveData<List<AssetInward>> manualAssetInwardMutableLiveData = new MutableLiveData<>();

    //Fetch Asset Inward Manual Response When List Item Click
    public MutableLiveData<List<AssetInwardAutoResponse>> manualAssetInwardResponseListMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> networkErrorMutableData = new MutableLiveData<>();

    /**
     * Insert Asset Inward Auto
     *
     * @param  tagList
     * @param apiResponseListener
     */
    public void InsertAssetInwardAuto(List<String> tagList, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null || tagList == null || tagList.isEmpty()) {
            return;
        }


        List<AssetInwardAutoRequest> assetInwardAutoRequest = getAssetInwardAutoRequest(tagList);



        if(assetInwardAutoRequest.isEmpty()) {
            apiResponseListener.onFailed("No Tag Found");
        }

            isDataLoading.setValue(true);
            AssetInwardRepository assetRepository = AssetInwardRepository.getInstance();
            assetRepository.InsertAssetInwardAuto(assetInwardAutoRequest, new ResponseListener<List<AssetInwardAutoResponse>>() {
                @Override
                public void onDataReceived(List<AssetInwardAutoResponse> assetDetails) {
                    if(assetDetails != null && !assetDetails.isEmpty()) {
                        autoAssetInwardResponseMutableLiveData.setValue(assetDetails);
                    } else {
                        autoAssetInwardResponseMutableLiveData.setValue(new ArrayList<>());
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
     * Insert Asset Inward Manual
     *
     * @param  assetInwardAutoResponse
     * @param  status
     * @param apiResponseListener
     */
    public void InsertAssetInwardManual(AssetInwardAutoResponse assetInwardAutoResponse, int status , ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        if(assetInwardAutoResponse == null) {
            apiResponseListener.onFailed("No input found");
            return;
        }

        AssetInwardManualRequest assetInwardManualRequest = new AssetInwardManualRequest();
        assetInwardManualRequest.GatePass = assetInwardAutoResponse.GatePass;
        assetInwardManualRequest.Requestid = assetInwardAutoResponse.Requestid;
        assetInwardManualRequest.Assetcode = assetInwardAutoResponse.Assetcode;
        assetInwardManualRequest.AssetName = assetInwardAutoResponse.AssetName;
        assetInwardManualRequest.AssetSrNo = assetInwardAutoResponse.AssetSrNo;
        assetInwardManualRequest.RFIDTag = assetInwardAutoResponse.RFIDTag;
        assetInwardManualRequest.VerifyStatus = status;
        assetInwardManualRequest.verifiedby = new SharedPreference(Application.getAppContext()).getUserId();
        assetInwardManualRequest.Jwt = new SharedPreference(Application.getAppContext()).getAccessToken();
                //assetInwardAutoResponse.verifiedby;


            isDataLoading.setValue(true);
            AssetInwardRepository assetRepository = AssetInwardRepository.getInstance();
            assetRepository.InsertAssetInwardManual(assetInwardManualRequest, new ResponseListener<AssetInwardManualResponse>() {
                @Override
                public void onDataReceived(AssetInwardManualResponse assetDetails) {
                    if(assetDetails != null) {
                        if(assetDetails.Erstatus != null && assetDetails.Erstatus.ErStatus != null && assetDetails.Erstatus.ErStatus.equals("1")) {
                            manualAssetInwardResponseMutableLiveData.setValue(assetDetails);
                            apiResponseListener.onSuccess(assetDetails.Erstatus.ErDescription);
                        } else {
                            apiResponseListener.onFailed("Asset Inward Insert Failed");
                        }

                    } else {
                        autoAssetInwardResponseMutableLiveData.setValue(new ArrayList<>());
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
     * Fetch Asset Inward Manual GatePass List
     *
     * @param Fromdate
     * @param Todate
     * @param Search
     * @param apiResponseListener
     */
    public void fetchAssetInwardManualGatePassList(String Fromdate, String Todate,  String Search, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        isDataLoading.setValue(true);
        AssetInwardRepository assetRepository = AssetInwardRepository.getInstance();
        assetRepository.fetchAssetInwardManualGatePassList(Fromdate, Todate,  Search, new ResponseListener<List<AssetInward>>() {
            @Override
            public void onDataReceived(List<AssetInward> assetDetails) {
                if(assetDetails != null && !assetDetails.isEmpty()) {
                    manualAssetInwardMutableLiveData.setValue(assetDetails);
                } else {
                    manualAssetInwardMutableLiveData.setValue(new ArrayList<>());
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
     * Fetch AssetInward Asset Details Via GatePass
     *
     * @param gatepassno
     * @param apiResponseListener
     */
    public void fetchAssetInwardAssetDetailsViaGatePass(String gatepassno, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        isDataLoading.setValue(true);
        AssetInwardRepository assetRepository = AssetInwardRepository.getInstance();
        assetRepository.fetchAssetInwardAssetDetailsViaGatePass(gatepassno, new ResponseListener<List<AssetInwardAutoResponse>>() {
            @Override
            public void onDataReceived(List<AssetInwardAutoResponse> assetDetails) {
                if(assetDetails != null && !assetDetails.isEmpty()) {
                    manualAssetInwardResponseListMutableLiveData.setValue(assetDetails);
                } else {
                    manualAssetInwardResponseListMutableLiveData.setValue(new ArrayList<>());
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


    @NonNull
    private List<AssetInwardAutoRequest> getAssetInwardAutoRequest(List<String> tagList) {
        List<AssetInwardAutoRequest> assetInwardAutoRequestList = new ArrayList<>();

        for (String tag : tagList) {
            AssetInwardAutoRequest  assetInwardAutoRequest = new AssetInwardAutoRequest();
            assetInwardAutoRequest.scanedby = new SharedPreference(Application.getAppContext()).getUserId();
            assetInwardAutoRequest.tagid = tag;
            assetInwardAutoRequest.Jwt = new SharedPreference(Application.getAppContext()).getAccessToken();
            assetInwardAutoRequestList.add(assetInwardAutoRequest);
        }
        return assetInwardAutoRequestList;
    }
}
