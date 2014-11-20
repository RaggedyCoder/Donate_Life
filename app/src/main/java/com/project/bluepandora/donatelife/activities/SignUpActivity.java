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
import android.widget.Toast;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.fragments.MobileVerificationFragment;
import com.project.bluepandora.donatelife.fragments.NameVerificationFragment;
import com.project.bluepandora.donatelife.fragments.PasswordVerificationFragment;
import com.project.bluepandora.donatelife.fragments.RegistrationCompleteFragment;

import java.util.ArrayList;

public class SignUpActivity extends ActionBarActivity {

    /**
     * Defines a tag for identifying log entries
     */
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private static int currentFragmentTrackNumber;
    private Fragment mContent;
    private ArrayList<Fragment> fragments;

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
        if (fragments == null) {
            fragments = new ArrayList<Fragment>();
        }
        if (mContent == null) {
            mContent = new MobileVerificationFragment();
            fragments.add(mContent);
            currentFragmentTrackNumber = 0;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.signup_container, mContent).commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "regContent",
                mContent);
    }

    public void changeFragment(Bundle bundle, int number) {
        if (number == 1) {
            if (fragments.size() == 1) {
                fragments.add(1, PasswordVerificationFragment.newInstance(bundle));

            } else {
                fragments.set(1, PasswordVerificationFragment.newInstance(bundle));
            }
        } else if (number == 2) {
            if (fragments.size() == 2) {
                fragments.add(2, NameVerificationFragment.newInstance(bundle));
            } else {
                fragments.set(2, NameVerificationFragment.newInstance(bundle));
            }
        } else if (number == 3) {
            if (fragments.size() == 3) {
                fragments.add(3, RegistrationCompleteFragment.newInstance(bundle));
            } else {
                fragments.set(3, RegistrationCompleteFragment.newInstance(bundle));
            }
        }
        Toast.makeText(this, fragments.size() + "", Toast.LENGTH_SHORT).show();
        currentFragmentTrackNumber = number;
        mContent = fragments.get(number);
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.to_left, R.anim.out_right).
                replace(R.id.signup_container, mContent).commit();
    }

    @Override
    public void onBackPressed() {
        if (mContent instanceof MobileVerificationFragment) {
            finish();
            super.onBackPressed();
        } else {
            mContent = fragments.get(--currentFragmentTrackNumber);
            getSupportFragmentManager().beginTransaction().
                    setCustomAnimations(R.anim.to_right, R.anim.out_left).
                    replace(R.id.signup_container, mContent).commit();
        }
    }
}
