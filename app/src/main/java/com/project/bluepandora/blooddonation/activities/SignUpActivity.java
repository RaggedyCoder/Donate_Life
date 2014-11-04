package com.project.bluepandora.blooddonation.activities;
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

import com.project.bluepandora.blooddonation.fragments.MobileVerificationFragment;
import com.project.bluepandora.donatelife.R;

public class SignUpActivity extends ActionBarActivity {

    /**
     * Defines a tag for identifying log entries
     */
    private static final String TAG = SignUpActivity.class.getSimpleName();

    private Fragment mContent;

    public SignUpActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "regContent");
        }
        if (mContent == null) {
            mContent = new MobileVerificationFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.signup_container, mContent).commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "regContent",
                mContent);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}
