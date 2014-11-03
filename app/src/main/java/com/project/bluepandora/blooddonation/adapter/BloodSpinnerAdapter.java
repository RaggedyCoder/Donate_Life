package com.project.bluepandora.blooddonation.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.project.bluepandora.blooddonation.data.BloodItem;
import com.project.bluepandora.donatelife.R;
import com.widget.CustomTextView;

import java.util.ArrayList;

public class BloodSpinnerAdapter extends BaseAdapter {

    // private Activity activity;
    private ArrayList<BloodItem> items;
    private LayoutInflater inflater;
    BloodItem bitem;

    public BloodSpinnerAdapter(Activity activity, ArrayList<BloodItem> items) {

        this.items = items;
        // this.activity = activity;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

        BloodItem bitem = (BloodItem) items.get(position);
        return bitem.getBloodId();
    }

    public int getId(int position) {

        BloodItem bitem = (BloodItem) items.get(position);
        return bitem.getBloodId();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item, null);
        }
        CustomTextView textView = (CustomTextView) convertView
                .findViewById(R.id.spinner_item_name);
        BloodItem bitem = items.get(position);
        textView.setText(bitem.getBloodName());

        textView.setSelected(true);
        return convertView;
    }

}
