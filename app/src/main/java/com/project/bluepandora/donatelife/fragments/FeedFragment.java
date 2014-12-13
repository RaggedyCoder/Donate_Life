package com.project.bluepandora.donatelife.fragments;

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
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.MainActivity;
import com.project.bluepandora.donatelife.activities.SettingsActivity;
import com.project.bluepandora.donatelife.adapter.FeedListAdapter;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.FeedItem;
import com.project.bluepandora.donatelife.data.HospitalItem;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.datasource.HospitalDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.helpers.ParamsBuilder;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.project.bluepandora.util.Utils;
import com.widget.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This fragment displays a list of Blood Feed stored in the server database.
 * Each item in the list shows the group name of the blood which they needed.
 * Along with the contact number who made the request,in which hospital
 * the blood is needed and also the amount.
 * <p/>
 * From this Fragment user can delete his own entry.And also the entry which he doesn't
 * wanted to see.
 */
public class FeedFragment extends Fragment implements URL {

    public static final String TAG = FeedFragment.class.getSimpleName();
    public static boolean firstTime = true;
    public static boolean slideChange = false;
    public JSONObject json;
    public SwipeRefreshLayout swipeRefreshLayout;
    public UserInfoItem userInfo;
    private ListView feedListView;
    private FeedListAdapter listAdapter;
    private List<Item> feedItems;
    private ImageButton actionButton;
    private Drawable mActionBarBackgroundDrawable;
    private ProgressBar progressBar;
    private CustomTextView mTitle;
    private View mCustomView;
    private OnRefreshListener mOnFeedRefresh;

