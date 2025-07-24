package com.zebra.demo.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zebra.demo.R;
import com.zebra.demo.data.remote.model.AssetAudit;
import com.zebra.demo.databinding.FragmentAssetAuditListingBinding;
import com.zebra.demo.utility.AssetAuditType;
import com.zebra.demo.utility.UIUtils;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.activity.MainActivity;
import com.zebra.demo.view.adapter.AssetAuditListAdapter;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.NetworkRetryListener;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import com.zebra.demo.viewmodel.AssetAuditViewModel;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;

import java.util.List;

public class AssetAuditListFragment
        extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, RecyclerViewItemClickListener, ApiResponseListener {


    public static final String ASSET_AUDIT_KEY =  "ASSET_AUDIT_KEY";
    AssetAuditViewModel assetAuditViewModel;
    FragmentAssetAuditListingBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set Listener
        setBarcodeRFIDScanResultListener(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_asset_audit_listing, container, false);
        assetAuditViewModel = new ViewModelProvider(this).get(AssetAuditViewModel.class);

        //Bundle
        if(getArguments() != null) {
            AssetAuditType assetAuditType = AssetAuditType.getAssetAuditType(getArguments().getInt(ASSET_AUDIT_KEY, AssetAuditType.IN_PROGRESS.ordinal()));
            if(assetAuditType != null) {
                assetAuditViewModel.setSelectedAssetAuditType(assetAuditType);
            }
        }

        setObserver();
        fetchData();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setToolBarTitleWithBack("Audit - (" + assetAuditViewModel.getSelectedAssetAuditTypeName() + ")");
        } else if(getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity)getActivity()).setToolBarTitleWithBack("Audit - (" + assetAuditViewModel.getSelectedAssetAuditTypeName() + ")");
            ((ActiveDeviceActivity)getActivity()).setSelectedFragment(this);
        }

        //Set Listener
        setBarcodeRFIDScanResultListener(this);
    }

    /**
     * Observer
     */
    private void setObserver() {
        if(getActivity() != null) {
            assetAuditViewModel.isDataLoading.observe(getActivity(), value -> {
                binding.rltAtvtyProgress.setVisibility(value ? View.VISIBLE : View.GONE);
            });

            assetAuditViewModel.assetAuditListMutableLiveData.observe(getActivity(), this::loadAssetAuditRclView);

            assetAuditViewModel.getNetworkErrorMutableData().observe(getActivity(), this::loadNetworkErrorView);
        }
    }


    /**
     * Adapter for Recycler View
     * @param assetAuditList
     */
    private void loadAssetAuditRclView(List<AssetAudit> assetAuditList) {
        if(assetAuditList != null && getActivity() != null) {
            binding.rview.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rview.setAdapter(new AssetAuditListAdapter( assetAuditList, this));
        }
    }

    /**
     * API Call based on Audit Type
     */
    private void fetchData() {
        if(assetAuditViewModel.getSelectedAssetAuditType() == AssetAuditType.IN_PROGRESS) {
            assetAuditViewModel.fetchAssetAuditInProgressList(this);
        } else if(assetAuditViewModel.getSelectedAssetAuditType() == AssetAuditType.COMPLETE) {
            assetAuditViewModel.fetchAssetAuditCompletedList(this);
        } else if(assetAuditViewModel.getSelectedAssetAuditType() == AssetAuditType.UPCOMING) {
            assetAuditViewModel.fetchAssetAuditUpcomingList(this);
        }
    }

    /**
     * Show network error
     * @param isNoNetwork
     */

    private void loadNetworkErrorView(boolean isNoNetwork) {
        if(isNoNetwork){
            UIUtils.showNetworkRetryDialog(getActivity(),new NetworkRetryListener() {
                @Override
                public void onClickRetry() {
                    fetchData();
                }

                @Override
                public void onClickClose() {

                }
            });
        }
    }

    @Override
    public void onRecyclerViewItemClickListener(int position, Object object) {


        if(assetAuditViewModel.getSelectedAssetAuditType() == AssetAuditType.IN_PROGRESS
                || assetAuditViewModel.getSelectedAssetAuditType() == AssetAuditType.UPCOMING) {
            if (object instanceof AssetAudit) {
                AssetAuditScanFragment assetAuditScanFragment = new AssetAuditScanFragment();
                Bundle bundle = new Bundle();
                //bundle.putString(AssetAuditScanFragment.AUDIT_ID_KEY, ((AssetAudit) object).Auditid);
                bundle.putString(AssetAuditScanFragment.AUDIT_ID_KEY, ((AssetAudit) object).Auditcode);
                /*bundle.putString(AssetAuditScanFragment.AUDIT_BUILDING_NAME_KEY, ((AssetAudit) object).Buildingname);
                bundle.putString(AssetAuditScanFragment.AUDIT_COMPLETE_PERCENTAGE_KEY, ((AssetAudit) object).Auditcompletion);*/
                assetAuditScanFragment.setArguments(bundle);
                loadFragment(assetAuditScanFragment);
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).switchToFragmentAddBackStack(fragment);
        } else if(getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity) getActivity()).switchToFragmentAddBackStack(fragment);
        }
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

    }
}
