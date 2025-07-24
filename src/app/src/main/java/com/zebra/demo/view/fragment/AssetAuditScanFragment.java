package com.zebra.demo.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.zebra.demo.R;
import com.zebra.demo.data.remote.model.AssetAudit;
import com.zebra.demo.data.remote.model.LocationDetail;
import com.zebra.demo.databinding.FragmentAssetAuditScanBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.activity.MainActivity;
import com.zebra.demo.view.adapter.FilterAutoCompleteTextViewAdapter;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.viewmodel.AssetAuditScanViewModel;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;

import java.util.ArrayList;
import java.util.List;

public class AssetAuditScanFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener {

    AssetAuditScanViewModel assetAuditScanViewModel;

    FragmentAssetAuditScanBinding binding;


    public static final String AUDIT_ID_KEY = "AUDIT_ID_KEY";
//    public static final String AUDIT_BUILDING_NAME_KEY = "AUDIT_BUILDING_NAME_KEY";
//    public static final String AUDIT_COMPLETE_PERCENTAGE_KEY = "AUDIT_COMPLETE_PERCENTAGE_KEY";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set Listener
        setBarcodeRFIDScanResultListener(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_asset_audit_scan, container, false);
        assetAuditScanViewModel = new ViewModelProvider(this).get(AssetAuditScanViewModel.class);

//        setSupportActionBar(binding.toolbar);
//        if(getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        setOnClickListener();
        setObserver();

