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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
     * A {@link Spinner} for showing the district's list
     */
    private Spinner districtSpinner;
    /**
     * A {@link android.widget.Spinner} for showing the blood group's list
     */
    private Spinner bloodSpinner;

    /**
     * A Custom BaseAdapter{@link SpinnerAdapter} for the bloodSpinner.
     */

    private SpinnerAdapter bloodAdapter;

    /**
     * A Custom BaseAdapter{@link SpinnerAdapter} for the districtSpinner.
     */

    private SpinnerAdapter districtAdapter;

    /**
     * A {@link CustomButton} for the Registration
     */
    private CustomButton registrationButton;
    /**
     * An {@link ArrayList} for storing the Name of the District only for this Fragment.
     */
    private ArrayList<Item> distItems;
    /**
     * An {@link ArrayList} for storing the Name of the Blood only for this Fragment.
     */
    private ArrayList<Item> bloodItems;

    /**
     * A database{@link ArrayList} for retrieve the stored district list.
     */
    private DistrictDataSource districtDatabase;

    /**
     * A database{@link ArrayList} for retrieve the stored blood group list.
     */
    private BloodDataSource bloodDatabase;

    /**
     * A TextField{@link CustomTextView} for showing the blood group of the registering user.
     */
    private CustomTextView avatar;

    /**
     * An item selection listener for the bloodSpinner.
     */
    private OnItemSelectedListener mbloodSpinnerItemSelectedListener;
    /**
     * An item selection listener for the districtSpinner.
     */
    private OnItemSelectedListener mDistrictItemSelectedListener;
    /**
     * A click detection listener for the Registration Button.
     */
    private View.OnClickListener mRegistrationListener;
    /**
     * A {@link android.app.ProgressDialog} for showing the user background work is going on.
     */
    private ProgressDialog pd;

    private DialogInterface.OnClickListener mOnClickListener;

    private DialogBuilder dialogBuilder;

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
        districtDatabase = new DistrictDataSource(getActivity());
        bloodDatabase = new BloodDataSource(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i(TAG, "onCreateView method called.");
        rootView = inflater.inflate(R.layout.fragment_registrationcomplete, container, false);
        bloodSpinner = (Spinner) rootView.findViewById(R.id.blood_group_spinner);
        districtSpinner = (Spinner) rootView.findViewById(R.id.district_spinner);
        registrationButton = (CustomButton) rootView.findViewById(R.id.registration);
        avatar = (CustomTextView) rootView.findViewById(R.id.avatar);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated method called.");
        bloodDatabase.open();
        districtDatabase.open();
        bloodItems = bloodDatabase.getAllBloodItem();
        distItems = districtDatabase.getAllDistrictItem();
        bloodDatabase.close();
        districtDatabase.close();
        bloodAdapter = new SpinnerAdapter(getActivity(), bloodItems);
        districtAdapter = new SpinnerAdapter(getActivity(), distItems);
        dialogBuilder = new DialogBuilder(getActivity(), TAG);
        bloodSpinner.setAdapter(bloodAdapter);
        districtSpinner.setAdapter(districtAdapter);
        bloodAdapter.notifyDataSetChanged();
        districtAdapter.notifyDataSetChanged();
        bloodSpinner.setOnItemSelectedListener(mbloodSpinnerItemSelectedListener);
        districtSpinner.setOnItemSelectedListener(mDistrictItemSelectedListener);
        registrationButton.setOnClickListener(mRegistrationListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach method called.");
        mbloodSpinnerItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BloodItem item = (BloodItem) bloodItems.get(bloodSpinner.getSelectedItemPosition());
                avatar.setText(item.getBloodName());
                if (avatar.getText().toString().length() > 2) {
                    avatar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
                } else {
                    avatar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 70);
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

                String distId = Integer.toString(
                        ((DistrictItem) distItems.get(districtSpinner.getSelectedItemPosition())).getDistId());
                String groupId = Integer.toString(
                        ((BloodItem) bloodItems.get(bloodSpinner.getSelectedItemPosition())).getBloodId());

                HashMap<String, String> params = ParamsBuilder.registerRequest(
                        getArguments().getString("firstName"),
                        getArguments().getString("lastName"),
                        distId,
                        groupId,
                        getArguments().getString("mobileNumber"),
                        getArguments().getString("password"));
                dialogBuilder.createProgressDialog(getActivity().getResources().getString(R.string.loading));
                getJsonData(params);
            }
        };
    }

    private void getJsonData(final HashMap<String, String> params) {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(TAG, "Response: " + response.toString());
                        dialogBuilder.getProgressDialog().dismiss();
                        try {
                            if (response.getInt("done") == 1) {
                                //createAlertDialog("Done!");
                                dialogBuilder.createAlertDialog(null, "Done!", mOnClickListener);

                            } else {
                                //createAlertDialog("Sorry! something went wrong.");
                                dialogBuilder.createAlertDialog("Sorry!something went wrong.");
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());

                dialogBuilder.getProgressDialog().dismiss();
                Toast.makeText(
                        RegistrationCompleteFragment.this.getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }
}
