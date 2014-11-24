package com.project.bluepandora.donatelife.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.project.bluepandora.donatelife.R;

import java.util.List;

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

        if (position >= 0 && position <= 1) {
            convertView = new View(activity);
            convertView.setLayoutParams(new GridView.LayoutParams(
                    250, activity.getResources().getDimensionPixelOffset(R.dimen.header_height)));

        } else if (position + 1 == getCount()) {

            convertView = inflater.inflate(R.layout.grid_item_add, null);
            convertView.setLayoutParams(new GridView.LayoutParams(
                    activity.getResources().getDisplayMetrics().widthPixels / 2,
                    activity.getResources().getDisplayMetrics().widthPixels / 2));
        } else {
            convertView = inflater.inflate(R.layout.grid_item, null);
            convertView.setLayoutParams(new GridView.LayoutParams(
                    activity.getResources().getDisplayMetrics().widthPixels / 2,
                    activity.getResources().getDisplayMetrics().widthPixels / 2));
        }

        return convertView;
    }

    static class ViewHolder {

    }
}
