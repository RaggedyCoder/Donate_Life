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
import android.widget.Toast;

import com.project.bluepandora.blooddonation.fragments.CheckRegistrationFragment;
import com.project.bluepandora.blooddonation.fragments.RegistrationStepFragment;
import com.project.bluepandora.donatelife.R;

public class RegistrationActivity extends ActionBarActivity {
    public Fragment mContent;
    public static boolean backPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "regContent");
        }
        if (mContent == null) {
            mContent = new CheckRegistrationFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.reg_container, mContent).commit();
        }
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
            RegistrationActivity.this.overridePendingTransition(
                    R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
            super.onBackPressed();
        } else
            Toast.makeText(this, "Press Back again to Exit", Toast.LENGTH_SHORT)
                    .show();
        backPressed = true;
    }

    public void changeFragement(String mobileNumber) {
        mContent = new RegistrationStepFragment(mobileNumber);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.reg_container, mContent).commit();
    }
}
