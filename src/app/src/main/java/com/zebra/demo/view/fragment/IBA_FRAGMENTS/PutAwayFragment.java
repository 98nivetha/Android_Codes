package com.zebra.demo.view.fragment.IBA_FRAGMENTS;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.zebra.demo.R;
import com.zebra.demo.data.remote.requestmodels.PutAwayScanningRequest;
import com.zebra.demo.data.remote.responsemodels.DisplayItem;
import com.zebra.demo.data.remote.responsemodels.GetAllRackDatasResponse;
import com.zebra.demo.data.remote.responsemodels.PutAwayScanningResponse;
import com.zebra.demo.data.remote.responsemodels.RackDetails;
import com.zebra.demo.databinding.FragmentPutawayBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.adapter.FilterAutoCompleteTextViewAdapter;
import com.zebra.demo.view.adapter.GenericAdapter;
import com.zebra.demo.view.fragment.BarcodeRFIDScanBaseFragment;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import com.zebra.demo.viewmodel.PutAwayViewModel;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PutAwayFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, RecyclerViewItemClickListener, ApiResponseListener {
    private PutAwayViewModel putAwayViewModel;
    private FragmentPutawayBinding binding;
    List<DisplayItem> scannedItemList = new ArrayList<>();
    private GenericAdapter<DisplayItem> adapter;

    private List<String> scannedRfids = new ArrayList<>(Arrays.asList(
            "E280699500004011F1F44CB2"
    ));


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
        setOnClickListener();
        adapter = new GenericAdapter<>(
                new ArrayList<>(),
                R.layout.item_scanned_material,
                (item, view) -> {
                    if (item instanceof DisplayItem) {
                        DisplayItem displayItem = (DisplayItem) item;
                        PutAwayScanningResponse.MaterialItem material = displayItem.getMaterialItem();

                        if (material != null) {
                            ((TextView) view.findViewById(R.id.txtMaterialCode)).setText("Code: " + material.getMaterialcode());
                            ((TextView) view.findViewById(R.id.txtMaterialName)).setText("Name: " + material.getMaterialname());
                            ((TextView) view.findViewById(R.id.txtQuantity)).setText("Qty: " + material.getQuantity());
                            ((TextView) view.findViewById(R.id.txtCci)).setText("CCI: " + material.isCci());
                            ((TextView) view.findViewById(R.id.txtRackGroup)).setText("Group: " + material.getRackgroup());
                            ((TextView) view.findViewById(R.id.txtRackCode)).setText("Rack Code: " + material.getRackcode());
                            ((TextView) view.findViewById(R.id.txtRfid)).setText("RFID: " + material.getRfid());

                            ImageView icon = view.findViewById(R.id.imgValidationIcon);

                            if (displayItem.isValid()) {
                                view.setBackgroundColor(Color.parseColor("#E0FFE0")); // Mild green
                                icon.setImageResource(R.drawable.tick);
                                icon.setColorFilter(Color.parseColor("#4CAF50")); // Green
                                icon.setOnClickListener(null); // No click action for valid
                            } else {
                                view.setBackgroundColor(Color.parseColor("#FFE0E0"));
                                icon.setImageResource(R.drawable.ic_close);
                                icon.setColorFilter(Color.parseColor("#F44336"));

                                // You must pass context, adapter, scannedItemList, and binding to this scope
                                icon.setOnClickListener(v -> {
                                    Context context = view.getContext(); // Safely get context from view

                                    new AlertDialog.Builder(context)
                                            .setTitle("Clear Invalid Items")
                                            .setMessage("Do you want to remove invalid items from the list?")
                                            .setPositiveButton("Yes", (dialog, which) -> {
                                                // Remove invalid items
                                                Iterator<DisplayItem> iterator = scannedItemList.iterator();
                                                while (iterator.hasNext()) {
                                                    if (!iterator.next().isValid()) {
                                                        iterator.remove();
                                                    }
                                                }

                                                adapter.updateList(scannedItemList); // Update adapter list

                                                binding.clearButton.setEnabled(false); // Disable the clear button
                                            })
                                            .setNegativeButton("No", null)
                                            .show();
                                });
                            }

                        } else {
                            Log.e("ADAPTER_ERROR", "MaterialItem is null for DisplayItem: " + item.toString());
                        }
                    } else {
                        Log.e("ADAPTER_ERROR", "Item is not DisplayItem: " + item);
                    }
                }
        );
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setVisibility(View.VISIBLE);
        putAwayViewModel.isDataLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && isLoading) {
                binding.rltAtvtyProgress.setVisibility(View.VISIBLE);
            } else {
                binding.rltAtvtyProgress.setVisibility(View.GONE);
            }
        });

        binding.clearButton.setVisibility(View.GONE);
        binding.submitButton.setVisibility(View.GONE);

        return binding.getRoot();
    }


    private void setOnClickListener() {
        binding.scanButton.setOnClickListener(view -> {
            RackDetails selectedRack = putAwayViewModel.selectedRackDetailObject.getValue();
            if (selectedRack == null || selectedRack.getRackid() == 0) {
                Log.e("ScanButton", "Rack is not selected or invalid rack ID");
                Toast.makeText(requireContext(), "Please select a rack first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (scannedRfids == null || scannedRfids.isEmpty()) {
                Log.e("ScanButton", "RFID list is empty");
                Toast.makeText(requireContext(), "No RFID scanned", Toast.LENGTH_SHORT).show();
                return;
            }

            PutAwayScanningRequest request = new PutAwayScanningRequest(
                    selectedRack.getRackid(),
                    scannedRfids
            );

            putAwayViewModel.sendPutAwayScanning(request, new ApiResponseListener() {
                @Override
                public void onSuccess(Object data) {
                    Log.d("API_SUCCESS", "PutAway API succeeded");
                    binding.clearButton.setEnabled(true);
                    if (data instanceof List) {
                        List<?> responseList = (List<?>) data;

                        scannedItemList.clear();

                        for (Object obj : responseList) {
                            if (obj instanceof DisplayItem) {
                                scannedItemList.add((DisplayItem) obj);
                            }
                        }

                        adapter.updateList(scannedItemList);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                }


                @Override
                public void onFailed(Object error) {
                    Log.e("API_FAILED", "PutAway API failed: " + error);
                }
            });
        });

        binding.submitButton.setOnClickListener(v -> {
            boolean hasInvalid = false;
            List<DisplayItem> scannedItems = this.scannedItemList;

            putAwayViewModel.insertPutAwayScanningData(scannedItems, new ApiResponseListener() {
                @Override
                public void onSuccess(Object data) {
                    Toast.makeText(requireContext(), "Submission successful!", Toast.LENGTH_SHORT).show();
                    scannedItemList.clear();
                    adapter.updateList(scannedItemList);
                    binding.recyclerView.setVisibility(View.GONE);
                }

                @Override
                public void onFailed(Object data) {
                    Toast.makeText(requireContext(), "Submission failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.clearButton.setOnClickListener(view -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Clear Invalid Items")
                    .setMessage("Do you want to remove invalid items from the list?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Iterator<DisplayItem> iterator = scannedItemList.iterator();
                        while (iterator.hasNext()) {
                            if (!iterator.next().isValid()) {
                                iterator.remove();
                            }
                        }
                        adapter.updateList(scannedItemList);
                        binding.clearButton.setEnabled(false);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }


    private void initMetaDetails() {
        if (putAwayViewModel == null) {
            return;
        }
        putAwayViewModel.fetchMetaDetails(this);
    }


    private void setObserver() {
        if (getActivity() != null) {
            putAwayViewModel.isDataLoading.observe(getViewLifecycleOwner(), isLoading -> {
            });
            putAwayViewModel.rackDetailsDDListLiveData.observe(getActivity(), this::loadRackDropdown);
            putAwayViewModel.getShowClearButton().observe(getViewLifecycleOwner(), visible -> {
                binding.clearButton.setVisibility(visible ? View.VISIBLE : View.GONE);
            });
            putAwayViewModel.getShowSubmitButton().observe(getViewLifecycleOwner(), visible -> {
                binding.submitButton.setVisibility(visible ? View.VISIBLE : View.GONE);
            });
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
                        putAwayViewModel.selectedRackDetailObject.setValue(rackDetails);
                        putAwayViewModel.selectedRackID.setValue(rackDetails.getRackid());

                        PutAwayScanningRequest request = new PutAwayScanningRequest(rackDetails.getRackid(), scannedRfids);
                        request.setRackid(rackDetails.getRackid());
//                        request.setRfids(scannedRfids);
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