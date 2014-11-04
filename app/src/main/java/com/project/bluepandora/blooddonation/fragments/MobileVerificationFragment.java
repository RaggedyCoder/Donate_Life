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
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.project.bluepandora.blooddonation.activities.SignUpActivity;
import com.project.bluepandora.blooddonation.adapter.CountryListAdapter;
import com.project.bluepandora.blooddonation.application.AppController;
import com.project.bluepandora.blooddonation.helpers.URL;
import com.project.bluepandora.blooddonation.volley.CustomRequest;
import com.project.bluepandora.donatelife.R;
import com.widget.CustomButton;
import com.widget.CustomEditText;
import com.widget.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p/>
 * This fragment is for the user to veryfing the mobile number.
 * <p/>
 */
public class MobileVerificationFragment extends Fragment {

    /**
     * Defines a tag for identifying log entries
     */
    private static final String TAG = MobileVerificationFragment.class.getSimpleName();
    /**
     * A {@link android.widget.Spinner} for showing the country's list
     */
    private Spinner countryNameSpinner;
    /**
     * A Custom BaseAdapter{@link com.project.bluepandora.blooddonation.adapter.CountryListAdapter} for the countryNameSpinner
     */
    private CountryListAdapter countryListAdapter;

    /**
     * A TextField{@link com.widget.CustomTextView} for showing the country code of the user
     */
    private CustomTextView countryCode;
    /**
     * A EditTextField{@link com.widget.CustomEditText} for the registered user to enter their mobile number.
     */
    private CustomEditText mobileNumber;
    /**
     * An {@link java.util.ArrayList} for storing the country codes only for this Fragment.
     */
    private ArrayList<String> countryCodes;

    /**
     * An {@link ArrayList} for storing the Name of the Country only for this Fragment.
     */
    private ArrayList<String> categories;
    /**
     * A Button {@link com.widget.CustomButton} for the un registered user to sign up.
     */
    private CustomButton verificationButton;
    /**
     * A {@link android.app.ProgressDialog} for showing the user background work is going on.
     */
    private ProgressDialog pd;
    /**
     * An item selection listener for the countryNameSpinner.
     */
    private AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    /**
     * A {@link TextWatcher} for the mobileNumber EditTextField.
     */
    private TextWatcher mobileTextWatcher;
    /**
     * A click detection listener for the SignUp Button.
     */
    private OnClickListener mVerificationListener;
    /**
     * A {@link View} for the whole fragment view.
     */
    private View rootView;

    public MobileVerificationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countryCodes = new ArrayList<String>();
        categories = new ArrayList<String>();
        countryListAdapter = new CountryListAdapter(getActivity(), categories);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_mobileverification, container, false);
        countryNameSpinner = (Spinner) rootView.findViewById(R.id.country_spinner);
        mobileNumber = (CustomEditText) rootView.findViewById(R.id.signup_phone);
        countryCode = (CustomTextView) rootView.findViewById(R.id.country_code);
        verificationButton = (CustomButton) rootView.findViewById(R.id.mobile_verification);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity) getActivity()).getSupportActionBar().hide();

        categories.add("Select a Country");
        categories.add("Bangladesh");

        countryCodes.add("");
        countryCodes.add("880");

        countryNameSpinner.setAdapter(countryListAdapter);
        countryListAdapter.notifyDataSetChanged();

        mobileNumber.addTextChangedListener(mobileTextWatcher);
        countryNameSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        verificationButton.setOnClickListener(mVerificationListener);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mobileTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && (s.charAt(0) != '1')) {
                    mobileNumber.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryCode.setText(countryCodes.get(position));
                countryCode.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        mVerificationListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileNumber.getText().length() == 0) {
                    createAlertDialog(getActivity().getResources().getString(R.string.Warning_no_number));
                    return;
                } else if (mobileNumber.getText().length() < 10) {
                    createAlertDialog(getActivity().getResources().getString(R.string.Warning_number_short));
                    return;
                }
                createProgressDialog();
                /**
                 * Tags and Value for Registration Request
                 * static final TAG->requestName.  static final Value->isRegistered
                 * static final TAG->mobileNumber. Value will be determined by the user.
                 */
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(URL.REQUEST_NAME, URL.REGISTER_CHECK);
                params.put(URL.MOBILE_TAG, "0" + mobileNumber.getText().toString());
                getJsonData(params);
            }
        };
    }

    private void createProgressDialog() {
        pd = new ProgressDialog(MobileVerificationFragment.this.getActivity());
        pd.setMessage(getActivity().getResources().getString(R.string.loading));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
    }

    private void createAlertDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setNeutralButton("Ok", null);
        alertDialog.show();
    }
    private void getJsonData(final HashMap<String, String> params) {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, URL.URL, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(TAG, "Response: " + response.toString());
                        pd.dismiss();
                        try {
                            if (response.getBoolean("reg")) {
                                createAlertDialog("This Mobile is already Registered");
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("mobileNumber", 0 + mobileNumber.getText().toString());
                                SignUpActivity signUpActivity = (SignUpActivity) getActivity();
                                signUpActivity.changeFragment(bundle, 1);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                        Toast.makeText(MobileVerificationFragment.this.getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());

                pd.dismiss();
                Toast.makeText(
                        MobileVerificationFragment.this.getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }
}


