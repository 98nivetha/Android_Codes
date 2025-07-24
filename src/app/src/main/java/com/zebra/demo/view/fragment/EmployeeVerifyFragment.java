package com.zebra.demo.view.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.zebra.demo.R;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.data.remote.model.EmployeeDetail;
import com.zebra.demo.data.remote.model.ScanAssetDetail;
import com.zebra.demo.databinding.EmployeeVerifyGridItemBinding;
import com.zebra.demo.databinding.FragmentEmployeeVerifyBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.activity.MainActivity;
import com.zebra.demo.view.adapter.EmployeeAutoCompleteTextViewAdapter;
import com.zebra.demo.view.adapter.EmployeeVerifyGridAdapter;
import com.zebra.demo.view.adapter.EmployeeVerifyListAdapter;
import com.zebra.demo.view.adapter.TestMyAdapter;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.ReaderTriggerEvent;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import com.zebra.demo.viewmodel.EmployeeVerifyViewModel;
import com.zebra.demo.zebralib.ActiveDeviceActivity;
import com.zebra.demo.zebralib.application.Application;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import com.zebra.demo.zebralib.rfidreader.rfid.RFIDController;

import java.util.ArrayList;
import java.util.List;

public class EmployeeVerifyFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, RecyclerViewItemClickListener, ReaderTriggerEvent {

    EmployeeVerifyViewModel employeeVerifyViewModel;

    FragmentEmployeeVerifyBinding binding;

    //EmployeeVerifyListAdapter employeeVerifyListAdapter;

