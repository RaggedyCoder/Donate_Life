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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.project.bluepandora.donatelife.R;
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

    public SpinnerAdapter(Activity activity, ArrayList<Item> items) {
        this.items = items;
        this.activity = activity;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
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

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.spinnerItemName = (CustomTextView) convertView.findViewById(R.id.spinner_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Item item = items.get(position);
        if (item instanceof DistrictItem) {
            String districtName = ((DistrictItem) item).getDistName();

            holder.spinnerItemName.setText(districtName);
        } else if (item instanceof HospitalItem) {
            holder.spinnerItemName.setText(((HospitalItem) item).getHospitalName());
        } else if (item instanceof BloodItem) {
            holder.spinnerItemName.setText(((BloodItem) item).getBloodName());
        }
        return convertView;
    }

    private static class ViewHolder {
        CustomTextView spinnerItemName;
    }
}
