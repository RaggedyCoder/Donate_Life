package com.project.bluepandora.blooddonation.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.project.bluepandora.donatelife.R;

import java.util.List;

public class DonationRecordAdapter extends BaseAdapter {


    List<String> header;
    private Activity activity;
    private LayoutInflater inflater;

    public DonationRecordAdapter(Activity activity, List<String> header) {
        this.activity = activity;
        this.header = header;
    }

    @Override
    public int getCount() {
        return header.size();
    }


    @Override
    public Object getItem(int position) {
        return header.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        convertView = inflater.inflate(R.layout.grid_item, null);
        if (position >= 0 && position <= 1)
            convertView.setLayoutParams(new GridView.LayoutParams(250,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            310, activity.getResources().getDisplayMetrics())));
        else {
            convertView.setLayoutParams(new GridView.LayoutParams(200,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            310 / 2, activity.getResources().getDisplayMetrics())));
        }
        return convertView;
    }


    static class ViewHolder {

    }
}
