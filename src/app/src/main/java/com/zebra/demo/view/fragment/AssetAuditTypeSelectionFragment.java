package com.zebra.demo.view.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import com.zebra.demo.R;
import com.zebra.demo.databinding.ActivityAssetAuditTypeSelectionBinding;
import com.zebra.demo.utility.AssetAuditType;
import com.zebra.demo.view.activity.MainActivity;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import java.util.List;

public class AssetAuditTypeSelectionFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener {


    ActivityAssetAuditTypeSelectionBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set Listener
        setBarcodeRFIDScanResultListener(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_asset_audit_type_selection, container, false);
        setOnClickListener();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setToolBarTitleWithBack(R.string.audit);
        }else if(getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity)getActivity()).setToolBarTitleWithBack(R.string.audit);
            ((ActiveDeviceActivity)getActivity()).setSelectedFragment(this);
        }

        //Set Listener
        setBarcodeRFIDScanResultListener(this);
    }

    /**
     * UI Click listener
     */
    private void setOnClickListener() {

        binding.inProgressRlay.setOnClickListener(view -> {

            AssetAuditListFragment assetAuditListFragment = new AssetAuditListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(AssetAuditListFragment.ASSET_AUDIT_KEY,AssetAuditType.IN_PROGRESS.ordinal());
            assetAuditListFragment.setArguments(bundle);

            if(getActivity() != null && getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToFragmentAddBackStack(assetAuditListFragment);
            } else if(getActivity() != null && getActivity() instanceof ActiveDeviceActivity) {
                ((ActiveDeviceActivity) getActivity()).switchToFragmentAddBackStack(assetAuditListFragment);
            }

        });

        binding.upcomingRlay.setOnClickListener(view -> {

            AssetAuditListFragment assetAuditListFragment = new AssetAuditListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(AssetAuditListFragment.ASSET_AUDIT_KEY,AssetAuditType.UPCOMING.ordinal());
            assetAuditListFragment.setArguments(bundle);

            if(getActivity() != null && getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToFragmentAddBackStack(assetAuditListFragment);
            } else if(getActivity() != null && getActivity() instanceof ActiveDeviceActivity) {
                ((ActiveDeviceActivity) getActivity()).switchToFragmentAddBackStack(assetAuditListFragment);
            }
        });

        binding.completedRlay.setOnClickListener(view -> {

            AssetAuditListFragment assetAuditListFragment = new AssetAuditListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(AssetAuditListFragment.ASSET_AUDIT_KEY,AssetAuditType.COMPLETE.ordinal());
            assetAuditListFragment.setArguments(bundle);

            if(getActivity() != null && getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToFragmentAddBackStack(assetAuditListFragment);
            } else if(getActivity() != null && getActivity() instanceof ActiveDeviceActivity) {
                ((ActiveDeviceActivity) getActivity()).switchToFragmentAddBackStack(assetAuditListFragment);
            }

        });

    }

    @Override
    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {

    }

    @Override
    public void scannerRFIDEvent(List<InventoryListItem> rfidDataList) {

    }
}
