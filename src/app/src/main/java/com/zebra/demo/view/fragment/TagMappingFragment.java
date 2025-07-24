//package com.zebra.demo.view.fragment;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//
//import androidx.activity.OnBackPressedCallback;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.databinding.DataBindingUtil;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.google.android.material.textfield.MaterialAutoCompleteTextView;
//import com.zebra.demo.R;
//import com.zebra.demo.data.remote.model.DAssetDetail;
//import com.zebra.demo.data.remote.model.DAssetclassDetail;
//import com.zebra.demo.data.remote.model.DBuildingDetail;
//import com.zebra.demo.data.remote.model.DCompanyDetail;
//import com.zebra.demo.data.remote.model.DFloorDetail;
//import com.zebra.demo.data.remote.model.DLocationDetail;
//import com.zebra.demo.data.remote.model.DSubAssetDetail;
//import com.zebra.demo.data.remote.model.DSubAssetPartDetail;
//import com.zebra.demo.data.remote.model.DSubPartDetails;
//import com.zebra.demo.data.remote.model.FilterAssetRFIDMappingData;
//import com.zebra.demo.data.remote.responsemodels.InwardDetail;
//import com.zebra.demo.data.remote.responsemodels.RfidMappingData;
//import com.zebra.demo.data.remote.responsemodels.RfidMappingResponse;
//import com.zebra.demo.databinding.FragmentTagMappingBinding;
//import com.zebra.demo.utility.Utility;
//import com.zebra.demo.view.activity.MainActivity;
//import com.zebra.demo.view.adapter.FilterAutoCompleteTextViewAdapter;
//import com.zebra.demo.view.listener.ApiResponseListener;
//import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
//import com.zebra.demo.viewmodel.IBA_VIEWMODEL.RfidMappingViewModel;
//import com.zebra.demo.viewmodel.TagMappingViewModel;
//import com.zebra.demo.zebralib.ActiveDeviceActivity;
//import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Tag Mapping Activity
// */
//public class TagMappingFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, ApiResponseListener {
//
//    //View Model
//    TagMappingViewModel tagMappingViewModel;
//    RfidMappingViewModel rfidMappingViewModel;
//    //Binding
   // FragmentTagMappingBinding binding;
