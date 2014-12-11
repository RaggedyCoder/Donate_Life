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

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.SettingsActivity;
import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.HospitalItem;
import com.project.bluepandora.donatelife.data.Item;
import com.widget.CustomTextView;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter {

    private ArrayList<Item> items;
    private LayoutInflater inflater;
    private Activity activity;
    private boolean banglaFilter;


    public SpinnerAdapter(Activity activity, ArrayList<Item> items) {
        this.items = items;
        this.activity = activity;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        banglaFilter = PreferenceManager.getDefaultSharedPreferences(
                activity).getBoolean(SettingsActivity.LANGUAGE_TAG, false);

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
        Item item = items.get(position);
        if (item instanceof DistrictItem) {
            return ((DistrictItem) item).getDistId();
        } else if (item instanceof HospitalItem) {
            return ((HospitalItem) item).getHospitalId();
        } else if (item instanceof BloodItem) {
            return ((BloodItem) item).getBloodId();
        } else {
            return -1;
        }
    }

    /**
     * Get the row position associated with the specified id in the list.
     *
     * @param id The id of the item within the adapter's data set whose item position we want.
     * @return The position of the item at the specified position.-1 will be returned the the
     * position not found.
     */
    public int getItemPosition(int id) {
        for (int position = 0; position < items.size(); position++) {
            Item item = items.get(position);
            if (item instanceof DistrictItem) {
                if (((DistrictItem) item).getDistId() == id) {
                    return position;
                }
            } else if (item instanceof BloodItem) {
                if (((BloodItem) item).getBloodId() == id) {
                    return position;
                }
            } else if (item instanceof HospitalItem) {
                if (((HospitalItem) item).getHospitalId() == id) {
                    return position;
                }
            }
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
            holder = new ViewHolder();
            holder.spinnerItemName = (CustomTextView) convertView.findViewById(R.id.spinner_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Item item = items.get(position);
        if (banglaFilter) {
            if (item instanceof DistrictItem) {
                holder.spinnerItemName.setText(((DistrictItem) item).getBanglaDistName());
            } else if (item instanceof HospitalItem) {
                holder.spinnerItemName.setText(((HospitalItem) item).getBanglaHospitalName());
            } else if (item instanceof BloodItem) {
                holder.spinnerItemName.setText(((BloodItem) item).getBanglaBloodName());
            }
        } else {
            if (item instanceof DistrictItem) {
                holder.spinnerItemName.setText(((DistrictItem) item).getDistName());
            } else if (item instanceof HospitalItem) {
                holder.spinnerItemName.setText(((HospitalItem) item).getHospitalName());
            } else if (item instanceof BloodItem) {
                holder.spinnerItemName.setText(((BloodItem) item).getBloodName());
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        CustomTextView spinnerItemName;
    }
}
