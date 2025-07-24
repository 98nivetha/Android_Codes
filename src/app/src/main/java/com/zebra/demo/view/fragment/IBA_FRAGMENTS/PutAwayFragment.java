package com.zebra.demo.view.fragment.IBA_FRAGMENTS;
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
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
import com.zebra.demo.data.remote.responsemodels.RackDetails;
import com.zebra.demo.databinding.FragmentPutawayBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.adapter.FilterAutoCompleteTextViewAdapter;
import com.zebra.demo.view.fragment.BarcodeRFIDScanBaseFragment;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import com.zebra.demo.viewmodel.PutAwayViewModel;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import java.util.List;

public class PutAwayFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, RecyclerViewItemClickListener, ApiResponseListener {
    private PutAwayViewModel putAwayViewModel;
    private FragmentPutawayBinding binding;

    @Override
    public void onSuccess(Object data) {
        if (data instanceof GetAllRackDatasResponse) {
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setBarcodeRFIDScanResultListener(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_putaway, container, false);
        putAwayViewModel = new ViewModelProvider(this).get(PutAwayViewModel.class);
        binding.setMaterialview(putAwayViewModel);
        setObserver();
        initMetaDetails();
        binding.getRoot().setOnClickListener(v -> Log.d("TEST", "Root clicked"));
        return binding.getRoot();
    }

//    private void setOnClickListener() {
//        Log.d("setOnClickListener", "Listener initialized");
//
//        binding.elblRack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("setOnClickListener", "elblRack clicked");
//
//                RackDetails selectedRack = putAwayViewModel.selectedRackDetailObject.getValue();
//                if (selectedRack != null) {
//                    Log.d("RACK_SELECTED", "Rack Name: " + selectedRack.getRackname() + ", Rack ID: " + selectedRack.getRackid());
//                } else {
//                    Log.d("RACK_SELECTED", "No rack selected");
//                }
//            }
//        });
//    }

    private void initMetaDetails() {
        if (putAwayViewModel == null) {
            return;
        }
        putAwayViewModel.fetchMetaDetails(this);
    }


    private void setObserver() {
        if (getActivity() != null) {
            putAwayViewModel.isDataLoading.observe(getViewLifecycleOwner(), isLoading -> {
                // binding.rltAtvtyProgress.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            });
            putAwayViewModel.rackDetailsDDListLiveData.observe(getActivity(), this::loadRackDropdown);
        }
    }


    @Override
    public void onFailed(Object data) {
        if (data instanceof String) {
            Utility.showToast(String.valueOf(data));
        }
    }

    private void loadRackDropdown(List<RackDetails> dRackDetails) {
        binding.ectlRack.setText("");
        if (dRackDetails != null) {
            setAutoCompleteTextView(binding.ectlRack, (List<Object>) (Object) dRackDetails);
        }
    }

    private void setAutoCompleteTextView(MaterialAutoCompleteTextView autoCompleteTextView, List<Object> objectList) {
        if (objectList != null && getActivity() != null) {
            FilterAutoCompleteTextViewAdapter assetHeaderArrayAdapter = new FilterAutoCompleteTextViewAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, objectList);
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(assetHeaderArrayAdapter);
            autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
                Object item = adapterView.getItemAtPosition(i);
                if (item instanceof RackDetails) { // Rack
                    RackDetails rackDetails = (RackDetails) item;
                    if (putAwayViewModel != null) {
                        putAwayViewModel.selectedRackDetail.setValue(rackDetails.getRackname());
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

    }

    @Override
    public void onRecyclerViewItemClickListener(int position, Object object) {

    }
}
