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
import com.project.bluepandora.donatelife.data.AboutItem;
import com.widget.CustomTextView;

import java.util.List;


public class AboutAdapter extends BaseAdapter {

    private List<AboutItem> items;
    private Activity activity;
    private LayoutInflater inflater;

    public AboutAdapter(Activity activity, List<AboutItem> items) {
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
        final ViewHolder holder;
        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.about_list_item, null);
            holder = new ViewHolder();
            holder.header = (CustomTextView) convertView.findViewById(R.id.about_header);
            holder.body = (CustomTextView) convertView.findViewById(R.id.about_body);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AboutItem item = items.get(position);
        holder.header.setText(item.getHeader());
        holder.body.setText(item.getBody());
        return convertView;
    }

    private static class ViewHolder {
        CustomTextView header;
        CustomTextView body;
    }
}
