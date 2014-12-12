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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.fragments.FeedBackFragment;
import com.project.bluepandora.donatelife.helpers.URL;

public class FeedbackActivity extends ActionBarActivity implements URL {

    // Defines a tag for identifying log entries
    private static final String TAG = FeedbackActivity.class.getSimpleName();
    // Defines a tag for identifying the fragment from the Bundle
    private static final String FRAGMENT_TAG = "mContent";
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        if (savedInstanceState == null) {
            mContent = new FeedBackFragment();
        } else {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_TAG);
        }
        getSupportFragmentManager().
                beginTransaction().replace(R.id.container, mContent).commit();
        Log.i(TAG, "OnCreate method called.");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, mContent);
        Log.i(TAG, "onSaveInstanceState method called.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.i(TAG, "home menuItem selected.");
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
