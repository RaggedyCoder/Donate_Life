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
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.adapter.SpinnerAdapter;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.helpers.DialogBuilder;
import com.project.bluepandora.donatelife.helpers.ParamsBuilder;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.widget.CustomButton;
import com.widget.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RegistrationCompleteFragment extends Fragment implements URL {

    /**
     * Defines a tag for identifying log entries
     */
    private static String TAG = RegistrationCompleteFragment.class.getSimpleName();
    /**
     * A {@link View} for the whole fragment view.
     */
    protected View rootView;
    /**
     * A database{@link ArrayList} for retrieve the stored district list.
     */
    DistrictDataSource districtDatabase;
    /**
     * A database{@link ArrayList} for retrieve the stored blood group list.
     */
    BloodDataSource bloodDatabase;
    /**
     * A Custom BaseAdapter{@link SpinnerAdapter} for the bloodGroupSpinner.
     */

    private SpinnerAdapter bloodAdapter;

    /**
     * A Custom BaseAdapter{@link SpinnerAdapter} for the districtSpinner.
     */

    private SpinnerAdapter districtAdapter;

    /**
     * An {@link ArrayList} for storing the Name of the District only for this Fragment.
     */
    private ArrayList<Item> distItems;
    /**
     * An {@link ArrayList} for storing the Name of the Blood only for this Fragment.
     */
    private ArrayList<Item> bloodItems;

    /**
     * An item selection listener for the bloodGroupSpinner.
     */
    private OnItemSelectedListener mbloodGroupSpinnerItemSelectedListener;
    /**
     * An item selection listener for the districtSpinner.
     */
    private OnItemSelectedListener mDistrictItemSelectedListener;
    /**
     * A click detection listener for the Registration Button.
     */
    private View.OnClickListener mRegistrationListener;
    private DialogInterface.OnClickListener mOnClickListener;

    private DialogBuilder dialogBuilder;

    private MainViewHolder mainViewHolder;

    private CustomRequest regCompleteRequest;
    /**
     * An ErrorListener{@link Response.ErrorListener} for the regCompleteRequest.
     * If there is any problem making a request to the server or during of it, mErrorListener
     * will receive the error message.
     */
    private Response.ErrorListener mErrorListener;

    /**
     * A Listener{@link Response.Listener<JSONObject>} for the regCompleteRequest.
     * After making a successful request to the server,server will send a {@link JSONObject}.
     * mJsonObjectListener will receive that JSONObject.
     */
    private Response.Listener<JSONObject> mJsonObjectListener;

    private HashMap<String, String> params;

    public RegistrationCompleteFragment() {

    }

    public static Fragment newInstance(Bundle bundle) {
        RegistrationCompleteFragment fragment = new RegistrationCompleteFragment();
        fragment.setArguments(bundle);
        Log.i(TAG, "New instance created.");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate method called.");
        bloodDatabase = new BloodDataSource(getActivity());
        bloodDatabase.open();
        bloodItems = bloodDatabase.getAllBloodItem();
        bloodDatabase.close();

        districtDatabase = new DistrictDataSource(getActivity());
        districtDatabase.open();
        distItems = districtDatabase.getAllDistrictItem();
        districtDatabase.close();

        bloodAdapter = new SpinnerAdapter(getActivity(), bloodItems);
        districtAdapter = new SpinnerAdapter(getActivity(), distItems);
        dialogBuilder = new DialogBuilder(getActivity(), TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i(TAG, "onCreateView method called.");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_registrationcomplete, container, false);
            mainViewHolder = new MainViewHolder();
            mainViewHolder.bloodGroupSpinner = (Spinner) rootView.findViewById(R.id.blood_group_spinner);
            mainViewHolder.districtSpinner = (Spinner) rootView.findViewById(R.id.district_spinner);
            mainViewHolder.registrationButton = (CustomButton) rootView.findViewById(R.id.registration_button);
            mainViewHolder.avatar = (CustomTextView) rootView.findViewById(R.id.avatar);

            rootView.setTag(mainViewHolder);
        } else {
            mainViewHolder = (MainViewHolder) rootView.getTag();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated method called.");
        mainViewHolder.bloodGroupSpinner.setAdapter(bloodAdapter);
        mainViewHolder.districtSpinner.setAdapter(districtAdapter);
        mainViewHolder.bloodGroupSpinner.setOnItemSelectedListener(mbloodGroupSpinnerItemSelectedListener);
        mainViewHolder.districtSpinner.setOnItemSelectedListener(mDistrictItemSelectedListener);
        mainViewHolder.registrationButton.setOnClickListener(mRegistrationListener);
        regCompleteRequest = new CustomRequest(Request.Method.POST, URL, params, mJsonObjectListener, mErrorListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach method called.");
        mbloodGroupSpinnerItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BloodItem item = (BloodItem) bloodItems.get(mainViewHolder.bloodGroupSpinner.getSelectedItemPosition());
                mainViewHolder.avatar.setText(item.getBloodName());
                if (mainViewHolder.avatar.getText().toString().length() > 2) {
                    mainViewHolder.avatar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
                } else {
                    mainViewHolder.avatar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        mOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        };
        mDistrictItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        mRegistrationListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DistrictItem districtItem = ((DistrictItem) distItems.get(mainViewHolder.districtSpinner.getSelectedItemPosition()));
                String distId = Integer.toString(districtItem.getDistId());
                BloodItem bloodItem = ((BloodItem) bloodItems.get(mainViewHolder.bloodGroupSpinner.getSelectedItemPosition()));
                String groupId = Integer.toString(bloodItem.getBloodId());

                params = ParamsBuilder.registerRequest(
                        getArguments().getString("firstName"),
                        getArguments().getString("lastName"),
                        distId,
                        groupId,
                        getArguments().getString("mobileNumber"),
                        getArguments().getString("password"));
                regCompleteRequest.setParams(params);
                dialogBuilder.createProgressDialog(getActivity().getResources().getString(R.string.loading));
                AppController.getInstance().addToRequestQueue(regCompleteRequest);
            }
        };
        mJsonObjectListener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                dialogBuilder.getProgressDialog().dismiss();
                try {
                    if (response.getInt("done") == 1) {
                        dialogBuilder.createAlertDialog(null, getString(R.string.done), mOnClickListener);

                    } else {
                        dialogBuilder.createAlertDialog(getString(R.string.unknown_server_error));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

        };
        mErrorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.e(TAG, "Error: " + error.getMessage());

                dialogBuilder.getProgressDialog().dismiss();
                Toast.makeText(
                        RegistrationCompleteFragment.this.getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        };
    }

    private static class MainViewHolder {
        /**
         * A {@link Spinner} for showing the district's list
         */
        private Spinner districtSpinner;
        /**
         * A {@link android.widget.Spinner} for showing the blood group's list
         */
        private Spinner bloodGroupSpinner;
        /**
         * A {@link CustomButton} for the Registration
         */
        private CustomButton registrationButton;
        /**
         * A TextField{@link CustomTextView} for showing the blood group of the registering user.
         */
        private CustomTextView avatar;
    }
}
