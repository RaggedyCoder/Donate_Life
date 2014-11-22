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
import android.support.v7.app.ActionBar;
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
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.SlideItem;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.fragments.FeedFragment;
import com.project.bluepandora.donatelife.fragments.ProfileDetailsFragment;
import com.project.bluepandora.donatelife.fragments.ProfileFragment;
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
    public static boolean backPressed = false;
    public static ArrayList<Fragment> fragments;
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
            Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
            // Releasing wake lock
            WakeLocker.release();
        }
    };
    public ActionBar act;
    public Fragment mContent;
    public int prevpos = 0;
    protected DrawerSlideListners mDrawerSlideListners;
    DrawerLayout mDrawerLayout;
    ListView mDrawerListView;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    AsyncTask<Void, Void, Void> mRegisterTask;
    private SlideMenuAdapter listAdapter;
    private List<Item> slideItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        act = getSupportActionBar();
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "mContent");
        }
//        GCMRegistrar.checkDevice(this);
        //      GCMRegistrar.checkManifest(this);
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                CommonUtilities.DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);
        Log.e(TAG, regId + " here");
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);
            Toast.makeText(this, "" + GCMRegistrar.getRegistrationId(this), Toast.LENGTH_LONG).show();
        } else {
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
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
            fragments = new ArrayList<Fragment>();
            fragments.add(new FeedFragment());
            fragments.add(new RequestFragment());
            fragments.add(new ProfileDetailsFragment());
            mContent = fragments.get(0);
        }
        mDrawerListView = (ListView) findViewById(R.id.list_slidermenu);
        slideItems = new ArrayList<Item>();

        listAdapter = new SlideMenuAdapter(this, slideItems);
        mDrawerListView.setAdapter(listAdapter);
        if (mContent instanceof RequestFragment) {
            mDrawerSlideListners = (DrawerSlideListners) fragments.get(1);
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
                    ((DrawerSlideListners) mContent).onDrawerslide(slideOffset);
                }
                Log.d(TAG, "" + slideOffset);
            }
        };
        String[] names = getResources()
                .getStringArray(R.array.nav_drawer_items);
        TypedArray icons = getResources().obtainTypedArray(
                R.array.nav_drawer_icons);
        TypedArray backgrounds = getResources().obtainTypedArray(
                R.array.nav_drawer_backgrounds);

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

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
        mDrawerListView.setOnItemClickListener(new ListItemListner());
        listAdapter.notifyDataSetInvalidated();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, mContent).commit();
        if (mContent instanceof FeedFragment) {
            listAdapter.setSelected(0);
            prevpos = 0;
        } else if (mContent instanceof RequestFragment) {
            mDrawerSlideListners = (DrawerSlideListners) fragments.get(1);
            listAdapter.setSelected(1);
            prevpos = 1;
        } else if (mContent instanceof ProfileFragment) {
            listAdapter.setSelected(2);
            prevpos = 2;
        }
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
            pd.setTitle("Logging out.");
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
                        Toast.makeText(MainActivity.this, "Check Your internet Connection", Toast.LENGTH_LONG).show();
                        return;
                    }
                    for (UserInfoItem i : items) {
                        userDatabase.deleteUserInfoitem(i);
                    }
                    userDatabase.close();
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

        if (backPressed) {
            finish();
            AppController.getInstance().getRequestQueue().getCache()
                    .remove(URL.URL);
            MainActivity.this.overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
            backPressed = false;
            FeedFragment.firstTime = true;
            finish();
            super.onBackPressed();
        } else {

            Toast.makeText(this, "Press Back again to Exit", Toast.LENGTH_SHORT)
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
        prevpos = 1;
        listAdapter.setSelected(1);
        mContent = fragments.get(1);
        mDrawerListView.setItemChecked(1, true);
        mDrawerListView.setSelection(1);
        mDrawerLayout.closeDrawer(mDrawerListView);
        mDrawerSlideListners = (DrawerSlideListners) fragments.get(1);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.grow, R.anim.dim)
                .replace(R.id.frame_container, mContent).commit();
    }

    public void switchContent(int pos) {

        AppController.getInstance().cancelPendingRequests();
        backPressed = false;
        if (pos == 3) {
            mDrawerLayout.closeDrawer(mDrawerListView);
            return;
        }
        if ((mContent instanceof RequestFragment)
                && (fragments.get(pos) instanceof RequestFragment)) {
            Toast.makeText(this, "returned", Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawer(mDrawerListView);
            return;
        } else if ((mContent instanceof FeedFragment)
                && (fragments.get(pos) instanceof FeedFragment)) {
            Toast.makeText(this, "returned", Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawer(mDrawerListView);
            return;
        } else if ((mContent instanceof ProfileDetailsFragment)
                && (fragments.get(pos) instanceof ProfileDetailsFragment)) {
            Toast.makeText(this, "returned", Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawer(mDrawerListView);
            return;
        }
        FeedFragment.slideChange = true;
        if (pos == 1) {
            mDrawerSlideListners = (DrawerSlideListners) fragments.get(1);
        }
        if (pos == 2) {
            mContent = new ProfileDetailsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left,
                            R.anim.slide_out_right)
                    .replace(R.id.frame_container, mContent).commit();
        } else {
            if ((mContent instanceof ProfileDetailsFragment)) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left,
                                R.anim.slide_out_right)
                        .remove(mContent).commit();
            }
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left,
                        R.anim.slide_out_right)
                .replace(R.id.frame_container, fragments.get(pos)).commit();
            mContent = fragments.get(pos);
        }
        mDrawerListView.setItemChecked(pos, true);
        mDrawerListView.setSelection(pos);
        mDrawerLayout.closeDrawer(mDrawerListView);
    }

    public interface DrawerSlideListners {
        public void onDrawerslide(float offset);
    }

    private class ListItemListner implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            listAdapter.setSelected(position);
            if (position == 4) {
                Intent intent =
                        new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(mDrawerListView);
                return;
            }
            if (position == 5) {
                Intent intent =
                        new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(mDrawerListView);
                return;
            }
            if (position > 3) {
                mDrawerLayout.closeDrawer(mDrawerListView);
                return;
            }
            if (prevpos != position) {
                switchContent(position);
                prevpos = position;
            } else {
                mDrawerLayout.closeDrawer(mDrawerListView);
            }
        }
    }
}