        if(getArguments() != null) {
            String auditId = getArguments().getString(AUDIT_ID_KEY);
//            String buildingName = getArguments().getString(AUDIT_BUILDING_NAME_KEY);
//            String completePercentage = getArguments().getString(AUDIT_COMPLETE_PERCENTAGE_KEY);

            if(auditId != null && !auditId.isEmpty()) {
                if(assetAuditScanViewModel != null) {
                    assetAuditScanViewModel.setSelectedAuditId(auditId);
                    /*assetAuditScanViewModel.setSelectedBuildingName(buildingName);
                    assetAuditScanViewModel.setSelectedCompletePercentage(completePercentage);*/
                }
            }
        }
        initFetchDetails();
        initBarcodeScannerMode();
        return binding.getRoot();
    }

    private void initBarcodeScannerMode() {
        if(getActivity() != null && getActivity() instanceof ActiveDeviceActivity) {
            ActiveDeviceActivity activeDeviceActivity = (ActiveDeviceActivity) getActivity();
            if (activeDeviceActivity != null) {
                activeDeviceActivity.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setToolBarTitleWithBack(R.string.audit);
        } else if(getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity)getActivity()).setToolBarTitleWithBack(R.string.audit);
            ((ActiveDeviceActivity)getActivity()).setSelectedFragment(this);
        }

        //Set Listener
        setBarcodeRFIDScanResultListener(this);
    }
    private void setObserver() {
        if(getActivity() != null) {
        assetAuditScanViewModel.isDataLoading.observe(getActivity(), value -> {
            binding.rltAtvtyProgress.setVisibility(value ? View.VISIBLE : View.GONE);
        });

        assetAuditScanViewModel.assetAuditDetailMutableLiveData.observe(getActivity(), value -> {
            if(value != null) {
                if(value.Data != null && value.Data.D_AuditDetails != null && !value.Data.D_AuditDetails.isEmpty()) {
                    setAssetAuditViewData(value.Data.D_AuditDetails.get(0));
                }

                //Location Detail
                /*if(value.Locations != null && !value.Locations.isEmpty()) {
                    setAutoCompleteTextView(binding.ectlLocation, (List<Object>) (Object) value.Locations);
                }*/

            }
        });

        assetAuditScanViewModel.assetAuditMutableLiveData.observe(getActivity(), value -> {
            if(value != null) {
                setAssetAuditViewData(value);
            }
        });

        assetAuditScanViewModel.getNetworkErrorMutableData().observe(getActivity(), this::loadNetworkErrorView);
        }
    }

    private void setAssetAuditViewData(AssetAudit assetAudit) {
        binding.valDepartmentTxt.setText(Utility.getCommonValue(assetAudit.Buildingname));
        binding.valScannedQtyTxt.setText(Utility.getCommonValue(String.valueOf(assetAudit.Incount))
                + " / " + Utility.getCommonValue(String.valueOf(assetAudit.Totalcount)) + " Qty");
        binding.valPercentageTxt.setText(Utility.getCommonValue(assetAudit.Auditcompletion) /*+ " / " + Utility.getCommonValue(String.valueOf(assetAudit.sys_qty))*/);
        binding.valAuditIdTxt.setText(Utility.getCommonValue(assetAudit.Auditincharge));
    }

    private void loadNetworkErrorView(boolean value) {
        if(value) {
            Utility.showToast("No Internet Connection");
            //Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setOnClickListener() {

        binding.btnRFID.setOnClickListener(view -> {

            //16-Feb-2025
            Utility.startOrStopEventReceived(getActivity());
            /*if(getActivity() != null && getActivity() instanceof ActiveDeviceActivity) {
                ActiveDeviceActivity activeDeviceActivity = (ActiveDeviceActivity) getActivity();
                if (activeDeviceActivity != null) {
                    //activeDeviceActivity.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE);
                    activeDeviceActivity.inventoryStartOrStop(null);
                }
            }*/




        });
    }

    /**
     * Load Init Detail
     */
    private void initFetchDetails() {
        if(assetAuditScanViewModel == null) {
            return;
        }

//        binding.valDepartmentTxt.setText(Utility.getCommonValue(assetAuditScanViewModel.getSelectedBuildingName()));
//        binding.valScannedQtyTxt.setText(Utility.getCommonValue(assetAuditScanViewModel.getSelectedCompletePercentage()) /*+ " / " + Utility.getCommonValue(String.valueOf(assetAudit.sys_qty))*/);
//        binding.valAuditIdTxt.setText(Utility.getCommonValue(assetAuditScanViewModel.getSelectedAuditId()));

        assetAuditScanViewModel.fetchAuditIdDetails(new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFailed(Object data) {
                if(data instanceof  String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    /**
     * Set AutoComplete Common method
     * @param autoCompleteTextView
     * @param objectList
     */
    private void setAutoCompleteTextView(MaterialAutoCompleteTextView autoCompleteTextView, List<Object> objectList) {
        if(objectList != null && getActivity() != null) {

            //ArrayAdapter<Object> assetHeaderArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, objectList);
            FilterAutoCompleteTextViewAdapter assetHeaderArrayAdapter = new FilterAutoCompleteTextViewAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, objectList);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(assetHeaderArrayAdapter);
            //Click Listener
            autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
                Object item = adapterView.getItemAtPosition(i);

                if (item instanceof LocationDetail){ //Location
                    LocationDetail locationDetail = (LocationDetail) item;
                    if(assetAuditScanViewModel != null) {
                        assetAuditScanViewModel.setSelectedLocationDetail(locationDetail);
                    }
                }
            });

            //Touch Listener
            autoCompleteTextView.setOnTouchListener((v, event) -> {
                autoCompleteTextView.showDropDown();
                return false;
            });
        }
    }

    private void insertAuditScanData(List<String> tagList) {
        assetAuditScanViewModel.insertAuditScanData(tagList, new ApiResponseListener() {

            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFailed(Object data) {
                if(data instanceof  String) {
                    Utility.showToast(String.valueOf(data));
                    //Toast.makeText(AssetAuditScanActivity.this, String.valueOf(data), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {

    }

    @Override
    public void scannerRFIDEvent(List<InventoryListItem> rfidDataList) {
        try {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (rfidDataList != null && !rfidDataList.isEmpty() && assetAuditScanViewModel != null
                                && assetAuditScanViewModel.isDataLoading != null && (assetAuditScanViewModel.isDataLoading.getValue() == null
                                || !assetAuditScanViewModel.isDataLoading.getValue())) {

                            //sendAssetViewRequest(rfidDataList.get(0).getTagID());
                            //sendAssetViewRequest("47906030479812364820828561709985");

                            List<String> tagList = new ArrayList<>();
                            for (InventoryListItem inventoryListItem : rfidDataList) {
                                Log.e("AssetView Fragment", "RFID Tag ID" + inventoryListItem.getTagID());
                                tagList.add(inventoryListItem.getTagID());
                            }

                            if (!tagList.isEmpty()) {
                                insertAuditScanData(tagList);
                            }

                        }
                    }
                });
            }
        } catch (Exception e) {
            //Utility.showToast("Exception1 : " + e);
        }
    }
}
