package com.project.bluepandora.donatelife.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.fragments.AboutFragment;

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
public class AboutActivity extends ActionBarActivity {

    private static final String TAG = AboutActivity.class.getSimpleName();
    private static final String FRAGMENT_TAG = "mContent";
    private Fragment mContent;
    private BackPressImp mbackPressImp;

    public AboutActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (savedInstanceState == null) {
            mContent = new AboutFragment();
        } else {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_TAG);
        }
        mbackPressImp = (BackPressImp) mContent;
        getSupportFragmentManager().
                beginTransaction().replace(R.id.container, mContent).commit();
    }

    public interface BackPressImp {
        public boolean onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, mContent);
    }

    @Override
    public void onBackPressed() {
        if (mbackPressImp.onBackPressed()) {
            finish();
            super.onBackPressed();
        } else {

        }
    }
}