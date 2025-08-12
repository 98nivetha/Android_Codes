package com.zebra.demo.view.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zebra.demo.R;
import com.zebra.demo.data.remote.responsemodels.MaterialDetail;
import java.util.ArrayList;
import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    private List<MaterialDetail> materialList = new ArrayList<>();

    public void setData(List<MaterialDetail> materials) {
        this.materialList = materials;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MaterialDetail material = materialList.get(position);
        String name = material.getMaterialname();
        if (name != null && !name.isEmpty()) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }
        holder.txtMaterialName.setText(name);
        holder.txtMaterialCode.setText("Code: " + material.getMaterialcode());
        holder.txtQuantity.setText("Qty: " + material.getQuantity());
        holder.txtPackingStandard.setText("Pack Std: " + material.getPackingstandard());
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaterialName, txtMaterialCode, txtQuantity, txtPackingStandard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaterialName = itemView.findViewById(R.id.txtMaterialName);
            txtMaterialCode = itemView.findViewById(R.id.txtMaterialCode);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPackingStandard = itemView.findViewById(R.id.txtPackingStandard);
        }
    }
}

