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
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.widget.CustomTextView;

import java.util.List;

public class ProfileDetailsAdapter extends BaseAdapter {

    UserInfoItem infoItem;
    List<String> header;
    private DistrictItem distItem;
    private BloodItem bloodItem;
    private Activity activity;
    private LayoutInflater inflater;
    private BloodDataSource bloodDatabase;
    private DistrictDataSource districtDatabase;
    private Resources resources;

    public ProfileDetailsAdapter(Activity activity, UserInfoItem infoItem, List<String> header) {
        this.activity = activity;
        this.infoItem = infoItem;
        this.header = header;
        bloodDatabase = new BloodDataSource(activity);
        bloodDatabase.open();
        districtDatabase = new DistrictDataSource(activity);
        districtDatabase.open();
        bloodItem = new BloodItem();
        bloodItem.setBloodId(infoItem.getGroupId());
        bloodItem = bloodDatabase.cursorToBloodItem(bloodDatabase
                .bloodItemToCursor(bloodItem));
        distItem = new DistrictItem();
        distItem.setDistId(infoItem.getDistId());
        distItem = districtDatabase.cursorToDistrictItem(districtDatabase
                .districtItemToCursor(distItem));
        bloodDatabase.close();
        districtDatabase.close();
        resources = activity.getResources();
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.profile_details, null);
            holder = new ViewHolder();
            holder.profileHeaderHolder = (RelativeLayout) convertView.findViewById(R.id.profile_header_holder);
            holder.profileEdit = (ImageButton) convertView.findViewById(R.id.profile_edit);
            holder.detailsHeader = (CustomTextView) convertView.findViewById(R.id.details_header);
            holder.detailsBody = (CustomTextView) convertView.findViewById(R.id.details_body);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position != 0) {
            holder.profileHeaderHolder.setVisibility(View.GONE);
        } else {
            holder.profileHeaderHolder.setVisibility(View.VISIBLE);
            holder.profileEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(activity, "pressed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        holder.detailsHeader.setText(header.get(position));
        holder.detailsBody.setText(getDescription(position));
        return convertView;
    }

    private String getDescription(int position) {
        if (position == 0) {
            return infoItem.getFirstName() + " " + infoItem.getLastName();
        } else if (position == 1) {
            return infoItem.getMobileNumber();
        } else if (position == 2) {
            return distItem.getDistName();
        } else if (position == 3) {
            return bloodItem.getBloodName();
        } else if (position == 4) {
            String totalDonation = resources.getQuantityString(R.plurals.donation,
                    Integer.parseInt(infoItem.getTotalDonation()), Integer.parseInt(infoItem.getTotalDonation()));
            if (totalDonation.equals(resources.getString(R.string.zero_grammar_correct))) {
                totalDonation = resources.getString(R.string.zero_grammar_result);
            }
            return totalDonation;
        }
        return null;
    }

    static class ViewHolder {
        RelativeLayout profileHeaderHolder;
        CustomTextView detailsHeader;
        ImageButton profileEdit;
        CustomTextView detailsBody;
    }
}
