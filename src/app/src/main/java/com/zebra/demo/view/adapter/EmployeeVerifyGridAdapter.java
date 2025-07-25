package com.zebra.demo.view.adapter;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zebra.demo.R;
import java.util.List;

public  class EmployeeVerifyGridAdapter extends BaseAdapter {
    private final List<String> items;
    private final Activity activity;

   public EmployeeVerifyGridAdapter(List<String> items, Activity activity) {
        this.items = items;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = (TextView) activity.getLayoutInflater().inflate(R.layout.employee_verify_grid_item, parent, false);
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(items.get(position));
        return textView;
    }
}