    public FeedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedItems = new ArrayList<Item>();
        UserDataSource data = new UserDataSource(getActivity());
        data.open();
        userInfo = data.getAllUserItem().get(0);
        data.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feed, container,
                false);
        setHasOptionsMenu(true);
        feedListView = (ListView) rootView.findViewById(R.id.feed_list_view);
        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        actionButton = (ImageButton) rootView.findViewById(R.id.action_button);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.actionbar_background);
        mActionBarBackgroundDrawable.setAlpha(255);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mCustomView = mInflater.inflate(R.layout.request_feed_actionbar, container, false);
        mTitle = (CustomTextView) mCustomView
                .findViewById(R.id.actionbar_title_text);
        if (firstTime) {
            actionButton.setVisibility(View.GONE);
        } else {
            actionButton.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private void makejson(FileInputStream feed) throws IOException {

        String temp = "";
        int content;
        while ((content = feed.read()) != -1) {
            temp += (char) content;
        }
        try {
            Log.e(TAG, temp);
            JSONObject json = new JSONObject(temp);
            parseJsonFeed(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        feedListView.setAdapter(listAdapter);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.holo_blue_dark,
                R.color.holo_green_dark,
                R.color.holo_purple,
                R.color.holo_red_dark);
        swipeRefreshLayout.setOnRefreshListener(mOnFeedRefresh);

        mTitle.setText(R.string.blood_feed);
        customizeActionbar();
        jsonObjectRequest();
        final AnimationListener animListener = new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int[] origin = new int[2];
                actionButton.getLocationOnScreen(origin);
                int xDest = feedListView.getMeasuredWidth() / 2;
                xDest -= (actionButton.getMeasuredWidth() / 2);
                int yDest = feedListView.getMeasuredHeight() / 2;
                yDest -= (actionButton.getMeasuredHeight() / 2)
                        - getActivity().getResources().getDimensionPixelSize(
                        R.dimen.abc_action_bar_default_height_material);
                final int newLeft = actionButton.getLeft() + xDest - origin[0];
                final int newTop = actionButton.getTop() + yDest - origin[1];
                actionButton.layout(newLeft, newTop,
                        newLeft + actionButton.getMeasuredWidth(), newTop
                                + actionButton.getMeasuredHeight());
                actionButton.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), R.anim.full_grow));
                MainActivity act = (MainActivity) getActivity();
                act.switchContent();
                Log.e(TAG, actionButton.getLeft() + " " + actionButton.getTop());
            }
        };
        actionButton.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View arg0) {
                DisplayMetrics dm = new DisplayMetrics();
                // this.getWindowManager().getDefaultDisplay().getMetrics( dm );
                getActivity().getWindowManager().getDefaultDisplay()
                        .getMetrics(dm);
                int[] origin = new int[2];
                actionButton.getLocationOnScreen(origin);
                int xDest = feedListView.getMeasuredWidth() / 2;
                xDest -= (actionButton.getMeasuredWidth() / 2);
                int yDest = feedListView.getMeasuredHeight() / 2;
                yDest -= (actionButton.getMeasuredHeight() / 2)
                        - getActivity().getResources().getDimensionPixelSize(
                        R.dimen.abc_action_bar_default_height_material);
                TranslateAnimation anim = new TranslateAnimation(0, xDest
                        - origin[0], 0, yDest - origin[1]);
                anim.setFillEnabled(true);
                anim.setDuration(getActivity().getResources().getInteger(
                        android.R.integer.config_longAnimTime));
                anim.setInterpolator(getActivity(),
                        android.R.interpolator.decelerate_quad);
                anim.setAnimationListener(animListener);
                Log.e(TAG, actionButton.getX() + " " + actionButton.getY());
                actionButton.startAnimation(anim);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {

        mOnFeedRefresh = new OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressBar.setVisibility(View.VISIBLE);
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        };
        super.onAttach(activity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            progressBar.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);
            jsonObjectRequest();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 4000);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        MenuItem settings = menu.add(Menu.NONE, R.id.action_refresh, 100,
                R.string.refresh);
        settings.setIcon(R.drawable.ic_replay_white_18dp);
        if (Utils.hasHoneycomb()) {
            settings.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {

        super.onResume();
        if (actionButton.getVisibility() == View.VISIBLE && slideChange) {
            actionButton.setVisibility(View.GONE);
        }
        if (!firstTime && slideChange) {
            slideChange = false;
            Handler h = new Handler();
            h.postDelayed(new Runnable() {

                @Override
                public void run() {

                    Log.e(TAG, "again");
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.startAnimation(AnimationUtils.loadAnimation(
                            getActivity(), R.anim.grow));
                }
            }, 800);
        }
        jsonObjectRequest();
    }

    private void jsonObjectRequest() {

        HashMap<String, String> params = null;

        boolean distFilter = PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getBoolean(SettingsActivity.DISTRICT_FILTER_TAG, false);
        boolean groupFilter = PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getBoolean(SettingsActivity.GROUP_FILTER_TAG, false);
        String distId = Integer.toString(userInfo.getDistId());
        String groupId = Integer.toString(userInfo.getGroupId());
        if (distFilter && groupFilter) {
            params = ParamsBuilder.bloodRequestFeed(distId, groupId);
        } else if (distFilter) {
            params = ParamsBuilder.bloodRequestFeed(Integer.parseInt(distId));
        } else if (groupFilter) {
            params = ParamsBuilder.bloodRequestFeed(groupId);
        } else {
            params = ParamsBuilder.bloodRequestFeed();
        }

        CustomRequest jsonReq = new CustomRequest(Method.POST, URL, params,
                new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(TAG, "Response: " + response.toString());

                        writeToMemory(response);
                        try {
                            if (response.get("message").equals(
                                    "Not Found Any Blood Request")) {
                                Toast.makeText(getActivity(),
                                        "Not Found Any Blood Request",
                                        Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (json != null) {
                            JSONArray feedArray = null;

                            try {

                                feedArray = response
                                        .getJSONArray("bloodRequest");
                            } catch (JSONException e1) {

                                e1.printStackTrace();
                            }

                            if (feedArray != null && (listAdapter.getCount() == feedArray
                                    .length())) {
                                Toast.makeText(getActivity(),
                                        R.string.no_update,
                                        Toast.LENGTH_LONG).show();

                            } else {
                                parseJsonFeed(response);
                            }
                        } else {
                            json = response;
                            parseJsonFeed(response);
                        }

                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                readFromMemory();
            }
        });
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private void readFromMemory() {
        try {
            FileInputStream feed = getActivity().openFileInput("feed.json");
            makejson(feed);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void writeToMemory(JSONObject response) {
        try {

            FileOutputStream out = getActivity().openFileOutput("feed.json",
                    ActionBarActivity.MODE_PRIVATE);
            BufferedWriter buff = new BufferedWriter(
                    new OutputStreamWriter(out));
            buff.write(response.toString());
            buff.flush();
            buff.close();
            out.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("bloodRequest");

            BloodDataSource bloodDatabase = new BloodDataSource(getActivity());
            bloodDatabase.open();
            DistrictDataSource districtDatabase = new DistrictDataSource(
                    getActivity());
            districtDatabase.open();
            HospitalDataSource hospitalDatabase = new HospitalDataSource(
                    getActivity());
            hospitalDatabase.open();
            feedItems.clear();
            for (int i = 0; i < feedArray.length(); i++) {

                JSONObject feedObj = (JSONObject) feedArray.get(i);
                boolean distFilter = PreferenceManager.getDefaultSharedPreferences(
                        getActivity()).getBoolean(SettingsActivity.DISTRICT_FILTER_TAG, false);
                boolean groupFiter = PreferenceManager.getDefaultSharedPreferences(
                        getActivity()).getBoolean(SettingsActivity.GROUP_FILTER_TAG, false);
                boolean banglaFilter = PreferenceManager.getDefaultSharedPreferences(
                        getActivity()).getBoolean(SettingsActivity.LANGUAGE_TAG, false);
                FeedItem item = new FeedItem();
                String mobile = feedObj.getString("mobileNumber");
                item.setContact(mobile);
                item.setName("name");
                item.setContact(feedObj.getString("mobileNumber"));
                item.setName("name");
                String date = feedObj.getString("reqTime");
                item.setTimeStamp(date);
                String emergency = feedObj.getString("emergency")
                        .compareTo("1") == 0 ? "yes" : null;
                item.setEmergency(emergency);
                BloodItem bi = new BloodItem();
                bi.setBloodId(Integer.parseInt(feedObj.getString("groupId")));
                if (groupFiter && userInfo.getGroupId() != bi.getBloodId()) {
                    continue;
                }
                BloodItem exbi = bloodDatabase.cursorToBloodItem(bloodDatabase
                        .bloodItemToCursor(bi));
                String amount = feedObj.getString("amount");
                Integer a = Integer.parseInt(amount);
                item.setBloodAmount(a);
                HospitalItem hi = new HospitalItem();
                hi.setHospitalId(Integer.parseInt(feedObj.getString("hospitalId")));
                HospitalItem exhi = hospitalDatabase
                        .cursorToHospitalItem(hospitalDatabase
                                .hospitalItemToCursor(hi));
                DistrictItem di = new DistrictItem();
                di.setDistId(exhi.getDistId());
                DistrictItem exdi = districtDatabase
                        .cursorToDistrictItem(districtDatabase
                                .districtItemToCursor(di));
                if (distFilter && userInfo.getDistId() != exdi.getDistId()) {
                    continue;
                }
                if (banglaFilter) {
                    item.setBloodGroup(exbi.getBanglaBloodName());
                    item.setHospital(exhi.getBanglaHospitalName());
                    item.setArea(exdi.getBanglaDistName());

                } else {
                    item.setBloodGroup(exbi.getBloodName());
                    item.setHospital(exhi.getHospitalName());
                    item.setArea(exdi.getDistName());
                }
                feedItems.add(item);
            }
            bloodDatabase.close();
            hospitalDatabase.close();
            districtDatabase.close();
            listAdapter.notifyDataSetChanged();
            if (firstTime) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        actionButton.setVisibility(View.VISIBLE);
                        actionButton.startAnimation(AnimationUtils
                                .loadAnimation(getActivity(), R.anim.grow));
                    }
                }, 200);
                firstTime = false;

            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        progressBar.setVisibility(View.GONE);
    }

    private void customizeActionbar() {
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setBackgroundDrawable(mActionBarBackgroundDrawable);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowTitleEnabled(false);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setHomeButtonEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowCustomEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowHomeEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setCustomView(mCustomView);
    }
}