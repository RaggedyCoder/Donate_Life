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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.project.bluepandora.blooddonation.adapter.BloodSpinnerAdapter;
import com.project.bluepandora.blooddonation.adapter.DistrictSpinnerAdapter;
import com.project.bluepandora.blooddonation.application.AppController;
import com.project.bluepandora.blooddonation.data.BloodItem;
import com.project.bluepandora.blooddonation.data.DistrictItem;
import com.project.bluepandora.blooddonation.datasource.BloodDataSource;
import com.project.bluepandora.blooddonation.datasource.DistrictDataSource;
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

public class RegistrationStepFragment extends Fragment {

    private static final String TAG = RegistrationStepFragment.class.getSimpleName();
    private Spinner spinnerBlood;
    private Spinner spinnerDistrict;
    private ArrayList<DistrictItem> dist;
    private String mobileNumber;
    private Button avatar;
    private CustomTextView mobileNumberView;
    private BloodSpinnerAdapter bloodAdapter;
    private DistrictSpinnerAdapter districtAdapter;
    private BloodDataSource bloodDatabase;
    private DistrictDataSource distDatabase;
    private ArrayList<BloodItem> bitems;
    private ArrayList<DistrictItem> ditems;
    private CustomButton submitButton;

    private CustomEditText firstName;
    private CustomEditText lastName;
    private CustomEditText password;

    public RegistrationStepFragment() {

    }

    public RegistrationStepFragment(String mobileNumber) {
        super();
        this.mobileNumber = mobileNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_registration_name,
                container, false);
        mobileNumberView = (CustomTextView) rootView
                .findViewById(R.id.mobile_number_reg);
        mobileNumberView.setText("+880" + mobileNumber);
        spinnerBlood = (Spinner) rootView.findViewById(R.id.bloodGroupSpinner);
        spinnerDistrict = (Spinner) rootView.findViewById(R.id.district);
        // getJSONOBJsonObject(params);
        bloodDatabase = new BloodDataSource(getActivity());
        bloodDatabase.open();
        bitems = bloodDatabase.getAllBloodItem();
        bloodDatabase.close();
        distDatabase = new DistrictDataSource(getActivity());
        distDatabase.open();
        ditems = distDatabase.getAllDistrictItem();
        distDatabase.close();
        bloodAdapter = new BloodSpinnerAdapter(getActivity(), bitems);
        districtAdapter = new DistrictSpinnerAdapter(getActivity(), ditems);
        spinnerBlood.setAdapter(bloodAdapter);
        spinnerDistrict.setAdapter(districtAdapter);
        avatar = (Button) rootView.findViewById(R.id.avatar_btn);
        submitButton = (CustomButton) rootView
                .findViewById(R.id.registration_submit);
        firstName = (CustomEditText) rootView
                .findViewById(R.id.registration_first_name);
        lastName = (CustomEditText) rootView
                .findViewById(R.id.registration_last_name);
        password = (CustomEditText) rootView
                .findViewById(R.id.registration_password);

        submitButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /**
                 * Tags and Value for Registration Request
                 * static final TAG->requestName.  static final Value->register
                 * static final TAG->firstName.    Value will be determined by the user.
                 * static final TAG->lastName.     Value will be determined by the user.
                 * static final TAG->groupId.      Value will be determined by the user.
                 * static final TAG->mobileNumber. Value will be determined by the user.
                 * static final TAG->keyWord.      Value will be determined by the user
                 */

                HashMap<String, String> params = new HashMap<String, String>();
                params.put(URL.REQUEST_NAME, URL.REGISTERREQUEST_PARAM);
                params.put(URL.FIRST_NAME_KEY, firstName.getText().toString());
                params.put(URL.LAST_NAME_KEY, lastName.getText().toString());
                params.put(URL.DISTRICTID_TAG,
                        spinnerDistrict.getSelectedItemId() + "");
                params.put(URL.GROUPID_TAG, spinnerBlood.getSelectedItemId()
                        + "");
                params.put(URL.MOBILE_TAG, "0" + mobileNumber);
                params.put(URL.PASSWORD_TAG, password.getText().toString());
                getRequest(params);
            }
        });
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinnerBlood.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                avatar.setText(bitems.get(position).getBloodName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getRequest(HashMap<String, String> params) {
        CustomRequest jsonReq = new CustomRequest(Method.POST, URL.URL, params,
                new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int done = response.getInt("done");
                            if (done == 1) {
                                Toast.makeText(getActivity(),
                                        "Registration Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Something went wrong.Please Try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

}
