package com.zebra.demo.view.adapter;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FilterAutoCompleteTextViewAdapter extends ArrayAdapter<Object> implements Filterable {
    private final List<Object> originalList;
    private List<Object> filteredList;

    public FilterAutoCompleteTextViewAdapter(@NonNull Context context, int resource, @NonNull List<Object> items) {
        super(context, resource, items);
        this.originalList = new ArrayList<>(items);
        this.filteredList = new ArrayList<>(items);
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = originalList;
                    results.count = originalList.size();
                } else {
                    List<Object> filtered = new ArrayList<>();
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Object item : originalList) {
                        if (item.toString().toLowerCase().contains(filterPattern)) {
                            filtered.add(item);
                        }
                    }

                    results.values = filtered;
                    results.count = filtered.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<Object>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}