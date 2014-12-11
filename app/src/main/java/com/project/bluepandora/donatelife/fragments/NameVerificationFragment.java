package com.project.bluepandora.donatelife.fragments;
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
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.SignUpActivity;
import com.widget.CustomButton;
import com.widget.CustomEditText;

public class NameVerificationFragment extends Fragment {
    /**
     * Defines a tag for identifying log entries
     */
    private static String TAG = NameVerificationFragment.class.getSimpleName();
    /**
     * A {@link View} for the whole fragment view.
     */
    protected View rootView;
    /**
     * A EditTextField{@link CustomEditText} for the First Name.
     */
    private CustomEditText firstNameField;
    /**
     * A EditTextField confirming the Last Name.
     */
    private CustomEditText lastNameField;
    /**
     * A Button {@link CustomButton} for the un registered user to verify the name.
     */
    private CustomButton verificationButton;
    /**
     * A click detection listener for the SignUp Button.
     */
    private View.OnClickListener mVerificationListener;

    public static Fragment newInstance(Bundle bundle) {
        NameVerificationFragment fragment = new NameVerificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_nameverification, container, false);
        verificationButton = (CustomButton) rootView.findViewById(R.id.name_verification);
        firstNameField = (CustomEditText) rootView.findViewById(R.id.registration_first_name);
        lastNameField = (CustomEditText) rootView.findViewById(R.id.registration_last_name);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        verificationButton.setOnClickListener(mVerificationListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mVerificationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstNameField.getText().toString().length() == 0) {
                    createAlertDialog(getString(R.string.first_name_rule));
                } else if (lastNameField.getText().toString().length() == 0) {
                    createAlertDialog(getString(R.string.last_name_rule));
                } else {
                    Bundle bundle = NameVerificationFragment.this.getArguments();
                    bundle.putString("firstName", firstNameField.getText().toString());
                    bundle.putString("lastName", lastNameField.getText().toString());
                    SignUpActivity signUpActivity = (SignUpActivity) getActivity();
                    signUpActivity.changeFragment(bundle, 3);
                }

            }
        };
    }

    private void createAlertDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.alert));
        alertDialog.setMessage(message);
        alertDialog.setNeutralButton("Ok", null);
        alertDialog.show();
    }
}
