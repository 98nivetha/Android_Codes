package com.zebra.demo.view.fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.zebra.demo.R;
import com.zebra.demo.data.remote.model.DAssetDetail;
import com.zebra.demo.data.remote.model.DAssetclassDetail;
import com.zebra.demo.data.remote.model.DBuildingDetail;
import com.zebra.demo.data.remote.model.DCompanyDetail;
import com.zebra.demo.data.remote.model.DFloorDetail;
import com.zebra.demo.data.remote.model.DLocationDetail;
import com.zebra.demo.data.remote.model.DSubAssetDetail;
import com.zebra.demo.data.remote.model.DSubAssetPartDetail;
import com.zebra.demo.data.remote.model.DSubPartDetails;
import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingData;
import com.zebra.demo.databinding.DialogSuccessSearchAssetBinding;
import com.zebra.demo.databinding.FragmentAssetSearchBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.activity.MainActivity;
import com.zebra.demo.view.adapter.FilterAutoCompleteTextViewAdapter;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.viewmodel.AssetSearchViewModel;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Asset Search Activity
 */
public class AssetSearchFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, ApiResponseListener {
    AssetSearchViewModel assetSearchViewModel;
    FragmentAssetSearchBinding binding;
    ShowSuccessDialog showSuccessDialog;

    boolean isPageInitLoad = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    private void showExitDialog() {
        if(isFieldDataPresent()) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("")
                    .setCancelable(false)
                    .setMessage("Are you sure you want to go back?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        getParentFragmentManager().popBackStack();
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            getParentFragmentManager().popBackStack();
        }
    }

    private void showClearDialog() {
        if(isFieldDataPresent()) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("")
                    .setCancelable(false)
                    .setMessage("Are you sure you want to clear Data?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        clearAllDataField();
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            clearAllDataField();
        }
    }

