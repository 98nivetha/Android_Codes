package com.zebra.demo.view.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zebra.demo.R;
import com.zebra.demo.data.remote.model.AssetInwardAutoResponse;
import com.zebra.demo.databinding.ListItemSubManualAssetInwardBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.listener.RecyclerAssetInwardViewItemClickListener;

import java.util.List;

/*AssetInwardManualListAdapter*/

public class AssetInwardManualSubListAdapter extends RecyclerView.Adapter<AssetInwardManualSubListAdapter.ViewHolder> {
    List<AssetInwardAutoResponse> assetInwardAutoResponseList;

    RecyclerAssetInwardViewItemClickListener recyclerAssetInwardViewItemClickListener;
    public AssetInwardManualSubListAdapter(List<AssetInwardAutoResponse> assetInwardAutoResponseList, RecyclerAssetInwardViewItemClickListener recyclerAssetInwardViewItemClickListener) {
        this.assetInwardAutoResponseList = assetInwardAutoResponseList;
        this.recyclerAssetInwardViewItemClickListener = recyclerAssetInwardViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemSubManualAssetInwardBinding listItemManualAssetInwardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_sub_manual_asset_inward, parent, false);
        return new ViewHolder(listItemManualAssetInwardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssetInwardAutoResponse assetInward = assetInwardAutoResponseList.get(position);

        holder.listItemSubManualAssetInwardBinding.valAssetNameTxt.setText(Utility.getCommonValue(assetInward.AssetName));
        holder.listItemSubManualAssetInwardBinding.valAssetCodeTxt.setText(Utility.getCommonValue(assetInward.Assetcode));
        holder.listItemSubManualAssetInwardBinding.valAssetSerialNoTxt.setText(Utility.getCommonValue(assetInward.AssetSrNo));
        holder.listItemSubManualAssetInwardBinding.valVerifyStatusTxt.setText(assetInward.VerifyStatus  == 0 ? "Not Ok" : "Ok");

        holder.listItemSubManualAssetInwardBinding.btnNotOk.setOnClickListener(view -> {
            if(recyclerAssetInwardViewItemClickListener != null) {
                recyclerAssetInwardViewItemClickListener.onRecyclerViewItemNotOkClickListener(position, assetInwardAutoResponseList.get(position));
            }
        });

        holder.listItemSubManualAssetInwardBinding.btnOk.setOnClickListener(view -> {
            if(recyclerAssetInwardViewItemClickListener != null) {
                recyclerAssetInwardViewItemClickListener.onRecyclerViewItemOkClickListener(position, assetInwardAutoResponseList.get(position));
            }
        });

        holder.listItemSubManualAssetInwardBinding.ouerLayerRlay.setOnClickListener(view -> {
            if(recyclerAssetInwardViewItemClickListener != null) {
                //recyclerViewItemClickListener.onRecyclerViewItemClickListener(position, assetInwardAutoResponseList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return assetInwardAutoResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ListItemSubManualAssetInwardBinding listItemSubManualAssetInwardBinding;

        public ViewHolder(ListItemSubManualAssetInwardBinding listItemSubManualAssetInwardBinding) {
            super(listItemSubManualAssetInwardBinding.getRoot());
            this.listItemSubManualAssetInwardBinding = listItemSubManualAssetInwardBinding;
        }
    }
}
