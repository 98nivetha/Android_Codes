package com.zebra.demo.view.fragment.IBA_FRAGMENTS;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.zebra.demo.R;
import com.zebra.demo.databinding.FragmentMaterialviewBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.fragment.BarcodeRFIDScanBaseFragment;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import com.zebra.demo.viewmodel.MaterialViewViewModel;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;

import java.util.List;

public class MaterialViewFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, RecyclerViewItemClickListener, ApiResponseListener {
    private MaterialViewViewModel materialViewViewModel;
    private FragmentMaterialviewBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setBarcodeRFIDScanResultListener(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_materialview, container, false);
        materialViewViewModel = new ViewModelProvider(this).get(MaterialViewViewModel.class);
        binding.setMaterialview(materialViewViewModel);
        setOnClickListener();
        setObserver();
        return binding.getRoot();
    }

    private void setOnClickListener() {
        binding.btnSubmit.setOnClickListener(view -> {
            Toast.makeText(requireContext(), "Fetching material details...", Toast.LENGTH_SHORT).show();
            materialViewViewModel.fetchMeterialDetails(this);
            if (getActivity() != null && getActivity() instanceof ActiveDeviceActivity) {
                ActiveDeviceActivity activeDeviceActivity = (ActiveDeviceActivity) getActivity();
                Utility.startOrStopEventReceived(getActivity());
                if (activeDeviceActivity != null) {
                    activeDeviceActivity.scanTrigger(null);
                }
            }
        });
    }

    @Override
    public void onSuccess(Object data) {

    }

    private void setObserver() {
        if (getActivity() != null) {
            materialViewViewModel.isDataLoading.observe(getActivity(), value -> {
                binding.rltAtvtyProgress.setVisibility(value ? View.VISIBLE : View.GONE);
            });
            materialViewViewModel.meterialDetailMutableLiveData.observe(getViewLifecycleOwner(), value -> {
                if (value != null && value.getData() != null && value.getData().getV_RfidDetails() != null) {
                    String materialName = Utility.getCommonValue(value.getData().getV_RfidDetails().getMaterialname());
                    String materialCode = Utility.getCommonValue(value.getData().getV_RfidDetails().getMaterialcode());
                    String packingStd = Utility.getCommonValue(String.valueOf(value.getData().getV_RfidDetails().getPackingstandard()));
                    String quantity = Utility.getCommonValue(String.valueOf(value.getData().getV_RfidDetails().getQuantity()));
                    String rackCode = Utility.getCommonValue(String.valueOf(value.getData().getV_RfidDetails().getRackcode()));
                    String inwardDate = Utility.getCommonValue(String.valueOf(value.getData().getV_RfidDetails().getInwarddate()));
                    Integer status = value.getData().getV_RfidDetails().getStatus();
                    Log.d("STATUS TEST", "STATUS: " + status);
                    String statusValue = (status != null && status == 1) ? "Active" : "InActive";
                    binding.valStatusTxt.setText(statusValue);
                    binding.valStatusTxt.setTextColor("Active".equalsIgnoreCase(statusValue) ? Color.GREEN : Color.RED);
                    binding.valMaterialNameTxt.setText(materialName);
                    binding.valMaterialCodeTxt.setText(materialCode);
                    binding.valPackingStdTxt.setText(packingStd);
                    binding.valQuantityTxt.setText(quantity);
                    binding.valLblRackcodeTxt.setText(rackCode);
                    binding.valInwardDtateTxt.setText(inwardDate);
                } else {
                    Log.e("MATERIAL_VIEW", "Received null data in meterialDetailMutableLiveData");
                }
            });
        }
    }


    @Override
    public void onFailed(Object data) {
        if (data instanceof String) {
            Utility.showToast(String.valueOf(data));
        }
    }

    @Override
    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {

    }

    @Override
    public void scannerRFIDEvent(List<InventoryListItem> rfidDataList) {

    }

    @Override
    public void onRecyclerViewItemClickListener(int position, Object object) {

    }
}