    MediaPlayer mp;

//    public static final String ASSET_SR_CODE_KEY = "ASSET_SR_CODE_KEY";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Set Listener
        setBarcodeRFIDScanResultListener(this);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_employee_verify, container, false);
        employeeVerifyViewModel = new ViewModelProvider(this).get(EmployeeVerifyViewModel.class);


        setOnClickListener();
        setObserver();

        /*if(getArguments() != null) {
            String srNoCode = getArguments().getString(ASSET_SR_CODE_KEY);
            if(srNoCode != null && !srNoCode.isEmpty()) {
                sendAssetViewRequest(srNoCode);
            }
        }*/
        initMetaDetails();



        // Sample Data
        /*List<String> dataList = new ArrayList<>();

        dataList.add("SNO");
        dataList.add("ASSET NAME");
        dataList.add("ASSIGNED TO");
        dataList.add("ASSIGNED TO CODE");

        dataList.add("1");
        dataList.add("DELL LAPTOP 00004556");
        dataList.add("SANTHOSH KUMAR J");
        dataList.add("AS001");

        dataList.add("2");
        dataList.add("BOAT BLUETOOTH 986");
        dataList.add("SANTHOSH KUMAR J");
        dataList.add("AS090");

        dataList.add("3");
        dataList.add("MOUSE");
        dataList.add("NIVETHA");
        dataList.add("ASS98");

        dataList.add("4");
        dataList.add("MOBILE PHONE");
        dataList.add("SANTHOSH KUMAR J");
        dataList.add("ASS90");*/


        /*for (int i = 1; i <= 20; i++) {
            dataList.add("ItemA ItemB ItemC ItemD Item" + i);
        }*/

        // Set Adapter
        //binding.gridView.setAdapter(new EmployeeVerifyGridAdapter(dataList, getActivity()));

        /*GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setNestedScrollingEnabled(false); // If inside ScrollView, this is needed

        binding.recyclerView.setAdapter(new TestMyAdapter(dataList));*/

        //setGridView();

        return binding.getRoot();
    }


    void setGridView(List<ScanAssetDetail> assetDetailList) {
        if(getActivity() == null || binding == null) {
            return;
        }
        clearGridView();

        if(assetDetailList != null && !assetDetailList.isEmpty()) {
            for (int i = 0; i < assetDetailList.size(); i++) {
                EmployeeVerifyGridItemBinding employeeVerifyGridItemBinding = EmployeeVerifyGridItemBinding.inflate(LayoutInflater.from(getActivity()));
                ScanAssetDetail assetDetail = assetDetailList.get(i);
                if(assetDetail != null) {
                    employeeVerifyGridItemBinding.snoTxt.setText(String.valueOf(i+1));
                    employeeVerifyGridItemBinding.assetNameTxt.setText(assetDetail.getAssetName());
                    employeeVerifyGridItemBinding.assignedToTxt.setText(assetDetail.getPLACEOF_ASSET_NAME());
                    employeeVerifyGridItemBinding.assignedToCodeTxt.setText(assetDetail.getPLACEOF_ASSET());

                    if(assetDetail.getEMP_AST_STATUS() == 1) {
                        employeeVerifyGridItemBinding.valueHeadderRlay.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
                    } else if(assetDetail.getEMP_AST_STATUS() == 2) {
                        employeeVerifyGridItemBinding.valueHeadderRlay.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                    } else {
                        employeeVerifyGridItemBinding.valueHeadderRlay.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.dark_gray));
                    }


                    binding.container.addView(employeeVerifyGridItemBinding.getRoot());
                }
            }
        }
    }

    void clearGridView() {
        if(getActivity() == null || binding == null) {
            return;
        }
        binding.container.removeAllViews();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolBarTitleWithBack(R.string.employee_verification);
        } else if (getActivity() instanceof ActiveDeviceActivity) {
            ((ActiveDeviceActivity) getActivity()).setToolBarTitleWithBack(R.string.employee_verification);
            ((ActiveDeviceActivity) getActivity()).setSelectedFragment(this);
        }

        //Set Listener
        setBarcodeRFIDScanResultListener(this);
        setReaderTriggerEvent(this);
    }

    /**
     * Load Init Meta Data for Dropdown
     */
    private void initMetaDetails() {
        if (employeeVerifyViewModel == null) {
            return;
        }
        employeeVerifyViewModel.employeeAssetVerificationInitial(new ApiResponseListener() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFailed(Object data) {

            }
        });
    }



    //Alert Sound

    /**
     * Speck the audio file
     *
     * @param sound
     */
    public void speak(int sound) {
        if (mp != null) {
            mp.release();
        }
        if (getActivity() != null) {
            mp = MediaPlayer.create(getActivity(), sound);
            mp.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mp != null) {
            mp.release();
        }
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
        if (getActivity() != null) {


            //Loading
            employeeVerifyViewModel.isDataLoading.observe(getActivity(), value -> {
                binding.rltAtvtyProgress.setVisibility(value ? View.VISIBLE : View.GONE);
            });

            //Employee Detail
            employeeVerifyViewModel.getSelecteEmployeeDetailMutableLiveData().observe(getActivity(), value -> {
                resetView();
                if (value != null) {
                    binding.valEmpCodeTxt.setText(Utility.getCommonValue(value.getEmployeeCode()));
                    binding.valEmpNameTxt.setText(Utility.getCommonValue(value.getEmployeeName()));
                    binding.valDepartmentCodeTxt.setText(Utility.getCommonValue(value.getDepartmentCode()));
                    binding.valDepartmentNameTxt.setText(Utility.getCommonValue(value.getDepartmentName()));
                    binding.valTagIdTxt.setText(Utility.getCommonValue(value.getTagid()));
                    binding.ectlEmployee.setText(value.getEmployeeName());
                    binding.rfidImg.setVisibility(View.GONE);
                } else {
                    binding.rfidImg.setVisibility(View.VISIBLE);
                }
            });

            //Employees List
            employeeVerifyViewModel.employeeDetailListResponseMutableLiveData.observe(getActivity(), this::loadEmployeesDropdown);

            //employeeVerifyViewModel.employeeVerifyTagUpdateResponseMutableLiveData.observe(getActivity(), value -> {
                ///TODO: Later
                /*if (value != null) {
                    if (value.ErStatus != null && value.ErStatus.equals("1")) {
                        resetData();
                        if (employeeVerifyViewModel != null) {
                            employeeVerifyViewModel.setEmployeeVerifyTagInsertRequests(new ArrayList<>());
                        }
                        Utility.showToast("" + value.ErDescription);
                        binding.scanBtnLayout.setVisibility(View.VISIBLE);
                        binding.submitRlay.setVisibility(View.GONE);
                    }
                }*/

            //});

            // employeeVerifyTagInsertRequests
            employeeVerifyViewModel.assertTagList.observe(getActivity(), value -> {
                int count = 0;
                if(value != null) {
                    count = value.size();
                }
                binding.valTotalTagScanned.setText(String.valueOf(count));
            });

            ///TODO: Later
            employeeVerifyViewModel.assetDetailListResponseMutableLiveData.observe(getActivity(), value -> {

                /*resetView();
                List<EmployeeDetail> employeeDetails = value.getEmployee_Details();
                if (employeeDetails != null && !employeeDetails.isEmpty()) {
                    EmployeeDetail employeeDetail = employeeDetails.get(0);
                    binding.valEmpCodeTxt.setText(Utility.getCommonValue(employeeDetail.getEmployeeCode()));
                    binding.valEmpNameTxt.setText(Utility.getCommonValue(employeeDetail.getEmployeeName()));
                    binding.valDepartmentCodeTxt.setText(Utility.getCommonValue(employeeDetail.getDepartmentCode()));
                    binding.valDepartmentNameTxt.setText(Utility.getCommonValue(employeeDetail.getDepartmentName()));
                    binding.valTagIdTxt.setText(Utility.getCommonValue(employeeDetail.getTagid()));
                }*/


                /*if (value.getChecked_Status() == 1 && value.getEmployee_Status() == 1) {
                    Utility.showToast("Scanned Successfully");
                    binding.scanBtnLayout.setVisibility(View.VISIBLE);
                    binding.submitRlay.setVisibility(View.GONE);
                } else if (value.getChecked_Status() == 1 && value.getEmployee_Status() == 0) {
                    Utility.showToast("Employee details not found");
                    speak(R.raw.alert);
                } else if (value.getChecked_Status() == 0 && value.getEmployee_Status() == 1) {
                    Utility.showToast("Tags not matching");
                    speak(R.raw.alert);
                } else if (value.getChecked_Status() == 0 && value.getEmployee_Status() == 0) {
                    Utility.showToast("Employee details not found and Tags not matching");
                    speak(R.raw.alert);
                }*/


                if (value != null && getActivity() != null) {
                    setGridView(value);
                    /*binding.rview.setLayoutManager(new LinearLayoutManager(getActivity()));
                    employeeVerifyListAdapter = new EmployeeVerifyListAdapter(value, this);
                    binding.rview.setAdapter(employeeVerifyListAdapter);*/
                } else {
                    clearGridView();
                    /*employeeVerifyListAdapter = null;
                    binding.rview.setAdapter(new EmployeeVerifyListAdapter(new ArrayList<>(), this));*/
                }

//                /*if (scanAssetDetails == null || scanAssetDetails.isEmpty()) {
//                    binding.scanBtnLayout.setVisibility(View.VISIBLE);
//                    binding.submitRlay.setVisibility(View.GONE);
//                } else {
//                    binding.scanBtnLayout.setVisibility(View.GONE);
//                    binding.submitRlay.setVisibility(View.VISIBLE);
//                }*/

            });

            employeeVerifyViewModel.getNetworkErrorMutableData().observe(getActivity(), this::loadNetworkErrorView);
        }
    }

    /**
     * Load Employees Dropdown
     *
     * @param employees
     */
    private void loadEmployeesDropdown(List<EmployeeDetail> employees) {
        binding.ectlEmployee.setText("");

        if (employees != null) {
            setAutoCompleteTextView(binding.ectlEmployee, employees);
        }
    }

    /**
     * Set AutoComplete Common method
     *
     * @param autoCompleteTextView
     * @param employeeDetailList
     */
    private void setAutoCompleteTextView(MaterialAutoCompleteTextView autoCompleteTextView, List<EmployeeDetail> employeeDetailList) {
        if (employeeDetailList != null && getActivity() != null) {

            EmployeeAutoCompleteTextViewAdapter adapter = new EmployeeAutoCompleteTextViewAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, employeeDetailList);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(adapter);
            //Click Listener
            autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
                Object item = adapterView.getItemAtPosition(i);

                if (item instanceof EmployeeDetail) { //Department
                    EmployeeDetail departmentDetail = (EmployeeDetail) item;
                    if (employeeVerifyViewModel != null) {
                        employeeVerifyViewModel.getSelecteEmployeeDetailMutableLiveData().setValue(departmentDetail);
                    }
                    employeesAllocatedAssetDetails("");
                    autoCompleteTextView.clearFocus();
                }
            });

            //Touch Listener
            autoCompleteTextView.setOnTouchListener((v, event) -> {
                autoCompleteTextView.showDropDown();
                return false;
            });
        }
    }

    /**
     * Fetch Filter Asset List based on Employee Selection
     */
    private void employeesAllocatedAssetDetails(String tagId) {
        if (employeeVerifyViewModel == null) {
            return;
        }
        employeeVerifyViewModel.employeesAllocatedAssetDetails(tagId, new ApiResponseListener() {
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

    private void loadNetworkErrorView(boolean value) {
        if (value) {
            Utility.showToast("No Internet Connection");
            //Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setOnClickListener() {

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllData();

            }
        });

        binding.rfidImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(employeeVerifyViewModel.getSelecteEmployeeDetailMutableLiveData().getValue() == null) {
                    Utility.startOrStopEventReceived(getActivity());
                    //employeesAllocatedAssetDetails("E2004702A39064267822010B");
                }
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.ectlRemarks.getText() == null || binding.ectlRemarks.getText().toString().isEmpty()) {
                    Utility.showToast("Remarks cannot be empty");
                } else {
                    if(employeeVerifyViewModel != null) {

                        if(employeeVerifyViewModel.selecteEmployeeDetailMutableLiveData.getValue() != null) {
                            employeeVerifyViewModel.selecteEmployeeDetailMutableLiveData.getValue().setRemarks(binding.ectlRemarks.getText().toString());
                            SharedPreference sharedPreference = new SharedPreference(Application.getAppContext());
                            employeeVerifyViewModel.selecteEmployeeDetailMutableLiveData.getValue().setCreatedBy(sharedPreference.getUserId());
                        }

                        employeeVerifyViewModel.employeeAssetScannedInsert(new ApiResponseListener() {
                            @Override
                            public void onSuccess(Object data) {
                                resetAllData();
                                if (data instanceof String) {
                                    Utility.showToast(String.valueOf(data));
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
                }

                /*if (employeeVerifyListAdapter != null && employeeVerifyListAdapter.getScanAssetDetailList() != null &&
                        !employeeVerifyListAdapter.getScanAssetDetailList().isEmpty()) {
                ///TODO: Later
                    if (!employeeVerifyListAdapter.isRemarkEmpty()) {


                        employeeVerifyViewModel.employeeVerifyTagUpdate("MSC155", employeeVerifyListAdapter.getScanAssetDetailList(), new ApiResponseListener() {

                            @Override
                            public void onSuccess(Object data) {

                            }

                            @Override
                            public void onFailed(Object data) {
                                resetData();
                                if (data instanceof String) {
                                    Utility.showToast(String.valueOf(data));
                                }
                            }
                        });
                    } else {
                        Utility.showToast("Remarks cannot be empty");
                    }
                }*/
            }
        });

        binding.btnRFID.setOnClickListener(view -> {

            ///TEST Start
            /*List<EmployeeVerifyTagInsertRequest> employeeVerifyTagInsertRequests = new ArrayList<>();


            employeeVerifyTagInsertRequests.add(new EmployeeVerifyTagInsertRequest("e2004702e93064267c7c0108", null));
            employeeVerifyTagInsertRequests.add(new EmployeeVerifyTagInsertRequest("4444", null));*/

            /*employeeVerifyTagInsertRequests.add(new EmployeeVerifyTagInsertRequest("E280699500004011F1F774CE", null));
            employeeVerifyTagInsertRequests.add(new EmployeeVerifyTagInsertRequest("e200470293064267c7c0108", null));
            employeeVerifyTagInsertRequests.add(new EmployeeVerifyTagInsertRequest("HLTC0002", null));
            employeeVerifyTagInsertRequests.add(new EmployeeVerifyTagInsertRequest("FD000002", null));*/

            /*employeeVerifyTagInsert(employeeVerifyTagInsertRequests);*/


            /*List<String> tagsList = new ArrayList<>();
            tagsList.add("119819819819819189819");
            tagsList.add("81981981981981981981");
            employeeVerifyViewModel.assertTagList.setValue(tagsList);
            startCallAPIAfterStopRFIDScan();*/

            ///TEST End

            if (getActivity() != null && getActivity() instanceof ActiveDeviceActivity && (RFIDController.mIsInventoryRunning == true)) {
                startCallAPIAfterStopRFIDScan();
            } else if (getActivity() != null && getActivity() instanceof ActiveDeviceActivity && (RFIDController.mIsInventoryRunning == false) &&
                    binding.scanBtnLayout.getVisibility() == View.VISIBLE && employeeVerifyViewModel != null) {
                employeeVerifyViewModel.setAssertTagList(new ArrayList<>());
            }

            //16-Feb-2025
            Utility.startOrStopEventReceived(getActivity());




            /*if (getActivity() != null && getActivity() instanceof ActiveDeviceActivity) {
                ActiveDeviceActivity activeDeviceActivity = (ActiveDeviceActivity) getActivity();
                if (activeDeviceActivity != null) {
                    //activeDeviceActivity.setTriggerMode(ENUM_TRIGGER_MODE.RFID_MODE);
                    activeDeviceActivity.inventoryStartOrStop(null);
                }
            }*/


            //sendAssetViewRequest("479060304798123648208285617099852");


            /*assetViewViewModel.sendAssetViewRequest("479060304798123648208285617099852", new ApiResponseListener() {

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
            });*/


        });

        binding.elblEmployee.setEndIconOnClickListener(v -> {
            resetAllData();

        });
    }


    private void employeeVerifyAssetFetch() {
        employeeVerifyViewModel.employeeVerifyAssetFetch(new ApiResponseListener() {

            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFailed(Object data) {
                //resetData();
                if (data instanceof String) {
                    Utility.showToast(String.valueOf(data));
                }
            }
        });
    }

    private void resetAllData() {
        binding.valEmpCodeTxt.setText("-");
        binding.valEmpNameTxt.setText("-");
        binding.valDepartmentCodeTxt.setText("-");
        binding.valDepartmentNameTxt.setText("-");
        binding.valTagIdTxt.setText("-");
        binding.ectlEmployee.setText("");
        binding.ectlRemarks.setText("");

        clearGridView();
        /*employeeVerifyListAdapter = null;
        binding.rview.setAdapter(new EmployeeVerifyListAdapter(new ArrayList<>(), this));*/
        if(employeeVerifyViewModel != null) {
            employeeVerifyViewModel.getSelecteEmployeeDetailMutableLiveData().setValue(null);
            employeeVerifyViewModel.setAssertTagList(new ArrayList<>());
            employeeVerifyViewModel.assetDetailListResponseMutableLiveData.setValue(null);
            employeeVerifyViewModel.globleAssetDetailListResponseMutableLiveData.setValue(null);
        }

        binding.scanBtnLayout.setVisibility(View.VISIBLE);
        binding.submitRlay.setVisibility(View.GONE);

    }

    private void resetView() {
        binding.valEmpCodeTxt.setText("-");
        binding.valEmpNameTxt.setText("-");
        binding.valDepartmentCodeTxt.setText("-");
        binding.valDepartmentNameTxt.setText("-");
        binding.valTagIdTxt.setText("-");
        binding.ectlEmployee.setText("");
        binding.ectlRemarks.setText("");

        clearGridView();
        /*employeeVerifyListAdapter = null;
        binding.rview.setAdapter(new EmployeeVerifyListAdapter(new ArrayList<>(), this));*/

        /*if (employeeVerifyViewModel != null) {
            employeeVerifyViewModel.setEmployeeVerifyTagInsertRequests(new ArrayList<>());
        }*/
    }

    @Override
    public void scannerBarcodeEvent(byte[] barcodeData, int barcodeType, int scannerID) {
        /*Log.d("AssetView Fragment", "Barcode -" +  new String(barcodeData));
        Log.d("AssetView Fragment", "Barcode -" + BarcodeTypes.getBarcodeTypeName(barcodeType));

        String data = new String(barcodeData);
        if(!data.isEmpty() && employeeVerifyViewModel != null
                && employeeVerifyViewModel.isDataLoading != null && (employeeVerifyViewModel.isDataLoading.getValue() == null
                || !employeeVerifyViewModel.isDataLoading.getValue())) {
            sendAssetViewRequest(data);
//            sendAssetViewRequest("47906030479812364820828561709985");
        }*/
    }

    @Override
    public void scannerRFIDEvent(List<InventoryListItem> rfidDataList) {
        //if (activity != null && activity instanceof ActiveDeviceActivity && (RFIDController.mIsInventoryRunning == true)) {
        try {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (binding.scanBtnLayout.getVisibility() == View.VISIBLE) {

                            if (rfidDataList != null && !rfidDataList.isEmpty() && employeeVerifyViewModel != null
                                    && employeeVerifyViewModel.isDataLoading != null && (employeeVerifyViewModel.isDataLoading.getValue() == null
                                    || !employeeVerifyViewModel.isDataLoading.getValue())) {


                                if(employeeVerifyViewModel.getSelecteEmployeeDetailMutableLiveData().getValue() == null) { //Employee Selection

                                    for (InventoryListItem inventoryListItem : rfidDataList) {
                                        String tagId = inventoryListItem.getTagID();
                                        if(tagId != null) {
                                            employeesAllocatedAssetDetails(tagId);
                                            Utility.triggerReleaseStopEventReceived(getActivity());
                                            break;
                                        }
                                    }

                                } else {
                                    if (employeeVerifyViewModel.getSelecteEmployeeDetailMutableLiveData().getValue() != null) { // Assert Selection
                                        for (InventoryListItem inventoryListItem : rfidDataList) {
                                            employeeVerifyViewModel.addEmployeeVerifyTagInsertRequests(inventoryListItem.getTagID());
                                        }
                                    } else {
                                        Utility.triggerReleaseStopEventReceived(getActivity());
                                    }
                                }
                            }
                        }

                        /*if (rfidDataList != null && !rfidDataList.isEmpty() && employeeVerifyViewModel != null
                                && employeeVerifyViewModel.isDataLoading != null && (employeeVerifyViewModel.isDataLoading.getValue() == null
                                || !employeeVerifyViewModel.isDataLoading.getValue())) {

                            //sendAssetViewRequest(rfidDataList.get(0).getTagID());
                            //sendAssetViewRequest("47906030479812364820828561709985");

                            List<EmployeeVerifyTagInsertRequest> employeeVerifyTagInsertRequests = new ArrayList<>();
                            for (InventoryListItem inventoryListItem : rfidDataList) {
                                Log.e("AssetView Fragment", "RFID Tag ID" + inventoryListItem.getTagID());
                                employeeVerifyTagInsertRequests.add(new EmployeeVerifyTagInsertRequest(inventoryListItem.getTagID(), null));
                            }

                            employeeVerifyTagInsert(employeeVerifyTagInsertRequests);
                        }*/
                    }
                });
            }
        } catch (Exception e) {
            //Utility.showToast("Exception1 : " + e);
        }
    }

    @Override
    public void onRecyclerViewItemClickListener(int position, Object object) {

    }

    @Override
    public void onStartReaderEvent() {
        if (binding.scanBtnLayout.getVisibility() == View.VISIBLE && employeeVerifyViewModel != null) {
            employeeVerifyViewModel.setAssertTagList(new ArrayList<>());
        }
    }

    @Override
    public void onStopReaderEvent() {
        /*if(employeeVerifyViewModel != null) {
            employeeVerifyViewModel.setEmployeeVerifyTagInsertRequests(new ArrayList<>());
        }*/

        startCallAPIAfterStopRFIDScan();
    }

    private void startCallAPIAfterStopRFIDScan() {
        try {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.scanBtnLayout.getVisibility() == View.VISIBLE && employeeVerifyViewModel != null && employeeVerifyViewModel.getAssertTagList() != null
                                && !employeeVerifyViewModel.getAssertTagList().isEmpty()) {
                            binding.scanBtnLayout.setVisibility(View.GONE);
                            binding.submitRlay.setVisibility(View.VISIBLE);
                            employeeVerifyAssetFetch();
                        }
                    }
                });
            }
        } catch (Exception e) {
            //Utility.showToast("Exception1 : " + e);
        }
    }

    /*@Override
    public void triggerPressEventRecieved() {
        super.triggerPressEventRecieved();
    }

    @Override
    public void triggerReleaseEventRecieved() {
        super.triggerReleaseEventRecieved();
    }*/

    /*public void handleStatusResponse(final RFIDResults results) {
        super.handleStatusResponse(results);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (results.equals(RFIDResults.RFID_BATCHMODE_IN_PROGRESS)) {
                    Utility.showToast("RFID_BATCHMODE_IN_PROGRESS");
                   Log.e("handleStatusResponse 2", "RFID_BATCHMODE_IN_PROGRESS");
                } else if(results.equals(RFIDResults.RFID_OPERATION_IN_PROGRESS )){
                    Utility.showToast("RFID_OPERATION_IN_PROGRESS");
                    Log.e("handleStatusResponse 2", "RFID_OPERATION_IN_PROGRESS");

                }else if (!results.equals(RFIDResults.RFID_API_SUCCESS)) {
                    Utility.showToast("RFID_API_SUCCESS");
                    Log.e("handleStatusResponse 2", "RFID_API_SUCCESS");
                }
            }
        });
    }*/
}
