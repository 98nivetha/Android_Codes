package com.zebra.demo.view.fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.zebra.demo.R;
import com.zebra.demo.databinding.ActivityAssetViewBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.activity.MainActivity;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.viewmodel.AssetViewViewModel;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import com.zebra.demo.zebralib.scanner.barcode.BarcodeTypes;
import java.util.List;

public class AssetViewFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener {

    AssetViewViewModel assetViewViewModel;

    ActivityAssetViewBinding binding;


    public static final String ASSET_SR_CODE_KEY = "ASSET_SR_CODE_KEY";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set Listener
        setBarcodeRFIDScanResultListener(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_asset_view, container, false);
        assetViewViewModel = new ViewModelProvider(this).get(AssetViewViewModel.class);

        binding.setAssetView(assetViewViewModel);

        setOnClickListener();
        setObserver();

        if(getArguments() != null) {
            String srNoCode = getArguments().getString(ASSET_SR_CODE_KEY);
            if(srNoCode != null && !srNoCode.isEmpty()) {
                sendAssetViewRequest(srNoCode);
            }
        }



        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setToolBarTitleWithBack(R.string.asset_view);
        } else if(getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity)getActivity()).setToolBarTitleWithBack(R.string.asset_view);
            ((ActiveDeviceActivity)getActivity()).setSelectedFragment(this);
        }

        //Set Listener
        setBarcodeRFIDScanResultListener(this);
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_asset_view);
//
//        assetViewViewModel = new ViewModelProvider(this).get(AssetViewViewModel.class);
//
//
//        setSupportActionBar(binding.toolbar);
//        if(getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        binding.setAssetView(assetViewViewModel);
//
//        setOnClickListener();
//        setObserver();
//
//        if(getIntent() != null) {
//            String srNoCode = getIntent().getStringExtra(ASSET_SR_CODE_KEY);
//            if(srNoCode != null && !srNoCode.isEmpty()) {
//                sendAssetViewRequest(srNoCode);
//            }
//        }
//
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return true;
//
//    }

    private void setObserver() {
        if(getActivity() != null) {
            assetViewViewModel.isDataLoading.observe(getActivity(), value -> {
                binding.rltAtvtyProgress.setVisibility(value ? View.VISIBLE : View.GONE);
            });
            assetViewViewModel.assetFilterDetailMutableLiveData.observe(getActivity(), value -> {
                String status = Utility.getCommonValue(String.valueOf(value.Status));
                String statusValue = status.equalsIgnoreCase("MSC001") ? "Active" : "InActive";
                binding.valAssetCodeTxt.setText(Utility.getCommonValue(String.valueOf(value.SapAssetNo)));
                binding.valAssetNameTxt.setText(Utility.getCommonValue(String.valueOf(value.Subitemname)));
                binding.valAssetSubcodeTxt.setText(Utility.getCommonValue(String.valueOf(value.Subassetpartscode)));
                binding.valSerialNoTxt.setText(Utility.getCommonValue(String.valueOf(value.Serialnumber)));
                binding.valInventoryNumberTxt.setText(Utility.getCommonValue(String.valueOf(value.Inventorynumber)));
                binding.valAssetClassTxt.setText(Utility.getCommonValue(String.valueOf(value.Assetclass)));
                binding.valCostCenterTxt.setText(Utility.getCommonValue(String.valueOf(value.Costcenter)));
                binding.valLocationTxt.setText(Utility.getCommonValue(String.valueOf(value.Locationname)));
                binding.valBuildingTxt.setText(Utility.getCommonValue(String.valueOf(value.Buildingname)));
                binding.valRfidTagTxt.setText(Utility.getCommonValue(String.valueOf(value.RfidTag)));
                binding.valStatusTxt.setText(statusValue);
                binding.valStatusTxt.setTextColor(statusValue.equalsIgnoreCase("Active") ? Color.GREEN : Color.RED);
            });

            assetViewViewModel.getNetworkErrorMutableData().observe(getActivity(), this::loadNetworkErrorView);
        }
    }

    private void loadNetworkErrorView(boolean value) {
        if(value) {
            Utility.showToast("No Internet Connection");
            //Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setOnClickListener() {
        binding.btnBarcode.setOnClickListener(view -> {
                if(getActivity() != null && getActivity() instanceof ActiveDeviceActivity) {
                    ActiveDeviceActivity activeDeviceActivity = (ActiveDeviceActivity) getActivity();
                    if (activeDeviceActivity != null) {
                        activeDeviceActivity.scanTrigger(null);
                    }
                }
        });

        binding.btnRFID.setOnClickListener(view -> {
            Utility.startOrStopEventReceived(getActivity());
        });
    }

    private void sendAssetViewRequest(String tagId) {
        assetViewViewModel.sendAssetViewRequest(tagId, new ApiResponseListener() {

            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFailed(Object data) {
                resetData();
                if(data instanceof  String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    private void resetData() {

        binding.valAssetCodeTxt.setText("-");
        binding.valAssetCodeTxt.setText("-");
        binding.valAssetNameTxt.setText("-");
        //binding.valQuantityTxt.setText("-");
        binding.valLocationTxt.setText("-");
        binding.valSerialNoTxt.setText("-");
        binding.valAssetClassTxt.setText("-");
        binding.valCostCenterTxt.setText("-");
        binding.valInventoryNumberTxt.setText("-");
        binding.valBuildingTxt.setText("-");
        binding.valStatusTxt.setText("-");
        binding.valRfidTagTxt.setText("-");


        /*binding.valSnoTxt.setText("-");
        binding.valAssetCodeTxt.setText("-");
        binding.valAssetNameTxt.setText("-");
        binding.valGroupTxt.setText("-");
        binding.valDepartmentTxt.setText("-");
        binding.valCostCentreTxt.setText("-");
        binding.valRegisterDateTxt.setText("-");
        binding.valLocationTxt.setText("-");
        binding.valAssetConditionTxt.setText("-");
        binding.valPurchaseDateTxt.setText("-");
        binding.valVendorNameTxt.setText("-");
        binding.valPurchaseCostTxt.setText("-");
        binding.valAssetLifeTxt.setText("-");*/
    }

    @Override
    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {
        Log.d("AssetView Fragment", "Barcode -" +  new String(barcodeData));
        Log.d("AssetView Fragment", "Barcode -" + BarcodeTypes.getBarcodeTypeName(barcodeType));

        String data = new String(barcodeData);
        if(!data.isEmpty() && assetViewViewModel != null
                && assetViewViewModel.isDataLoading != null && (assetViewViewModel.isDataLoading.getValue() == null
                || !assetViewViewModel.isDataLoading.getValue())) {
            sendAssetViewRequest(data);
//            sendAssetViewRequest("47906030479812364820828561709985");
        }
    }

    @Override
    public void scannerRFIDEvent(List<InventoryListItem> rfidDataList) {
        try {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (rfidDataList != null && !rfidDataList.isEmpty() && assetViewViewModel != null
                                && assetViewViewModel.isDataLoading != null && (assetViewViewModel.isDataLoading.getValue() == null
                                || !assetViewViewModel.isDataLoading.getValue())) {

                            sendAssetViewRequest(rfidDataList.get(0).getTagID());
                            //sendAssetViewRequest("47906030479812364820828561709985");


                            for (InventoryListItem inventoryListItem : rfidDataList) {
                                Log.e("AssetView Fragment", "RFID Tag ID" + inventoryListItem.getTagID());
                            }

                            Utility.triggerReleaseStopEventReceived(getActivity());
                        }
                    }
                });
            }
        } catch (Exception e) {
            //Utility.showToast("Exception1 : " + e);
        }
    }
}