//
//
//    //Auto check callback controller
//    boolean isAutoChecked = false;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                showExitDialog();
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
//
//    }
//
//    private void showExitDialog() {
//        if (isFieldDataPresent()) {
//            new AlertDialog.Builder(requireContext())
//                    .setTitle("")
//                    .setCancelable(false)
//                    .setMessage("Are you sure you want to go back?")
//                    .setPositiveButton("Yes", (dialog, which) -> {
//                        getParentFragmentManager().popBackStack();
//                    })
//                    .setNegativeButton("No", null)
//                    .show();
//        } else {
//            getParentFragmentManager().popBackStack();
//        }
//    }
//
//    private void showClearDialog() {
//        if (isFieldDataPresent()) {
//            new AlertDialog.Builder(requireContext())
//                    .setTitle("")
//                    .setCancelable(false)
//                    .setMessage("Are you sure you want to clear Data?")
//                    .setPositiveButton("Yes", (dialog, which) -> {
//                        clearAllDataField();
//                    })
//                    .setNegativeButton("No", null)
//                    .show();
//        } else {
//            clearAllDataField();
//        }
//    }
//
//    private void showSwitchMappingDialog() {
//        if (isFieldDataPresent()) {
//            new AlertDialog.Builder(requireContext())
//                    .setTitle("")
//                    .setCancelable(false)
//                    .setMessage("By selecting New/Re Mapping Data will be clear, do you want to proceed ?")
//                    .setPositiveButton("Yes", (dialog, which) -> {
//                        clearAllDataField();
//                    })
//                    .setNegativeButton("No", (dialogInterface, i) -> {
//
//                        if (binding.mappingTGtp.getCheckedButtonId() == binding.newMappingBtn.getId()) {
//                            binding.mappingTGtp.check(binding.remappingBtn.getId());
//                        } else {
//                            binding.mappingTGtp.check(binding.newMappingBtn.getId());
//                        }
//
//                    })
//                    .show();
//        } else {
//            clearAllDataField();
//        }
//    }
//
//    private boolean isFieldDataPresent() {
//        if (tagMappingViewModel != null && binding != null && binding.ectlTagCode.getText() != null) {
//            return tagMappingViewModel.selectedDCompanyDetail != null ||
//                    tagMappingViewModel.selectedDBuildingDetail != null ||
//                    tagMappingViewModel.selectedDFloorDetail != null ||
//                    tagMappingViewModel.selectedDLocationDetail != null ||
//                    tagMappingViewModel.selectedDAssetclassDetail != null ||
//                    tagMappingViewModel.selectedDAssetDetail != null ||
//                    tagMappingViewModel.selectedDSubAssetDetail != null ||
//                    tagMappingViewModel.selectedDSubAssetPartDetail != null ||
//                    !binding.ectlTagCode.getText().toString().isEmpty();
//        }
//        return false;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        //Set Listener
//        setBarcodeRFIDScanResultListener(this);
//
//        //Binding
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tag_mapping, container, false);
//
//        //View Model
//        tagMappingViewModel = new ViewModelProvider(this).get(TagMappingViewModel.class);
//
//        //Click Listener
//        setOnClickListener();
//
//        //Observer
//        setObserver();
//
//        //Call initial load Dropdown API
//        initMetaDetails();
//
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (getActivity() instanceof MainActivity) {
//            ((MainActivity) getActivity()).setToolBarTitleWithBack(R.string.tag_mapping);
//        } else if (getActivity() instanceof ActiveDeviceActivity) {
//            ((ActiveDeviceActivity) getActivity()).setToolBarTitleWithBack(R.string.tag_mapping);
//            ((ActiveDeviceActivity) getActivity()).setSelectedFragment(this);
//        }
//
//        //Set Listener
//        setBarcodeRFIDScanResultListener(this);
//    }
//
//
//    /**
//     * Observers
//     */
//    private void setObserver() {
//        if (tagMappingViewModel == null || getActivity() == null) {
//            return;
//        }
//
//        //Check Network error
//        tagMappingViewModel.getNetworkErrorMutableData().observe(getActivity(), this::loadNetworkErrorView);
//
//        //Progress bar
//        tagMappingViewModel.isDataLoading.observe(getActivity(), value -> {
//            binding.rltAtvtyProgress.setVisibility(value ? View.VISIBLE : View.GONE);
//        });
//
//        //Tag No Validator
//        tagMappingViewModel.tagCodeValidationText.observe(getActivity(), value -> {
//            binding.elblTagCode.setError(value);
//            binding.elblTagCode.setErrorEnabled(value != null);
//        });
//
//        //Company Validator
//        tagMappingViewModel.companyDDValidationText.observe(getActivity(), value -> {
//            binding.elblCompany.setError(value);
//            binding.elblCompany.setErrorEnabled(value != null);
//        });
//
//        //Building Validator
//        tagMappingViewModel.buildingDDValidationText.observe(getActivity(), value -> {
//            binding.elblBuildingName.setError(value);
//            binding.elblBuildingName.setErrorEnabled(value != null);
//        });
//
//        //Floor Validator
//        tagMappingViewModel.floorDDValidationText.observe(getActivity(), value -> {
//            binding.elblFloor.setError(value);
//            binding.elblFloor.setErrorEnabled(value != null);
//        });
//
//        //Location Validator
//        tagMappingViewModel.locationDDValidationText.observe(getActivity(), value -> {
//            binding.elblLocation.setError(value);
//            binding.elblLocation.setErrorEnabled(value != null);
//        });
//
//        //Asset Class Validator
//        tagMappingViewModel.assetClassDDValidationText.observe(getActivity(), value -> {
//            binding.elblAssetClass.setError(value);
//            binding.elblAssetClass.setErrorEnabled(value != null);
//        });
//
//        //Asset Validator
//        tagMappingViewModel.assetDDValidationText.observe(getActivity(), value -> {
//            binding.elblAsset.setError(value);
//            binding.elblAsset.setErrorEnabled(value != null);
//        });
//
//        //Sub Asset Validator
//        tagMappingViewModel.subAssetDDValidationText.observe(getActivity(), value -> {
//            binding.elblSubAsset.setError(value);
//            binding.elblSubAsset.setErrorEnabled(value != null);
//        });
//
//        //Sub Asset Validator
//        tagMappingViewModel.subAssetPartCodeDDValidationText.observe(getActivity(), value -> {
//            binding.elblSubAssetPartCode.setError(value);
//            binding.elblSubAssetPartCode.setErrorEnabled(value != null);
//        });
//
//
//        //Company Dropdown
//        tagMappingViewModel.companyDetailsDDListLiveData.observe(getActivity(), this::loadCompanyDropdown);
//
//        //Building Dropdown
//        tagMappingViewModel.buildingDetailsDDListLiveData.observe(getActivity(), this::loadBuildingDropdown);
//
//        //Floor Dropdown
//        tagMappingViewModel.floorDetailsDDListLiveData.observe(getActivity(), this::loadFloorDropdown);
//
//        //Location Dropdown
//        tagMappingViewModel.locationDetailsDDListLiveData.observe(getActivity(), this::loadLocationDropdown);
//
//        //Asset Class Dropdown
//        tagMappingViewModel.assetClassDetailsDDListLiveData.observe(getActivity(), this::loadAssetClassDropdown);
//
//        //Asset Dropdown
//        tagMappingViewModel.assetDetailsDDListLiveData.observe(getActivity(), this::loadAssetDropdown);
//
//        //Sub Asset Dropdown
//        tagMappingViewModel.subAssetDetailsDDListLiveData.observe(getActivity(), this::loadSubAssetDropdown);
//
//        //Sub Asset Part Code Dropdown
//        tagMappingViewModel.subAssetPartDetailsDDListLiveData.observe(getActivity(), this::loadSubAssetPartDropdown);
//
//
//    }
//
//    //Load Company Dropdown
//    private void loadCompanyDropdown(List<DCompanyDetail> dCompanyDetailList) {
//        binding.ectlCompany.setText("");
//        if (dCompanyDetailList != null) {
//            setAutoCompleteTextView(binding.ectlCompany, (List<Object>) (Object) dCompanyDetailList);
//        }
//    }
//
//    //Load Building Dropdown
//    private void loadBuildingDropdown(List<DBuildingDetail> dBuildingDetailList) {
//        binding.ectlBuildingName.setText("");
//        if (dBuildingDetailList != null) {
//            setAutoCompleteTextView(binding.ectlBuildingName, (List<Object>) (Object) dBuildingDetailList);
//        }
//    }
//
//    //Load Floor Dropdown
//    private void loadFloorDropdown(List<DFloorDetail> dFloorDetailList) {
//        binding.ectlFloor.setText("");
//        if (dFloorDetailList != null) {
//            setAutoCompleteTextView(binding.ectlFloor, (List<Object>) (Object) dFloorDetailList);
//        }
//    }
//
//    //Load Location Dropdown
//    private void loadLocationDropdown(List<DLocationDetail> dLocationDetailList) {
//        binding.ectlLocation.setText("");
//        if (dLocationDetailList != null) {
//            setAutoCompleteTextView(binding.ectlLocation, (List<Object>) (Object) dLocationDetailList);
//        }
//    }
//
//    //Load Asset Class Dropdown
//    private void loadAssetClassDropdown(List<DAssetclassDetail> dAssetclassDetailList) {
//        binding.ectlAssetClass.setText("");
//        if (dAssetclassDetailList != null) {
//            setAutoCompleteTextView(binding.ectlAssetClass, (List<Object>) (Object) dAssetclassDetailList);
//        }
//    }
//
//    //Load Asset Dropdown
//    private void loadAssetDropdown(List<DAssetDetail> assetDetailList) {
//        binding.ectlAsset.setText("");
//        if (assetDetailList != null) {
//            setAutoCompleteTextView(binding.ectlAsset, (List<Object>) (Object) assetDetailList);
//        }
//    }
//
//    //Load Sub Asset Dropdown
//    private void loadSubAssetDropdown(List<DSubAssetDetail> dSubAssetDetailList) {
//        binding.ectlSubAsset.setText("");
//        if (dSubAssetDetailList != null) {
//            setAutoCompleteTextView(binding.ectlSubAsset, (List<Object>) (Object) dSubAssetDetailList);
//        }
//    }
//
//    //Load Sub Asset Part Dropdown
//    private void loadSubAssetPartDropdown(List<DSubAssetPartDetail> dSubAssetPartDetailList) {
//        binding.ectlSubAssetPartCode.setText("");
//        if (dSubAssetPartDetailList != null) {
//            setAutoCompleteTextView(binding.ectlSubAssetPartCode, (List<Object>) (Object) dSubAssetPartDetailList);
//        }
//    }
//
//    /**
//     * Load Network Error
//     *
//     * @param value
//     */
//    private void loadNetworkErrorView(boolean value) {
//        if (value) {
//            Utility.showToast("No Internet Connection");
//        }
//    }
//
//    /**
//     * Load Init Dropdown Datas
//     *
//     * @param filterAssetRFIDMappingData
//     */
//    private void loadInitDropdown(FilterAssetRFIDMappingData filterAssetRFIDMappingData) {
//        if (filterAssetRFIDMappingData == null) {
//            return;
//        }
//
//        if (tagMappingViewModel != null) {
//            //Company
//            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
//                tagMappingViewModel.companyDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_CompanyDetails);
//            }
//
//            //Building
//            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
//                tagMappingViewModel.buildingDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_BuildingDetails);
//            }
//
//            //Floor
//            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
//                tagMappingViewModel.floorDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_FloorDetails);
//            }
//
//            //Location
//            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
//                tagMappingViewModel.locationDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_LocationDetails);
//            }
//
//            //Asset Class
//            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
//                tagMappingViewModel.assetClassDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_AssetclassDetails);
//            }
//
//            //Asset
//            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
//                tagMappingViewModel.assetDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_AssetResgiterDetails);
//            }
//
//            //Sub Asset
//            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
//                tagMappingViewModel.subAssetDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_AssetSubcodeDetails);
//            }
//
//            //Sub Asset Part
//            if (filterAssetRFIDMappingData.D_CompanyDetails != null) {
//                tagMappingViewModel.subAssetPartDetailsDDListLiveData.setValue(filterAssetRFIDMappingData.D_AssetSubcodepartsDetails);
//            }
//        }
//
//    }
//
//
//
//
//    private void closeKeyBoard() {
//        try {
//            if (getActivity() != null) {
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (getActivity().getCurrentFocus() != null) {
//                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//                }
//            }
//        } catch (Exception e) {
//
//        }
//
//    }
//
//    /**
//     * Set AutoComplete Common method
//     *
//     * @param autoCompleteTextView
//     * @param objectList
//     */
//    private void setAutoCompleteTextView(MaterialAutoCompleteTextView autoCompleteTextView, List<Object> objectList) {
//        if (objectList != null && getActivity() != null) {
//
//            FilterAutoCompleteTextViewAdapter assetHeaderArrayAdapter = new FilterAutoCompleteTextViewAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, objectList);
//            autoCompleteTextView.setThreshold(1);
//            autoCompleteTextView.setAdapter(assetHeaderArrayAdapter);
//            //Click Listener
//            autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
//                Object item = adapterView.getItemAtPosition(i);
//
//                if (item instanceof DCompanyDetail) { //Company
//                    DCompanyDetail dCompanyDetail = (DCompanyDetail) item;
//                    if (tagMappingViewModel != null) {
//                        tagMappingViewModel.selectedDCompanyDetail = dCompanyDetail;
//                    }
//                    resetFieldsOnCompanySelection();
//                    fetchBuildingDetailsRequest();
//                    closeKeyBoard();
//                } else if (item instanceof DBuildingDetail) { //Building
//                    DBuildingDetail dBuildingDetail = (DBuildingDetail) item;
//                    if (tagMappingViewModel != null) {
//                        tagMappingViewModel.selectedDBuildingDetail = dBuildingDetail;
//                    }
//                    resetFieldsOnBuildingSelection();
//                    fetchFloorDetailsRequest();
//                    closeKeyBoard();
//                } else if (item instanceof DFloorDetail) { //Floor
//                    DFloorDetail dFloorDetail = (DFloorDetail) item;
//                    if (tagMappingViewModel != null) {
//                        tagMappingViewModel.selectedDFloorDetail = dFloorDetail;
//                    }
//                    resetFieldsOnFloorSelection();
//                    fetchLocationDetailsRequest();
//                    closeKeyBoard();
//                } else if (item instanceof DLocationDetail) { //Location
//                    DLocationDetail dLocationDetail = (DLocationDetail) item;
//                    if (tagMappingViewModel != null) {
//                        tagMappingViewModel.selectedDLocationDetail = dLocationDetail;
//                    }
//                    resetFieldsOnLocationSelection();
//                    fetchAssetClasDetailsRequest();
//                    closeKeyBoard();
//                } else if (item instanceof DAssetclassDetail) { //Asset Class
//                    DAssetclassDetail dAssetclassDetail = (DAssetclassDetail) item;
//                    if (tagMappingViewModel != null) {
//                        tagMappingViewModel.selectedDAssetclassDetail = dAssetclassDetail;
//                    }
//                    resetFieldsOnAssetClassSelection();
//                    fetchAssetDetailsRequest();
//                    closeKeyBoard();
//                } else if (item instanceof DAssetDetail) { //Asset Detail
//                    DAssetDetail dAssetDetail = (DAssetDetail) item;
//                    if (tagMappingViewModel != null) {
//
//                        tagMappingViewModel.selectedDAssetDetail = dAssetDetail;
//                    }
//                    resetFieldsOnAssetSelection();
//                    fetchSubAssetDetailsRequest();
//                    closeKeyBoard();
//                } else if (item instanceof DSubAssetDetail) { //Sub Asset
//                    DSubAssetDetail dSubAssetDetail = (DSubAssetDetail) item;
//                    if (tagMappingViewModel != null) {
//                        tagMappingViewModel.selectedDSubAssetDetail = dSubAssetDetail;
//                    }
//                    resetFieldsOnSubAssetSelection();
//                    fetchSubAssetPartDetailsRequest();
//                    closeKeyBoard();
//                } else if (item instanceof DSubAssetPartDetail) { //Sub Asset Part
//                    DSubAssetPartDetail dSubAssetPartDetail = (DSubAssetPartDetail) item;
//                    if (tagMappingViewModel != null) {
//                        tagMappingViewModel.selectedDSubAssetPartDetail = dSubAssetPartDetail;
//                        fetchAllDropDownValuesBySubAssetPartDetailsRequest();
//                    }
//                    closeKeyBoard();
//                    binding.ectlSubAssetPartCode.clearFocus();
//                }
//
//            });
//
//            //Touch Listener
//            /*autoCompleteTextView.setOnTouchListener((v, event) -> {
//                autoCompleteTextView.showDropDown();
//                return false;
//            });*/
//        }
//    }
//
//
//    //Setup all Dropdown Values
//    public void setupAppDropdownData(DSubPartDetails dSubPartDetails) {
//        if (tagMappingViewModel != null) {
//
//            //Company
//            DCompanyDetail dCompanyDetail = tagMappingViewModel.isCompanyFound(dSubPartDetails.Companycode);
//            if (dCompanyDetail != null) {
//                tagMappingViewModel.selectedDCompanyDetail = dCompanyDetail;
//                binding.ectlCompany.setText(dCompanyDetail.toString());
//            }
//
//            //Building
//            DBuildingDetail dBuildingDetail = tagMappingViewModel.isBuildingFound(dSubPartDetails.Building);
//            if (dBuildingDetail != null) {
//                tagMappingViewModel.selectedDBuildingDetail = dBuildingDetail;
//                binding.ectlBuildingName.setText(dBuildingDetail.toString());
//            }
//
//            //Floor
//            DFloorDetail dFloorDetail = tagMappingViewModel.isFloorFound(dSubPartDetails.Floorcode);
//            if (dFloorDetail != null) {
//                tagMappingViewModel.selectedDFloorDetail = dFloorDetail;
//                binding.ectlFloor.setText(dFloorDetail.toString());
//            }
//
//            //Location
//            DLocationDetail dLocationDetail = tagMappingViewModel.isLocationFound(dSubPartDetails.Locationcode);
//            if (dLocationDetail != null) {
//                tagMappingViewModel.selectedDLocationDetail = dLocationDetail;
//                binding.ectlLocation.setText(dLocationDetail.toString());
//            }
//
//            //Asset Class
//            DAssetclassDetail dAssetclassDetail = tagMappingViewModel.isAssetclassFound(dSubPartDetails.Assetclass);
//            if (dAssetclassDetail != null) {
//                tagMappingViewModel.selectedDAssetclassDetail = dAssetclassDetail;
//                binding.ectlAssetClass.setText(dAssetclassDetail.toString());
//            }
//
//            //Asset
//            DAssetDetail dAssetDetail = tagMappingViewModel.isAssetFound(dSubPartDetails.SapAssetNo);
//            if (dAssetDetail != null) {
//                tagMappingViewModel.selectedDAssetDetail = dAssetDetail;
//                binding.ectlAsset.setText(dAssetDetail.toString());
//            }
//
//            //Sub Asset
//            DSubAssetDetail dSubAssetDetail = tagMappingViewModel.isSubAssetFound(dSubPartDetails.Subassetcode);
//            if (dSubAssetDetail != null) {
//                tagMappingViewModel.selectedDSubAssetDetail = dSubAssetDetail;
//                binding.ectlSubAsset.setText(dSubAssetDetail.toString());
//            }
//
//            //Sub Asset Part Code
//            DSubAssetPartDetail dSubAssetPartDetail = tagMappingViewModel.isSubAssetPartFound(dSubPartDetails.Subassetpartscode);
//            if (dSubAssetPartDetail != null) {
//                tagMappingViewModel.selectedDSubAssetPartDetail = dSubAssetPartDetail;
//                tagMappingViewModel.selectedDSubAssetPartDetail.Assetrfidmappingid = dSubPartDetails.Assetrfidmappingid;
//                tagMappingViewModel.selectedDSubAssetPartDetail.Assetrfidmappingcode = dSubPartDetails.Assetrfidmappingcode;
//                tagMappingViewModel.selectedDSubAssetPartDetail.Createdon = dSubPartDetails.Createdon;
//
//                binding.ectlSubAssetPartCode.setText(dSubAssetPartDetail.toString());
//            }
//
//        }
//
//
//    }
//
//    //Clear reset All UI and View Model Data
//    private void clearAllDataField() {
//        binding.ectlCompany.setText("");
//        binding.ectlBuildingName.setText("");
//        binding.ectlFloor.setText("");
//        binding.ectlLocation.setText("");
//        binding.ectlAssetClass.setText("");
//        binding.ectlAsset.setText("");
//        binding.ectlSubAsset.setText("");
//        binding.ectlSubAssetPartCode.setText("");
//        binding.ectlTagCode.setText("");
//
//        if (tagMappingViewModel != null) {
//
//            //Reset Selection
//            tagMappingViewModel.selectedDCompanyDetail = null;
//            tagMappingViewModel.selectedDBuildingDetail = null;
//            tagMappingViewModel.selectedDFloorDetail = null;
//            tagMappingViewModel.selectedDLocationDetail = null;
//            tagMappingViewModel.selectedDAssetclassDetail = null;
//            tagMappingViewModel.selectedDAssetDetail = null;
//            tagMappingViewModel.selectedDSubAssetDetail = null;
//            tagMappingViewModel.selectedDSubAssetPartDetail = null;
//
//            //Reset List
//            tagMappingViewModel.companyDetailsDDListLiveData.setValue(new ArrayList<>());
//            tagMappingViewModel.buildingDetailsDDListLiveData.setValue(new ArrayList<>());
//            tagMappingViewModel.floorDetailsDDListLiveData.setValue(new ArrayList<>());
//            tagMappingViewModel.locationDetailsDDListLiveData.setValue(new ArrayList<>());
//            tagMappingViewModel.assetClassDetailsDDListLiveData.setValue(new ArrayList<>());
//            tagMappingViewModel.assetDetailsDDListLiveData.setValue(new ArrayList<>());
//            tagMappingViewModel.subAssetDetailsDDListLiveData.setValue(new ArrayList<>());
//            tagMappingViewModel.subAssetPartDetailsDDListLiveData.setValue(new ArrayList<>());
//
//            //Rest Error
//            tagMappingViewModel.tagCodeValidationText.setValue(null);
//            tagMappingViewModel.companyDDValidationText.setValue(null);
//            tagMappingViewModel.buildingDDValidationText.setValue(null);
//            tagMappingViewModel.floorDDValidationText.setValue(null);
//            tagMappingViewModel.locationDDValidationText.setValue(null);
//            tagMappingViewModel.assetClassDDValidationText.setValue(null);
//            tagMappingViewModel.assetDDValidationText.setValue(null);
//            tagMappingViewModel.subAssetDDValidationText.setValue(null);
//            tagMappingViewModel.subAssetPartCodeDDValidationText.setValue(null);
//
//
//
//        }
//    }
//
//
//    /* API Fetch Starts */
//    //Fetch Data from API
//    private void fetchBuildingDetailsRequest() {
//        if (tagMappingViewModel == null) {
//            return;
//        }
//        tagMappingViewModel.fetchBuildingDetailsRequest(binding.ectlCompany.getText().toString(), new ApiResponseListener() {
//            @Override
//            public void onSuccess(Object data) {
//            }
//
//            @Override
//            public void onFailed(Object data) {
//                /*binding.ectlAssetName.setText("");
//                binding.ectlAssetCode.setText("");*/
//                if (data instanceof String) {
//                    Utility.showToast(String.valueOf(data));
//                }
//            }
//        });
//    }
//
//    //Fetch Data from API
//    private void fetchFloorDetailsRequest() {
//        if (tagMappingViewModel == null) {
//            return;
//        }
//        tagMappingViewModel.fetchFloorDetailsRequest(binding.ectlBuildingName.getText().toString(), new ApiResponseListener() {
//            @Override
//            public void onSuccess(Object data) {
//            }
//
//            @Override
//            public void onFailed(Object data) {
//                if (data instanceof String) {
//                    Utility.showToast(String.valueOf(data));
//                }
//            }
//        });
//    }
//
//    //Fetch Data from API
//    private void fetchLocationDetailsRequest() {
//        if (tagMappingViewModel == null) {
//            return;
//        }
//        tagMappingViewModel.fetchLocationDetailsRequest(binding.ectlFloor.getText().toString(), binding.ectlBuildingName.getText().toString(), new ApiResponseListener() {
//            @Override
//            public void onSuccess(Object data) {
//            }
//
//            @Override
//            public void onFailed(Object data) {
//                if (data instanceof String) {
//                    Utility.showToast(String.valueOf(data));
//                }
//            }
//        });
//    }
//
//    //Fetch Data from API
//    private void fetchAssetClasDetailsRequest() {
//        if (tagMappingViewModel == null) {
//            return;
//        }
//        tagMappingViewModel.fetchAssetClasDetailsRequest(binding.ectlLocation.getText().toString(), new ApiResponseListener() {
//            @Override
//            public void onSuccess(Object data) {
//            }
//
//            @Override
//            public void onFailed(Object data) {
//                /*binding.ectlAssetName.setText("");
//                binding.ectlAssetCode.setText("");*/
//                if (data instanceof String) {
//                    Utility.showToast(String.valueOf(data));
//                }
//            }
//        });
//    }
//
//    //Fetch Data from API
//    private void fetchAssetDetailsRequest() {
//        if (tagMappingViewModel == null) {
//            return;
//        }
//        tagMappingViewModel.fetchAssetDetailsRequest(binding.ectlAssetClass.getText().toString(), binding.ectlLocation.getText().toString(), new ApiResponseListener() {
//            @Override
//            public void onSuccess(Object data) {
//            }
//
//            @Override
//            public void onFailed(Object data) {
//                /*binding.ectlAssetName.setText("");
//                binding.ectlAssetCode.setText("");*/
//                if (data instanceof String) {
//                    Utility.showToast(String.valueOf(data));
//                }
//            }
//        });
//    }
//
//    //Fetch Data from API
//    private void fetchSubAssetDetailsRequest() {
//        if (tagMappingViewModel == null) {
//            return;
//        }
//        tagMappingViewModel.fetchSubAssetDetailsRequest(binding.ectlLocation.getText().toString(), binding.ectlAsset.getText().toString(), new ApiResponseListener() {
//            @Override
//            public void onSuccess(Object data) {
//            }
//
//            @Override
//            public void onFailed(Object data) {
//                /*binding.ectlAssetName.setText("");
//                binding.ectlAssetCode.setText("");*/
//                if (data instanceof String) {
//                    Utility.showToast(String.valueOf(data));
//                }
//            }
//        });
//    }
//
//    //Fetch SubAssetPart Data from API
//    private void fetchSubAssetPartDetailsRequest() {
//        if (tagMappingViewModel == null) {
//            return;
//        }
//        //tagMappingViewModel.fetchSubAssetPartDetailsRequest(binding.mappingTGtp.getCheckedButtonId() == binding.remappingBtn.getId() ? "0" : "1", /*binding.ectlLocation.getText().toString(),*/ binding.ectlSubAsset.getText().toString(), new ApiResponseListener() {
//        tagMappingViewModel.fetchSubAssetPartDetailsRequest(binding.mappingTGtp.getCheckedButtonId() == binding.remappingBtn.getId() ? "1" : "0", /*binding.ectlLocation.getText().toString(),*/ binding.ectlSubAsset.getText().toString(), new ApiResponseListener() {
//            @Override
//            public void onSuccess(Object data) {
//            }
//
//            @Override
//            public void onFailed(Object data) {
//                /*binding.ectlAssetName.setText("");
//                binding.ectlAssetCode.setText("");*/
//                if (data instanceof String) {
//                    Utility.showToast(String.valueOf(data));
//                }
//            }
//        });
//    }
//
//    //Fetch All Dropdown Data from API
//    private void fetchAllDropDownValuesBySubAssetPartDetailsRequest() {
//        if (tagMappingViewModel == null) {
//            return;
//        }
//
//
//        tagMappingViewModel.fetchAllDropDownValuesBySubAssetPartDetailsRequest(binding.ectlSubAssetPartCode.getText().toString(), new ApiResponseListener() {
//            @Override
//            public void onSuccess(Object data) {
//
//                if (data instanceof DSubPartDetails) {
//                    DSubPartDetails dSubPartDetails = (DSubPartDetails) data;
//                    //String subAssetPartCode = binding.ectlSubAssetPartCode.getText().toString();
//
//                    String tagNo = "";
//                    if (binding.ectlTagCode.getText() != null) {
//                        tagNo = binding.ectlTagCode.getText().toString();
//                    }
//                    //Clear All Data
//                    clearAllDataField();
//
//                    binding.ectlTagCode.setText(tagNo);
//
//                    setupAppDropdownData(dSubPartDetails);
//
//
//                }
//
//            }
//
//            @Override
//            public void onFailed(Object data) {
//                if (data instanceof String) {
//                    Utility.showToast(String.valueOf(data));
//                }
//            }
//        });
//    }
//
//    /* API Fetch Ends */
//
//    /* Reset Dropdown Starts  */
//    //Reset Company Relate fields
//    private void resetFieldsOnCompanySelection() {
//        binding.ectlBuildingName.setText("");
//        if (tagMappingViewModel != null) {
//            //Reset Selection
//            tagMappingViewModel.selectedDBuildingDetail = null;
//            //Reset List
//            tagMappingViewModel.buildingDetailsDDListLiveData.setValue(new ArrayList<>());
//            //Rest Error
//            tagMappingViewModel.buildingDDValidationText.setValue(null);
//        }
//        resetFieldsOnBuildingSelection();
//    }
//
//    //Reset Building Relate fields
//    private void resetFieldsOnBuildingSelection() {
//        binding.ectlFloor.setText("");
//        if (tagMappingViewModel != null) {
//            //Reset Selection
//            tagMappingViewModel.selectedDFloorDetail = null;
//            //Reset List
//            tagMappingViewModel.floorDetailsDDListLiveData.setValue(new ArrayList<>());
//            //Rest Error
//            tagMappingViewModel.floorDDValidationText.setValue(null);
//        }
//        resetFieldsOnFloorSelection();
//    }
//
//    //Reset Floor Relate fields
//    private void resetFieldsOnFloorSelection() {
//        binding.ectlLocation.setText("");
//        if (tagMappingViewModel != null) {
//
//            //Reset Selection
//            tagMappingViewModel.selectedDLocationDetail = null;
//            //Reset List
//            tagMappingViewModel.locationDetailsDDListLiveData.setValue(new ArrayList<>());
//            //Rest Error
//            tagMappingViewModel.locationDDValidationText.setValue(null);
//        }
//        resetFieldsOnLocationSelection();
//    }
//
//    //Reset Location Relate fields
//    private void resetFieldsOnLocationSelection() {
//        binding.ectlAssetClass.setText("");
//        if (tagMappingViewModel != null) {
//            //Reset Selection
//            tagMappingViewModel.selectedDAssetclassDetail = null;
//            //Reset List
//            tagMappingViewModel.assetClassDetailsDDListLiveData.setValue(new ArrayList<>());
//            //Rest Error
//            tagMappingViewModel.assetClassDDValidationText.setValue(null);
//        }
//        resetFieldsOnAssetClassSelection();
//    }
//
//    //Reset Asset Class Relate fields
//    private void resetFieldsOnAssetClassSelection() {
//        binding.ectlAsset.setText("");
//        if (tagMappingViewModel != null) {
//            //Reset Selection
//            tagMappingViewModel.selectedDAssetDetail = null;
//            //Reset List
//            tagMappingViewModel.assetDetailsDDListLiveData.setValue(new ArrayList<>());
//            //Rest Error
//            tagMappingViewModel.assetDDValidationText.setValue(null);
//        }
//        resetFieldsOnAssetSelection();
//    }
//
//    //Reset Asset Relate fields
//    private void resetFieldsOnAssetSelection() {
//        binding.ectlSubAsset.setText("");
//        if (tagMappingViewModel != null) {
//            //Reset Selection
//            tagMappingViewModel.selectedDSubAssetDetail = null;
//            //Reset List
//            tagMappingViewModel.subAssetDetailsDDListLiveData.setValue(new ArrayList<>());
//            //Rest Error
//            tagMappingViewModel.subAssetDDValidationText.setValue(null);
//        }
//        resetFieldsOnSubAssetSelection();
//    }
//
//    //Reset Sub Asset  Relate fields
//    private void resetFieldsOnSubAssetSelection() {
//
//        binding.ectlSubAssetPartCode.setText("");
//        if (tagMappingViewModel != null) {
//            //Reset Selection
//            tagMappingViewModel.selectedDSubAssetPartDetail = null;
//            //Reset List
//            tagMappingViewModel.subAssetPartDetailsDDListLiveData.setValue(new ArrayList<>());
//            //Rest Error
//            tagMappingViewModel.subAssetPartCodeDDValidationText.setValue(null);
//        }
//    }
//
//    /* Reset Dropdown Ends  */
//
//
//    /**
//     * OnClick Listener
//     */
//    private void setOnClickListener() {
//
//
//        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
//
//            /*binding.ectlCompany.dismissDropDown();
//            binding.ectlBuildingName.dismissDropDown();
//            binding.ectlFloor.dismissDropDown();
//            binding.ectlLocation.dismissDropDown();
//            binding.ectlAssetClass.dismissDropDown();
//            binding.ectlAsset.dismissDropDown();
//            binding.ectlSubAsset.dismissDropDown();
//            binding.ectlSubAssetPartCode.dismissDropDown();*/
//
//        });
//
//
//        binding.searchImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                closeKeyBoard();
//                binding.ectlSubAssetPartCode.clearFocus();
//                fetchAllDropDownValuesBySubAssetPartDetailsRequest();
//            }
//        });
//
//        //Clear Button
//        binding.btnClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showClearDialog();
//
//            }
//        });
//
//
//        binding.newMappingBtn.setOnClickListener(view -> {
//            showSwitchMappingDialog();
//        });
//
//        binding.remappingBtn.setOnClickListener(view -> {
//            showSwitchMappingDialog();
//        });
//
//        //Save Tage Mapping
//        binding.btnSubmit.setOnClickListener(view -> {
//
//            String companyName = binding.ectlCompany.getText().toString();
//            String buildingName = binding.ectlBuildingName.getText().toString();
//            String floorName = binding.ectlFloor.getText().toString();
//            String locationName = binding.ectlLocation.getText().toString();
//            String assetClass = binding.ectlAssetClass.getText().toString();
//            String assetName = binding.ectlAsset.getText().toString();
//            String subAssetCode = binding.ectlSubAsset.getText().toString();
//            String subAssetPartCode = binding.ectlSubAssetPartCode.getText().toString();
//            String tagNo = binding.ectlTagCode.getText().toString();
//
//
//            if (binding.mappingTGtp.getCheckedButtonId() == binding.newMappingBtn.getId()) {
//
//                tagMappingViewModel.insertAssetRFIDMapping(companyName, buildingName, floorName, locationName,
//                        assetClass, assetName, subAssetCode, subAssetPartCode,
//                        tagNo, new ApiResponseListener() {
//                            @Override
//                            public void onSuccess(Object data) {
//                                if (data instanceof String) {
//                                    clearAllDataField();
//                                    Utility.showToast(String.valueOf(data));
//                                }
//                            }
//
//                            @Override
//                            public void onFailed(Object data) {
//                                if (data instanceof String) {
//                                    Utility.showToast(String.valueOf(data));
//                                }
//                            }
//                        });
//            } else if (binding.mappingTGtp.getCheckedButtonId() == binding.remappingBtn.getId()) {
//                tagMappingViewModel.updateAssetRFIDMapping(companyName, buildingName, floorName, locationName,
//                        assetClass, assetName, subAssetCode, subAssetPartCode,
//                        tagNo, new ApiResponseListener() {
//                            @Override
//                            public void onSuccess(Object data) {
//                                if (data instanceof String) {
//                                    clearAllDataField();
//                                    Utility.showToast(String.valueOf(data));
//                                }
//                            }
//
//                            @Override
//                            public void onFailed(Object data) {
//                                if (data instanceof String) {
//                                    Utility.showToast(String.valueOf(data));
//                                }
//                            }
//                        });
//            }
//
//        });
//
//
//        //RFID Button
//        binding.rfidImg.setOnClickListener(view -> {
//
//            //16-Feb-2025
//            Utility.startOrStopEventReceived(getActivity());
//
//
//        });
//
//    }
//
//    /**
//     * Load Init Meta Data for Dropdown
//     */
//    private void initMetaDetails() {
//        if (tagMappingViewModel == null) {
//            return;
//        }
//        tagMappingViewModel.fetchMetaDetails(/*binding.mappingTGtp.getCheckedButtonId() == binding.remappingBtn.getId() ? 1 : 0,*/ this);
//    }
//
//    @Override
//    public void onSuccess(Object data) {
//
//    }
//
//    @Override
//    public void onFailed(Object data) {
//        if (data instanceof String) {
//            Utility.showToast(String.valueOf(data));
//        }
//    }
//
//    @Override
//    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {
//
//        /*String data = new String(barcodeData);
//        if (!data.isEmpty() && tagMappingViewModel != null
//                && tagMappingViewModel.isDataLoading != null && (tagMappingViewModel.isDataLoading.getValue() == null
//                || !tagMappingViewModel.isDataLoading.getValue())) {
//            tagMappingViewModel.fetchAssetDetailsViaScanQrcode(data, this);
//        }*/
//    }
//
//    @Override
//    public void scannerRFIDEvent(List<InventoryListItem> rfidDataList) {
//        try {
//            if (getActivity() != null) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if (rfidDataList != null && !rfidDataList.isEmpty()) {
//
//                            if (/*rfidDataList != null && !rfidDataList.isEmpty() && */tagMappingViewModel != null
//                                    && tagMappingViewModel.isDataLoading != null && (tagMappingViewModel.isDataLoading.getValue() == null
//                                    || !tagMappingViewModel.isDataLoading.getValue())) {
//
//                                if (binding != null && binding.ectlTagCode.getText() != null && binding.ectlTagCode.getText().toString().isEmpty()) {
//                                    binding.ectlTagCode.setText(rfidDataList.get(0).getTagID());
//                                    Utility.triggerReleaseStopEventReceived(getActivity());
//                                }
//
//                            }
//
//                        }
//                    }
//                });
//            }
//        } catch (Exception e) {
//            //Utility.showToast("Exception1 : " + e);
//        }
//    }
//
//}
//
