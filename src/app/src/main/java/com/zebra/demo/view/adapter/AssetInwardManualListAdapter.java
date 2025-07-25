package com.zebra.demo.view.adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.zebra.demo.R;
import com.zebra.demo.data.remote.model.AssetInward;
import com.zebra.demo.databinding.ListItemManualAssetInwardBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;

import java.util.List;

/*AssetInwardManualListAdapter*/

public class AssetInwardManualListAdapter extends RecyclerView.Adapter<AssetInwardManualListAdapter.ViewHolder> {
    List<AssetInward> assetInwardList;

    RecyclerViewItemClickListener recyclerViewItemClickListener;
    public AssetInwardManualListAdapter(List<AssetInward> assetInwardList, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.assetInwardList = assetInwardList;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemManualAssetInwardBinding listItemManualAssetInwardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_manual_asset_inward, parent, false);
        return new ViewHolder(listItemManualAssetInwardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssetInward assetInward = assetInwardList.get(position);

        holder.listItemManualAssetInwardBinding.valGatePassNoTxt.setText(Utility.getCommonValue(assetInward.GatePass));
        holder.listItemManualAssetInwardBinding.valVendorNameTxt.setText(Utility.getCommonValue(assetInward.Vendor));
        holder.listItemManualAssetInwardBinding.valOutDateTxt.setText(Utility.getAppDateFromServerDate(assetInward.Assetout_on));

        holder.listItemManualAssetInwardBinding.ouerLayerRlay.setOnClickListener(view -> {
            if(recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener.onRecyclerViewItemClickListener(position, assetInwardList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return assetInwardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ListItemManualAssetInwardBinding listItemManualAssetInwardBinding;

        public ViewHolder(ListItemManualAssetInwardBinding listItemManualAssetInwardBinding) {
            super(listItemManualAssetInwardBinding.getRoot());
            this.listItemManualAssetInwardBinding = listItemManualAssetInwardBinding;
        }
    }
}
