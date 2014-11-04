package com.project.bluepandora.blooddonation.fragments;
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
import android.app.ProgressDialog;
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

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.project.bluepandora.blooddonation.activities.MainActivity;
import com.project.bluepandora.blooddonation.activities.SignUpActivity;
import com.project.bluepandora.blooddonation.adapter.CountryListAdapter;
import com.project.bluepandora.blooddonation.application.AppController;
import com.project.bluepandora.blooddonation.data.UserInfoItem;
import com.project.bluepandora.blooddonation.datasource.UserDataSource;
import com.project.bluepandora.blooddonation.helpers.URL;
import com.project.bluepandora.blooddonation.jsonperser.JSONParser;
import com.project.bluepandora.blooddonation.volley.CustomRequest;
import com.project.bluepandora.donatelife.R;
import com.widget.CustomButton;
import com.widget.CustomEditText;
import com.widget.CustomTextView;

import org.json.JSONArray;
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
public class LogInFragment extends Fragment {

    // Defines a tag for identifying log entries
    private final String TAG = LogInFragment.class.getSimpleName();
    /**
     * A {@link View} for the whole fragment view.
     */
    protected View rootView;
    /**
     * A TextField{@link CustomTextView} for showing the country code of the user
     */
    private CustomTextView contryCode;
    /**
     * A {@link Spinner} for showing the country's list
     */
    private Spinner countryNameSpinner;

    /**
     * A Custom BaseAdapter{@link CountryListAdapter} for the countryNameSpinner
     */
    private CountryListAdapter countryListAdapter;
    /**
     * A Button{@link CustomButton} for sending the username and password to the server
     * to check the user is valid or not.
     */
    private CustomButton signInButton;
    /**
     * A Button {@link CustomButton} for the un registered user to sign up.
     */
    private CustomButton signUpButton;
    /**
     * A {@link ProgressDialog} for showing the user background work is going on.
     */
    private ProgressDialog pd;
    /**
     * An {@link ArrayList} for storing the country codes only for this Fragment.
     */
    private ArrayList<String> countryCodes;

