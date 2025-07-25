package com.zebra.demo.view.adapter;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zebra.demo.data.remote.model.EmployeeDetail;
import java.util.ArrayList;
import java.util.List;

public class EmployeeAutoCompleteTextViewAdapter extends ArrayAdapter<EmployeeDetail> implements Filterable {
    private final List<EmployeeDetail> originalList;
    private List<EmployeeDetail> filteredList;

    public EmployeeAutoCompleteTextViewAdapter(@NonNull Context context, int resource, @NonNull List<EmployeeDetail> items) {
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
    public EmployeeDetail getItem(int position) {
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
                    List<EmployeeDetail> filtered = new ArrayList<>();
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (EmployeeDetail item : originalList) {
                        if (item.getEmployeeName() != null &&  item.getEmployeeCode() != null && (item.getEmployeeName().toLowerCase().contains(filterPattern) || item.getEmployeeCode().toLowerCase().contains(filterPattern))) {
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
                filteredList = (List<EmployeeDetail>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}