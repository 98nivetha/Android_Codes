package com.zebra.demo.view.fragment.IBA_FRAGMENTS;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.zebra.demo.R;
import com.zebra.demo.data.remote.responsemodels.RackDetails;
import com.zebra.demo.databinding.FragmentRockwiseinfoBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.adapter.MaterialAdapter;
import com.zebra.demo.view.fragment.BarcodeRFIDScanBaseFragment;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import com.zebra.demo.viewmodel.RockWiseInfoViewModel;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RackWiseInfoFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, RecyclerViewItemClickListener, ApiResponseListener {
    private RockWiseInfoViewModel rockWiseInfoViewModel;
    private FragmentRockwiseinfoBinding binding;
    private boolean isPageInitLoad = true;
    private MaterialAdapter materialAdapter;

    @Override
    public void onSuccess(Object data) {
    }

    @Override
    public void onFailed(Object data) {
        if (data instanceof String) {
            Utility.showToast(String.valueOf(data));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setBarcodeRFIDScanResultListener(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rockwiseinfo, container, false);
        View view = binding.getRoot();
        rockWiseInfoViewModel = new ViewModelProvider(this).get(RockWiseInfoViewModel.class);
        materialAdapter = new MaterialAdapter();
        binding.rvMaterials.setAdapter(materialAdapter);
        binding.rvMaterials.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.setRackwise(rockWiseInfoViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        if (isPageInitLoad) {
            setObserver();
            fetchRackData();
            isPageInitLoad = false;
        }
        return view;
    }


    private void fetchRackData() {
        if (rockWiseInfoViewModel != null) {
            rockWiseInfoViewModel.fetchRackDataList(this);
        }
    }

    private void setObserver() {
        if (rockWiseInfoViewModel == null || getActivity() == null) {
            return;
        }
        rockWiseInfoViewModel.rackDDValidationText.observe(getViewLifecycleOwner(), value -> {
            binding.elblRack.setError(value);
            binding.elblRack.setErrorEnabled(value != null);
        });
        rockWiseInfoViewModel.getRackDetailLiveData().observe(getViewLifecycleOwner(), this::loadRackDropdown);
        rockWiseInfoViewModel.getMaterialDetailsLiveData().observe(getViewLifecycleOwner(), materialList -> {
            if (materialList != null) {
                materialAdapter.setData(materialList);
            }
        });
    }


    private void loadRackDropdown(List<RackDetails> dRackDetailList) {
        if (dRackDetailList != null && !dRackDetailList.isEmpty()) {
            setAutoCompleteTextView(binding.ectlRack, dRackDetailList, RackDetails::getRackname);
        }
        binding.ectlRack.setOnItemClickListener((parent, view, position, id) -> {
            RackDetails selected = dRackDetailList.get(position);
            binding.ectlRack.setText(selected.getRackname(), false);
            rockWiseInfoViewModel.selectedRack = selected;
            rockWiseInfoViewModel.selectedRackLiveData.setValue(selected);
            rockWiseInfoViewModel.fetchAllMaterialsByRack(this);
            closeKeyBoard();
        });
    }

    private <T> void setAutoCompleteTextView(MaterialAutoCompleteTextView view, List<T> list, Function<T, String> nameMapper) {
        List<String> names = new ArrayList<>();

        for (T item : list) {
            String name = nameMapper.apply(item);
            if (name != null && !name.trim().isEmpty()) {
                names.add(name);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                names
        );
        view.setAdapter(adapter);
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

    private void closeKeyBoard() {
        try {
            if (getActivity() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
        }
    }
}
