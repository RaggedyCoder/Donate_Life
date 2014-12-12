package com.project.bluepandora.donatelife.activities;

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
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.adapter.SlideMenuAdapter;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.DRItem;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.SlideItem;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.DRDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.fragments.FeedFragment;
import com.project.bluepandora.donatelife.fragments.ProfileDetailsFragment;
import com.project.bluepandora.donatelife.fragments.RequestFragment;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.services.ServerUtilities;
import com.project.bluepandora.util.CommonUtilities;
import com.project.bluepandora.util.ConnectionManager;
import com.project.bluepandora.util.WakeLocker;

import java.util.ArrayList;
import java.util.List;

import static com.project.bluepandora.util.CommonUtilities.EXTRA_MESSAGE;
import static com.project.bluepandora.util.CommonUtilities.SENDER_ID;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FEED_FRAGMENT = 0;
    private static final int REQUEST_FRAGMENT = 1;
    private static final int PROFILE_FRAGMENT = 2;
    private static final int HOSPITAL_FRAGMENT = 3;
    private static final int SETTINGS_ACTIVITY = 4;
    private static final int FEEDBACK_ACTIVITY = 5;
    private static final int ABOUT_ACTIVITY = 6;
    public static boolean backPressed = false;
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            WakeLocker.acquire(getApplicationContext());
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
            WakeLocker.release();
        }
    };
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private AsyncTask<Void, Void, Void> mRegisterTask;
    private Fragment mContent;
    private int prevPos = 0;
    private DrawerSlideListeners mDrawerSlideListeners;
    private SlideMenuAdapter listAdapter;
    private List<Item> slideItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                CommonUtilities.DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);
        Log.e(TAG, "GCM ID for this mobile-" + regId);
        if (regId.equals("")) {
            GCMRegistrar.register(this, SENDER_ID);
            Toast.makeText(this, "" + GCMRegistrar.getRegistrationId(this), Toast.LENGTH_LONG).show();
        } else {
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                Log.i(TAG, "GCM Registration message" + "Already registered with GCM");
            } else {
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        UserDataSource database = new UserDataSource(context);
                        database.open();
                        UserInfoItem userInfoItem = database.getAllUserItem().get(0);
                        database.close();
                        ServerUtilities.register(context, userInfoItem.getMobileNumber(), regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
                };
                mRegisterTask.execute(null, null, null);
            }
        }
        if (mContent == null) {
            mContent = new FeedFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, mContent).commit();
        mDrawerListView = (ListView) findViewById(R.id.list_slidermenu);
        createSlidingMenu();
        listAdapter = new SlideMenuAdapter(this, slideItems);
        mDrawerListView.setAdapter(listAdapter);
        if (mContent instanceof RequestFragment) {
            mDrawerSlideListeners = (DrawerSlideListeners) mContent;
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (mContent instanceof RequestFragment) {
                    mDrawerSlideListeners.onDrawerSlide(slideOffset);
                }
                Log.d(TAG, "" + slideOffset);
            }
        };
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mDrawerListView.setOnItemClickListener(new ListItemListener());
        listAdapter.notifyDataSetInvalidated();
        if (mContent instanceof FeedFragment) {
            listAdapter.setSelected(0);
            prevPos = 0;
        } else if (mContent instanceof RequestFragment) {
            mDrawerSlideListeners = (DrawerSlideListeners) mContent;
            listAdapter.setSelected(1);
            prevPos = 1;
        }
    }

    private void createSlidingMenu() {
        slideItems = new ArrayList<Item>();
        String[] names = getResources()
                .getStringArray(R.array.nav_drawer_items);
        TypedArray icons = getResources().obtainTypedArray(
                R.array.nav_drawer_icons);
        TypedArray backgrounds = getResources().obtainTypedArray(
                R.array.nav_drawer_backgrounds);


        SlideItem item;
        for (int i = 0; i < names.length; i++) {
            item = new SlideItem();
            item.setSlideItem(names[i]);
            item.setIcons(icons.getResourceId(i, -1));
            item.setBackground(backgrounds.getResourceId(i, -1));
            slideItems.add(item);

        }
        icons.recycle();
        backgrounds.recycle();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.action_logout) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCancelable(true);
            pd.setTitle(getString(R.string.log_out_message));
            pd.show();
            deleteFile("feed.json");
            final ConnectionManager con = new ConnectionManager(this);
            final UserDataSource userDatabase = new UserDataSource(this);
            userDatabase.open();
            final ArrayList<UserInfoItem> items = userDatabase.getAllUserItem();
            mRegisterTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    if (con.isConnectingToInternet()) {
                        ServerUtilities.unregister(MainActivity.this, items.get(0).getMobileNumber());
                    } else {
                        return null;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    if (!con.isConnectingToInternet()) {
                        Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
                        return;
                    }
                    for (UserInfoItem i : items) {
                        userDatabase.deleteUserInfoitem(i);
                    }
                    userDatabase.close();
                    DRDataSource dataSource = new DRDataSource(MainActivity.this);
                    dataSource.open();
                    ArrayList<DRItem> items = dataSource.getAllDRItem();
                    for (DRItem item : items) {
                        dataSource.deleteDistrictitem(item);
                    }
                    dataSource.close();
                    pd.dismiss();
                    Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                    mRegisterTask = null;
                }

            };
            mRegisterTask.execute(null, null, null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);

    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(mDrawerListView)) {
            mDrawerLayout.closeDrawer(mDrawerListView);
            return;
        }
        if (backPressed) {
            finish();
            AppController.getInstance().getRequestQueue().getCache()
                    .remove(URL.URL);
            backPressed = false;
            MainActivity.this.overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
            FeedFragment.firstTime = true;
            finish();
        } else {

            Toast.makeText(this, R.string.exit_notice, Toast.LENGTH_SHORT)
                    .show();
            backPressed = true;
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {
                    backPressed = false;
                }
            }, 2000);
        }
    }

    public void switchContent() {
        prevPos = 1;
        listAdapter.setSelected(1);
        mContent = new RequestFragment();
        mDrawerListView.setItemChecked(1, true);
        mDrawerListView.setSelection(1);
        mDrawerLayout.closeDrawer(mDrawerListView);
        mDrawerSlideListeners = (DrawerSlideListeners) mContent;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.grow, R.anim.dim)
                .replace(R.id.frame_container, mContent).commit();
    }

    public void switchContent(int position) {


        Intent intent;
        AppController.getInstance().cancelPendingRequests();
        switch (position) {
            case FEED_FRAGMENT:
                mContent = new FeedFragment();
                break;
            case REQUEST_FRAGMENT:
                mContent = new RequestFragment();
                mDrawerSlideListeners = (DrawerSlideListeners) mContent;
                break;
            case PROFILE_FRAGMENT:
                mContent = new ProfileDetailsFragment();
                break;
            case HOSPITAL_FRAGMENT:
                break;
            case SETTINGS_ACTIVITY:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                finish();
                startActivity(intent);
                return;
            case FEEDBACK_ACTIVITY:
                intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
                return;
            case ABOUT_ACTIVITY:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return;
        }
        backPressed = false;
        FeedFragment.slideChange = true;
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                replace(R.id.frame_container, mContent).commit();
        mDrawerListView.setItemChecked(position, true);
        mDrawerListView.setSelection(position);
    }

    @Override
    protected void onPause() {
        AppController.getInstance().cancelPendingRequests();
        super.onPause();
    }

    public interface DrawerSlideListeners {
        public void onDrawerSlide(float offset);
    }

    private class ListItemListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            listAdapter.setSelected(position);
            if (prevPos != position) {
                mDrawerLayout.closeDrawer(mDrawerListView);
                if (position < 4) {
                    prevPos = position;
                }
                switchContent(position);
            } else {
                mDrawerLayout.closeDrawer(mDrawerListView);
                Log.i(TAG, "Returned to previous fragment without creating a new one");
            }
        }
    }
}