    private boolean isFieldDataPresent() {
        if(assetSearchViewModel != null) {
            return assetSearchViewModel.selectedDCompanyDetail != null ||
                    assetSearchViewModel.selectedDBuildingDetail != null ||
                    assetSearchViewModel.selectedDFloorDetail != null ||
                    assetSearchViewModel.selectedDLocationDetail != null ||
                    assetSearchViewModel.selectedDAssetclassDetail != null ||
                    assetSearchViewModel.selectedDAssetDetail != null ||
                    assetSearchViewModel.selectedDSubAssetDetail != null ||
                    assetSearchViewModel.selectedDSubAssetPartDetail != null ||
                    assetSearchViewModel.selectedDSubPartDetails != null;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Set Listener
        setBarcodeRFIDScanResultListener(this);

        //Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_asset_search, container, false);

        //View Model
        assetSearchViewModel = new ViewModelProvider(this).get(AssetSearchViewModel.class);

        //Click Listener
        setOnClickListener();

        if(isPageInitLoad) {

            //Observer
            setObserver();

            //Call initial load Dropdown API
            initMetaDetails();
        } else {
            if(assetSearchViewModel != null) {

                //Company Dropdown
                loadCompanyDropdown(assetSearchViewModel.companyDetailsDDListLiveData.getValue());

                //Building Dropdown
                assetSearchViewModel.buildingDetailsDDListLiveData.observe(getActivity(), this::loadBuildingDropdown);

                //Floor Dropdown
                assetSearchViewModel.floorDetailsDDListLiveData.observe(getActivity(), this::loadFloorDropdown);

                //Location Dropdown
                assetSearchViewModel.locationDetailsDDListLiveData.observe(getActivity(), this::loadLocationDropdown);

                //Asset Class Dropdown
                assetSearchViewModel.assetClassDetailsDDListLiveData.observe(getActivity(), this::loadAssetClassDropdown);

                //Asset Dropdown
                assetSearchViewModel.assetDetailsDDListLiveData.observe(getActivity(), this::loadAssetDropdown);

                //Sub Asset Dropdown
                assetSearchViewModel.subAssetDetailsDDListLiveData.observe(getActivity(), this::loadSubAssetDropdown);

                //Sub Asset Part Code Dropdown
                assetSearchViewModel.subAssetPartDetailsDDListLiveData.observe(getActivity(), this::loadSubAssetPartDropdown);

                //Init Dropdown
                //loadInitDropdown(assetSearchViewModel.metaDetailMutableLiveData.getValue());

                //Asset Code
                //loadAssetCodeDropdown(assetSearchViewModel.assetCodeDropDownList.getValue());

                //Assert Name List
                //loadAssetHeaderDropdown(assetSearchViewModel.assetHeaderDropDownList.getValue());

            }
        }

        isPageInitLoad = false;

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setToolBarTitleWithBack(R.string.asset_search);
        } else if(getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity)getActivity()).setToolBarTitleWithBack(R.string.asset_search);
            ((ActiveDeviceActivity)getActivity()).setSelectedFragment(this);
        }

        //Set Listener
        setBarcodeRFIDScanResultListener(this);
    }

    /**
     * Observers
     */
    private void setObserver() {
        if(assetSearchViewModel == null || getActivity() == null) {
            return;
        }

        //Check Network error
        assetSearchViewModel.getNetworkErrorMutableData().observe(getActivity(), this::loadNetworkErrorView);

        //Progress bar
        assetSearchViewModel.isDataLoading.observe(getActivity(), value -> {
            binding.rltAtvtyProgress.setVisibility(value ? View.VISIBLE : View.GONE);
        });

        //Company Validator
        assetSearchViewModel.companyDDValidationText.observe(getActivity(), value -> {
            binding.elblCompany.setError(value);
            binding.elblCompany.setErrorEnabled(value != null);
        });

        //Building Validator
        assetSearchViewModel.buildingDDValidationText.observe(getActivity(), value -> {
            binding.elblBuildingName.setError(value);
            binding.elblBuildingName.setErrorEnabled(value != null);
        });

        //Floor Validator
        assetSearchViewModel.floorDDValidationText.observe(getActivity(), value -> {
            binding.elblFloor.setError(value);
            binding.elblFloor.setErrorEnabled(value != null);
        });

        //Location Validator
        assetSearchViewModel.locationDDValidationText.observe(getActivity(), value -> {
            binding.elblLocation.setError(value);
            binding.elblLocation.setErrorEnabled(value != null);
        });

        //Asset Class Validator
        assetSearchViewModel.assetClassDDValidationText.observe(getActivity(), value -> {
            binding.elblAssetClass.setError(value);
            binding.elblAssetClass.setErrorEnabled(value != null);
        });

        //Asset Validator
        assetSearchViewModel.assetDDValidationText.observe(getActivity(), value -> {
            binding.elblAsset.setError(value);
            binding.elblAsset.setErrorEnabled(value != null);
        });

        //Sub Asset Validator
        assetSearchViewModel.subAssetDDValidationText.observe(getActivity(), value -> {
            binding.elblSubAsset.setError(value);
            binding.elblSubAsset.setErrorEnabled(value != null);
        });

        //Sub Asset Validator
        assetSearchViewModel.subAssetPartCodeDDValidationText.observe(getActivity(), value -> {
            binding.elblSubAssetPartCode.setError(value);
            binding.elblSubAssetPartCode.setErrorEnabled(value != null);
        });
        //Company Dropdown
        assetSearchViewModel.companyDetailsDDListLiveData.observe(getActivity(), this::loadCompanyDropdown);

        //Building Dropdown
        assetSearchViewModel.buildingDetailsDDListLiveData.observe(getActivity(), this::loadBuildingDropdown);

        //Floor Dropdown
        assetSearchViewModel.floorDetailsDDListLiveData.observe(getActivity(), this::loadFloorDropdown);

        //Location Dropdown
        assetSearchViewModel.locationDetailsDDListLiveData.observe(getActivity(), this::loadLocationDropdown);

        //Asset Class Dropdown
        assetSearchViewModel.assetClassDetailsDDListLiveData.observe(getActivity(), this::loadAssetClassDropdown);

        //Asset Dropdown
        assetSearchViewModel.assetDetailsDDListLiveData.observe(getActivity(), this::loadAssetDropdown);

        //Sub Asset Dropdown
        assetSearchViewModel.subAssetDetailsDDListLiveData.observe(getActivity(), this::loadSubAssetDropdown);

        //Sub Asset Part Code Dropdown
        assetSearchViewModel.subAssetPartDetailsDDListLiveData.observe(getActivity(), this::loadSubAssetPartDropdown);
    }

    //Load Company Dropdown
    private void loadCompanyDropdown(List<DCompanyDetail> dCompanyDetailList) {
        binding.ectlCompany.setText("");
        if (dCompanyDetailList != null) {
            setAutoCompleteTextView(binding.ectlCompany, (List<Object>) (Object) dCompanyDetailList);
        }
    }

    //Load Building Dropdown
    private void loadBuildingDropdown(List<DBuildingDetail> dBuildingDetailList) {
        binding.ectlBuildingName.setText("");
        if (dBuildingDetailList != null) {
            setAutoCompleteTextView(binding.ectlBuildingName, (List<Object>) (Object) dBuildingDetailList);
        }
    }

    //Load Floor Dropdown
    private void loadFloorDropdown(List<DFloorDetail> dFloorDetailList) {
        binding.ectlFloor.setText("");
        if (dFloorDetailList != null) {
            setAutoCompleteTextView(binding.ectlFloor, (List<Object>) (Object) dFloorDetailList);
        }
    }

    //Load Location Dropdown
    private void loadLocationDropdown(List<DLocationDetail> dLocationDetailList) {
        binding.ectlLocation.setText("");
        if (dLocationDetailList != null) {
            setAutoCompleteTextView(binding.ectlLocation, (List<Object>) (Object) dLocationDetailList);
        }
    }

    //Load Asset Class Dropdown
    private void loadAssetClassDropdown(List<DAssetclassDetail> dAssetclassDetailList) {
        binding.ectlAssetClass.setText("");
        if (dAssetclassDetailList != null) {
            setAutoCompleteTextView(binding.ectlAssetClass, (List<Object>) (Object) dAssetclassDetailList);
        }
    }

    //Load Asset Dropdown
    private void loadAssetDropdown(List<DAssetDetail> assetDetailList) {
        binding.ectlAsset.setText("");
        if (assetDetailList != null) {
            setAutoCompleteTextView(binding.ectlAsset, (List<Object>) (Object) assetDetailList);
        }
    }

    //Load Sub Asset Dropdown
    private void loadSubAssetDropdown(List<DSubAssetDetail> dSubAssetDetailList) {
        binding.ectlSubAsset.setText("");
        if (dSubAssetDetailList != null) {
            setAutoCompleteTextView(binding.ectlSubAsset, (List<Object>) (Object) dSubAssetDetailList);
        }
    }

    //Load Sub Asset Part Dropdown
    private void loadSubAssetPartDropdown(List<DSubAssetPartDetail> dSubAssetPartDetailList) {
        binding.ectlSubAssetPartCode.setText("");
        if (dSubAssetPartDetailList != null) {
            setAutoCompleteTextView(binding.ectlSubAssetPartCode, (List<Object>) (Object) dSubAssetPartDetailList);
        }
    }

    /**
     * Load Network Error
     * @param value
     */
    private void loadNetworkErrorView(boolean value) {
        if(value) {
            Utility.showToast("No Internet Connection");
        }
    }

    /**
     * Load Init Dropdown Datas
     *
     * @param filterAssetRFIDMappingData
     */
    private void loadInitDropdown(FilterAssetRFIDMappingData filterAssetRFIDMappingData) {
        if (filterAssetRFIDMappingData == null) {
            return;
        }

        if(assetSearchViewModel != null) {
            //Company
            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
                assetSearchViewModel.companyDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_CompanyDetails);
            }

            //Building
            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
                assetSearchViewModel.buildingDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_BuildingDetails);
            }

            //Floor
            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
                assetSearchViewModel.floorDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_FloorDetails);
            }

            //Location
            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
                assetSearchViewModel.locationDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_LocationDetails);
            }

            //Asset Class
            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
                assetSearchViewModel.assetClassDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_AssetclassDetails);
            }

            //Asset
            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
                assetSearchViewModel.assetDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_AssetResgiterDetails);
            }

            //Sub Asset
            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
                assetSearchViewModel.subAssetDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_AssetSubcodeDetails);
            }

            //Sub Asset Part
            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
                assetSearchViewModel.subAssetPartDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_AssetSubcodepartsDetails);
            }
        }

    }

    private void closeKeyBoard() {
        try {
            if(getActivity() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * Set AutoComplete Common method
     *
     * @param autoCompleteTextView
     * @param objectList
     */
    private void setAutoCompleteTextView(MaterialAutoCompleteTextView autoCompleteTextView, List<Object> objectList) {
        if (objectList != null && getActivity() != null) {

            FilterAutoCompleteTextViewAdapter assetHeaderArrayAdapter = new FilterAutoCompleteTextViewAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, objectList);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(assetHeaderArrayAdapter);
            //Click Listener
            autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
                Object item = adapterView.getItemAtPosition(i);

                if (item instanceof DCompanyDetail) { //Company
                    DCompanyDetail dCompanyDetail = (DCompanyDetail) item;
                    if (assetSearchViewModel != null) {
                        assetSearchViewModel.selectedDCompanyDetail = dCompanyDetail;
                    }
                    resetFieldsOnCompanySelection();
                    fetchBuildingDetailsRequest();
                    closeKeyBoard();
                } else if (item instanceof DBuildingDetail) { //Building
                    DBuildingDetail dBuildingDetail = (DBuildingDetail) item;
                    if (assetSearchViewModel != null) {
                        assetSearchViewModel.selectedDBuildingDetail = dBuildingDetail;
                    }
                    resetFieldsOnBuildingSelection();
                    fetchFloorDetailsRequest();
                    closeKeyBoard();
                } else if (item instanceof DFloorDetail) { //Floor
                    DFloorDetail dFloorDetail = (DFloorDetail) item;
                    if (assetSearchViewModel != null) {
                        assetSearchViewModel.selectedDFloorDetail = dFloorDetail;
                    }
                    resetFieldsOnFloorSelection();
                    fetchLocationDetailsRequest();
                    closeKeyBoard();
                } else if (item instanceof DLocationDetail) { //Location
                    DLocationDetail dLocationDetail = (DLocationDetail) item;
                    if (assetSearchViewModel != null) {
                        assetSearchViewModel.selectedDLocationDetail = dLocationDetail;
                    }
                    resetFieldsOnLocationSelection();
                    fetchAssetClasDetailsRequest();
                    closeKeyBoard();
                } else if (item instanceof DAssetclassDetail) { //Asset Class
                    DAssetclassDetail dAssetclassDetail = (DAssetclassDetail) item;
                    if (assetSearchViewModel != null) {
                        assetSearchViewModel.selectedDAssetclassDetail = dAssetclassDetail;
                    }
                    resetFieldsOnAssetClassSelection();
                    fetchAssetDetailsRequest();
                    closeKeyBoard();
                } else if (item instanceof DAssetDetail) { //Asset Detail
                    DAssetDetail dAssetDetail = (DAssetDetail) item;
                    if (assetSearchViewModel != null) {

                        assetSearchViewModel.selectedDAssetDetail = dAssetDetail;
                    }
                    resetFieldsOnAssetSelection();
                    fetchSubAssetDetailsRequest();
                    closeKeyBoard();
                } else if (item instanceof DSubAssetDetail) { //Sub Asset
                    DSubAssetDetail dSubAssetDetail = (DSubAssetDetail) item;
                    if (assetSearchViewModel != null) {
                        assetSearchViewModel.selectedDSubAssetDetail = dSubAssetDetail;
                    }
                    resetFieldsOnSubAssetSelection();
                    fetchSubAssetPartDetailsRequest();
                    closeKeyBoard();
                } else if (item instanceof DSubAssetPartDetail) { //Sub Asset Part
                    DSubAssetPartDetail dSubAssetPartDetail = (DSubAssetPartDetail) item;
                    if (assetSearchViewModel != null) {
                        assetSearchViewModel.selectedDSubAssetPartDetail = dSubAssetPartDetail;

                        fetchAllDropDownValuesBySubAssetPartDetailsRequest(dSubAssetPartDetail.toString());
                    }
                    closeKeyBoard();
                    binding.ectlSubAssetPartCode.clearFocus();
                }

            });

            //Touch Listener
            /*autoCompleteTextView.setOnTouchListener((v, event) -> {
                autoCompleteTextView.showDropDown();
                return false;
            });*/
        }
    }
    public void setupAppDropdownData(DSubPartDetails dSubPartDetails) {
        if(assetSearchViewModel != null) {

            //Selected Sub Part
            assetSearchViewModel.selectedDSubPartDetails = dSubPartDetails;

            //Company
            DCompanyDetail dCompanyDetail = assetSearchViewModel.isCompanyFound(dSubPartDetails.Companycode);
            if(dCompanyDetail != null) {
                assetSearchViewModel.selectedDCompanyDetail = dCompanyDetail;
                binding.ectlCompany.setText(dCompanyDetail.toString());
            }

            //Building
            DBuildingDetail dBuildingDetail = assetSearchViewModel.isBuildingFound(dSubPartDetails.Building);
            if(dBuildingDetail != null) {
                assetSearchViewModel.selectedDBuildingDetail = dBuildingDetail;
                binding.ectlBuildingName.setText(dBuildingDetail.toString());
            }

            //Floor
            DFloorDetail dFloorDetail = assetSearchViewModel.isFloorFound(dSubPartDetails.Floorcode);
            if(dFloorDetail != null) {
                assetSearchViewModel.selectedDFloorDetail = dFloorDetail;
                binding.ectlFloor.setText(dFloorDetail.toString());
            }

            //Location
            DLocationDetail dLocationDetail = assetSearchViewModel.isLocationFound(dSubPartDetails.Locationcode);
            if(dLocationDetail != null) {
                assetSearchViewModel.selectedDLocationDetail = dLocationDetail;
                binding.ectlLocation.setText(dLocationDetail.toString());
            }

            //Asset Class
            DAssetclassDetail dAssetclassDetail = assetSearchViewModel.isAssetclassFound(dSubPartDetails.Assetclass);
            if(dAssetclassDetail != null) {
                assetSearchViewModel.selectedDAssetclassDetail = dAssetclassDetail;
                binding.ectlAssetClass.setText(dAssetclassDetail.toString());
            }

            //Asset
            DAssetDetail dAssetDetail = assetSearchViewModel.isAssetFound(dSubPartDetails.SapAssetNo);
            if(dAssetDetail != null) {
                assetSearchViewModel.selectedDAssetDetail = dAssetDetail;
                binding.ectlAsset.setText(dAssetDetail.toString());
            }

            //Sub Asset
            DSubAssetDetail dSubAssetDetail = assetSearchViewModel.isSubAssetFound(dSubPartDetails.Subassetcode);
            if(dSubAssetDetail != null) {
                assetSearchViewModel.selectedDSubAssetDetail = dSubAssetDetail;
                binding.ectlSubAsset.setText(dSubAssetDetail.toString());
            }

            //Sub Asset Part Code
            DSubAssetPartDetail dSubAssetPartDetail = assetSearchViewModel.isSubAssetPartFound(dSubPartDetails.Subassetpartscode);
            if(dSubAssetPartDetail != null) {
                assetSearchViewModel.selectedDSubAssetPartDetail = dSubAssetPartDetail;
                binding.ectlSubAssetPartCode.setText(dSubAssetPartDetail.toString());
            }

        }


    }

    //Clear reset All UI and View Model Data
    private void clearAllDataField() {
        binding.ectlCompany.setText("");
        binding.ectlBuildingName.setText("");
        binding.ectlFloor.setText("");
        binding.ectlLocation.setText("");
        binding.ectlAssetClass.setText("");
        binding.ectlAsset.setText("");
        binding.ectlSubAsset.setText("");
        binding.ectlSubAssetPartCode.setText("");

        if (assetSearchViewModel != null) {

            //Reset Selection
            assetSearchViewModel.selectedDCompanyDetail = null;
            assetSearchViewModel.selectedDBuildingDetail = null;
            assetSearchViewModel.selectedDFloorDetail = null;
            assetSearchViewModel.selectedDLocationDetail = null;
            assetSearchViewModel.selectedDAssetclassDetail = null;
            assetSearchViewModel.selectedDAssetDetail = null;
            assetSearchViewModel.selectedDSubAssetDetail = null;
            assetSearchViewModel.selectedDSubAssetPartDetail = null;
            assetSearchViewModel.selectedDSubPartDetails = null;

            //Reset List
            assetSearchViewModel.companyDetailsDDListLiveData.setValue(new ArrayList<>());
            assetSearchViewModel.buildingDetailsDDListLiveData.setValue(new ArrayList<>());
            assetSearchViewModel.floorDetailsDDListLiveData.setValue(new ArrayList<>());
            assetSearchViewModel.locationDetailsDDListLiveData.setValue(new ArrayList<>());
            assetSearchViewModel.assetClassDetailsDDListLiveData.setValue(new ArrayList<>());
            assetSearchViewModel.assetDetailsDDListLiveData.setValue(new ArrayList<>());
            assetSearchViewModel.subAssetDetailsDDListLiveData.setValue(new ArrayList<>());
            assetSearchViewModel.subAssetPartDetailsDDListLiveData.setValue(new ArrayList<>());

            //Rest Error
            assetSearchViewModel.tagCodeValidationText.setValue(null);
            assetSearchViewModel.companyDDValidationText.setValue(null);
            assetSearchViewModel.buildingDDValidationText.setValue(null);
            assetSearchViewModel.floorDDValidationText.setValue(null);
            assetSearchViewModel.locationDDValidationText.setValue(null);
            assetSearchViewModel.assetClassDDValidationText.setValue(null);
            assetSearchViewModel.assetDDValidationText.setValue(null);
            assetSearchViewModel.subAssetDDValidationText.setValue(null);
            assetSearchViewModel.subAssetPartCodeDDValidationText.setValue(null);


            //assetSearchViewModel.assetCodeValidationText.setValue(null);
            if (assetSearchViewModel.metaDetailMutableLiveData != null && assetSearchViewModel.metaDetailMutableLiveData.getValue() != null) {
                loadInitDropdown(assetSearchViewModel.metaDetailMutableLiveData.getValue());
            }
        }
    }


    /* API Fetch Starts */
    //Fetch Data from API
    private void fetchBuildingDetailsRequest() {
        if (assetSearchViewModel == null) {
            return;
        }
        assetSearchViewModel.fetchBuildingDetailsRequest(binding.ectlCompany.getText().toString(), new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {
            }

            @Override
            public void onFailed(Object data) {
                /*binding.ectlAssetName.setText("");
                binding.ectlAssetCode.setText("");*/
                if (data instanceof String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    //Fetch Data from API
    private void fetchFloorDetailsRequest() {
        if (assetSearchViewModel == null) {
            return;
        }
        assetSearchViewModel.fetchFloorDetailsRequest(binding.ectlBuildingName.getText().toString(), new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {
            }

            @Override
            public void onFailed(Object data) {
                if (data instanceof String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    //Fetch Data from API
    private void fetchLocationDetailsRequest() {
        if (assetSearchViewModel == null) {
            return;
        }
        assetSearchViewModel.fetchLocationDetailsRequest(binding.ectlFloor.getText().toString(), binding.ectlBuildingName.getText().toString(), new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {
            }

            @Override
            public void onFailed(Object data) {
                if (data instanceof String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    //Fetch Data from API
    private void fetchAssetClasDetailsRequest() {
        if (assetSearchViewModel == null) {
            return;
        }
        assetSearchViewModel.fetchAssetClasDetailsRequest(binding.ectlLocation.getText().toString(), new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {
            }

            @Override
            public void onFailed(Object data) {
                /*binding.ectlAssetName.setText("");
                binding.ectlAssetCode.setText("");*/
                if (data instanceof String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    //Fetch Data from API
    private void fetchAssetDetailsRequest() {
        if (assetSearchViewModel == null) {
            return;
        }
        assetSearchViewModel.fetchAssetDetailsRequest(binding.ectlAssetClass.getText().toString(), binding.ectlLocation.getText().toString(), new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {
            }

            @Override
            public void onFailed(Object data) {
                /*binding.ectlAssetName.setText("");
                binding.ectlAssetCode.setText("");*/
                if (data instanceof String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    //Fetch Data from API
    private void fetchSubAssetDetailsRequest() {
        if (assetSearchViewModel == null) {
            return;
        }
        assetSearchViewModel.fetchSubAssetDetailsRequest(binding.ectlLocation.getText().toString(), binding.ectlAsset.getText().toString(), new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {
            }

            @Override
            public void onFailed(Object data) {
                /*binding.ectlAssetName.setText("");
                binding.ectlAssetCode.setText("");*/
                if (data instanceof String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    //Fetch SubAssetPart Data from API
    private void fetchSubAssetPartDetailsRequest() {
        if (assetSearchViewModel == null) {
            return;
        }
        assetSearchViewModel.fetchSubAssetPartDetailsRequest("0" , binding.ectlSubAsset.getText().toString(), new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {
            }

            @Override
            public void onFailed(Object data) {
                /*binding.ectlAssetName.setText("");
                binding.ectlAssetCode.setText("");*/
                if (data instanceof String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    //Fetch All Dropdown Data from API
    private void fetchAllDropDownValuesBySubAssetPartDetailsRequest(String subAssetPartCode) {
        if (assetSearchViewModel == null) {
            return;
        }
        assetSearchViewModel.fetchAllDropDownValuesBySubAssetPartDetailsRequest(subAssetPartCode , new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {

                if(data instanceof DSubPartDetails) {
                    DSubPartDetails dSubPartDetails = (DSubPartDetails) data;
                    //String subAssetPartCode = binding.ectlSubAssetPartCode.getText().toString();

                    //Clear All Data
                    clearAllDataField();

                    setupAppDropdownData(dSubPartDetails);


                    //TODO Remove this lines
                   /* showSuccessDialog = new ShowSuccessDialog(getActivity(), dSubPartDetails);
                    showSuccessDialog.show();
                    Window window = showSuccessDialog.getWindow();
                    if (window != null) {
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }*/
                }

            }

            @Override
            public void onFailed(Object data) {
                if (data instanceof String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    /* API Fetch Ends */


    /* Reset Dropdown Starts  */
    //Reset Company Relate fields
    private void resetFieldsOnCompanySelection() {
        binding.ectlBuildingName.setText("");
        if (assetSearchViewModel != null) {
            //Reset Selection
            assetSearchViewModel.selectedDBuildingDetail = null;
            //Reset List
            assetSearchViewModel.buildingDetailsDDListLiveData.setValue(new ArrayList<>());
            //Rest Error
            assetSearchViewModel.buildingDDValidationText.setValue(null);
        }
        resetFieldsOnBuildingSelection();
    }

    //Reset Building Relate fields
    private void resetFieldsOnBuildingSelection() {
        binding.ectlFloor.setText("");
        if (assetSearchViewModel != null) {
            //Reset Selection
            assetSearchViewModel.selectedDFloorDetail = null;
            //Reset List
            assetSearchViewModel.floorDetailsDDListLiveData.setValue(new ArrayList<>());
            //Rest Error
            assetSearchViewModel.floorDDValidationText.setValue(null);
        }
        resetFieldsOnFloorSelection();
    }

    //Reset Floor Relate fields
    private void resetFieldsOnFloorSelection() {
        binding.ectlLocation.setText("");
        if (assetSearchViewModel != null) {

            //Reset Selection
            assetSearchViewModel.selectedDLocationDetail = null;
            //Reset List
            assetSearchViewModel.locationDetailsDDListLiveData.setValue(new ArrayList<>());
            //Rest Error
            assetSearchViewModel.locationDDValidationText.setValue(null);
        }
        resetFieldsOnLocationSelection();
    }

    //Reset Location Relate fields
    private void resetFieldsOnLocationSelection() {
        binding.ectlAssetClass.setText("");
        if (assetSearchViewModel != null) {
            //Reset Selection
            assetSearchViewModel.selectedDAssetclassDetail = null;
            //Reset List
            assetSearchViewModel.assetClassDetailsDDListLiveData.setValue(new ArrayList<>());
            //Rest Error
            assetSearchViewModel.assetClassDDValidationText.setValue(null);
        }
        resetFieldsOnAssetClassSelection();
    }

    //Reset Asset Class Relate fields
    private void resetFieldsOnAssetClassSelection() {
        binding.ectlAsset.setText("");
        if (assetSearchViewModel != null) {
            //Reset Selection
            assetSearchViewModel.selectedDAssetDetail = null;
            //Reset List
            assetSearchViewModel.assetDetailsDDListLiveData.setValue(new ArrayList<>());
            //Rest Error
            assetSearchViewModel.assetDDValidationText.setValue(null);
        }
        resetFieldsOnAssetSelection();
    }

    //Reset Asset Relate fields
    private void resetFieldsOnAssetSelection() {
        binding.ectlSubAsset.setText("");
        if (assetSearchViewModel != null) {
            //Reset Selection
            assetSearchViewModel.selectedDSubAssetDetail = null;
            //Reset List
            assetSearchViewModel.subAssetDetailsDDListLiveData.setValue(new ArrayList<>());
            //Rest Error
            assetSearchViewModel.subAssetDDValidationText.setValue(null);
        }
        resetFieldsOnSubAssetSelection();
    }

    //Reset Sub Asset  Relate fields
    private void resetFieldsOnSubAssetSelection() {

        binding.ectlSubAssetPartCode.setText("");
        if (assetSearchViewModel != null) {
            //Reset Selection
            assetSearchViewModel.selectedDSubAssetPartDetail = null;
            assetSearchViewModel.selectedDSubPartDetails = null;
            //Reset List
            assetSearchViewModel.subAssetPartDetailsDDListLiveData.setValue(new ArrayList<>());
            //Rest Error
            assetSearchViewModel.subAssetPartCodeDDValidationText.setValue(null);
        }
    }

    /* Reset Dropdown Ends  */

    /**
     * OnClick Listener
     */
    private void setOnClickListener() {
        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
        });
        binding.searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeKeyBoard();
                binding.ectlSubAssetPartCode.clearFocus();
                fetchAllDropDownValuesBySubAssetPartDetailsRequest(binding.ectlSubAssetPartCode.getText().toString());
            }
        });

        //Clear Button
        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClearDialog();

            }
        });

        //RFID Button
        binding.btnRFID.setOnClickListener(view -> {
            if(binding.ectlSubAssetPartCode.getText().toString().isEmpty()) {
                if(assetSearchViewModel != null) {
                    assetSearchViewModel.subAssetPartCodeDDValidationText.setValue("Select Sub Asset Part Code");
                }
                return;
            } else {
                if (assetSearchViewModel != null) {
                    assetSearchViewModel.subAssetPartCodeDDValidationText.setValue("");
                }
            }

            if(assetSearchViewModel == null || assetSearchViewModel.selectedDSubPartDetails == null) {
                Utility.showToast("No Sub Asset Part Code selected");
                return;
            }

            Utility.startOrStopEventReceived(getActivity());
            //Todo : Fetch RFID from the device
        });

    }

    /**
     * Load Init Meta Data for Dropdown
     */
    private void initMetaDetails() {
        if(assetSearchViewModel == null) {
            return;
        }
        assetSearchViewModel.fetchMetaDetails(this);
    }


    @Override
    public void onSuccess(Object data) {

    }

    @Override
    public void onFailed(Object data) {
        if(data instanceof  String) {
            Utility.showToast(String.valueOf(data));
        }
    }

    @Override
    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {

    }

    @Override
    public void scannerRFIDEvent(List<InventoryListItem> rfidDataList) {

        try {
            if(getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (rfidDataList != null && !rfidDataList.isEmpty() && assetSearchViewModel.selectedDSubPartDetails != null && assetSearchViewModel.selectedDSubPartDetails.RfidTag != null &&
                                (showSuccessDialog == null || !showSuccessDialog.isShowing())) {
                            for (InventoryListItem inventoryListItem : rfidDataList) {
                                if (assetSearchViewModel.selectedDSubPartDetails.RfidTag.equals(inventoryListItem.getTagID())) {
                                    showSuccessDialog = new ShowSuccessDialog(getActivity(), assetSearchViewModel.selectedDSubPartDetails);
                                    showSuccessDialog.show();
                                    Window window = showSuccessDialog.getWindow();
                                    if (window != null) {
                                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    }
                                    Utility.triggerReleaseStopEventReceived(getActivity());
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
        }
    }

    public class ShowSuccessDialog extends Dialog  {

        public Activity activity;

        DSubPartDetails selectedDSubPartDetails;
        DialogSuccessSearchAssetBinding binding;

        public ShowSuccessDialog(Activity activity, DSubPartDetails selectedDSubPartDetails) {
            super(activity);
            this.activity = activity;
            this.selectedDSubPartDetails = selectedDSubPartDetails;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setCancelable(false);
            DialogSuccessSearchAssetBinding binding = DataBindingUtil
                    .inflate(LayoutInflater.from(activity), R.layout.dialog_success_search_asset, null, false);
            setContentView(binding.getRoot());

            binding.closeImg.setOnClickListener(view -> dismiss());
            binding.btnViewAsset.setOnClickListener(view -> {
                      if(selectedDSubPartDetails != null && selectedDSubPartDetails.RfidTag != null && activity != null) {
                          AssetViewFragment fragment = new AssetViewFragment();
                          Bundle bundle = new Bundle();
                          bundle.putString(AssetViewFragment.ASSET_SR_CODE_KEY,selectedDSubPartDetails.RfidTag);
                          fragment.setArguments(bundle);
                          loadFragment(fragment);
                      }
            });
        }

        private void loadFragment(Fragment fragment) {
            if(getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToFragmentAddBackStack(fragment);
            } else if(getActivity() instanceof ActiveDeviceActivity) {
                ((ActiveDeviceActivity) getActivity()).switchToFragmentAddBackStack(fragment);
            }
            dismiss();
        }
    }
}