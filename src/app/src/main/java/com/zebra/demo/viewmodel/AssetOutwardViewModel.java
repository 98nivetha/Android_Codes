package com.zebra.demo.viewmodel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.data.remote.exception.ErrorResource;
import com.zebra.demo.data.remote.exception.ExceptionHandler;
import com.zebra.demo.data.remote.listener.ExceptionHandlerListener;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.ApiResponse;
import com.zebra.demo.data.remote.model.AssetDetail;
import com.zebra.demo.data.remote.model.AssetOutward;
import com.zebra.demo.data.remote.model.AssetViewRequest;
import com.zebra.demo.data.remote.repository.AssetOutwardRepository;
import com.zebra.demo.data.remote.repository.AssetRepository;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.zebralib.application.Application;
import java.util.ArrayList;
import java.util.List;

public class AssetOutwardViewModel extends ViewModel {



    public MutableLiveData<List<AssetOutward>> assetOutwardListMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> networkErrorMutableData = new MutableLiveData<>();


    /**
     * Sending Asset Inward Request to API
     *
     * @param  Gatepassno
     * @param apiResponseListener
     */
    public void fetchAssetOutwardGatePassDetailsList(String Gatepassno, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        if(Gatepassno == null || Gatepassno.trim().equals("")) {
            apiResponseListener.onFailed("Gate Pass No can not be empty");
        }

            isDataLoading.setValue(true);
            AssetOutwardRepository assetOutwardRepository = AssetOutwardRepository.getInstance();
            assetOutwardRepository.fetchAssetOutwardGatePassDetailsList(Gatepassno, new ResponseListener<List<AssetOutward>>() {
                @Override
                public void onDataReceived(List<AssetOutward> assetOutwards) {
                    if(assetOutwards != null && !assetOutwards.isEmpty()) {
                        assetOutwardListMutableLiveData.setValue(assetOutwards);
                    } else {
                        assetOutwardListMutableLiveData.setValue(new ArrayList<>());
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
     * Insert Asset Outward Details
     *
     * @param Gatepassno
     * @param apiResponseListener
     */
    public void insertAssetOutwardDetails(String Gatepassno, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        if(Gatepassno == null || Gatepassno.trim().equals("")) {
            apiResponseListener.onFailed("Gate Pass No can not be empty");
            return;
        }

        if(assetOutwardListMutableLiveData.getValue() == null || assetOutwardListMutableLiveData.getValue().isEmpty()) {
            apiResponseListener.onFailed("No asset found");
            return;
        }

            isDataLoading.setValue(true);
            AssetOutwardRepository assetOutwardRepository = AssetOutwardRepository.getInstance();
            assetOutwardRepository.insertAssetOutwardDetails(Gatepassno, assetOutwardListMutableLiveData.getValue() , new ResponseListener<List<ApiResponse>>() {
                @Override
                public void onDataReceived(List<ApiResponse> apiResponseList) {
                    if(apiResponseList != null && !apiResponseList.isEmpty() && apiResponseList.get(0).ErDescription != null) {
                        apiResponseListener.onFailed(apiResponseList.get(0).ErDescription);
                        //assetOutwardListMutableLiveData.setValue(assetOutwards);
                    } else {
                        //assetOutwardListMutableLiveData.setValue(new ArrayList<>());
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
        assetRepository.fetchAssetDetails(assetViewRequest, new ResponseListener<List<AssetDetail>>() {
            @Override
            public void onDataReceived(List<AssetDetail> assetDetails) {
                if(assetDetails != null && !assetDetails.isEmpty()) {
                    for (AssetDetail assetDetail : assetDetails) {
                        setAssetOutwardFromAssetDetail(assetDetail);
                    }
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

    /**
     * Sending Asset View Multi RFID Request to API
     *
     * @param apiResponseListener
     */
    public void fetchMultipleAssetdetailsviaRFID(String tagIds, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        if(tagIds == null || tagIds.trim().isEmpty()) {
            apiResponseListener.onFailed("No Tag Id Found");
        }

        isDataLoading.setValue(true);
        AssetRepository assetRepository = AssetRepository.getInstance();
        assetRepository.fetchMultipleAssetdetailsviaRFID(tagIds, new ResponseListener<List<AssetDetail>>() {
            @Override
            public void onDataReceived(List<AssetDetail> assetDetails) {
                if(assetDetails != null && !assetDetails.isEmpty()) {
                    for (AssetDetail assetDetail : assetDetails) {
                        setAssetOutwardFromAssetDetail(assetDetail);
                    }
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

    public boolean setStatusOkByTag(String tag) {
        boolean isFound = false;
        if(assetOutwardListMutableLiveData != null && tag != null && assetOutwardListMutableLiveData.getValue() != null) {
            if(!assetOutwardListMutableLiveData.getValue().isEmpty()) {
                for (AssetOutward assetOutward : assetOutwardListMutableLiveData.getValue()) {
                    if (tag.equals(assetOutward.RFIDTag)) {
                        if(assetOutward.Requestid != null) {
                            assetOutward.VerifyStatus = 1;
                            assetOutward.verifiedby = new SharedPreference(Application.getAppContext()).getUserId();
                            assetOutwardListMutableLiveData.setValue(assetOutwardListMutableLiveData.getValue());
                        }
                        isFound = true;
                        break;
                    }
                }
                if(!isFound) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setStatusOkBySrNo(String srNo) {
        boolean isFound = false;
        if(assetOutwardListMutableLiveData != null && srNo != null && assetOutwardListMutableLiveData.getValue() != null) {
            if(!assetOutwardListMutableLiveData.getValue().isEmpty()) {
                for (AssetOutward assetOutward : assetOutwardListMutableLiveData.getValue()) {
                    if (srNo.equals(assetOutward.AssetSrNo)) {
                        if(assetOutward.Requestid != null) {
                            assetOutward.VerifyStatus = 1;
                            assetOutward.verifiedby = new SharedPreference(Application.getAppContext()).getUserId();
                            assetOutwardListMutableLiveData.setValue(assetOutwardListMutableLiveData.getValue());
                        }
                        isFound = true;
                        break;
                    }
                }
                if(!isFound) {
                    return true;
                }
            }
        }
        return false;
    }

    void setAssetOutwardFromAssetDetail(AssetDetail assetDetail) {
        if(assetDetail != null && assetOutwardListMutableLiveData != null
                 && assetOutwardListMutableLiveData.getValue() != null
                && !assetOutwardListMutableLiveData.getValue().isEmpty()) {

            AssetOutward assetOutward = new AssetOutward();
            assetOutward.GatePass = assetOutwardListMutableLiveData.getValue().get(0).GatePass;
            assetOutward.Requestid = null;
            assetOutward.Assetcode = assetDetail.AssetCode;
            assetOutward.AssetName =  assetDetail.AssetName;
            //assetOutward.AssetSrNo =  assetDetail.AssetSrCode;
            assetOutward.AssetSrNo =  assetDetail.AssetCode;
            assetOutward.RFIDTag =  assetDetail.RFIDTag;
            assetOutward.Incharge =  null;
            assetOutward.Vendor =  "";//assetDetail.Vendor;
            assetOutward.Address =  null;
            assetOutward.Vehicle =  null;
            assetOutward.Reason =  null;
            assetOutward.VerifyStatus = 0;
            assetOutward.verifiedby = new SharedPreference(Application.getAppContext()).getUserId();
            assetOutward.Assetout_on = null;
            List<AssetOutward> assetOutwardList = assetOutwardListMutableLiveData.getValue();
            assetOutwardList.add(assetOutward);
            assetOutwardListMutableLiveData.setValue(assetOutwardList);
        }
    }
}