    /**
     * An {@link ArrayList} for storing the Name of the Country only for this Fragment.
     */
    private ArrayList<String> categories;
    /**
     * A EditTextField{@link CustomEditText} for the registered user to enter their mobile number.
     */
    private CustomEditText mobileNumber;
    /**
     * A EditTextField{@link CustomEditText} for the registered user to enter their password.
     */
    private CustomEditText password;
    /**
     * A {@link HashMap} for the post request parameter.
     */
    private HashMap<String, String> params = new HashMap<String, String>();
    /**
     * A {@link JSONParser} for parsing the data.
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
     * Fragments require an empty constructor.
     */
    public LogInFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parse = new JSONParser(getActivity());
        countryCodes = new ArrayList<String>();
        categories = new ArrayList<String>();
        countryListAdapter = new CountryListAdapter(getActivity(),
                categories);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.fragment_signin, container, false);
        mobileNumber = (CustomEditText) rootView.findViewById(R.id.registration_phone);
        password = (CustomEditText) rootView.findViewById(R.id.check_reg_pass);
        signInButton = (CustomButton) rootView.findViewById(R.id.registration_submit);
        signUpButton = (CustomButton) rootView.findViewById(R.id.signup);
        countryNameSpinner = (Spinner) rootView.findViewById(R.id.registration_country);
        contryCode = (CustomTextView) rootView.findViewById(R.id.registration_cc);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        categories.add("Select a Country");
        categories.add("Bangladesh");

        countryCodes.add("");
        countryCodes.add("880");

        countryNameSpinner.setAdapter(countryListAdapter);
        countryListAdapter.notifyDataSetChanged();

        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .hide();

        mobileNumber.addTextChangedListener(mobileTextWatcher);
        countryNameSpinner.setOnItemSelectedListener(mOnItemSelectedListener);

        signInButton.setOnClickListener(mSignInListener);
        signUpButton.setOnClickListener(mSignUpListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mobileTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count > 0 && (s.charAt(0) != '1')) {
                    mobileNumber.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mOnItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contryCode.setText(countryCodes.get(position));
                contryCode.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        mSignInListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                mobileNumber.clearFocus();
                password.clearFocus();
                if (mobileNumber.getText().length() == 0) {
                    createAlertDialog(getActivity().getResources().getString(R.string.Warning_no_number));
                    return;
                } else if (mobileNumber.getText().length() < 10) {
                    createAlertDialog(getActivity().getResources().getString(R.string.Warning_number_short));
                    return;
                }
                if (contryCode.getText().length() == 0) {
                    createAlertDialog(getActivity().getResources().getString(R.string.Warning_select_a_country));
                    return;
                }
                createProgressDialog();
                params = new HashMap<String, String>();
                params.put(URL.REQUEST_NAME, URL.BLOODLIST_PARAM);
                getJsonData(params);
                params = new HashMap<String, String>();
                params.put(URL.REQUEST_NAME, URL.DISTRICTLIST_PARAM);
                getJsonData(params);
                params = new HashMap<String, String>();
                params.put(URL.REQUEST_NAME, URL.HOSPITALLIST_PARAM);
                getJsonData(params);
                params = new HashMap<String, String>();
                params.put(URL.REQUEST_NAME, URL.REGISTER_CHECK);
                params.put(URL.MOBILE_TAG, "0" + mobileNumber.getText());
                getJsonData(params);
            }
        };
        mSignUpListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(signUpIntent);
            }
        };
    }

    private void createProgressDialog() {
        pd = new ProgressDialog(LogInFragment.this
                .getActivity());
        pd.setMessage(getActivity().getResources().getString(
                R.string.loading));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
    }

    private void createAlertDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.show();
    }

    private void parseJsonregdata(JSONObject response) {
        try {
            boolean data = response.getBoolean("reg");
            if (data) {
                if (password.getText().length() == 0) {
                    password.setError(getActivity().getResources().getString(
                            R.string.Warning_enter_a_password));
                    pd.dismiss();
                    return;
                }
                params = new HashMap<String, String>();
                params.put(URL.REQUEST_NAME, URL.USER_INFO);
                params.put(URL.MOBILE_TAG, "" + 0 + mobileNumber.getText());
                params.put(URL.PASSWORD_TAG, "" + password.getText());
                getJsonData(params);

            } else {
                if (password.getText().length() != 0) {
                    password.setError(getActivity().getResources().getString(
                            R.string.Warning_remove_the_password));
                    pd.dismiss();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJsonUserInfo(JSONObject response) {

        int data;
        try {
            data = response.getInt("done");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        if (data == 0) {
            pd.dismiss();
        } else if (data == 1) {
            UserDataSource userDataBase = new UserDataSource(getActivity());
            userDataBase.open();
            UserInfoItem item = new UserInfoItem();
            JSONArray feedArray = null;
            try {
                feedArray = response.getJSONArray("profile");
            } catch (JSONException e) {

                e.printStackTrace();
            }
            if (feedArray != null) {
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject temp = null;
                    try {
                        temp = (JSONObject) feedArray.get(i);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                    if (temp != null) {
                        try {
                            item.setFirstName(temp.getString("firstName"));
                            item.setLastName(temp.getString("lastName"));
                            item.setKeyWord("" + password.getText());
                            item.setMobileNumber("" + 0
                                    + mobileNumber.getText());
                            item.setGroupId(Integer.parseInt(temp
                                    .getString("groupId")));
                            item.setDistId(Integer.parseInt(temp
                                    .getString("distId")));
                            pd.dismiss();
                            try {
                                userDataBase.createUserInfoItem(item);
                                userDataBase.close();
                                Intent intent = new Intent(getActivity(),
                                        MainActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
            }

        }
    }

    private void getJsonData(final HashMap<String, String> params) {

        CustomRequest jsonReq = new CustomRequest(Method.POST, URL.URL, params,
                new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        VolleyLog.d(TAG, "Response: " + response.toString());


                        if (params.containsValue(URL.BLOODLIST_PARAM)) {
                            parse.parseJsonBlood(response);
                        } else if (params
                                .containsValue(URL.DISTRICTLIST_PARAM)) {
                            parse.parseJsonDistrict(response);
                        } else if (params
                                .containsValue(URL.HOSPITALLIST_PARAM)) {
                            parse.parseJsonHospital(response);
                        } else if (params.containsValue(""
                                + password.getText())
                                && params.containsValue("" + 0
                                + mobileNumber.getText())) {
                            Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                            parseJsonUserInfo(response);
                        } else if (params.containsValue("" + 0
                                + mobileNumber.getText())) {
                            parseJsonregdata(response);
                        }
                    }

                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pd.dismiss();
                Toast.makeText(
                        LogInFragment.this.getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

}
