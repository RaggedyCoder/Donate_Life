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

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.fragments.SettingsFragment;
import com.project.bluepandora.util.Utils;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String DISTRICT_FILTER_TAG = "pref_filter_district";
    public static final String GROUP_FILTER_TAG = "pref_filter_blood_group";
    public static final String VIBRATION_TAG = "pref_notification_vibration";
    public static final String RINGTONE_TAG = "pref_notification_ringtone";
    public static final String NOTIFICATION_TAG = "pref_notification_receive";
    public static final String LANGUAGE_TAG = "pref_app_language";

    private static final String TAG = SettingsActivity.class.getSimpleName();
    private static final String FRAGMENT_TAG = "mContent";
    private Fragment mContent;

    public SettingsActivity() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate Method Called.");
        setTitle(R.string.settings);
        if (Utils.hasHoneycomb()) {
            if (savedInstanceState == null) {
                mContent = new SettingsFragment();
            } else {
                mContent = getFragmentManager().getFragment(savedInstanceState, FRAGMENT_TAG);
            }
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, mContent).commit();
        } else {
            setContentView(R.layout.activity_settings_legacy);
            Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
            actionbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
            actionbar.setNavigationOnClickListener(new NavigationClickListener());
            //noinspection deprecation
            addPreferencesFromResource(R.xml.settings);
        }
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onSaveInstanceState(@Nullable Bundle outState) {
        Log.i(TAG, " onSaveInstanceState Method Called.");
        if (outState != null) {
            super.onSaveInstanceState(outState);
            if (Utils.hasHoneycomb()) {
                getFragmentManager().putFragment(outState, FRAGMENT_TAG, mContent);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, " onStop Method Called.");
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, " onBackPressed Method Called.");
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(TAG, "SharedPreference Changed which key is-" + key);
        if (key.equals(LANGUAGE_TAG)) {
            restartActivity();
            Log.i(TAG, "activity restarted.");
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private class NavigationClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startMainActivity();
            Log.i(TAG, "Main Activity started.");
        }
    }
}
