package com.project.bluepandora.donatelife.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.fragments.SettingsFragment;
import com.project.bluepandora.util.Utils;

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
public class SettingsActivity extends PreferenceActivity {

    public static final String DISTRICT_FILTER_TAG = "pref_filter_district";
    public static final String GROUP_FILTER_TAG = "pref_filter_blood_group";
    public static final String VIBRATION_TAG = "pref_filter_vibration";
    public static final String NOTIFICATION_TAG = "pref_filter_notification";

    public SettingsActivity() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Settings");
        if (Utils.hasHoneycomb()) {
            Log.e("Settings", PreferenceManager.getDefaultSharedPreferences(
                    this).getBoolean(SettingsActivity.NOTIFICATION_TAG, true) + "");
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment()).commit();
        } else {
            setContentView(R.layout.activity_settings_legacy);
            Toolbar actionbar = (Toolbar) findViewById(R.id.actionbar);
            actionbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha));
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            addPreferencesFromResource(R.xml.settings);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
