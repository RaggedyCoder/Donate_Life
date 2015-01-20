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
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
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
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.helpers.ParamsBuilder;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.jsonperser.JSONParser;
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

    private static final String TAG = FeedFragment.class.getSimpleName();
    private static final int[] refreshColorSchemes = {R.color.holo_red_light, R.color.holo_blue_light,
            R.color.holo_green_light, R.color.holo_purple};
    public static boolean firstTime = true;
    public static boolean slideChange = false;
    public JSONObject json;
    public UserInfoItem userInfo;
    private FeedListAdapter listAdapter;
    private List<Item> feedItems;
    private View actionbarView;
    private OnRefreshListener mOnFeedRefresh;
    private SharedPreferences preferences;
    private Resources resources;
    private ActionBar actionBar;

    private View rootView;

    private MainViewHolder mainViewHolder;
    private ActionbarViewHolder actionbarViewHolder;

    private CustomRequest bloodFeedRequest;
    private Listener<JSONObject> jsonListener;
    private ErrorListener errorListener;

    public FeedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedItems = new ArrayList<Item>();
        listAdapter = new FeedListAdapter(getActivity(), feedItems);
        UserDataSource data = new UserDataSource(getActivity());
        data.open();
        userInfo = data.getAllUserItem().get(0);
        data.close();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        resources = getResources();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_feed, container, false);
            mainViewHolder = new MainViewHolder();
            mainViewHolder.feedListView = (ListView) rootView.findViewById(R.id.feed_list_view);
            mainViewHolder.actionButton = (ImageButton) rootView.findViewById(R.id.action_button);
            mainViewHolder.progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
            mainViewHolder.swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
            rootView.setTag(mainViewHolder);
        } else {
            mainViewHolder = (MainViewHolder) rootView.getTag();
        }
        if (actionbarView == null) {
            actionbarView = inflater.inflate(R.layout.request_feed_actionbar, container, false);
            actionbarViewHolder = new ActionbarViewHolder();
            actionbarViewHolder.mTitle = (CustomTextView) actionbarView.findViewById(R.id.actionbar_title_text);
            actionbarViewHolder.actionBarBackgroundDrawable = resources.getDrawable(R.drawable.actionbar_background);
            actionbarView.setTag(actionbarViewHolder);
        } else {
            actionbarViewHolder = (ActionbarViewHolder) actionbarView.getTag();
            if (actionbarViewHolder.actionBarBackgroundDrawable == null) {
                actionbarViewHolder.actionBarBackgroundDrawable = resources.getDrawable(R.drawable.actionbar_background);
            }
        }
        if (firstTime) {
            mainViewHolder.actionButton.setVisibility(View.GONE);
        } else {
            mainViewHolder.actionButton.setVisibility(View.VISIBLE);
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
        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        mainViewHolder.feedListView.setAdapter(listAdapter);
        mainViewHolder.swipeRefreshLayout.setColorSchemeResources(refreshColorSchemes);
        mainViewHolder.swipeRefreshLayout.setOnRefreshListener(mOnFeedRefresh);
        actionbarViewHolder.actionBarBackgroundDrawable.setAlpha(0xFF);
        actionbarViewHolder.mTitle.setText(R.string.blood_feed);
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
                mainViewHolder.actionButton.getLocationOnScreen(origin);
                int xDest = mainViewHolder.feedListView.getMeasuredWidth() / 2;
                xDest -= (mainViewHolder.actionButton.getMeasuredWidth() / 2);
                int yDest = mainViewHolder.feedListView.getMeasuredHeight() / 2;
                yDest -= (mainViewHolder.actionButton.getMeasuredHeight() / 2)
                        - getActivity().getResources().getDimensionPixelSize(
                        R.dimen.abc_action_bar_default_height_material);
                final int newLeft = mainViewHolder.actionButton.getLeft() + xDest - origin[0];
                final int newTop = mainViewHolder.actionButton.getTop() + yDest - origin[1];
                mainViewHolder.actionButton.layout(newLeft, newTop,
                        newLeft + mainViewHolder.actionButton.getMeasuredWidth(), newTop
                                + mainViewHolder.actionButton.getMeasuredHeight());
                mainViewHolder.actionButton.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), R.anim.full_grow));
                MainActivity act = (MainActivity) getActivity();
                act.switchContent(1);
                Log.e(TAG, mainViewHolder.actionButton.getLeft() + " " + mainViewHolder.actionButton.getTop());
            }
        };
        mainViewHolder.actionButton.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View arg0) {
                DisplayMetrics dm = new DisplayMetrics();
                // this.getWindowManager().getDefaultDisplay().getMetrics( dm );
                getActivity().getWindowManager().getDefaultDisplay()
                        .getMetrics(dm);
                int[] origin = new int[2];
                mainViewHolder.actionButton.getLocationOnScreen(origin);
                int xDest = mainViewHolder.feedListView.getMeasuredWidth() / 2;
                xDest -= (mainViewHolder.actionButton.getMeasuredWidth() / 2);
                int yDest = mainViewHolder.feedListView.getMeasuredHeight() / 2;
                yDest -= (mainViewHolder.actionButton.getMeasuredHeight() / 2)
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
                Log.e(TAG, mainViewHolder.actionButton.getX() + " " + mainViewHolder.actionButton.getY());
                mainViewHolder.actionButton.startAnimation(anim);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {

        mOnFeedRefresh = new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainViewHolder.progressBar.setVisibility(View.VISIBLE);
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainViewHolder.swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

            }
        };
        super.onAttach(activity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            mainViewHolder.progressBar.setVisibility(View.VISIBLE);
            mainViewHolder.swipeRefreshLayout.setRefreshing(true);
            jsonObjectRequest();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mainViewHolder.swipeRefreshLayout.setRefreshing(false);
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
        if (mainViewHolder.actionButton.getVisibility() == View.VISIBLE && slideChange) {
            mainViewHolder.actionButton.setVisibility(View.GONE);
        }
        if (!firstTime && slideChange) {
            slideChange = false;
            Handler h = new Handler();
            h.postDelayed(new Runnable() {

                @Override
                public void run() {

                    Log.e(TAG, "again");
                    mainViewHolder.actionButton.setVisibility(View.VISIBLE);
                    mainViewHolder.actionButton.startAnimation(AnimationUtils.loadAnimation(
                            getActivity(), R.anim.grow));
                }
            }, 800);
        }
        jsonObjectRequest();
    }

    private void jsonObjectRequest() {
        boolean distFilter = preferences.getBoolean(SettingsActivity.DISTRICT_FILTER_TAG, false);
        boolean groupFilter = preferences.getBoolean(SettingsActivity.GROUP_FILTER_TAG, false);
        Item distItem = new DistrictItem(userInfo.getDistId(), null, null);
        Item groupItem = new BloodItem(null, null, userInfo.getGroupId());
        HashMap<String, String> params;
        if (distFilter && groupFilter) {
            params = ParamsBuilder.bloodRequestFeed(distItem, groupItem);
        } else if (distFilter) {
            params = ParamsBuilder.bloodRequestFeed(distItem);
        } else if (groupFilter) {
            params = ParamsBuilder.bloodRequestFeed(groupItem);
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
                                mainViewHolder.progressBar.setVisibility(View.GONE);
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
            JSONParser jsonParser = new JSONParser(getActivity(), TAG, true);
            feedItems.clear();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = jsonParser.parseJsonFeed(feedObj);
                if (item == null) {
                    continue;
                }
                feedItems.add(item);
            }
            jsonParser.closeAlldatabase();
            listAdapter.notifyDataSetChanged();
            if (firstTime) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainViewHolder.actionButton.setVisibility(View.VISIBLE);
                        mainViewHolder.actionButton.startAnimation(AnimationUtils
                                .loadAnimation(getActivity(), R.anim.grow));
                    }
                }, 200);
                firstTime = false;
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        mainViewHolder.progressBar.setVisibility(View.GONE);
    }

    private void customizeActionbar() {
        actionBar.setBackgroundDrawable(actionbarViewHolder.actionBarBackgroundDrawable);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setCustomView(actionbarView);
    }

    private static class MainViewHolder {
        public SwipeRefreshLayout swipeRefreshLayout;
        private ListView feedListView;
        private ImageButton actionButton;
        private ProgressBar progressBar;
    }

    private static class ActionbarViewHolder {
        private CustomTextView mTitle;
        private Drawable actionBarBackgroundDrawable;
    }
}