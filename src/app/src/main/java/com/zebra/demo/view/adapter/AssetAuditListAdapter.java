package com.zebra.demo.view.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.zebra.demo.R;
import com.zebra.demo.data.remote.model.AssetAudit;
import com.zebra.demo.databinding.ListItemAssetAuditBinding;
import com.zebra.demo.utility.Utility;
import com.zebra.demo.view.listener.RecyclerViewItemClickListener;
import java.util.List;

/*AssetAuditListAdapter*/

public class AssetAuditListAdapter extends RecyclerView.Adapter<AssetAuditListAdapter.ViewHolder> {
    List<AssetAudit> assetAuditList;

    RecyclerViewItemClickListener recyclerViewItemClickListener;
    public AssetAuditListAdapter(List<AssetAudit> assetAuditList, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.assetAuditList = assetAuditList;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemAssetAuditBinding listItemAssetAuditBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item_asset_audit, parent, false);
        return new ViewHolder(listItemAssetAuditBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssetAudit assetAudit = assetAuditList.get(position);

        holder.listItemAssetAuditBinding.valAuditIdTxt.setText(Utility.getCommonValue(assetAudit.Auditcode));
        holder.listItemAssetAuditBinding.valCompletionTxt.setText(Utility.getCommonValue(assetAudit.Auditcompletion));
        holder.listItemAssetAuditBinding.valDepartmentTxt.setText(Utility.getCommonValue(assetAudit.Buildingname));
        holder.listItemAssetAuditBinding.valFromDateTxt.setText(Utility.getAppDateFromServerDateOnly(assetAudit.Auditconductfrom));
        holder.listItemAssetAuditBinding.valToDateTxt.setText(Utility.getAppDateFromServerDateOnly(assetAudit.Auditconductto));

        holder.listItemAssetAuditBinding.ouerLayerRlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onRecyclerViewItemClickListener(position, assetAuditList.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return assetAuditList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ListItemAssetAuditBinding listItemAssetAuditBinding;

        public ViewHolder(ListItemAssetAuditBinding listItemAssetAuditBinding) {
            super(listItemAssetAuditBinding.getRoot());
            this.listItemAssetAuditBinding = listItemAssetAuditBinding;
        }
    }
}
