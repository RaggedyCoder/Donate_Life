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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.MainActivity;
import com.project.bluepandora.donatelife.activities.SignUpActivity;
import com.project.bluepandora.donatelife.adapter.CountryListAdapter;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.helpers.DialogBuilder;
import com.project.bluepandora.donatelife.helpers.ParamsBuilder;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.jsonperser.JSONParser;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.widget.CustomButton;
import com.widget.CustomEditText;
import com.widget.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p/>
 * This fragment is for the user to log in to the Main Activity.
 * Which is for the blood feed,request profile and other main operation.
 * <p/>
 */
public class LogInFragment extends Fragment implements URL {

    // Defines a tag for identifying log entries
    private final String TAG = LogInFragment.class.getSimpleName();
    /**
     * A {@link View} for the whole fragment view.
     */
    private View rootView;

    /**
     * A Custom BaseAdapter{@link com.project.bluepandora.donatelife.adapter.CountryListAdapter}
     * for the countryNameSpinner
     */
    private CountryListAdapter countryListAdapter;
    /**
     * An {@link ArrayList} for storing the country codes only for this Fragment.
     */
    private ArrayList<String> countryCodes;

    /**
     * An {@link ArrayList} for storing the Name of the Country only for this Fragment.
     */
    private ArrayList<String> categories;
    /**
     * A {@link HashMap} for the post request parameter.
     */
    private HashMap<String, String> params = new HashMap<String, String>();
    /**
     * A {@link com.project.bluepandora.donatelife.jsonperser.JSONParser} for parsing the data.
     */
    private JSONParser parse;
    /**
     * A {@link TextWatcher} for the mobileNumber EditTextField.
     */
    private TextWatcher mobileTextWatcher;
    /**
     * An item selection listener for the countryNameSpinner.
     */
    private OnItemSelectedListener mOnItemSelectedListener;
    /**
     * A click detection listener for the SignIn Button.
     */
    private OnClickListener mSignInListener;

    /**
     * A click detection listener for the SignUp Button.
     */
    private OnClickListener mSignUpListener;

    /**
     * A flag to detect whether to go to sign up or not
     */
    private boolean signUp = false;

    private DialogBuilder dialogBuilder;

    private MainViewHolder mainViewHolder;

    /**
     * A CustomRequest{@link CustomRequest} for the server.This is for sending the user log in or the
     * sign up request to the server.If the server receive the request.It will send a JSONObject.
     */
    private CustomRequest serverRequest;
    /**
     * An ErrorListener{@link com.android.volley.Response.ErrorListener} for the serverRequest.
     * If there is any problem making a request to the server or during of it, mErrorListener
     * will receive the error message.
     */
    private Response.ErrorListener mErrorListener;

    /**
     * A Listener{@link Response.Listener<JSONObject>} for the serverRequest.
     * After making a successful request to the server,server will send a {@link JSONObject}.
     * mJsonObjectListener will receive that JSONObject.
     */
    private Response.Listener<JSONObject> mJsonObjectListener;


