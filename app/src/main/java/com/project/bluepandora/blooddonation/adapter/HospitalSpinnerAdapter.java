package com.project.bluepandora.blooddonation.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.project.bluepandora.blooddonation.data.HospitalItem;
import com.project.bluepandora.donatelife.R;
import com.widget.CustomTextView;

import java.util.ArrayList;

@SuppressLint("InflateParams")
public class HospitalSpinnerAdapter extends BaseAdapter {

    // private Activity activity;
    private ArrayList<HospitalItem> items;
    private LayoutInflater inflater;

    public HospitalSpinnerAdapter(Activity activity,
                                  ArrayList<HospitalItem> items) {

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

        HospitalItem hitem = (HospitalItem) items.get(position);
        return hitem.getHospitalId();
    }

    public int getId(int position) {

        HospitalItem hitem = (HospitalItem) items.get(position);
        return hitem.getHospitalId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item, null);
        }
        CustomTextView textView = (CustomTextView) convertView
                .findViewById(R.id.spinner_item_name);

        HospitalItem hitem = items.get(position);
        textView.setText(hitem.getHospitalName());

        textView.setSelected(true);
        return convertView;
    }

}
