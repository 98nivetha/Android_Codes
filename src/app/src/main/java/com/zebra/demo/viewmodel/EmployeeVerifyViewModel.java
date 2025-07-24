package com.zebra.demo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zebra.demo.data.remote.exception.ErrorResource;
import com.zebra.demo.data.remote.exception.ExceptionHandler;
import com.zebra.demo.data.remote.listener.ExceptionHandlerListener;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.ApiResponse;
import com.zebra.demo.data.remote.model.EmployeeAssetScannedInsertRequest;
import com.zebra.demo.data.remote.model.EmployeeDetail;
import com.zebra.demo.data.remote.model.EmployeeVerifyAssetFetchRequest;
import com.zebra.demo.data.remote.model.EmployeeVerifyAssetFetchResponse;
import com.zebra.demo.data.remote.model.EmployeeVerifyAssetFetchTagIdRequest;
import com.zebra.demo.data.remote.model.EmployeesAllocatedAssetDetailRequest;
import com.zebra.demo.data.remote.model.EmployeesAllocatedAssetDetailsResponse;
import com.zebra.demo.data.remote.model.ScanAssetDetail;
import com.zebra.demo.data.remote.repository.EmployeeVerifyRepository;
import com.zebra.demo.view.listener.ApiResponseListener;

import java.util.ArrayList;
import java.util.List;

public class EmployeeVerifyViewModel extends ViewModel {


    //public MutableLiveData<EmployeeVerifyTagInsertResponse> employeeVerifyTagInsertResponseMutableLiveData = new MutableLiveData<>();
    //public MutableLiveData<List<EmployeeVerifyTagInsertRequest>> employeeVerifyTagInsertRequests = new MutableLiveData<>();

    //Employee Drop Down
    public MutableLiveData<List<EmployeeDetail>> employeeDetailListResponseMutableLiveData = new MutableLiveData<>();

    //Assert List (ListView)
    public MutableLiveData<List<ScanAssetDetail>> assetDetailListResponseMutableLiveData = new MutableLiveData<>();

    //Assert List - Actual Assigned to Employees
    public MutableLiveData<List<ScanAssetDetail>> globleAssetDetailListResponseMutableLiveData = new MutableLiveData<>();

    //public MutableLiveData<ApiResponse> employeeVerifyTagUpdateResponseMutableLiveData = new MutableLiveData<>();

    //Loader
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();

    //Internet Error Message
    private MutableLiveData<Boolean> networkErrorMutableData = new MutableLiveData<>();

    //Scanned Tags
    public MutableLiveData<List<String>> assertTagList = new MutableLiveData<>();

    //Selected Employee Detail from Dropdown
    public MutableLiveData<EmployeeDetail> selecteEmployeeDetailMutableLiveData = new MutableLiveData<>();


    public List<String> getAssertTagList() {
        if (assertTagList != null) {
            return assertTagList.getValue();
        }
        return null;
    }

    public void setAssertTagList(MutableLiveData<List<String>> assertTagList) {
        this.assertTagList = assertTagList;
    }

    public void setAssertTagList(List<String> assertTagList) {
        if (this.assertTagList != null) {
            this.assertTagList.setValue(assertTagList);
        }
    }

    public void addEmployeeVerifyTagInsertRequests(String tagId) {
        if (this.assertTagList == null) {
            this.assertTagList = new MutableLiveData<>();
        }
        if (tagId != null) {

            List<String> tempList = assertTagList.getValue();
            if (tempList == null) {
                tempList = new ArrayList<>();
            }


            if (!isTagAlreadyExist(tempList, tagId)) {
                tempList.add(tagId);
                this.assertTagList.setValue(tempList);
            }
        }

    }