    /**
     * Fragments require an empty constructor.
     */
    public LogInFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parse = new JSONParser(getActivity(), TAG);
        countryCodes = new ArrayList<String>();
        categories = new ArrayList<String>();
        countryListAdapter = new CountryListAdapter(getActivity(), categories);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_signin, container, false);
            mainViewHolder = new MainViewHolder();
            mainViewHolder.mobileNumberEditText = (CustomEditText) rootView.findViewById(R.id.mobile_number_edit_text);
            mainViewHolder.passwordEditText = (CustomEditText) rootView.findViewById(R.id.password_edit_text);
            mainViewHolder.logInButton = (CustomButton) rootView.findViewById(R.id.log_in_button);
            mainViewHolder.signUpButton = (CustomButton) rootView.findViewById(R.id.sign_up_button);
            mainViewHolder.countryNameSpinner = (Spinner) rootView.findViewById(R.id.country_spinner);
            mainViewHolder.countryCodeTextView = (CustomTextView) rootView.findViewById(R.id.country_code_text_view);
            rootView.setTag(mainViewHolder);
        } else {
            mainViewHolder = (MainViewHolder) rootView.getTag();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        categories.add("Select a Country");
        categories.add("Bangladesh");
        countryCodes.add("");
        countryCodes.add("880");
        mainViewHolder.countryNameSpinner.setAdapter(countryListAdapter);
        countryListAdapter.notifyDataSetChanged();
        dialogBuilder = new DialogBuilder(getActivity(), TAG);
        ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
        mainViewHolder.mobileNumberEditText.addTextChangedListener(mobileTextWatcher);
        mainViewHolder.countryNameSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        mainViewHolder.logInButton.setOnClickListener(mSignInListener);
        mainViewHolder.signUpButton.setOnClickListener(mSignUpListener);
        serverRequest = new CustomRequest(Request.Method.POST, URL, mJsonObjectListener, mErrorListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mJsonObjectListener = new Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Response: " + response.toString());
                String requestName;
                try {
                    requestName = response.getString("requestName");
                } catch (JSONException e) {
                    requestName = "";
                    e.printStackTrace();
                }
                if (requestName.equals(BLOODLIST_PARAM)) {
                    parse.parseJsonBlood(response);
                    params = ParamsBuilder.districtList();
                    serverRequest.setParams(params);
                    AppController.getInstance().addToRequestQueue(serverRequest);
                } else if (requestName.equals(DISTRICTLIST_PARAM)) {
                    parse.parseJsonDistrict(response);
                    params = ParamsBuilder.hospitalList();
                    serverRequest.setParams(params);
                    AppController.getInstance().addToRequestQueue(serverRequest);
                } else if (requestName.equals(HOSPITALLIST_PARAM)) {
                    parse.parseJsonHospital(response);
                    if (signUp) {
                        dialogBuilder.getProgressDialog().dismiss();
                        Intent signUpIntent = new Intent(getActivity(), SignUpActivity.class);
                        startActivity(signUpIntent);
                        signUp = false;
                    } else {
                        params = ParamsBuilder.donationRecord(
                                "0" + mainViewHolder.mobileNumberEditText.getText().toString());
                        serverRequest.setParams(params);
                        AppController.getInstance().addToRequestQueue(serverRequest);
                    }
                } else if (requestName.equals(GET_DONATION_RECORD_PARAM)) {
                    dialogBuilder.getProgressDialog().dismiss();
                    if (parse.parseDonationInfo(response)) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    } else {
                        dialogBuilder.createProgressDialog(getString(R.string.unknown_server_error));
                    }
                } else if (requestName.equals(USER_INFO)) {
                    if (parse.parseUserInfo(response,
                            "0" + mainViewHolder.mobileNumberEditText.getText().toString(),
                            mainViewHolder.passwordEditText.getText().toString())) {
                        params = ParamsBuilder.bloodGroupList();
                        serverRequest.setParams(params);
                        AppController.getInstance().addToRequestQueue(serverRequest);
                    } else {
                        dialogBuilder.getProgressDialog().dismiss();
                        dialogBuilder.createAlertDialog(getString(R.string.invalid_username_password));
                    }
                } else if (requestName.equals(REGISTER_CHECK)) {
                    dialogBuilder.getProgressDialog().dismiss();
                    if (parse.parseRegistrationCheck(response)) {
                        dialogBuilder.createAlertDialog(getString(R.string.already_registered));
                    } else {
                        dialogBuilder.createAlertDialog(getString(R.string.not_registered));
                    }
                } else {
                    dialogBuilder.getProgressDialog().dismiss();
                    dialogBuilder.createProgressDialog(getString(R.string.unknown_server_error));
                }
            }
        };
        mErrorListener = new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                dialogBuilder.getProgressDialog().dismiss();
                Toast.makeText(
                        LogInFragment.this.getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        mobileTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && (s.charAt(0) != '1')) {
                    mainViewHolder.mobileNumberEditText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mOnItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainViewHolder.countryCodeTextView.setText(countryCodes.get(position));
                mainViewHolder.countryCodeTextView.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        mSignInListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                mainViewHolder.mobileNumberEditText.clearFocus();
                mainViewHolder.passwordEditText.clearFocus();
                if (mainViewHolder.mobileNumberEditText.getText().length() == 0) {
                    dialogBuilder.createAlertDialog(getActivity().getResources().getString(R.string.Warning_no_number));
                    return;
                } else if (mainViewHolder.mobileNumberEditText.getText().length() < 10) {
                    dialogBuilder.createAlertDialog(getActivity().getResources().getString(R.string.Warning_number_short));
                    return;
                } else if (mainViewHolder.countryCodeTextView.getText().length() == 0) {
                    dialogBuilder.createAlertDialog(getActivity().getResources().getString(R.string.Warning_select_a_country));
                    return;
                }
                dialogBuilder.createProgressDialog(getActivity().getResources().getString(R.string.loading));
                if (mainViewHolder.mobileNumberEditText.getText().length() != 0 &&
                        mainViewHolder.passwordEditText.getText().length() == 0) {
                    params = ParamsBuilder.registerCheckRequest(
                            "0" + mainViewHolder.mobileNumberEditText.getText().toString());
                } else {
                    params = ParamsBuilder.userInfoRequest(
                            "0" + mainViewHolder.mobileNumberEditText.getText().toString(),
                            mainViewHolder.passwordEditText.getText().toString());
                }
                serverRequest.setParams(params);
                AppController.getInstance().addToRequestQueue(serverRequest);
            }
        };
        mSignUpListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.createProgressDialog(getActivity().getResources().getString(R.string.loading));
                signUp = true;
                ArrayList<Item> items = new ArrayList<Item>();
                BloodDataSource bloodDataSource = new BloodDataSource(getActivity());
                bloodDataSource.open();
                try {
                    items = bloodDataSource.getAllBloodItem();
                } catch (Exception e) {
                    Log.e(TAG, "Empty database");
                }
                if (items.size() == 0) {
                    params = ParamsBuilder.bloodGroupList();
                    serverRequest.setParams(params);
                    AppController.getInstance().addToRequestQueue(serverRequest);
                } else {
                    dialogBuilder.getProgressDialog().dismiss();
                    Intent signUpIntent = new Intent(getActivity(), SignUpActivity.class);
                    startActivity(signUpIntent);
                    signUp = false;
                }
            }
        };
    }

    private static class MainViewHolder {
        /**
         * A TextField{@link CustomTextView} for showing the country code of the user
         */
        private CustomTextView countryCodeTextView;
        /**
         * A {@link Spinner} for showing the country's list
         */
        private Spinner countryNameSpinner;
        /**
         * A Button{@link CustomButton} for sending the username and password to the server
         * to check the user is valid or not.
         */
        private CustomButton logInButton;
        /**
         * A Button {@link CustomButton} for the un registered user to sign up.
         */
        private CustomButton signUpButton;
        /**
         * A EditTextField{@link CustomEditText} for the registered user to enter their mobile number.
         */
        private CustomEditText mobileNumberEditText;
        /**
         * A EditTextField{@link CustomEditText} for the registered user to enter their password.
         */
        private CustomEditText passwordEditText;
    }
}
