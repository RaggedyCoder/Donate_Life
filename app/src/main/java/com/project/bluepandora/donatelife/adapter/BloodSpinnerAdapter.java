package com.project.bluepandora.donatelife.adapter;
/*
 * Copyright (C) 2014 The Blue Pandora Project Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.data.BloodItem;
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
