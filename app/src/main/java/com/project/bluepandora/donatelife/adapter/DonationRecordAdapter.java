package com.project.bluepandora.donatelife.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.data.DRItem;
import com.widget.CustomTextView;

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


    List<DRItem> items;
    private Activity activity;
    private LayoutInflater inflater;

    public DonationRecordAdapter(Activity activity, List<DRItem> items) {
        this.activity = activity;
        this.items = items;
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
        final int pos = position;
        final ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, parent,
                    false);
            holder = new ViewHolder();
            setholder(convertView, holder);
            convertView.setTag(holder);
        } else if (((ViewHolder) convertView.getTag()).needInflate) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_item, parent,
                    false);
            setholder(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position >= 0 && position <= 1) {
            convertView.setLayoutParams(new GridView.LayoutParams(
                    250, activity.getResources().getDimensionPixelOffset(R.dimen.header_height)));
            holder.gridBackground.setBackgroundResource(android.R.color.transparent);
            convertView.setVisibility(View.INVISIBLE);
        } else if (position + 1 == getCount()) {
            convertView.setLayoutParams(new GridView.LayoutParams(
                    activity.getResources().getDisplayMetrics().widthPixels / 2,
                    activity.getResources().getDisplayMetrics().widthPixels / 2));
            convertView.setVisibility(View.VISIBLE);
            holder.gridBackground.setBackgroundResource(R.drawable.new_record);
            holder.bloodDummyImageHolder.setVisibility(View.GONE);
            holder.addDummyImageHolder.setVisibility(View.VISIBLE);
            holder.recordHolder.setVisibility(View.GONE);
            holder.optionButton.setVisibility(View.GONE);
        } else {
            convertView.setLayoutParams(new GridView.LayoutParams(
                    activity.getResources().getDisplayMetrics().widthPixels / 2,
                    activity.getResources().getDisplayMetrics().widthPixels / 2));
            convertView.setVisibility(View.VISIBLE);
            holder.addDummyImageHolder.setVisibility(View.GONE);
            holder.bloodDummyImageHolder.setVisibility(View.VISIBLE);
            holder.gridBackground.setBackgroundResource(R.drawable.red_panel);
            holder.recordHolder.setVisibility(View.VISIBLE);
            holder.optionButton.setVisibility(View.VISIBLE);
        }
        DRItem item = items.get(position);
        holder.donationDate.setText(item.getDonationTime());
        return convertView;
    }

    private void setholder(View convertView, ViewHolder holder) {
        holder.donationDate = (CustomTextView) convertView.findViewById(R.id.donation_date);
        holder.gridBackground = (LinearLayout) convertView.findViewById(R.id.grid_background);
        holder.recordHolder = (LinearLayout) convertView.findViewById(R.id.record_holder);
        holder.bloodDummyImageHolder = (RelativeLayout) convertView.findViewById(R.id.blood_dummy_image);
        holder.addDummyImageHolder = (RelativeLayout) convertView.findViewById(R.id.add_dummy_image);
        holder.optionButton = (ImageButton) convertView.findViewById(R.id.option_button);
        holder.needInflate = false;
    }
    static class ViewHolder {
        public boolean needInflate;
        CustomTextView donationDate;
        LinearLayout gridBackground;
        LinearLayout recordHolder;
        RelativeLayout bloodDummyImageHolder;
        RelativeLayout addDummyImageHolder;
        ImageButton optionButton;
    }
}
