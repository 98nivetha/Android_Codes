package com.zebra.demo.view.fragment.IBA_FRAGMENTS;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.zebra.demo.R;
import com.zebra.demo.data.local.SharedPreference;
import com.zebra.demo.data.remote.requestmodels.ReturnPartsInsertUpdateRequest;
import com.zebra.demo.data.remote.requestmodels.ReturnPartsValidInfoRequest;
import com.zebra.demo.data.remote.requestmodels.StockInwardRequest;
import com.zebra.demo.data.remote.responsemodels.ReturnDefectiveTypeResponse;
import com.zebra.demo.data.remote.responsemodels.ReturnPartsValidInfoResponse;
import com.zebra.demo.databinding.FragmentReallocateBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.adapter.FilterAutoCompleteTextViewAdapter;
import com.zebra.demo.view.adapter.GenericAdapter;
import com.zebra.demo.view.fragment.BarcodeRFIDScanBaseFragment;
import com.zebra.demo.view.listener.ApiResponseListener;
import com.zebra.demo.view.listener.BarcodeRFIDScanResultListener;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import com.zebra.demo.viewmodel.PutAwayViewModel;
import com.zebra.demo.viewmodel.ReAllocateViewModel;
import com.zebra.demo.viewmodel.RockWiseInfoViewModel;
import com.zebra.demo.zebralib.rfidreader.inventory.InventoryListItem;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReAllocateFragment extends BarcodeRFIDScanBaseFragment implements BarcodeRFIDScanResultListener, RecyclerViewItemClickListener, ApiResponseListener {
    private FragmentReallocateBinding binding;
    private ReAllocateViewModel reAllocateViewModel;
    private PutAwayViewModel putAwayViewModel;
    private RockWiseInfoViewModel rockWiseInfoViewModel;
    private List<String> scannedRfids = new ArrayList<>(Arrays.asList("E280699500004011F1F44CB2"));
    // At class level

    private GenericAdapter<ReturnPartsValidInfoResponse.ValidItem> adapter;
    private final List<ReturnPartsValidInfoResponse.ValidItem> validItems = new ArrayList<>();
    private ReturnPartsValidInfoResponse.ValidItem clickedItemTemp;
    List<ReturnPartsValidInfoResponse.ValidItem> scannedItemList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reallocate, container, false);
        reAllocateViewModel = new ViewModelProvider(this).get(ReAllocateViewModel.class);
        putAwayViewModel = new ViewModelProvider(this).get(PutAwayViewModel.class);
        rockWiseInfoViewModel = new ViewModelProvider(this).get(RockWiseInfoViewModel.class);
        setOnClickListener();
        initMetaDetails();
        adapter = new GenericAdapter<>(new ArrayList<>(), R.layout.item_scanned_material, (item, view) -> {
            ((TextView) view.findViewById(R.id.txtMaterialCode)).setText("Code: " + item.getMaterialcode());
            ((TextView) view.findViewById(R.id.txtMaterialName)).setText("Name: " + item.getMaterialname());
            ((TextView) view.findViewById(R.id.txtQuantity)).setText("Qty: " + item.getQuantity());
            ((TextView) view.findViewById(R.id.txtCci)).setText("CCI: " + item.isCci());
            ((TextView) view.findViewById(R.id.txtRackGroup)).setText("Group: " + item.getRackgroup());
            ((TextView) view.findViewById(R.id.txtRackCode)).setText("Rack Code: " + item.getRackcode());
            ((TextView) view.findViewById(R.id.txtRfid)).setText("RFID: " + item.getRfid());

            ImageView icon = view.findViewById(R.id.imgValidationIcon);

            if (item.isCci()) {
                view.setBackgroundColor(Color.parseColor("#E0FFE0"));
                icon.setImageResource(R.drawable.tick);
                icon.setColorFilter(Color.parseColor("#4CAF50"));
                ((TextView) view.findViewById(R.id.txtCci)).setText("CCI: YES (Important)");
            } else {
                view.setBackgroundColor(Color.parseColor("#FFE0E0"));
                icon.setImageResource(R.drawable.ic_close);
                icon.setColorFilter(Color.parseColor("#F44336"));
            }

            view.setOnClickListener(v -> {
                new AlertDialog.Builder(requireContext()).setTitle("Select Part Type").setMessage("Is this item Good or Defective?").setPositiveButton("Good", (dialog, which) -> {
                    showGoodQuantityDialog(item);
                }).setNegativeButton("Defective", (dialog, which) -> {
                    reAllocateViewModel.metaDetailsForDefectiveDropdown(new ApiResponseListener() {
                        @Override
                        public void onSuccess(Object response) {
                            requireActivity().runOnUiThread(() -> {
                                List<ReturnDefectiveTypeResponse.MaterialDetail> materialList = reAllocateViewModel.materialDetailsDDListLiveData.getValue();
                                List<ReturnDefectiveTypeResponse.RackDetail> rackList = reAllocateViewModel.rackDetailsDDListLiveData.getValue();

                                if (materialList == null || rackList == null || materialList.isEmpty() || rackList.isEmpty()) {
                                    Toast.makeText(requireContext(), "Dropdown data is empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                try {
                                    showDefectiveDialog(item, materialList, rackList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailed(Object data) {
                            requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "Failed to load dropdown data", Toast.LENGTH_SHORT).show());
                        }
                    });
                }).show();
            });
            reAllocateViewModel.isDataLoading.observe(getViewLifecycleOwner(), isLoading -> {
                if (isLoading != null && isLoading) {
                    binding.rltAtvtyProgress.setVisibility(View.VISIBLE);
                } else {
                    binding.rltAtvtyProgress.setVisibility(View.GONE);
                }
            });
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setVisibility(View.GONE);
        binding.setReAllocateView(reAllocateViewModel);
        putAwayViewModel.getQuantity().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer quantity) {
                StockInwardRequest.IbaIvmInvStockinwardPackDetail detail = new StockInwardRequest.IbaIvmInvStockinwardPackDetail();
                detail.setQuantity(quantity);
            }
        });
        setObserver();
        return binding.getRoot();
    }


    private void showGoodQuantityDialog(ReturnPartsValidInfoResponse.ValidItem clickedItem) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_good_quantity, null);
        EditText quantityInput = dialogView.findViewById(R.id.editQuantity);

        new AlertDialog.Builder(requireContext()).setTitle("Enter Quantity").setView(dialogView).setPositiveButton("Submit", (dialog, which) -> {
            String quantityStr = quantityInput.getText().toString().trim();

            if (quantityStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            putAwayViewModel.setQuantity(quantity);
            SharedPreference sharedPreference = new SharedPreference(requireContext());
            Integer userId = sharedPreference.getCreatedByUserId();
            StockInwardRequest.IbaIvmInvStockinwardPackDetail detail = new StockInwardRequest.IbaIvmInvStockinwardPackDetail();
            detail.setPackid(clickedItem.getPackid());
            detail.setDetailid(clickedItem.getDetailid());
            detail.setInwardid(clickedItem.getInwardid());
            detail.setModelid(clickedItem.getModelid());
            detail.setModelname("MODEL-1998");
            detail.setMaterialid(clickedItem.getMaterialid());
            detail.setMaterialname(clickedItem.getMaterialname());
            detail.setQuantity(quantity);
            detail.setRfid(clickedItem.getRfid());
            detail.setRackid(clickedItem.getRackid());
            detail.setStatus(1);
            detail.setCreateby(userId);
            detail.setModifiedby(userId);
            detail.setCreatedon(Utility.getCurrentDateTimeString());
            detail.setModifiedon(Utility.getCurrentDateTimeString());
            List<ReturnPartsValidInfoResponse.ValidItem> scannedItems = new ArrayList<>();
            scannedItems.add(clickedItem);
            putAwayViewModel.insertPutAwayScanningDataForReallocate(scannedItems, new ApiResponseListener() {
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
            Toast.makeText(requireContext(), "Qty: " + quantity, Toast.LENGTH_SHORT).show();
        }).setNegativeButton("Cancel", null).show();
    }

    private void showDefectiveDialog(ReturnPartsValidInfoResponse.ValidItem clickedItem, List<ReturnDefectiveTypeResponse.MaterialDetail> materialList, List<ReturnDefectiveTypeResponse.RackDetail> rackList) {

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_defective_quantity, null);

        MaterialAutoCompleteTextView materialDropdown = dialogView.findViewById(R.id.ectl_material);
        MaterialAutoCompleteTextView rackDropdown = dialogView.findViewById(R.id.ectl_rack);
        EditText quantityInput = dialogView.findViewById(R.id.editQuantity);
        ImageView qrIcon = dialogView.findViewById(R.id.qr_icon);
        final int[] selectedMaterialId = {-1};
        final int[] selectedRackId = {-1};

        ArrayAdapter<String> materialAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, getMaterialNames(materialList));
        materialDropdown.setAdapter(materialAdapter);
        materialDropdown.setOnItemClickListener((parent, view, position, id) -> {
            selectedMaterialId[0] = materialList.get(position).getMaterialId();
        });

        ArrayAdapter<String> rackAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, getRackCodes(rackList));
        rackDropdown.setAdapter(rackAdapter);
        rackDropdown.setOnItemClickListener((parent, view, position, id) -> {
            selectedRackId[0] = rackList.get(position).getRackId();
        });
        qrIcon.setOnClickListener(v -> {
            Log.d("TEST CLICK", "TEST");
            rockWiseInfoViewModel.fetchRackDataList(new ApiResponseListener() {
                @Override
                public void onSuccess(Object response) {
                    List<ReturnDefectiveTypeResponse.RackDetail> rackList = (List<ReturnDefectiveTypeResponse.RackDetail>) response;
                    ArrayAdapter<String> rackAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, getRackCodes(rackList));
                    rackDropdown.setAdapter(rackAdapter);
                }

                @Override
                public void onFailed(Object data) {
                    Toast.makeText(requireContext(), "Failed to refresh racks", Toast.LENGTH_SHORT).show();
                }
            });
        });

        materialDropdown.setText("", false);
        rackDropdown.setText("", false);

        new AlertDialog.Builder(requireContext()).setTitle("Select Material, Rack & Quantity").setView(dialogView).setPositiveButton("Submit", (dialog, which) -> {
            String quantityStr = quantityInput.getText().toString().trim();

            if (selectedMaterialId[0] == -1 || selectedRackId[0] == -1) {
                Toast.makeText(requireContext(), "Please select material and rack", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!quantityStr.isEmpty()) {
                SharedPreference sharedPreference = new SharedPreference(requireContext());
                Integer userIdStr = sharedPreference.getCreatedByUserId();
                ReturnPartsInsertUpdateRequest.PicklistDetail detail = new ReturnPartsInsertUpdateRequest.PicklistDetail();
                detail.setPackid(clickedItem.getPackid());
                detail.setDetailid(clickedItem.getDetailid());
                detail.setInwardid(clickedItem.getInwardid());
                detail.setModelid(clickedItem.getModelid());
                detail.setMaterialcode(clickedItem.getMaterialcode());
                detail.setMaterialname(clickedItem.getMaterialname());
                detail.setCci(clickedItem.isCci());
                detail.setRfid(clickedItem.getRfid());
                detail.setRackcode(clickedItem.getRackcode());
                detail.setRacktype(clickedItem.getRacktype());
                detail.setMaterialid(selectedMaterialId[0]);
                detail.setRackid(selectedRackId[0]);
                detail.setQuantity(Integer.parseInt(quantityStr));
                detail.setStatus(1);
                detail.setCreateby(userIdStr);
                detail.setModifiedby(userIdStr);
                detail.setCreatedon(Utility.getCurrentDateTimeAsLocalDateTime());
                detail.setModifiedon(Utility.getCurrentDateTimeAsLocalDateTime());
                detail.setStockindate(Utility.getCurrentDateTimeAsLocalDateTime());
                ReturnPartsInsertUpdateRequest insertRequest = new ReturnPartsInsertUpdateRequest();
                insertRequest.setReturnid(0);
                insertRequest.setReturncode("");
                insertRequest.setCreatedon(Utility.getCurrentDateTimeAsLocalDateTime());
                insertRequest.setReturndate(Utility.getCurrentDateTimeAsLocalDateTime());
                insertRequest.setTotalquantity(detail.getQuantity());
                insertRequest.setReturnedby(userIdStr);
                insertRequest.setStatus(1);
                insertRequest.setCreatedby(userIdStr);
                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put("IbaIvmInvStockreturnHdr", insertRequest);
                requestMap.put("IbaIvmInvStockreturnDetail", Collections.singletonList(detail));
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")))).create();
                reAllocateViewModel.insertAndUpdateReturnStack(requestMap, new ApiResponseListener() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("Reallocate", "Insert Success: " + new Gson().toJson(response));
                        adapter.clearItems();
                    }

                    @Override
                    public void onFailed(Object data) {
                        Log.e("ReAllocateInsert", "Insert Failed: " + new Gson().toJson(data));
                        Toast.makeText(getContext(), "Insert Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(requireContext(), "Please enter quantity", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", null).show();
    }

    private List<String> getMaterialNames(List<ReturnDefectiveTypeResponse.MaterialDetail> list) {
        List<String> names = new ArrayList<>();
        if (list != null) {
            for (ReturnDefectiveTypeResponse.MaterialDetail item : list) {
                names.add(item.getMaterialName());
            }
        }
        return names;
    }

    private List<String> getRackCodes(List<ReturnDefectiveTypeResponse.RackDetail> list) {
        List<String> codes = new ArrayList<>();
        if (list != null) {
            for (ReturnDefectiveTypeResponse.RackDetail item : list) {
                codes.add(item.getRackCode());
            }
        }
        return codes;
    }


    private void setOnClickListener() {
        binding.scanButton.setOnClickListener(view -> {
            if (scannedRfids == null || scannedRfids.isEmpty()) {
                Toast.makeText(getContext(), "No RFID data to scan.", Toast.LENGTH_SHORT).show();
                return;
            }
            ReturnPartsValidInfoRequest request = new ReturnPartsValidInfoRequest(scannedRfids);
            reAllocateViewModel.checkValidPartsByRFIDForReallocate(request, new ApiResponseListener() {
                @Override
                public void onSuccess(Object data) {
                    if (data instanceof ReturnPartsValidInfoResponse) {
                        ReturnPartsValidInfoResponse response = (ReturnPartsValidInfoResponse) data;
                        if (response.isResult() && response.getData().getValid() != null) {
                            Log.d("TEST RES", "RESPONSE" + response.getData().getValid());
                            validItems.clear();
                            validItems.addAll(response.getData().getValid());
                            adapter.updateList(validItems);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getContext(), "No valid parts found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Unexpected response format", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailed(Object data) {
                    Toast.makeText(getContext(), "Failed to validate parts", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public void onSuccess(Object data) {
        setBarcodeRFIDScanResultListener(this);
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

    private void initMetaDetails() {
        if (reAllocateViewModel == null) {
            return;
        }
        reAllocateViewModel.metaDetailsForDefectiveDropdown(this);
    }

    private void setObserver() {
        if (getActivity() != null) {
            reAllocateViewModel.isDataLoading.observe(getViewLifecycleOwner(), isLoading -> {
            });
        }
        reAllocateViewModel.materialDetailsDDListLiveData.observe(getViewLifecycleOwner(), materials -> {
            List<ReturnDefectiveTypeResponse.RackDetail> racks = reAllocateViewModel.rackDetailsDDListLiveData.getValue();
            if (materials != null && racks != null && clickedItemTemp != null) {
                showDefectiveDialog(clickedItemTemp, materials, racks);
                clickedItemTemp = null;
            }
        });

        reAllocateViewModel.rackDetailsDDListLiveData.observe(getViewLifecycleOwner(), racks -> {
            List<ReturnDefectiveTypeResponse.MaterialDetail> materials = reAllocateViewModel.materialDetailsDDListLiveData.getValue();
            if (materials != null && racks != null && clickedItemTemp != null) {
                showDefectiveDialog(clickedItemTemp, materials, racks);
                clickedItemTemp = null;
            }
        });
        putAwayViewModel.getQuantity().observe(getViewLifecycleOwner(), quantity -> {
            Log.d("Quantity", "Observed quantity: " + quantity);
        });
    }
}
