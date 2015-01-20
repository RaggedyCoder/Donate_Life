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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.SignUpActivity;
import com.project.bluepandora.donatelife.helpers.DialogBuilder;
import com.widget.CustomButton;
import com.widget.CustomEditText;

/**
 * A Fragment for the {@link SignUpActivity}.The job in this fragment is to ask the user to create a
 * password for his/her ID for donate life.the {@link #rootView} contains the UI for the fragment.All the
 * child of the parent view contain in the {@link MainViewHolder}.
 * For creating this fragment using the code snippet given bellow is totally forbidden.
 * <pre class="prettyprint">
 * PasswordVerificationFragment passwordVerificationFragment= new PasswordVerificationFragment();
 * </pre>
 * Instead of this the code snippet below must be followed.
 * <pre class="prettyprint">
 * Bundle bundle= getBundle();
 * PasswordVerificationFragment passwordVerificationFragment= PasswordVerificationFragment.newInstance(bundle);
 * </pre>
 */
public class PasswordVerificationFragment extends Fragment {
    /**
     * Defines a tag for identifying log entries
     */
    private static String TAG = PasswordVerificationFragment.class.getSimpleName();
    /**
     * A {@link View} for the whole fragment view.
     */
    private View rootView;

    private MainViewHolder mainViewHolder;

    private DialogBuilder dialogBuilder;

    public static Fragment newInstance(Bundle bundle) {
        PasswordVerificationFragment fragment = new PasswordVerificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogBuilder = new DialogBuilder(getActivity(), TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_password_verification, container, false);
            mainViewHolder = new MainViewHolder();
            mainViewHolder.passwordVerificationButton = (CustomButton) rootView.findViewById(R.id.password_verification_button);
            mainViewHolder.passwordEditText = (CustomEditText) rootView.findViewById(R.id.password_edit_text);
            mainViewHolder.confirmPasswordEditText = (CustomEditText) rootView.findViewById(R.id.confirm_password_edit_text);
            rootView.setTag(mainViewHolder);
        } else {
            mainViewHolder = (MainViewHolder) rootView.getTag();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewHolder.passwordVerificationButton.setOnClickListener(new SignUpButtonListener());
    }

    /**
     * This method is for checking the password whether it has an English Upper Case Alphabet.
     *
     * @param keyWord The password string.Which is created by the user.
     * @return true the keyWord contains at least one upper case letter.
     */
    private boolean hasUpperCase(String keyWord) {
        boolean flag = false;
        for (char k : keyWord.toCharArray()) {
            if ((k >= 'A' && k <= 'Z')) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * This method is for checking the password whether it has an English Lower Case Alphabet.
     *
     * @param keyWord The password string.Which is created by the user.
     * @return true the keyWord contains at least one lower case letter.
     */
    private boolean hasLowerCase(String keyWord) {
        boolean flag = false;
        for (char k : keyWord.toCharArray()) {
            if ((k >= 'a' && k <= 'z')) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * This method is for checking the password whether it has an English Numerical digit.
     *
     * @param keyWord The password string.Which is created by the user.
     * @return true the keyWord contains at least one numerical digit.
     */
    private boolean hasNumber(String keyWord) {
        boolean flag = false;
        for (char k : keyWord.toCharArray()) {
            if ((k >= '0' && k <= '9')) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     *
     */
    private void goToNextFragment() {
        Bundle bundle = PasswordVerificationFragment.this.getArguments();
        bundle.putString("password", mainViewHolder.passwordEditText.getText().toString());
        SignUpActivity signUpActivity = (SignUpActivity) getActivity();
        signUpActivity.changeFragment(bundle, 2);
    }

    private static class MainViewHolder {
        /**
         * A EditTextField{@link CustomEditText} for the password.
         */
        private CustomEditText passwordEditText;
        /**
         * A EditTextField confirming the password.
         */
        private CustomEditText confirmPasswordEditText;
        /**
         * A Button {@link CustomButton} for the un registered user to verify the password.
         */
        private CustomButton passwordVerificationButton;
    }

    private class SignUpButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            final String passwordString = mainViewHolder.passwordEditText.getText().toString();
            final String confirmPasswordString = mainViewHolder.confirmPasswordEditText.getText().toString();
            if (passwordString.length() == 0) {
                dialogBuilder.createAlertDialog(getString(R.string.warning_confirm_password));
            } else if (confirmPasswordString.length() == 0) {
                dialogBuilder.createAlertDialog(getString(R.string.warning_enter_a_password));
            } else if (!passwordString.equals(confirmPasswordString)) {
                dialogBuilder.createAlertDialog(getString(R.string.warning_password_not_matched));
            } else if (passwordString.length() < 6) {
                dialogBuilder.createAlertDialog(getString(R.string.warning_password_not_matched));
            } else if (!hasUpperCase(passwordString)) {
                dialogBuilder.createAlertDialog(getString(R.string.warning_no_uppercase));
            } else if (!hasLowerCase(passwordString)) {
                dialogBuilder.createAlertDialog(getString(R.string.warning_no_lowercase));
            } else if (!hasNumber(passwordString)) {
                dialogBuilder.createAlertDialog(getString(R.string.warning_no_number_digit));
            } else {
                goToNextFragment();
            }
        }
    }
}
