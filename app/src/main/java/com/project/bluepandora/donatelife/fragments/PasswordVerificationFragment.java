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

public class PasswordVerificationFragment extends Fragment {
    /**
     * Defines a tag for identifying log entries
     */
    private static String TAG = PasswordVerificationFragment.class.getSimpleName();
    /**
     * A {@link View} for the whole fragment view.
     */
    protected View rootView;
    /**
     * A EditTextField{@link CustomEditText} for the password.
     */
    private CustomEditText passwordField;
    /**
     * A EditTextField confirming the password.
     */
    private CustomEditText confirmPasswordField;
    /**
     * A Button {@link CustomButton} for the un registered user to verify the password.
     */
    private CustomButton verificationButton;
    /**
     * A click detection listener for the SignUp Button.
     */
    private View.OnClickListener mVerificationListener;

    public static Fragment newInstance(Bundle bundle) {
        PasswordVerificationFragment fragment = new PasswordVerificationFragment();
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
        rootView = inflater.inflate(R.layout.fragment_passwordverification, container, false);
        verificationButton = (CustomButton) rootView.findViewById(R.id.password_verification);
        passwordField = (CustomEditText) rootView.findViewById(R.id.password);
        confirmPasswordField = (CustomEditText) rootView.findViewById(R.id.password_confirm);
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
                if (passwordField.getText().toString().length() == 0) {
                    createAlertDialog("Please confirm your Password.");
                } else if (confirmPasswordField.getText().toString().length() == 0) {
                    createAlertDialog(getString(R.string.Warning_enter_a_password));
                } else if (!passwordField.getText().toString().equals(confirmPasswordField.getText().toString())) {
                    createAlertDialog(getString(R.string.Warning_password_not_matched));
                } else {
                    Bundle bundle = PasswordVerificationFragment.this.getArguments();
                    bundle.putString("password", passwordField.getText().toString());
                    SignUpActivity signUpActivity = (SignUpActivity) getActivity();
                    signUpActivity.changeFragment(bundle, 2);
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
