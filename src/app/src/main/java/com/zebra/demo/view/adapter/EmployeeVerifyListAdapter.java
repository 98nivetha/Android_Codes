package com.zebra.demo.view.adapter;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zebra.demo.R;
import com.zebra.demo.data.remote.model.AssetOutward;
import com.zebra.demo.data.remote.model.ScanAssetDetail;
import com.zebra.demo.databinding.ListItemAssetOutwardBinding;
import com.zebra.demo.databinding.ListItemEmpVerifyRemarkBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;

import java.util.List;

/*AssetOutwardListAdapter*/

public class EmployeeVerifyListAdapter extends RecyclerView.Adapter<EmployeeVerifyListAdapter.ViewHolder> {
    List<ScanAssetDetail> scanAssetDetailList;
    //boolean flag = false;


    RecyclerViewItemClickListener recyclerViewItemClickListener;
    public EmployeeVerifyListAdapter(List<ScanAssetDetail> scanAssetDetailList, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.scanAssetDetailList = scanAssetDetailList;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemEmpVerifyRemarkBinding listItemEmpVerifyRemarkBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_emp_verify_remark, parent, false);
        return new ViewHolder(listItemEmpVerifyRemarkBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScanAssetDetail scanAssetDetail = scanAssetDetailList.get(position);

        holder.listItemEmpVerifyRemarkBinding.valAssetNameTxt.setText(Utility.getCommonValue(scanAssetDetail.getAssetName()));
        holder.listItemEmpVerifyRemarkBinding.valAssetCodeTxt.setText(Utility.getCommonValue(scanAssetDetail.getAssetCode()));

        holder.listItemEmpVerifyRemarkBinding.valPlaceofAssetNameTxt.setText(Utility.getCommonValue(scanAssetDetail.getPLACEOF_ASSET_NAME()));
        holder.listItemEmpVerifyRemarkBinding.valPlaceofAssetTxt.setText(Utility.getCommonValue(scanAssetDetail.getPLACEOF_ASSET()));

        holder.listItemEmpVerifyRemarkBinding.valScanIdTxt.setText(Utility.getCommonValue(scanAssetDetail.getScanid()));

        holder.listItemEmpVerifyRemarkBinding.imgStatus.setVisibility(View.GONE);

        if(scanAssetDetail.getEMP_AST_STATUS() == 1) {
            holder.listItemEmpVerifyRemarkBinding.imgStatus.setVisibility(View.VISIBLE);
            holder.listItemEmpVerifyRemarkBinding.imgStatus.setImageResource(R.drawable.ic_right);
        } else if(scanAssetDetail.getEMP_AST_STATUS() == 2) {
            holder.listItemEmpVerifyRemarkBinding.imgStatus.setVisibility(View.VISIBLE);
            holder.listItemEmpVerifyRemarkBinding.imgStatus.setImageResource(R.drawable.ic_wrong);
        }



        holder.listItemEmpVerifyRemarkBinding.ectlRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                scanAssetDetail.setRemarks(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*if(flag && (scanAssetDetail.getRemarks() == null || scanAssetDetail.getRemarks().isEmpty())) {
            holder.listItemEmpVerifyRemarkBinding.ectlRemarks.setError("Remark cannot be empty");
        } else {
            holder.listItemEmpVerifyRemarkBinding.ectlRemarks.setError(null);
        }*/

        /*holder.listItemEmpVerifyRemarkBinding.ouerLayerRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onRecyclerViewItemClickListener(position, scanAssetDetailList.get(position));
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return scanAssetDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ListItemEmpVerifyRemarkBinding listItemEmpVerifyRemarkBinding;

        public ViewHolder(ListItemEmpVerifyRemarkBinding listItemEmpVerifyRemarkBinding) {
            super(listItemEmpVerifyRemarkBinding.getRoot());
            this.listItemEmpVerifyRemarkBinding = listItemEmpVerifyRemarkBinding;
        }
    }

    public List<ScanAssetDetail> getScanAssetDetailList() {
        return scanAssetDetailList;
    }

    /*public boolean isRemarkEmpty() {

        flag = false;

        if(scanAssetDetailList != null && !scanAssetDetailList.isEmpty()) {

            for (int i = 0; i < scanAssetDetailList.size(); i++) {
                if(scanAssetDetailList.get(i).getRemarks() == null || scanAssetDetailList.get(i).getRemarks().isEmpty()) {
                    flag = true;
                    break;
                }
            }

        }

        notifyDataSetChanged();

        return flag;
    }*/
}
