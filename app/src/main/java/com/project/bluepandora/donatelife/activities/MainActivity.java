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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gcm.GCMRegistrar;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.DRItem;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.DRDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.fragments.FeedFragment;
import com.project.bluepandora.donatelife.fragments.MenuFragment;
import com.project.bluepandora.donatelife.fragments.ProfileDetailsFragment;
import com.project.bluepandora.donatelife.fragments.RequestFragment;
import com.project.bluepandora.donatelife.fragments.SmallMenuFragment;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.receiver.GCMBroadcastReceiver;
import com.project.bluepandora.donatelife.services.ServerUtilities;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.project.bluepandora.util.CommonUtilities;
import com.project.bluepandora.util.ConnectionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.project.bluepandora.util.CommonUtilities.SENDER_ID;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity implements MenuFragment.FragmentSwitch {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FEED_FRAGMENT = 0;
    private static final int REQUEST_FRAGMENT = 1;
    private static final int PROFILE_FRAGMENT = 2;
    //private static final int HOSPITAL_FRAGMENT = 3;
    private static final int SETTINGS_ACTIVITY = 3;
    private static final int FEEDBACK_ACTIVITY = 4;
    private static final int ABOUT_ACTIVITY = 5;
    public static boolean backPressed = false;
    private GCMBroadcastReceiver gcmBroadcastReceiver;
    private DrawerLayout mDrawerLayout;
    private SlidingPaneLayout mSlidingPaneLayout;
    private PanelSlideListener mPanelSlideListener;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private AsyncTask<Void, Void, Void> mRegisterTask;
    private Fragment mContent;
    private Fragment mListContent;
    private Fragment mSmallListContent;
    private DrawerSlideListeners mDrawerSlideListeners;
    private boolean tabScreen;
    private SlidePaneListeners mSlidePaneListeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabScreen = getResources().getBoolean(R.bool.tab_screen);
        Log.e(TAG, "" + tabScreen);
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            mListContent = getSupportFragmentManager().getFragment(savedInstanceState, "mListContent");
            if (tabScreen) {
                mListContent = getSupportFragmentManager().getFragment(savedInstanceState, "mListContent");
            }
        } else {
            mContent = new FeedFragment();
            mListContent = new MenuFragment();
            if (tabScreen) {
                mSmallListContent = new SmallMenuFragment();
            }
        }
        GCMRegistration();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, mContent).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.slide_menu_container, mListContent).commit();
        if (tabScreen) {
            getSupportFragmentManager().beginTransaction().replace(R.id.small_menu_container, mSmallListContent).commit();
        }
        if (mContent instanceof RequestFragment) {
            mDrawerSlideListeners = (DrawerSlideListeners) mContent;
        }
        if (!tabScreen) {
            usingSlidingDrawer();
        } else {
            usingSlidingLayout();
        }
    }

    private void usingSlidingLayout() {

        mSlidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.slider_layout);
        mSlidingPaneLayout.setShadowResourceLeft(R.drawable.list_drawer_shadow_2);
        // mSlidingPaneLayout.setParallaxDistance(200);
        mSlidePaneListeners = (SlidePaneListeners) mSmallListContent;
        mPanelSlideListener = new PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float slideOffset) {
                if (mContent instanceof RequestFragment) {
                    mDrawerSlideListeners.onDrawerSlide(slideOffset);
                }
                mSlidePaneListeners.onPaneSlide(slideOffset);
                Log.i(TAG, "" + slideOffset);
            }

            @Override
            public void onPanelOpened(View view) {

            }

            @Override
            public void onPanelClosed(View view) {

            }
        };
        mSlidingPaneLayout.setPanelSlideListener(mPanelSlideListener);
    }

    private void usingSlidingDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.list_drawer_shadow, Gravity.START);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
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
    }

    private void GCMRegistration() {
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        gcmBroadcastReceiver = new GCMBroadcastReceiver();
        registerReceiver(gcmBroadcastReceiver, new IntentFilter(
                CommonUtilities.DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);
        Log.e(TAG, "GCM ID for this mobile-" + regId);
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);
            Log.e(TAG, "GCM ID for this mobile-" + regId);
        } else {
            // Device is already registered on GCM
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
                        UserDataSource database = new UserDataSource(context);
                        database.open();
                        UserInfoItem userInfoItem = database.getAllUserItem().get(0);
                        database.close();
                        Log.i(TAG, "REG ID for register-" + regId);
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
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(gcmBroadcastReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        if (!tabScreen)
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
        if (!tabScreen) {
            if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        }
        if (id == R.id.action_logout) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCancelable(true);
            pd.setMessage(getString(R.string.log_out_message));
            pd.show();
            final ConnectionManager con = new ConnectionManager(this);
            final UserDataSource userDatabase = new UserDataSource(this);
            if (!con.isConnectingToInternet()) {
                Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
            }
            userDatabase.open();
            final ArrayList<UserInfoItem> items = userDatabase.getAllUserItem();
            Map<String, String> params = new HashMap<String, String>();
            params.put(URL.REQUEST_NAME, URL.GCMREGISTER_PARAM);
            params.put(URL.MOBILE_TAG, items.get(0).getMobileNumber());
            params.put(URL.GCM_TAG, " ");
            CustomRequest customRequest = new CustomRequest(Request.Method.POST, URL.URL, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Log.e(TAG, jsonObject.toString(1));
                        if (jsonObject.getInt("done") == 1) {
                            GCMRegistrar.setRegisteredOnServer(MainActivity.this, false);
                            deleteFile("feed.json");
                            userDatabase.open();
                            ArrayList<UserInfoItem> userInfoItems = userDatabase.getAllUserItem();
                            for (UserInfoItem userInfoItem : userInfoItems) {
                                userDatabase.deleteUserInfoitem(userInfoItem);
                            }
                            userDatabase.close();
                            pd.dismiss();
                            DRDataSource dataSource = new DRDataSource(MainActivity.this);
                            dataSource.open();
                            ArrayList<DRItem> drItems;
                            try {
                                drItems = dataSource.getAllDRItem();
                                for (DRItem drItem : drItems) {
                                    dataSource.deleteDistrictitem(drItem);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Empty donation record database");
                            }
                            dataSource.close();
                            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            pd.dismiss();
                            Toast.makeText(MainActivity.this, R.string.unknown_server_error, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_LONG).show();
                }
            });
            AppController.getInstance().addToRequestQueue(customRequest);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        getSupportFragmentManager().putFragment(outState, "mListContent", mListContent);
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
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

    @Override
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
            // case HOSPITAL_FRAGMENT:
            //   break;
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
    }

    @Override
    public void closeDownMenu() {
        if (!tabScreen)
            mDrawerLayout.closeDrawer(Gravity.START);
        else {
            mSlidingPaneLayout.closePane();
        }
    }

    @Override
    protected void onPause() {
        AppController.getInstance().cancelPendingRequests();
        super.onPause();
    }

    public interface DrawerSlideListeners {
        public void onDrawerSlide(float offset);
    }

    public interface SlidePaneListeners {
        public void onPaneSlide(float Offset);
    }

}