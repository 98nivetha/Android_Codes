package com.zebra.demo.view.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.zebra.demo.R;
import com.zebra.demo.data.remote.model.AssetOutward;
import com.zebra.demo.databinding.ListItemAssetOutwardBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import java.util.List;

/*AssetOutwardListAdapter*/

public class AssetOutwardListAdapter extends RecyclerView.Adapter<AssetOutwardListAdapter.ViewHolder> {
    List<AssetOutward> assetOutwardList;

    RecyclerViewItemClickListener recyclerViewItemClickListener;
    public AssetOutwardListAdapter(List<AssetOutward> assetOutwardList, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.assetOutwardList = assetOutwardList;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemAssetOutwardBinding listItemAssetOutwardBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_asset_outward, parent, false);
        return new ViewHolder(listItemAssetOutwardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssetOutward assetOutward = assetOutwardList.get(position);

        holder.listItemAssetOutwardBinding.valAssetNameTxt.setText(Utility.getCommonValue(assetOutward.AssetName));
        holder.listItemAssetOutwardBinding.valAssetSerialNoTxt.setText(Utility.getCommonValue(assetOutward.AssetSrNo));
        holder.listItemAssetOutwardBinding.valVerifyStatusTxt.setText(assetOutward.VerifyStatus == 0 ? "Not Ok" : "Ok");

        holder.listItemAssetOutwardBinding.ouerLayerRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onRecyclerViewItemClickListener(position, assetOutwardList.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return assetOutwardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ListItemAssetOutwardBinding listItemAssetOutwardBinding;

        public ViewHolder(ListItemAssetOutwardBinding listItemAssetOutwardBinding) {
            super(listItemAssetOutwardBinding.getRoot());
            this.listItemAssetOutwardBinding = listItemAssetOutwardBinding;
        }
    }
}
