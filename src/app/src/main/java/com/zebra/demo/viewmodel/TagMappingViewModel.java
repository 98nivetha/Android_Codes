package com.zebra.demo.viewmodel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.data.remote.exception.ErrorResource;
import com.zebra.demo.data.remote.exception.ExceptionHandler;
import com.zebra.demo.data.remote.listener.ExceptionHandlerListener;
import com.zebra.demo.data.remote.listener.ResponseListener;
import com.zebra.demo.data.remote.model.DAssetDetail;
import com.zebra.demo.data.remote.model.DAssetclassDetail;
import com.zebra.demo.data.remote.model.DBuildingDetail;
import com.zebra.demo.data.remote.model.DCompanyDetail;
import com.zebra.demo.data.remote.model.DFloorDetail;
import com.zebra.demo.data.remote.model.DLocationDetail;
import com.zebra.demo.data.remote.model.DSubAssetDetail;
import com.zebra.demo.data.remote.model.DSubAssetPartDetail;
import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingData;
import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.model.InsertAssetRFIDMappingRequest;
import com.zebra.demo.data.remote.model.InsertAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.model.UpdateAssetRFIDMappingRequest;
import com.zebra.demo.data.remote.model.UpdateAssetRFIDMappingResponse;
import com.zebra.demo.data.remote.repository.IBA_REPOSITORY.RfidMappingRepository;
import com.zebra.demo.data.remote.repository.TagMappingRepository;
import com.zebra.demo.data.remote.responsemodels.InwardDetail;
import com.zebra.demo.data.remote.responsemodels.RfidMappingData;
import com.zebra.demo.data.remote.responsemodels.RfidMappingResponse;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.zebralib.application.Application;
import java.util.List;

public class TagMappingViewModel extends ViewModel {

    //Get All List init
    public MutableLiveData<List<InwardDetail>> inwardDetailListLiveData = new MutableLiveData<>();

    //Dropdown List
    public MutableLiveData<List<DCompanyDetail>> companyDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<DBuildingDetail>> buildingDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<DFloorDetail>> floorDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<DLocationDetail>> locationDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<DAssetclassDetail>> assetClassDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<DAssetDetail>> assetDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<DSubAssetDetail>> subAssetDetailsDDListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<DSubAssetPartDetail>> subAssetPartDetailsDDListLiveData = new MutableLiveData<>();

    //Dropdown Selection
    public DCompanyDetail selectedDCompanyDetail;
    public DBuildingDetail selectedDBuildingDetail;
    public DFloorDetail selectedDFloorDetail;
    public DLocationDetail selectedDLocationDetail;
    public DAssetclassDetail selectedDAssetclassDetail;
    public DAssetDetail selectedDAssetDetail;
    public DSubAssetDetail selectedDSubAssetDetail;
    public DSubAssetPartDetail selectedDSubAssetPartDetail;

    //Network Fields
    public MutableLiveData<Boolean> isDataLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> networkErrorMutableData = new MutableLiveData<>();

    //Validation Field
    public MutableLiveData<String> tagCodeValidationText = new MutableLiveData<>();
    public MutableLiveData<String> companyDDValidationText = new MutableLiveData<>();
    public MutableLiveData<String> buildingDDValidationText = new MutableLiveData<>();
    public MutableLiveData<String> floorDDValidationText = new MutableLiveData<>();
    public MutableLiveData<String> locationDDValidationText = new MutableLiveData<>();
    public MutableLiveData<String> assetClassDDValidationText = new MutableLiveData<>();
    public MutableLiveData<String> assetDDValidationText = new MutableLiveData<>();
    public MutableLiveData<String> subAssetDDValidationText = new MutableLiveData<>();
    public MutableLiveData<String> subAssetPartCodeDDValidationText = new MutableLiveData<>();


    /**
     * Sending Init Dropdown Request to API
     *
     * @param apiResponseListener
     */
    public void fetchMetaDetails(ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();

   }


