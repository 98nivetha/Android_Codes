package com.zebra.demo.view.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zebra.demo.view.listener.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GenericAdapter<T> extends RecyclerView.Adapter<GenericAdapter<T>.ViewHolder> {

    private List<T> itemList;
    private int layoutId;
    private BiConsumer<T, View> bindFunction;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public GenericAdapter(List<T> itemList, int layoutId, BiConsumer<T, View> bindFunction) {
        this.itemList = itemList;
        this.layoutId = layoutId;
        this.bindFunction = bindFunction;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T item = itemList.get(position);
        bindFunction.accept(item, holder.itemView);
        holder.itemView.setOnClickListener(v -> {
            if (recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener.onRecyclerViewItemClickListener(position, item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener listener) {
        this.recyclerViewItemClickListener = listener;

    }

    public void updateList(List<T> newList) {
        if (itemList == null) {
            itemList = new ArrayList<>();
        } else {
            itemList.clear();
        }
        itemList.addAll(newList);
        notifyDataSetChanged();
    }

}


