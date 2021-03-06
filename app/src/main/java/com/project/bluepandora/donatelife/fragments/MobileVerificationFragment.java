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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.SignUpActivity;
import com.project.bluepandora.donatelife.adapter.CountryListAdapter;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.helpers.URL;
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
 * This fragment is for the user to verifying the mobile number.
 * <p/>
 */
public class MobileVerificationFragment extends Fragment implements URL {

    /**
     * Defines a tag for identifying log entries
     */
    private static final String TAG = MobileVerificationFragment.class.getSimpleName();
    /**
     * A {@link Spinner} for showing the country's list
     */
    private Spinner countryNameSpinner;
    /**
     * A Custom BaseAdapter{@link com.project.bluepandora.donatelife.adapter.CountryListAdapter} for the countryNameSpinner
     */
    private CountryListAdapter countryListAdapter;

    /**
     * A TextField{@link =CustomTextView} for showing the country code of the user
     */
    private CustomTextView countryCodeTextView;
    /**
     * A EditTextField{@link CustomEditText} for the registered user to enter their mobile number.
     */
    private CustomEditText mobileNumberEditText;
    /**
     * An {@link ArrayList} for storing the country codes only for this Fragment.
     */
    private ArrayList<String> countryCodeTextViews;

    /**
     * An {@link ArrayList} for storing the Name of the Country only for this Fragment.
     */
    private ArrayList<String> categories;
    /**
     * A Button {@link CustomButton} for the un registered user to sign up.
     */
    private CustomButton mobileVerificationButton;
    /**
     * A {@link ProgressDialog} for showing the user background work is going on.
     */
    private ProgressDialog pd;
    /**
     * An item selection listener for the countryNameSpinner.
     */
    private OnItemSelectedListener mOnItemSelectedListener;
    /**
     * A {@link TextWatcher} for the mobileNumberEditText EditTextField.
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
        countryCodeTextViews = new ArrayList<String>();
        categories = new ArrayList<String>();
        countryListAdapter = new CountryListAdapter(getActivity(), categories);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_mobile_verification, container, false);
        countryNameSpinner = (Spinner) rootView.findViewById(R.id.country_spinner);
        mobileNumberEditText = (CustomEditText) rootView.findViewById(R.id.mobile_number_edit_text);
        countryCodeTextView = (CustomTextView) rootView.findViewById(R.id.country_code_text_view);
        mobileVerificationButton = (CustomButton) rootView.findViewById(R.id.mobile_verification_button);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity) getActivity()).getSupportActionBar().hide();

        categories.add("Select a Country");
        categories.add("Bangladesh");

        countryCodeTextViews.add("");
        countryCodeTextViews.add("880");

        countryNameSpinner.setAdapter(countryListAdapter);
        countryListAdapter.notifyDataSetChanged();

        mobileNumberEditText.addTextChangedListener(mobileTextWatcher);
        countryNameSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        mobileVerificationButton.setOnClickListener(mVerificationListener);


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
                    mobileNumberEditText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryCodeTextView.setText(countryCodeTextViews.get(position));
                countryCodeTextView.setError(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        mVerificationListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobileNumberEditText.getText().length() == 0) {
                    createAlertDialog(getActivity().getResources().getString(R.string.warning_no_mobile_number));
                    return;
                } else if (mobileNumberEditText.getText().length() < 10) {
                    createAlertDialog(getActivity().getResources().getString(R.string.warning_number_short));
                    return;
                }
                createProgressDialog();
                /**
                 * Tags and Value for Registration Request
                 * static final TAG->requestName.  static final Value->isRegistered
                 * static final TAG->mobileNumberEditText. Value will be determined by the user.
                 */
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(REQUEST_NAME, REGISTER_CHECK);
                params.put(MOBILE_TAG, "0" + mobileNumberEditText.getText().toString());
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
        alertDialog.setTitle(getString(R.string.alert));
        alertDialog.setMessage(message);
        alertDialog.setNeutralButton(getString(R.string.ok), null);
        alertDialog.show();
    }

    private void getJsonData(final HashMap<String, String> params) {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(TAG, "Response: " + response.toString());
                        pd.dismiss();
                        try {
                            if (response.getInt("reg") == 1) {
                                createAlertDialog(getString(R.string.already_registered));
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("mobileNumber", 0 + mobileNumberEditText.getText().toString());
                                SignUpActivity signUpActivity = (SignUpActivity) getActivity();
                                signUpActivity.changeFragment(bundle, 1);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                createAlertDialog(getString(R.string.unknown_server_error));
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pd.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }
}