    /**
     * Insert Asset RFID Mapping
     *
     * @param companyName
     * @param buildingName
     * @param floorName
     * @param locationName
     * @param assetClass
     * @param assetName
     * @param subAssetcode
     * @param subAssetPartCode
     * @param tagNo
     * @param apiResponseListener
     */
    public void insertAssetRFIDMapping(
            String companyName, String buildingName, String floorName, String locationName,
            String assetClass, String assetName, String subAssetcode, String subAssetPartCode,
            String tagNo,
            ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        companyDDValidationText.setValue(null);
        buildingDDValidationText.setValue(null);
        floorDDValidationText.setValue(null);
        locationDDValidationText.setValue(null);
        assetClassDDValidationText.setValue(null);
        assetDDValidationText.setValue(null);
        subAssetDDValidationText.setValue(null);
        subAssetPartCodeDDValidationText.setValue(null);

        //Company
        if (selectedDCompanyDetail != null) {
            if (!selectedDCompanyDetail.Companyname.equals(companyName)) {
                companyDDValidationText.setValue("Company not selected");
                apiResponseListener.onFailed("Company not selected");
                return;
            }
        } else {
            companyDDValidationText.setValue("Company not selected");
            apiResponseListener.onFailed("Company not selected");
            return;
        }

        //Building
        if (selectedDBuildingDetail != null) {
            if (!selectedDBuildingDetail.Buildingname.equals(buildingName)) {
                buildingDDValidationText.setValue("Building not selected");
                apiResponseListener.onFailed("Building not selected");
                return;
            }
        } else {
            buildingDDValidationText.setValue("Building not selected");
            apiResponseListener.onFailed("Building not selected");
            return;
        }

        //Floor
        if (selectedDFloorDetail != null) {
            if (!selectedDFloorDetail.Floorname.equals(floorName)) {
                floorDDValidationText.setValue("Floor not selected");
                apiResponseListener.onFailed("Floor not selected");
                return;
            }
        } else {
            floorDDValidationText.setValue("Floor not selected");
            apiResponseListener.onFailed("Floor not selected");
            return;
        }

        //Location
        if (selectedDLocationDetail != null) {
            if (!selectedDLocationDetail.Locationname.equals(locationName)) {
                locationDDValidationText.setValue("Location not selected");
                apiResponseListener.onFailed("Location not selected");
                return;
            }
        } else {
            locationDDValidationText.setValue("Location not selected");
            apiResponseListener.onFailed("Location not selected");
            return;
        }

        //Asset Class
        if (selectedDAssetclassDetail != null) {
            if (!selectedDAssetclassDetail.Assetclassdescription.equals(assetClass)) {
                assetClassDDValidationText.setValue("Asset Class not selected");
                apiResponseListener.onFailed("Asset Class not selected");
                return;
            }
        } else {
            assetClassDDValidationText.setValue("Asset Class not selected");
            apiResponseListener.onFailed("Asset Class not selected");
            return;
        }

        //Asset
        if (selectedDAssetDetail != null) {
            if (!selectedDAssetDetail.SapAssetName.equals(assetName)) {
                assetDDValidationText.setValue("Asset not selected");
                apiResponseListener.onFailed("Asset not selected");
                return;
            }
        } else {
            assetDDValidationText.setValue("Asset not selected");
            apiResponseListener.onFailed("Asset not selected");
            return;
        }


        //Sub Asset
        if (selectedDSubAssetDetail != null) {
            if (!selectedDSubAssetDetail.Subitemname.equals(subAssetcode)) {
                subAssetDDValidationText.setValue("Sub Asset not selected");
                apiResponseListener.onFailed("Sub Asset not selected");
                return;
            }
        } else {
            subAssetDDValidationText.setValue("Sub Asset not selected");
            apiResponseListener.onFailed("Sub Asset not selected");
            return;
        }


        //Sub Asset Part
        if (selectedDSubAssetPartDetail != null) {
            if (!selectedDSubAssetPartDetail.Subassetpartscode.equals(subAssetPartCode)) {
                subAssetPartCodeDDValidationText.setValue("Sub Asset Part not selected");
                apiResponseListener.onFailed("Sub Asset Part not selected");
                return;
            }
        } else {
            subAssetPartCodeDDValidationText.setValue("Sub Asset Part not selected");
            apiResponseListener.onFailed("Sub Asset Part not selected");
            return;
        }

        if (tagNo == null || tagNo.trim().isEmpty()) {
            tagCodeValidationText.setValue("Tag No can not be empty");
            apiResponseListener.onFailed("Tag No can not be empty");
            return;
        }


        InsertAssetRFIDMappingRequest insertAssetRFIDMappingRequest = new InsertAssetRFIDMappingRequest();

        insertAssetRFIDMappingRequest.Assetrfidmappingid = 0;
        insertAssetRFIDMappingRequest.Assetrfidmappingcode = "";
        insertAssetRFIDMappingRequest.Companycode = selectedDCompanyDetail.Companycode;
        insertAssetRFIDMappingRequest.Buildingcode = selectedDBuildingDetail.Buildingcode;
        insertAssetRFIDMappingRequest.Floorcode = selectedDFloorDetail.Floorcode;
        insertAssetRFIDMappingRequest.Locationcode = selectedDLocationDetail.Locationcode;
        insertAssetRFIDMappingRequest.Assetclasscode = selectedDAssetclassDetail.Assetclasscode;
        insertAssetRFIDMappingRequest.Assetcode = selectedDAssetDetail.SapAssetNo;
        insertAssetRFIDMappingRequest.Assetsubcode = selectedDSubAssetDetail.Subassetcode;
        insertAssetRFIDMappingRequest.Subassetpartscode = selectedDSubAssetPartDetail.Subassetpartscode;
        insertAssetRFIDMappingRequest.RfidTag = tagNo;
        insertAssetRFIDMappingRequest.Tagtype = "";
        insertAssetRFIDMappingRequest.Status = "MSC001";
        insertAssetRFIDMappingRequest.Createdby = new SharedPreference(Application.getAppContext()).getUserId();
        insertAssetRFIDMappingRequest.Createdon = Utility.getCurrentDateTime();
        insertAssetRFIDMappingRequest.Modifiedby = new SharedPreference(Application.getAppContext()).getUserId();
        insertAssetRFIDMappingRequest.Modifiedon = Utility.getCurrentDateTime();


        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();
        tagMappingRepository.insertAssetRFIDMapping(insertAssetRFIDMappingRequest, new ResponseListener<InsertAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(InsertAssetRFIDMappingResponse response) {

                if (response != null) {
                    if (response.Result) {
                        apiResponseListener.onSuccess(Utility.getCommonValue(response.Message));
                    } else {
                        apiResponseListener.onFailed(Utility.getCommonValue(response.Message));
                    }
                } else {
                    apiResponseListener.onFailed("Mapping Failed");
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
                    ExceptionHandler.handleListenerException(apiResponseListener, t, getExceptionHandler());
                    isDataLoading.setValue(false);
                }
            }
        });

    }

    /**
     * Update Asset RFID Mapping
     *
     * @param companyName
     * @param buildingName
     * @param floorName
     * @param locationName
     * @param assetClass
     * @param assetName
     * @param subAssetcode
     * @param subAssetPartCode
     * @param tagNo
     * @param apiResponseListener
     */
    public void updateAssetRFIDMapping(
            String companyName, String buildingName, String floorName, String locationName,
            String assetClass, String assetName, String subAssetcode, String subAssetPartCode,
            String tagNo,
            ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        companyDDValidationText.setValue(null);
        buildingDDValidationText.setValue(null);
        floorDDValidationText.setValue(null);
        locationDDValidationText.setValue(null);
        assetClassDDValidationText.setValue(null);
        assetDDValidationText.setValue(null);
        subAssetDDValidationText.setValue(null);
        subAssetPartCodeDDValidationText.setValue(null);

        //Company
        if (selectedDCompanyDetail != null) {
            if (!selectedDCompanyDetail.Companyname.equals(companyName)) {
                companyDDValidationText.setValue("Company not selected");
                apiResponseListener.onFailed("Company not selected");
                return;
            }
        } else {
            companyDDValidationText.setValue("Company not selected");
            apiResponseListener.onFailed("Company not selected");
            return;
        }

        //Building
        if (selectedDBuildingDetail != null) {
            if (!selectedDBuildingDetail.Buildingname.equals(buildingName)) {
                buildingDDValidationText.setValue("Building not selected");
                apiResponseListener.onFailed("Building not selected");
                return;
            }
        } else {
            buildingDDValidationText.setValue("Building not selected");
            apiResponseListener.onFailed("Building not selected");
            return;
        }

        //Floor
        if (selectedDFloorDetail != null) {
            if (!selectedDFloorDetail.Floorname.equals(floorName)) {
                floorDDValidationText.setValue("Floor not selected");
                apiResponseListener.onFailed("Floor not selected");
                return;
            }
        } else {
            floorDDValidationText.setValue("Floor not selected");
            apiResponseListener.onFailed("Floor not selected");
            return;
        }

        //Location
        if (selectedDLocationDetail != null) {
            if (!selectedDLocationDetail.Locationname.equals(locationName)) {
                locationDDValidationText.setValue("Location not selected");
                apiResponseListener.onFailed("Location not selected");
                return;
            }
        } else {
            locationDDValidationText.setValue("Location not selected");
            apiResponseListener.onFailed("Location not selected");
            return;
        }

        //Asset Class
        if (selectedDAssetclassDetail != null) {
            if (!selectedDAssetclassDetail.Assetclassdescription.equals(assetClass)) {
                assetClassDDValidationText.setValue("Asset Class not selected");
                apiResponseListener.onFailed("Asset Class not selected");
                return;
            }
        } else {
            assetClassDDValidationText.setValue("Asset Class not selected");
            apiResponseListener.onFailed("Asset Class not selected");
            return;
        }

        //Asset
        if (selectedDAssetDetail != null) {
            if (!selectedDAssetDetail.SapAssetName.equals(assetName)) {
                assetDDValidationText.setValue("Asset not selected");
                apiResponseListener.onFailed("Asset not selected");
                return;
            }
        } else {
            assetDDValidationText.setValue("Asset not selected");
            apiResponseListener.onFailed("Asset not selected");
            return;
        }


        //Sub Asset
        if (selectedDSubAssetDetail != null) {
            if (!selectedDSubAssetDetail.Subitemname.equals(subAssetcode)) {
                subAssetDDValidationText.setValue("Sub Asset not selected");
                apiResponseListener.onFailed("Sub Asset not selected");
                return;
            }
        } else {
            subAssetDDValidationText.setValue("Sub Asset not selected");
            apiResponseListener.onFailed("Sub Asset not selected");
            return;
        }


        //Sub Asset Part
        if (selectedDSubAssetPartDetail != null) {
            if (!selectedDSubAssetPartDetail.Subassetpartscode.equals(subAssetPartCode)) {
                subAssetPartCodeDDValidationText.setValue("Sub Asset Part not selected");
                apiResponseListener.onFailed("Sub Asset Part not selected");
                return;
            }
        } else {
            subAssetPartCodeDDValidationText.setValue("Sub Asset Part not selected");
            apiResponseListener.onFailed("Sub Asset Part not selected");
            return;
        }

        if (tagNo == null || tagNo.trim().isEmpty()) {
            tagCodeValidationText.setValue("Tag No can not be empty");
            apiResponseListener.onFailed("Tag No can not be empty");
            return;
        }


        UpdateAssetRFIDMappingRequest updateAssetRFIDMappingRequest = new UpdateAssetRFIDMappingRequest();

        //updateAssetRFIDMappingRequest.Assetrfidmappingid = selectedDSubAssetPartDetail.Assetregisterid;
        updateAssetRFIDMappingRequest.Assetrfidmappingid = selectedDSubAssetPartDetail.Assetrfidmappingid;
        updateAssetRFIDMappingRequest.Assetrfidmappingcode = selectedDSubAssetPartDetail.Assetrfidmappingcode;
        updateAssetRFIDMappingRequest.Companycode = selectedDCompanyDetail.Companycode;
        updateAssetRFIDMappingRequest.Buildingcode = selectedDBuildingDetail.Buildingcode;
        updateAssetRFIDMappingRequest.Floorcode = selectedDFloorDetail.Floorcode;
        updateAssetRFIDMappingRequest.Locationcode = selectedDLocationDetail.Locationcode;
        updateAssetRFIDMappingRequest.Assetclasscode = selectedDAssetclassDetail.Assetclasscode;
        updateAssetRFIDMappingRequest.Assetcode = selectedDAssetDetail.SapAssetNo;
        updateAssetRFIDMappingRequest.Assetsubcode = selectedDSubAssetDetail.Subassetcode;
        updateAssetRFIDMappingRequest.Subassetpartscode = selectedDSubAssetPartDetail.Subassetpartscode;
        updateAssetRFIDMappingRequest.RfidTag = tagNo;
        updateAssetRFIDMappingRequest.Tagtype = "";
        updateAssetRFIDMappingRequest.Status = "MSC001";
        updateAssetRFIDMappingRequest.Createdby = new SharedPreference(Application.getAppContext()).getUserId();
        updateAssetRFIDMappingRequest.Createdon =  selectedDSubAssetPartDetail.Createdon; //Utility.getCurrentDateTime();
        updateAssetRFIDMappingRequest.Modifiedby = new SharedPreference(Application.getAppContext()).getUserId();
        updateAssetRFIDMappingRequest.Modifiedon = Utility.getCurrentDateTime();


        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();
        tagMappingRepository.updateAssetRFIDMapping(updateAssetRFIDMappingRequest, new ResponseListener<UpdateAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(UpdateAssetRFIDMappingResponse response) {

                if (response != null) {
                    if (response.Result) {
                        apiResponseListener.onSuccess(Utility.getCommonValue(response.Message));
                    } else {
                        apiResponseListener.onFailed(Utility.getCommonValue(response.Message));
                    }
                } else {
                    apiResponseListener.onFailed("Mapping Failed");
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
                    ExceptionHandler.handleListenerException(apiResponseListener, t, getExceptionHandler());
                    isDataLoading.setValue(false);
                }
            }
        });

    }

    /**
     * Fetch Building DropDown List
     *
     * @param companyName
     * @param apiResponseListener
     */
    public void fetchBuildingDetailsRequest(String companyName, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        companyDDValidationText.setValue(null);

        if (selectedDCompanyDetail != null) {
            if (!selectedDCompanyDetail.Companyname.equals(companyName)) {
                companyDDValidationText.setValue("Company not selected");
                apiResponseListener.onFailed("Company not selected");
                return;
            }
        } else {
            companyDDValidationText.setValue("Company not selected");
            apiResponseListener.onFailed("Company not selected");
            return;
        }

        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();

        tagMappingRepository.fetchBuildingDetailsRequest(selectedDCompanyDetail.Companycode, new ResponseListener<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                if (assetDetails != null && assetDetails.Data != null && assetDetails.Data.D_BuildingDetails != null && !assetDetails.Data.D_BuildingDetails.isEmpty()) {
                    buildingDetailsDDListLiveData.setValue(assetDetails.Data.D_BuildingDetails);
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
     * Fetch Floor DropDown List
     *
     * @param buildingName
     * @param apiResponseListener
     */
    public void fetchFloorDetailsRequest(String buildingName, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        buildingDDValidationText.setValue(null);

        if (selectedDBuildingDetail != null) {
            if (!selectedDBuildingDetail.Buildingname.equals(buildingName)) {
                buildingDDValidationText.setValue("Building not selected");
                apiResponseListener.onFailed("Building not selected");
                return;
            }
        } else {
            buildingDDValidationText.setValue("Building not selected");
            apiResponseListener.onFailed("Building not selected");
            return;
        }

        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();


        tagMappingRepository.fetchFloorDetailsRequest(selectedDBuildingDetail.Buildingcode, new ResponseListener<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                if (assetDetails != null && assetDetails.Data != null && assetDetails.Data.D_FloorDetails != null && !assetDetails.Data.D_FloorDetails.isEmpty()) {
                    floorDetailsDDListLiveData.setValue(assetDetails.Data.D_FloorDetails);
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
     * Fetch Location DropDown List
     *
     * @param floorName
     * @param buildingName
     * @param apiResponseListener
     */
    public void fetchLocationDetailsRequest(String floorName, String buildingName, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        buildingDDValidationText.setValue(null);

        if (selectedDBuildingDetail != null) {
            if (!selectedDBuildingDetail.Buildingname.equals(buildingName)) {
                buildingDDValidationText.setValue("Building not selected");
                apiResponseListener.onFailed("Building not selected");
                return;
            }
        } else {
            buildingDDValidationText.setValue("Building not selected");
            apiResponseListener.onFailed("Building not selected");
            return;
        }

        floorDDValidationText.setValue(null);

        if (selectedDFloorDetail != null) {
            if (!selectedDFloorDetail.Floorname.equals(floorName)) {
                floorDDValidationText.setValue("Floor not selected");
                apiResponseListener.onFailed("Floor not selected");
                return;
            }
        } else {
            floorDDValidationText.setValue("Floor not selected");
            apiResponseListener.onFailed("Floor not selected");
            return;
        }

        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();


        tagMappingRepository.fetchLocationDetailsRequest(selectedDFloorDetail.Floorcode, selectedDBuildingDetail.Buildingcode, new ResponseListener<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                if (assetDetails != null && assetDetails.Data != null && assetDetails.Data.D_LocationDetails != null && !assetDetails.Data.D_LocationDetails.isEmpty()) {
                    locationDetailsDDListLiveData.setValue(assetDetails.Data.D_LocationDetails);
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
     * Fetch Asset Class DropDown List
     *
     * @param locationName
     * @param apiResponseListener
     */
    public void fetchAssetClasDetailsRequest(String locationName, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        locationDDValidationText.setValue(null);

        if (selectedDLocationDetail != null) {
            if (!selectedDLocationDetail.Locationname.equals(locationName)) {
                locationDDValidationText.setValue("Location not selected");
                apiResponseListener.onFailed("Location not selected");
                return;
            }
        } else {
            locationDDValidationText.setValue("Location not selected");
            apiResponseListener.onFailed("Location not selected");
            return;
        }

        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();

        tagMappingRepository.fetchAssetClasDetailsRequest(selectedDLocationDetail.Locationcode, new ResponseListener<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                if (assetDetails != null && assetDetails.Data != null && assetDetails.Data.D_AssetClassDetails != null && !assetDetails.Data.D_AssetClassDetails.isEmpty()) {
                    assetClassDetailsDDListLiveData.setValue(assetDetails.Data.D_AssetClassDetails);
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
     * Fetch Asset Detail DropDown List
     *
     * @param assetClass
     * @param locationName
     * @param apiResponseListener
     */
    public void fetchAssetDetailsRequest(String assetClass, String locationName, ApiResponseListener apiResponseListener) {


        if (apiResponseListener == null) {
            return;
        }

        locationDDValidationText.setValue(null);

        if (selectedDLocationDetail != null) {
            if (!selectedDLocationDetail.Locationname.equals(locationName)) {
                locationDDValidationText.setValue("Location not selected");
                apiResponseListener.onFailed("Location not selected");
                return;
            }
        } else {
            locationDDValidationText.setValue("Location not selected");
            apiResponseListener.onFailed("Location not selected");
            return;
        }

        assetClassDDValidationText.setValue(null);

        if (selectedDAssetclassDetail != null) {
            if (!selectedDAssetclassDetail.Assetclassdescription.equals(assetClass)) {
                assetClassDDValidationText.setValue("Asset Class not selected");
                apiResponseListener.onFailed("Asset Class not selected");
                return;
            }
        } else {
            assetClassDDValidationText.setValue("Asset Class not selected");
            apiResponseListener.onFailed("Asset Class not selected");
            return;
        }

        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();


        tagMappingRepository.fetchAssetDetailsRequest(selectedDAssetclassDetail.Assetclasscode, selectedDLocationDetail.Locationcode, new ResponseListener<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                if (assetDetails != null && assetDetails.Data != null && assetDetails.Data.D_AssetDetails != null && !assetDetails.Data.D_AssetDetails.isEmpty()) {
                    assetDetailsDDListLiveData.setValue(assetDetails.Data.D_AssetDetails);
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
     * Fetch Sub Asset Detail DropDown List
     *
     * @param locationName
     * @param assetName
     * @param apiResponseListener
     */
    public void fetchSubAssetDetailsRequest(String locationName, String assetName, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        locationDDValidationText.setValue(null);

        if (selectedDLocationDetail != null) {
            if (!selectedDLocationDetail.Locationname.equals(locationName)) {
                locationDDValidationText.setValue("Location not selected");
                apiResponseListener.onFailed("Location not selected");
                return;
            }
        } else {
            locationDDValidationText.setValue("Location not selected");
            apiResponseListener.onFailed("Location not selected");
            return;
        }


        assetDDValidationText.setValue(null);

        if (selectedDAssetDetail != null) {
            if (!selectedDAssetDetail.SapAssetName.equals(assetName)) {
                assetDDValidationText.setValue("Asset not selected");
                apiResponseListener.onFailed("Asset not selected");
                return;
            }
        } else {
            assetDDValidationText.setValue("Asset not selected");
            apiResponseListener.onFailed("Asset not selected");
            return;
        }

        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();

        tagMappingRepository.fetchSubAssetDetailsRequest(selectedDAssetDetail.SapAssetNo, selectedDLocationDetail.Locationcode, new ResponseListener<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                if (assetDetails != null && assetDetails.Data != null && assetDetails.Data.D_SubAssetDetails != null && !assetDetails.Data.D_SubAssetDetails.isEmpty()) {
                    subAssetDetailsDDListLiveData.setValue(assetDetails.Data.D_SubAssetDetails);
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
     * Fetch Sub Asset Part Detail DropDown List
     *
     * @param tagStatus
     * @param subAssetcode
     * @param apiResponseListener
     */
    public void fetchSubAssetPartDetailsRequest(String tagStatus, String subAssetcode, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        /*locationDDValidationText.setValue(null);

        if (selectedDLocationDetail != null) {
            if (!selectedDLocationDetail.Locationname.equals(locationName)) {
                locationDDValidationText.setValue("Location not selected");
                apiResponseListener.onFailed("Location not selected");
                return;
            }
        } else {
            locationDDValidationText.setValue("Location not selected");
            apiResponseListener.onFailed("Location not selected");
            return;
        }*/

        subAssetDDValidationText.setValue(null);

        if (selectedDSubAssetDetail != null) {
            if (!selectedDSubAssetDetail.Subitemname.equals(subAssetcode)) {
                subAssetDDValidationText.setValue("Sub Asset not selected");
                apiResponseListener.onFailed("Sub Asset not selected");
                return;
            }
        } else {
            subAssetDDValidationText.setValue("Sub Asset not selected");
            apiResponseListener.onFailed("Sub Asset not selected");
            return;
        }

        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();


        tagMappingRepository.fetchSubAssetPartDetailsRequest(selectedDSubAssetDetail.Subassetcode, tagStatus/*selectedDLocationDetail.Locationcode, "RFIDMapping"*/, new ResponseListener<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                if (assetDetails != null && assetDetails.Data != null && assetDetails.Data.D_SubAssetPartDetails != null && !assetDetails.Data.D_SubAssetPartDetails.isEmpty()) {
                    subAssetPartDetailsDDListLiveData.setValue(assetDetails.Data.D_SubAssetPartDetails);
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
     * Fetch All Dropdown Values by Sub Part Code Dropdown Value
     *
     * @param subAssetPartscode
     * @param apiResponseListener
     */
    public void fetchAllDropDownValuesBySubAssetPartDetailsRequest(String subAssetPartscode, ApiResponseListener apiResponseListener) {

        if (apiResponseListener == null) {
            return;
        }

        subAssetPartCodeDDValidationText.setValue(null);

        if (subAssetPartscode == null || subAssetPartscode.trim().isEmpty()) {
            subAssetPartCodeDDValidationText.setValue("Please enter Sub Asset Part Code");
            return;
        }

        isDataLoading.setValue(true);
        TagMappingRepository tagMappingRepository = TagMappingRepository.getInstance();

        tagMappingRepository.fetchAllDropDownValuesBySubAssetPartDetailsRequest(subAssetPartscode, new ResponseListener<FilterAssetRFIDMappingResponse>() {
            @Override
            public void onDataReceived(FilterAssetRFIDMappingResponse assetDetails) {
                if (assetDetails != null && assetDetails.Data != null && assetDetails.Data.D_SubPartDetails != null && !assetDetails.Data.D_SubPartDetails.isEmpty()) {
                    //subAssetPartDetailsDDListLiveData.setValue(assetDetails.Data.D_SubPartDetails);
                    apiResponseListener.onSuccess(assetDetails.Data.D_SubPartDetails.get(0));
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

    //Check Sub Asset Part Present
    public DSubAssetPartDetail isSubAssetPartFound(String SubAssetPartcode) {
        if (SubAssetPartcode == null || subAssetPartDetailsDDListLiveData.getValue() == null) {
            return null;
        }
        for (DSubAssetPartDetail dSubAssetPartDetail : subAssetPartDetailsDDListLiveData.getValue()) {
            if (dSubAssetPartDetail.Subassetpartscode != null && dSubAssetPartDetail.Subassetpartscode.equals(SubAssetPartcode)) {
                return dSubAssetPartDetail;
            }
        }
        return null;
    }

    //Check Sub Asset Present
    public DSubAssetDetail isSubAssetFound(String SubAssetcode) {
        if (SubAssetcode == null || subAssetDetailsDDListLiveData.getValue() == null) {
            return null;
        }
        for (DSubAssetDetail dSubAssetDetail : subAssetDetailsDDListLiveData.getValue()) {
            if (dSubAssetDetail.Subassetcode != null && dSubAssetDetail.Subassetcode.equals(SubAssetcode)) {
                return dSubAssetDetail;
            }
        }
        return null;
    }

    //Check Asset Present
    public DAssetDetail isAssetFound(String Assetcode) {
        if (Assetcode == null || assetDetailsDDListLiveData.getValue() == null) {
            return null;
        }
        for (DAssetDetail dAssetDetail : assetDetailsDDListLiveData.getValue()) {
            if (dAssetDetail.SapAssetNo != null && dAssetDetail.SapAssetNo.equals(Assetcode)) {
                return dAssetDetail;
            }
        }
        return null;
    }

    //Check Asset Class Present
    public DAssetclassDetail isAssetclassFound(String Assetclasscode) {
        if (Assetclasscode == null || assetClassDetailsDDListLiveData.getValue() == null) {
            return null;
        }
        for (DAssetclassDetail dAssetclassDetail : assetClassDetailsDDListLiveData.getValue()) {
            if (dAssetclassDetail.Assetclasscode != null && dAssetclassDetail.Assetclasscode.equals(Assetclasscode)) {
                return dAssetclassDetail;
            }
        }
        return null;
    }

    //Check Location Present
    public DLocationDetail isLocationFound(String Locationcode) {
        if (Locationcode == null || locationDetailsDDListLiveData.getValue() == null) {
            return null;
        }
        for (DLocationDetail dLocationDetail : locationDetailsDDListLiveData.getValue()) {
            if (dLocationDetail.Locationcode != null && dLocationDetail.Locationcode.equals(Locationcode)) {
                return dLocationDetail;
            }
        }
        return null;
    }

    //Check Floor Present
    public DFloorDetail isFloorFound(String floorCode) {
        if (floorCode == null || floorDetailsDDListLiveData.getValue() == null) {
            return null;
        }
        for (DFloorDetail dFloorDetail : floorDetailsDDListLiveData.getValue()) {
            if (dFloorDetail.Floorcode != null && dFloorDetail.Floorcode.equals(floorCode)) {
                return dFloorDetail;
            }
        }
        return null;
    }

    //Check Building Present
    public DBuildingDetail isBuildingFound(String buildingCode) {
        if (buildingCode == null || buildingDetailsDDListLiveData.getValue() == null) {
            return null;
        }
        for (DBuildingDetail dBuildingDetail : buildingDetailsDDListLiveData.getValue()) {
            if (dBuildingDetail.Buildingcode != null && dBuildingDetail.Buildingcode.equals(buildingCode)) {
                return dBuildingDetail;
            }
        }
        return null;
    }

    //Check Company Present
    public DCompanyDetail isCompanyFound(String Companycode) {
        if (Companycode == null || companyDetailsDDListLiveData.getValue() == null) {
            return null;
        }
        for (DCompanyDetail dCompanyDetail : companyDetailsDDListLiveData.getValue()) {
            if (dCompanyDetail.Companycode != null && dCompanyDetail.Companycode.equals(Companycode)) {
                return dCompanyDetail;
            }
        }
        return null;
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













