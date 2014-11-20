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
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.fragments.LogInFragment;

public class LogInActivity extends ActionBarActivity {
    public static boolean backPressed = false;
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "regContent");
        }
        if (mContent == null) {
            mContent = new LogInFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.login_container, mContent).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "regContent",
                mContent);
    }

    @Override
    public void onBackPressed() {

        if (backPressed) {
            finish();
            LogInActivity.this.overridePendingTransition(
                    R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
            super.onBackPressed();
        } else
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