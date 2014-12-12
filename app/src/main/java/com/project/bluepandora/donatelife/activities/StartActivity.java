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

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.UserDataSource;

import java.util.ArrayList;
import java.util.Locale;

public class StartActivity extends Activity {

    private static final String TAG = StartActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean banglaMode = PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean(SettingsActivity.LANGUAGE_TAG, false);
        final Locale locale;
        if (banglaMode) {
            String languageToLoad = "bn";
            locale = new Locale(languageToLoad);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, null);
        } else {
            locale = Locale.ENGLISH;
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, null);
        }
        UserDataSource userDatabase = new UserDataSource(this);
        ArrayList<UserInfoItem> items = null;
        userDatabase.open();
        try {
            items = userDatabase.getAllUserItem();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        if (items != null) {
            if (items.size() == 1) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
