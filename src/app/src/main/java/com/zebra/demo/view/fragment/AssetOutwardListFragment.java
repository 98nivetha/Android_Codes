package com.zebra.demo.view.fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.zebra.demo.R;
import com.zebra.demo.data.remote.model.AssetOutward;
import com.zebra.demo.databinding.FragmentAssetOutwardListingBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.activity.MainActivity;
import com.zebra.demo.view.adapter.AssetOutwardListAdapter;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import com.zebra.demo.viewmodel.AssetOutwardViewModel;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import java.util.ArrayList;
import java.util.List;

public class AssetOutwardListFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, RecyclerViewItemClickListener, ApiResponseListener {


    AssetOutwardViewModel assetOutwardViewModel;
    FragmentAssetOutwardListingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set Listener
        setBarcodeRFIDScanResultListener(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_asset_outward_listing, container, false);
        assetOutwardViewModel = new ViewModelProvider(this).get(AssetOutwardViewModel.class);

        setClickListener();
        setObserver();

        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setToolBarTitleWithBack(R.string.asset_outward);
        } else if(getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity)getActivity()).setToolBarTitleWithBack(R.string.asset_outward);
            ((ActiveDeviceActivity)getActivity()).setSelectedFragment(this);
        }

        //Set Listener
        setBarcodeRFIDScanResultListener(this);
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_asset_outward_listing);
        assetOutwardViewModel = new ViewModelProvider(this).get(AssetOutwardViewModel.class);

        //Toolbar
        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setClickListener();
        setObserver();
        //fetchData();
    }*/

    /**
     * Click Listener
     */
    private void setClickListener() {
        binding.ectlGatepassno.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                fetchData();
                return true;
            }
            return false;
        });

        binding.elblGatepassno.setEndIconOnClickListener(view -> {
            fetchData();
        });

        binding.btnSubmit.setOnClickListener(view -> {
            assetOutwardViewModel.insertAssetOutwardDetails(String.valueOf(binding.ectlGatepassno.getText()), new ApiResponseListener() {
                @Override
                public void onSuccess(Object data) {
                    if(data instanceof  String) {
                        Utility.showToast(String.valueOf(data));
                    }
                }

                @Override
                public void onFailed(Object data) {
                    if(data instanceof  String) {
                        Utility.showToast(String.valueOf(data));
                    }
                }
            });

        });

        binding.barcodeImg.setOnClickListener(view -> {

            if(getActivity() != null && getActivity() instanceof ActiveDeviceActivity) {
                ActiveDeviceActivity activeDeviceActivity = (ActiveDeviceActivity) getActivity();
                if (activeDeviceActivity != null) {
                    //activeDeviceActivity.setTriggerMode(ENUM_TRIGGER_MODE.BARCODE_MODE);
                    activeDeviceActivity.scanTrigger(null);
                }
            }
        });

        binding.btnScanBarcode.setOnClickListener(view -> {

            if(getActivity() != null && getActivity() instanceof ActiveDeviceActivity) {
                ActiveDeviceActivity activeDeviceActivity = (ActiveDeviceActivity) getActivity();
                if (activeDeviceActivity != null) {
                    //activeDeviceActivity.setTriggerMode(ENUM_TRIGGER_MODE.BARCODE_MODE);
                    activeDeviceActivity.scanTrigger(null);
                }
            }

            /*String data = "TIDCHNOE00199-1";
            if(!data.isEmpty() && assetOutwardViewModel != null
                    && assetOutwardViewModel.isDataLoading != null && (assetOutwardViewModel.isDataLoading.getValue() == null
                    || !assetOutwardViewModel.isDataLoading.getValue())) {
                if(binding != null) {
                    if(binding.ectlGatepassno.getText() != null && binding.ectlGatepassno.getText().toString().isEmpty()) {
                        binding.ectlGatepassno.setText(data);
                    } else {
                        boolean canFindAsset = assetOutwardViewModel.setStatusOkBySrNo(data);
                        if(canFindAsset) {
                            findAsset(data);
                        }
                    }
                }
            }*/

        });

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

            /*List<String> tagIds = new ArrayList<>();
            String[] tagIdsTest = {"E200001D370C01901960A3DB"};

            for (String test : tagIdsTest) {
                boolean canFindAsset = assetOutwardViewModel.setStatusOkByTag(test);
                if(canFindAsset) {
                    tagIds.add(test);
                }
            }

            if(!tagIds.isEmpty()) {
                String tagIdsStr = TextUtils.join(",", tagIds);
                findAssetRFID(tagIdsStr);
            }*/
        });

    }

    /**
     * Observer
     */
    private void setObserver() {
        if(getActivity() != null) {
            assetOutwardViewModel.isDataLoading.observe(getActivity(), value -> {
                binding.rltAtvtyProgress.setVisibility(value ? View.VISIBLE : View.GONE);
            });

            assetOutwardViewModel.assetOutwardListMutableLiveData.observe(getActivity(), this::loadAssetOutwardRclView);

            assetOutwardViewModel.getNetworkErrorMutableData().observe(getActivity(), this::loadNetworkErrorView);
        }
    }

    /**
     * On Click page back button
     *
     * @return default
     */
    /*@Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;

    }*/

    /**
     * Adapter for Recycler View
     * @param assetOutwardList
     */
    private void loadAssetOutwardRclView(List<AssetOutward> assetOutwardList) {
        if(assetOutwardList != null && getActivity() != null) {
            binding.rview.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rview.setAdapter(new AssetOutwardListAdapter( assetOutwardList, this));
        }
    }

    /**
     * API Call based on Asset Outward
     */
    private void fetchData() {
        assetOutwardViewModel.fetchAssetOutwardGatePassDetailsList(String.valueOf(binding.ectlGatepassno.getText()), this);
    }

    /**
     * Show network error
     * @param isNoNetwork
     */

    private void loadNetworkErrorView(boolean isNoNetwork) {
        if(isNoNetwork){
            Utility.showToast("No Internet connection available");
        }
    }

    @Override
    public void onRecyclerViewItemClickListener(int position, Object object) {

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
        String data = new String(barcodeData);
        if(!data.isEmpty() && assetOutwardViewModel != null
                && assetOutwardViewModel.isDataLoading != null && (assetOutwardViewModel.isDataLoading.getValue() == null
                || !assetOutwardViewModel.isDataLoading.getValue())) {
            if(binding != null) {
                if(binding.ectlGatepassno.getText() != null && binding.ectlGatepassno.getText().toString().isEmpty()) {
                    binding.ectlGatepassno.setText(data);
                } else {
                    boolean canFindAsset = assetOutwardViewModel.setStatusOkBySrNo(data);
                    if(canFindAsset) {
                        findAsset(data);
                    }
                }
            }
        }
    }

    @Override
    public void scannerRFIDEvent(List<InventoryListItem> rfidDataList) {
        try {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (rfidDataList != null && !rfidDataList.isEmpty() && assetOutwardViewModel != null
                                && assetOutwardViewModel.isDataLoading != null && (assetOutwardViewModel.isDataLoading.getValue() == null
                                || !assetOutwardViewModel.isDataLoading.getValue())) {


                            //sendAssetViewRequest(rfidDataList.get(0).getTagID());

                            List<String> tagIds = new ArrayList<>();

                            for (InventoryListItem inventoryListItem : rfidDataList) {
                                boolean canFindAsset = assetOutwardViewModel.setStatusOkByTag(inventoryListItem.getTagID());
                                if (canFindAsset) {
                                    tagIds.add(inventoryListItem.getTagID());
                                }
                            }

                            if (!tagIds.isEmpty()) {
                                String tagIdsStr = TextUtils.join(",", tagIds);
                                findAssetRFID(tagIdsStr);
                            }

                        }
                    }
                });
            }
        } catch (Exception e) {
            //Utility.showToast("Exception1 : " + e);
        }
    }

    public void findAsset(String tags) {
        assetOutwardViewModel.sendAssetViewRequest(tags, new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {
//                if(data instanceof  String) {
//                    Utility.showToast(String.valueOf(data));
//                }
            }

            @Override
            public void onFailed(Object data) {
                if(data instanceof  String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    public void findAssetRFID(String tags) {
        assetOutwardViewModel.fetchMultipleAssetdetailsviaRFID(tags, new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {
//                if(data instanceof  String) {
//                    Utility.showToast(String.valueOf(data));
//                }
            }

            @Override
            public void onFailed(Object data) {
                if(data instanceof  String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }
}
