package com.zebra.demo.view.fragment;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.zebra.demo.R;
import com.zebra.demo.data.remote.model.AssetInward;
import com.zebra.demo.data.remote.model.AssetInwardAutoResponse;
import com.zebra.demo.databinding.DialogAssetInwardManualBinding;
import com.zebra.demo.databinding.FragmentAssetInwardListingBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.activity.MainActivity;
import com.zebra.demo.view.adapter.AssetInwardManualListAdapter;
import com.zebra.demo.view.adapter.AssetInwardManualSubListAdapter;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.RecyclerAssetInwardViewItemClickListener;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import com.zebra.demo.viewmodel.AssetInwardViewModel;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AssetInwardListingFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, RecyclerViewItemClickListener {

    AssetInwardViewModel assetInwardViewModel;

    FragmentAssetInwardListingBinding binding;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set Listener
        setBarcodeRFIDScanResultListener(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_asset_inward_listing, container, false);
        assetInwardViewModel = new ViewModelProvider(this).get(AssetInwardViewModel.class);

        setOnClickListener();
        setObserver();
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

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_asset_inward_listing);

        assetInwardViewModel = new ViewModelProvider(this).get(AssetInwardViewModel.class);


        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        setOnClickListener();
        setObserver();

    }*/

    /*@Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;

    }*/

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity)getActivity()).setToolBarTitleWithBack(R.string.asset_inward);
        } else if(getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity)getActivity()).setToolBarTitleWithBack(R.string.asset_inward);
            ((ActiveDeviceActivity)getActivity()).setSelectedFragment(this);
        }

        //Set Listener
        setBarcodeRFIDScanResultListener(this);
    }


    private void setObserver() {

        if(getActivity() == null) {
            return;
        }

        assetInwardViewModel.isDataLoading.observe(getActivity(), value -> {
            binding.rltAtvtyProgress.setVisibility(value ? View.VISIBLE : View.GONE);
        });

        assetInwardViewModel.getNetworkErrorMutableData().observe(getActivity(), this::loadNetworkErrorView);

        //Automatic After Insert
        assetInwardViewModel.autoAssetInwardResponseMutableLiveData.observe(getActivity(), value -> {

            if(value == null || value.isEmpty()) {
                resetAutomaticData();
            } else {
                binding.valGatePassNoTxt.setText(Utility.getCommonValue(value.get(0).GatePass));
                binding.valAssetNameTxt.setText(Utility.getCommonValue(value.get(0).AssetName));
                binding.valAssetSrNoTxt.setText(Utility.getCommonValue(value.get(0).AssetSrNo));
                binding.valInchargeTxt.setText(Utility.getCommonValue(value.get(0).Incharge));
                binding.valReasonTxt.setText(Utility.getCommonValue(value.get(0).Reason));
                binding.valVendorNameTxt.setText(Utility.getCommonValue(value.get(0).Vendor));
                binding.valAddressTxt.setText(Utility.getCommonValue(value.get(0).Address));
            }
        });

        //Manual Filter Result
        assetInwardViewModel.manualAssetInwardMutableLiveData.observe(getActivity(), value -> {
            if(value == null || value.isEmpty()) {
                loadAssetInwardManualRclView(new ArrayList<>());
            } else {
                loadAssetInwardManualRclView(value);
            }
        });

        //Manual Dialog
        assetInwardViewModel.manualAssetInwardResponseListMutableLiveData.observe(getActivity(), value -> {
            if(value != null && !value.isEmpty()) {
                AssetInwardManualDialog assetInwardManualDialog = new AssetInwardManualDialog(getActivity(),value);
                assetInwardManualDialog.show();
                Window window = assetInwardManualDialog.getWindow();
                if(window != null) {
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }
        });

    }

    private void loadNetworkErrorView(boolean value) {
        if(value) {
            Utility.showToast("No Internet Connection");
            //Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setOnClickListener() {

        //Toggle Button
        binding.inwardTypeTGtp.addOnButtonCheckedListener((materialButtonToggleGroup, checkedId, isChecked) -> {
            if(isChecked) {
                if (checkedId == binding.automaticBtn.getId()) {
                    binding.manualRlay.setVisibility(View.GONE);
                    binding.automaticRlay.setVisibility(View.VISIBLE);
                } else if (checkedId == binding.manualBtn.getId()) {
                    binding.manualRlay.setVisibility(View.VISIBLE);
                    binding.automaticRlay.setVisibility(View.GONE);
                }
            }
        });

        //Automatic
        binding.btnRFID.setOnClickListener(view -> {

            //Testing
//            List<String> tagList = new ArrayList<>();
//            tagList.add("09192920349422");
//            insertAssetInwardAuto(tagList);



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

        //Manual
        binding.ectlGatepassno.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                fetchManualListData();
                return true;
            }
            return false;
        });
        binding.elblGatepassno.setEndIconOnClickListener(view -> {
            fetchManualListData();
        });


        //From Date
        binding.ectlFrom.setFocusableInTouchMode(false);
        binding.ectlFrom.setShowSoftInputOnFocus(false);
        binding.ectlFrom.setOnClickListener(v -> {
            if(getActivity() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                if (!binding.ectlFrom.getText().toString().equals("")) {
                    try {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(binding.ectlFrom.getText().toString());
                        calendar.setTime(date1);
                    } catch (ParseException e) {
                    } catch (Exception e) {
                    }
                }

                if (fromDatePickerDialog == null) {
                    fromDatePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, (view, year, monthOfYear, dayOfMonth) -> {

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        binding.ectlFrom.setText(sdf.format(calendar.getTime()));
                        fetchManualListData();
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                }
                fromDatePickerDialog.show();
            }
        });

        //To Date
        binding.ectlTo.setFocusableInTouchMode(false);
        binding.ectlTo.setShowSoftInputOnFocus(false);
        binding.ectlTo.setOnClickListener(v -> {
                    if(getActivity() != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        if (!binding.ectlTo.getText().toString().equals("")) {
                            try {
                                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(binding.ectlTo.getText().toString());
                                calendar.setTime(date1);
                            } catch (ParseException e) {
                            } catch (Exception e) {
                            }
                        }

                        if (toDatePickerDialog == null) {
                            toDatePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, (view, year, monthOfYear, dayOfMonth) -> {

                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                binding.ectlTo.setText(sdf.format(calendar.getTime()));
                                fetchManualListData();
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        }
                        toDatePickerDialog.show();
                    }
        });
    }



    /**
     * API Call based on Asset Outward
     */
    private void fetchManualListData() {
        assetInwardViewModel.fetchAssetInwardManualGatePassList(String.valueOf(binding.ectlFrom.getText()), String.valueOf(binding.ectlTo.getText()), String.valueOf(binding.ectlGatepassno.getText()), new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFailed(Object data) {
                if(data instanceof  String) {
                    resetAutomaticData();
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    private void resetAutomaticData() {
        binding.valGatePassNoTxt.setText("-");
        binding.valAssetNameTxt.setText("-");
        binding.valAssetSrNoTxt.setText("-");
        binding.valInchargeTxt.setText("-");
        binding.valReasonTxt.setText("-");
        binding.valVendorNameTxt.setText("-");
        binding.valAddressTxt.setText("-");
    }

    /**
     * Adapter for Recycler View
     * @param assetInwardList
     */
    private void loadAssetInwardManualRclView(List<AssetInward> assetInwardList) {
        if(assetInwardList != null && getActivity() != null) {
            binding.rview.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.rview.setAdapter(new AssetInwardManualListAdapter( assetInwardList, this));
        }
    }

    @Override
    public void onRecyclerViewItemClickListener(int position, Object object) {

        if(object instanceof AssetInward) {
            assetInwardViewModel.fetchAssetInwardAssetDetailsViaGatePass(((AssetInward) object).GatePass, new ApiResponseListener() {
                @Override
                public void onSuccess(Object data) {

                }

                @Override
                public void onFailed(Object data) {
                    if(data instanceof  String) {
                        resetAutomaticData();
                        Utility.showToast(String.valueOf(data));
                    }
                }
            });
        }
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
                        if (binding.inwardTypeTGtp.getCheckedButtonId() == binding.automaticBtn.getId()) {
                            if (rfidDataList != null && !rfidDataList.isEmpty() && assetInwardViewModel != null
                                    && assetInwardViewModel.isDataLoading != null && (assetInwardViewModel.isDataLoading.getValue() == null
                                    || !assetInwardViewModel.isDataLoading.getValue())) {

//                sendAssetViewRequest(rfidDataList.get(0).getTagID());
                                List<String> tagList = new ArrayList<>();
                                for (InventoryListItem inventoryListItem : rfidDataList) {
                                    Log.e("Inward Fragment", "RFID Tag ID" + inventoryListItem.getTagID());
                                    tagList.add(inventoryListItem.getTagID());
                                }

                                if (!tagList.isEmpty()) {
                                    insertAssetInwardAuto(tagList);
                                }

                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            //Utility.showToast("Exception1 : " + e);
        }

    }

    private void insertAssetInwardAuto(List<String> tagList) {
        if(assetInwardViewModel != null) {
            assetInwardViewModel.InsertAssetInwardAuto(tagList, new ApiResponseListener() {
                @Override
                public void onSuccess(Object data) {

                }

                @Override
                public void onFailed(Object data) {
                    if (data instanceof String) {
                        resetAutomaticData();
                        Utility.showToast(String.valueOf(data));
                    }
                }
            });
        }
    }


    public class AssetInwardManualDialog extends Dialog implements ApiResponseListener, RecyclerAssetInwardViewItemClickListener {

        public Activity activity;

        List<AssetInwardAutoResponse> assetInwardAutoResponseList;
        DialogAssetInwardManualBinding binding;



        public AssetInwardManualDialog(Activity activity, List<AssetInwardAutoResponse> assetInwardAutoResponseList) {
            super(activity);
            this.activity = activity;
            this.assetInwardAutoResponseList = assetInwardAutoResponseList;
        }

        private void loadAssetInwardManualRclView(List<AssetInwardAutoResponse> assetInwardAutoResponseList) {
            if(assetInwardAutoResponseList != null && getActivity() != null) {
                binding.rview.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.rview.setAdapter(new AssetInwardManualSubListAdapter( assetInwardAutoResponseList, this));
            }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            binding = DataBindingUtil
                    .inflate(LayoutInflater.from(activity), R.layout.dialog_asset_inward_manual, null, false);
            setContentView(binding.getRoot());
            setViewData();
            binding.closeImg.setOnClickListener(view -> dismiss());
            binding.btnOk.setOnClickListener(view -> {
                //assetInwardViewModel.InsertAssetInwardManual(assetInwardAutoResponse, 1, this);
            });
            binding.btnNotOk.setOnClickListener(view -> {
                //assetInwardViewModel.InsertAssetInwardManual(assetInwardAutoResponse, 0, this);
            });

            loadAssetInwardManualRclView(assetInwardAutoResponseList);
        }

        private void setViewData() {
            if(assetInwardAutoResponseList != null && !assetInwardAutoResponseList.isEmpty() && binding != null) {
                AssetInwardAutoResponse assetInwardAutoResponse = assetInwardAutoResponseList.get(0);
                binding.valAssetNameTxt.setText(Utility.getCommonValue(assetInwardAutoResponse.AssetName));
                binding.valAssetCodeTxt.setText(Utility.getCommonValue(assetInwardAutoResponse.Assetcode));
                binding.valOutDateTxt.setText(Utility.getAppDateFromServerDate(assetInwardAutoResponse.Assetout_on));
                binding.valSnoTxt.setText(Utility.getCommonValue(assetInwardAutoResponse.AssetSrNo));
                binding.valLastGatePassNoTxt.setText(Utility.getCommonValue(assetInwardAutoResponse.GatePass));
            }
        }

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

        @Override
        public void onRecyclerViewItemOkClickListener(int position, Object object) {
            assetInwardViewModel.InsertAssetInwardManual(assetInwardAutoResponseList.get(position), 1, new ApiResponseListener() {
                @Override
                public void onSuccess(Object data) {
                    if(assetInwardViewModel != null && assetInwardViewModel.manualAssetInwardResponseMutableLiveData != null
                            && assetInwardViewModel.manualAssetInwardResponseMutableLiveData.getValue() != null
                            && assetInwardViewModel.manualAssetInwardResponseMutableLiveData.getValue().InwardDetails != null) {
                        assetInwardAutoResponseList = assetInwardViewModel.manualAssetInwardResponseMutableLiveData.getValue().InwardDetails;
                        loadAssetInwardManualRclView(assetInwardAutoResponseList);
                    }
                    if(data instanceof  String) {
                        resetAutomaticData();
                        Utility.showToast(String.valueOf(data));
                    }
                }

                @Override
                public void onFailed(Object data) {
                    if(data instanceof  String) {
                        resetAutomaticData();
                        Utility.showToast(String.valueOf(data));
                    }
                }
            });
        }

        @Override
        public void onRecyclerViewItemNotOkClickListener(int position, Object object) {
            assetInwardViewModel.InsertAssetInwardManual(assetInwardAutoResponseList.get(position), 0, new ApiResponseListener() {
                @Override
                public void onSuccess(Object data) {

                    if(assetInwardViewModel != null && assetInwardViewModel.manualAssetInwardResponseMutableLiveData != null
                            && assetInwardViewModel.manualAssetInwardResponseMutableLiveData.getValue() != null
                            && assetInwardViewModel.manualAssetInwardResponseMutableLiveData.getValue().InwardDetails != null) {
                        assetInwardAutoResponseList = assetInwardViewModel.manualAssetInwardResponseMutableLiveData.getValue().InwardDetails;
                        loadAssetInwardManualRclView(assetInwardAutoResponseList);
                    }

                    if(data instanceof  String) {
                        resetAutomaticData();
                        Utility.showToast(String.valueOf(data));
                    }
                }

                @Override
                public void onFailed(Object data) {
                    if(data instanceof  String) {
                        resetAutomaticData();
                        Utility.showToast(String.valueOf(data));
                    }
                }
            });
        }
    }
}
