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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.FeedItem;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.project.bluepandora.util.ListViewAnimator;
import com.widget.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

@SuppressLint("NewApi")
public class FeedListAdapter extends BaseAdapter {

    public static class ViewHolder {
        public boolean needInflate;
        CustomTextView timestamp;
        CustomTextView emergency;
        CustomTextView bloodGroup;
        CustomTextView bloodAmount;
        CustomTextView hospital;
        CustomTextView contact;
        CustomTextView area;
        ImageButton button1;
        PopupMenu popupMenu;
    }

    private Activity activity;
    private LayoutInflater inflater;
    private List<Item> feedItems;
    private UserInfoItem userInfo;
    private ProgressDialog dialog;

    public FeedListAdapter(Activity activity, List<Item> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
        UserDataSource database = new UserDataSource(activity);
        database.open();
        userInfo = database.getAllUserItem().get(0);
        database.close();
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SimpleDateFormat"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final ViewHolder holder;
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.card_feed_view, parent,
                    false);
            holder = new ViewHolder();
            // setting the timestamp
            setholder(convertView, holder);
            convertView.setTag(holder);
        } else if (((ViewHolder) convertView.getTag()).needInflate) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.card_feed_view, parent,
                    false);
            setholder(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final FeedItem item = (FeedItem) feedItems.get(position);
        // Converting timestamp into x ago format
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getDefault());
        Date date = null;
        try {
            date = isoFormat.parse(item.getTimeStamp());
        } catch (ParseException e) {

            e.printStackTrace();
        }
        Date dateNow = new Date();
        String string = isoFormat.format(dateNow);
        try {
            dateNow = isoFormat.parse(string);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                date.getTime(), dateNow.getTime(), DateUtils.SECOND_IN_MILLIS);
        holder.timestamp.setText(timeAgo);

        if (!TextUtils.isEmpty(item.getEmergency())) {
            holder.emergency.setText(R.string.emergency_text);

        } else {
            holder.emergency.setText(R.string.non_emergency_text);
        }
        final View view = convertView;
        final FeedItem list = item;
        holder.button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupMenu(item.getContact(), holder);
            }

        });
        holder.popupMenu
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.action_delete_own) {
                            deleteRequest(view, pos, list);
                            dialog = new ProgressDialog(activity);
                            dialog.setTitle(R.string.delete_request);
                            dialog.setMessage(activity.getResources()
                                    .getString(R.string.progressing));
                            dialog.setCancelable(false);
                            dialog.show();
                            return true;
                        }
                        if (item.getItemId() == R.id.action_call) {
                            Intent call = new Intent(Intent.ACTION_DIAL, Uri
                                    .parse("tel:" + list.getContact()));
                            activity.startActivity(call);
                        } else if (item.getItemId() == R.id.action_sms) {
                            Intent sendIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("sms:" + list.getContact()));
                            sendIntent.putExtra("sms_body",
                                    "I'm available for giving blood.");
                            activity.startActivity(sendIntent);
                        } else if (item.getItemId() == R.id.action_delete) {
                            ListViewAnimator.deleteCell(view, pos, feedItems,
                                    FeedListAdapter.this, activity);
                        }
                        return false;
                    }
                });
        holder.bloodGroup.setText(item.getBloodGroup());
        String blood = activity.getResources().getQuantityString(
                R.plurals.bags, item.getBloodAmount(), item.getBloodAmount());
        holder.bloodAmount.setText(blood);
        holder.hospital.setSelected(true);
        holder.hospital.setText(item.getHospital());
        holder.area.setText(item.getArea());
        holder.contact.setText(item.getContact());
        holder.contact.setSelected(true);
        return convertView;
    }

    private void setholder(View convertView, ViewHolder holder) {
        holder.timestamp = (CustomTextView) convertView
                .findViewById(R.id.timeStamp_text_view);
        holder.emergency = (CustomTextView) convertView
                .findViewById(R.id.emergency_text_view);
        holder.button1 = (ImageButton) convertView
                .findViewById(R.id.option_button_1);
        holder.bloodGroup = (CustomTextView) convertView
                .findViewById(R.id.bloodGroupID_text_view);
        holder.bloodAmount = (CustomTextView) convertView
                .findViewById(R.id.bloodAmountID_text_view);
        holder.hospital = (CustomTextView) convertView
                .findViewById(R.id.hospitalNameID_text_view);
        holder.area = (CustomTextView) convertView
                .findViewById(R.id.areaNameID_text_view);
        holder.contact = (CustomTextView) convertView
                .findViewById(R.id.mobileNumberID_text_view);
        holder.popupMenu = new PopupMenu(activity, holder.button1);
        holder.needInflate = false;
    }

    private void createPopupMenu(String contact, ViewHolder holder) {
        if (contact.equals(userInfo.getMobileNumber())) {
            holder.popupMenu.getMenu().clear();
            holder.popupMenu.getMenuInflater().inflate(
                    R.menu.feed_own_item_delete, holder.popupMenu.getMenu());
            holder.popupMenu.show();

        } else {
            holder.popupMenu.getMenu().clear();
            holder.popupMenu.getMenuInflater().inflate(R.menu.feed_item_delete,
                    holder.popupMenu.getMenu());
            holder.popupMenu
                    .getMenu()
                    .getItem(0)
                    .setTitle(
                            holder.popupMenu.getMenu().getItem(0).getTitle()
                                    + " " + contact);
            holder.popupMenu
                    .getMenu()
                    .getItem(1)
                    .setTitle(
                            holder.popupMenu.getMenu().getItem(1).getTitle()
                                    + " " + contact);
            holder.popupMenu.show();
        }
    }

    private void deleteRequest(final View view, final int pos, FeedItem item) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(URL.REQUEST_NAME, URL.BLOODREQUEST_DELETE_PARAM);
        params.put(URL.MOBILE_TAG, item.getContact());
        params.put(URL.REQUESTTIME_TAG, item.getTimeStamp());

        CustomRequest jsonReq = new CustomRequest(Method.POST, URL.URL, params,
                new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int i = 0;
                            i = response.getInt("done");
                            if (i == 1) {
                                ListViewAnimator.deleteCell(view, pos,
                                        feedItems, FeedListAdapter.this,
                                        activity);
                                Toast.makeText(activity,
                                        "Your own request has been deleted.",
                                        Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(activity,
                                        "Something went wrong.",
                                        Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }
}