    boolean isTagAlreadyExist(List<String> tempList, String tag) {

        if (tempList != null && tag != null) {
            for (int i = 0; i < tempList.size(); i++) {
                String tagId = tempList.get(i);
                if (tagId != null && tagId.equals(tag)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Employee List
     *
     * @param apiResponseListener
     */
    public void employeeAssetVerificationInitial(ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

//        if(employeeVerifyTagInsertRequests == null || employeeVerifyTagInsertRequests.isEmpty()) {
//            apiResponseListener.onFailed("No Tag Id Found");
//        }


        isDataLoading.setValue(true);
        EmployeeVerifyRepository employeeVerifyRepository = EmployeeVerifyRepository.getInstance();
        employeeVerifyRepository.employeeAssetVerificationInitial(new ResponseListener<List<EmployeeDetail>>() {
            @Override
            public void onDataReceived(List<EmployeeDetail> employeeDetailList) {
                //if(assetDetails != null && !assetDetails.isEmpty()) {
                employeeDetailListResponseMutableLiveData.setValue(employeeDetailList);
                    /*} else {
                        apiResponseListener.onFailed("No data found");
                    }*/
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
                    ExceptionHandler.handleListenerException(apiResponseListener, t, getExceptionHandler());
                    isDataLoading.setValue(false);
                }
            }
        });

    }

    /**
     * Get Employee Detail with Assert
     *
     * @param tagId
     * @param apiResponseListener
     */
    public void employeesAllocatedAssetDetails(String tagId, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        String selectedEmployeeId = "";
        String selectedTagID = tagId == null ? "" : tagId;
        if (selecteEmployeeDetailMutableLiveData != null && selecteEmployeeDetailMutableLiveData.getValue() != null && selecteEmployeeDetailMutableLiveData.getValue().getEmployeeCode() != null) {
            selectedEmployeeId = selecteEmployeeDetailMutableLiveData.getValue().getEmployeeCode();
        }


        if (selectedEmployeeId.isEmpty() && selectedTagID.isEmpty()) {
            apiResponseListener.onFailed("No Tag Id  or Employee Code Found");
            return;
        }


        isDataLoading.setValue(true);
        EmployeeVerifyRepository employeeVerifyRepository = EmployeeVerifyRepository.getInstance();
        EmployeesAllocatedAssetDetailRequest employeesAllocatedAssetDetailRequest = new EmployeesAllocatedAssetDetailRequest(selectedTagID, selectedEmployeeId);
        employeeVerifyRepository.employeesAllocatedAssetDetails(employeesAllocatedAssetDetailRequest, new ResponseListener<EmployeesAllocatedAssetDetailsResponse>() {
            @Override
            public void onDataReceived(EmployeesAllocatedAssetDetailsResponse response) {
                if (response != null && response.getEmployeeDetail() != null) {
                    selecteEmployeeDetailMutableLiveData.setValue(response.getEmployeeDetail());
                    assetDetailListResponseMutableLiveData.setValue(response.getAssetDetail());
                    globleAssetDetailListResponseMutableLiveData.setValue(response.getAssetDetail());

                } else {
                    apiResponseListener.onFailed("No data found");
                }
                //if(assetDetails != null && !assetDetails.isEmpty()) {
                //employeeDetailListResponseMutableLiveData.setValue(employeeDetailList);
                    /*} else {
                        apiResponseListener.onFailed("No data found");
                    }*/
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
                    ExceptionHandler.handleListenerException(apiResponseListener, t, getExceptionHandler());
                    isDataLoading.setValue(false);
                }
            }
        });

    }

    /**
     * Employee Asset Scanned Insert
     *
     * @param apiResponseListener
     */
    public void employeeAssetScannedInsert(ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }


        if (selecteEmployeeDetailMutableLiveData.getValue() == null) {
            apiResponseListener.onFailed("Please select Employee Detail");
            return;
        }

        if (assetDetailListResponseMutableLiveData.getValue() == null || assetDetailListResponseMutableLiveData.getValue().isEmpty()) {
            apiResponseListener.onFailed("No Asset Found");
            return;
        }


        isDataLoading.setValue(true);
        EmployeeVerifyRepository employeeVerifyRepository = EmployeeVerifyRepository.getInstance();

        EmployeeAssetScannedInsertRequest employeeAssetScannedInsertRequest = new EmployeeAssetScannedInsertRequest(selecteEmployeeDetailMutableLiveData.getValue(), assetDetailListResponseMutableLiveData.getValue());
        employeeVerifyRepository.employeeAssetScannedInsert(employeeAssetScannedInsertRequest, new ResponseListener<ApiResponse>() {
            @Override
            public void onDataReceived(ApiResponse response) {
                if (response != null && response.ErStatus != null && response.ErStatus.equals("1")) {
                    apiResponseListener.onSuccess(response.ErDescription);
                } else {
                    apiResponseListener.onFailed("Failed to Save data");
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
     * Get Assert Status
     *
     * @param apiResponseListener
     */
    public void employeeVerifyAssetFetch(ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }


        if (selecteEmployeeDetailMutableLiveData.getValue() == null) {
            apiResponseListener.onFailed("Please select Employee Detail");
            return;
        }

        /*if (assetDetailListResponseMutableLiveData.getValue() == null || assetDetailListResponseMutableLiveData.getValue().isEmpty()) {
            apiResponseListener.onFailed("No Asset Found");
            return;
        }*/

        if (assertTagList.getValue() == null || assertTagList.getValue().isEmpty()) {
            apiResponseListener.onFailed("No Tag Scanned");
            return;
        }


        isDataLoading.setValue(true);
        EmployeeVerifyRepository employeeVerifyRepository = EmployeeVerifyRepository.getInstance();

        List<EmployeeVerifyAssetFetchTagIdRequest> employeeVerifyAssetFetchTagIdRequestList = new ArrayList<>();

        for (int i = 0; i < assertTagList.getValue().size(); i++) {
            String tag = assertTagList.getValue().get(i);
            employeeVerifyAssetFetchTagIdRequestList.add(new EmployeeVerifyAssetFetchTagIdRequest(tag));
        }

        EmployeeVerifyAssetFetchRequest employeeVerifyAssetFetchRequest = new EmployeeVerifyAssetFetchRequest(employeeVerifyAssetFetchTagIdRequestList);

        employeeVerifyRepository.employeeVerifyAssetFetch(employeeVerifyAssetFetchRequest, new ResponseListener<EmployeeVerifyAssetFetchResponse>() {
            @Override
            public void onDataReceived(EmployeeVerifyAssetFetchResponse response) {
                if (response != null && response.getErStatus() != null && response.getErStatus().equals("1")) {
                    List<ScanAssetDetail> listScanAssetDetailList = assetDetailListResponseMutableLiveData.getValue();
                    List<ScanAssetDetail> globScanAssetDetailList = globleAssetDetailListResponseMutableLiveData.getValue();
                    List<ScanAssetDetail> respScannedAssetDetailList = response.getAssetDetail();
                    //List<ScanAssetDetail> newAssetDetailList = new ArrayList<>();


                    if (respScannedAssetDetailList == null || respScannedAssetDetailList.isEmpty()) {
                        apiResponseListener.onFailed("No Asset Found");
                    } else {

                        if (globScanAssetDetailList == null) {
                            globScanAssetDetailList = new ArrayList<>();
                        }

                        if (listScanAssetDetailList == null) {
                            listScanAssetDetailList = new ArrayList<>();
                        }

                        //Tag Response Assert
                        for (int j = 0; j < respScannedAssetDetailList.size(); j++) {
                            ScanAssetDetail respScannedAssetDetail = respScannedAssetDetailList.get(j);
                            boolean isAssertFound = false;
                            //Glob (Actual Employee Asserts)
                            for (int i = 0; i < globScanAssetDetailList.size(); i++) {
                                ScanAssetDetail globScanAssetDetail = globScanAssetDetailList.get(i);
                                if (globScanAssetDetail != null && respScannedAssetDetail != null) {
                                    if (globScanAssetDetail.getAssetCode().equals(respScannedAssetDetail.getAssetCode())) {
                                        //List Array
                                        for (int k = 0; k < listScanAssetDetailList.size(); k++) {
                                            ScanAssetDetail listScanAssetDetail = listScanAssetDetailList.get(k);
                                            if (globScanAssetDetail.getAssetCode().equals(listScanAssetDetail.getAssetCode())) {
                                                listScanAssetDetail.setEMP_AST_STATUS(1);
                                                isAssertFound = true;
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }

                            if (!isAssertFound) {
                                //List Array
                                for (int k = 0; k < listScanAssetDetailList.size(); k++) {
                                    ScanAssetDetail listScanAssetDetail = listScanAssetDetailList.get(k);
                                    if (respScannedAssetDetail != null && respScannedAssetDetail.getAssetCode().equals(listScanAssetDetail.getAssetCode())) {
                                        isAssertFound = true;
                                        break;
                                    }
                                }
                                //If new Asset Add it
                                if (!isAssertFound && respScannedAssetDetail != null) {
                                    respScannedAssetDetail.setEMP_AST_STATUS(2);
                                    listScanAssetDetailList.add(respScannedAssetDetail);
                                }
                            }
                        }
                        assetDetailListResponseMutableLiveData.setValue(listScanAssetDetailList);
                    }
                } else {
                    apiResponseListener.onFailed("No Tag Found");
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

    public MutableLiveData<EmployeeDetail> getSelecteEmployeeDetailMutableLiveData() {
        return selecteEmployeeDetailMutableLiveData;
    }

    public void setSelecteEmployeeDetailMutableLiveData(MutableLiveData<EmployeeDetail> selecteEmployeeDetailMutableLiveData) {
        this.selecteEmployeeDetailMutableLiveData = selecteEmployeeDetailMutableLiveData;
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
