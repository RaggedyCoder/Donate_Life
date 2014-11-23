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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.MainActivity;
import com.project.bluepandora.donatelife.adapter.BloodSpinnerAdapter;
import com.project.bluepandora.donatelife.adapter.DistrictSpinnerAdapter;
import com.project.bluepandora.donatelife.adapter.HospitalSpinnerAdapter;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.HospitalItem;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.datasource.HospitalDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.widget.CustomButton;
import com.widget.CustomScrollView;
import com.widget.CustomScrollView.OnScrollChangedListener;
import com.widget.CustomTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//import android.widget.Toast;

@SuppressLint("InflateParams")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RequestFragment extends Fragment implements MainActivity.DrawerSlideListeners {

    private static final String NO_HOSPITAL_TAG = "Sorry No Hospital Name is available for this City";
    private Spinner district;
    private Spinner blood;
    private Spinner hospital;
    private Spinner amount;

    private CustomButton doneButton;
    private CheckBox emergencyCheck;

    private ArrayList<DistrictItem> distItems;
    private ArrayList<BloodItem> bloodItems;
    private ArrayList<HospitalItem> hospitalItems;
    private ArrayList<UserInfoItem> userInfoItems;
    private ArrayList<String> amounts;

    private ArrayAdapter<String> amountAdapter;

    private HospitalDataSource hospitalDatabase;
    private DistrictDataSource districtDatabase;
    private BloodDataSource bloodDatabase;
    private UserDataSource userDatase;

    private BloodSpinnerAdapter bloodAdapter;
    private DistrictSpinnerAdapter districtAdapter;
    private HospitalSpinnerAdapter hospitalAdapter;

    private Drawable mActionBarBackgroundDrawable;
    private CustomTextView mTitle;
    private int currentAlpha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setTheme(R.style.ThemeFeed);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_request,
                container, false);
        district = (Spinner) rootView.findViewById(R.id.district_spinner);
        blood = (Spinner) rootView.findViewById(R.id.blood_group_spinner);
        hospital = (Spinner) rootView.findViewById(R.id.hospital_spinner);
        amount = (Spinner) rootView.findViewById(R.id.amount_spinner);
        doneButton = (CustomButton) rootView.findViewById(R.id.done_button);
        CustomScrollView sc = (CustomScrollView) rootView.findViewById(R.id.scrollview);
        emergencyCheck = (CheckBox) rootView
                .findViewById(R.id.emergency_checkBox);
        emergencyCheck.isChecked();
        userInfoItems = new ArrayList<UserInfoItem>();

        userDatase = new UserDataSource(getActivity());
        userDatase.open();
        userInfoItems = userDatase.getAllUserItem();
        userDatase.close();

        bloodDatabase = new BloodDataSource(getActivity());
        bloodDatabase.open();
        bloodItems = bloodDatabase.getAllBloodItem();
        bloodDatabase.close();

        amounts = new ArrayList<String>();
        for (int i = 1; i <= 6; i++) {
            amounts.add("" + i);
        }
        amountAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, amounts);
        amount.setAdapter(amountAdapter);
        bloodAdapter = new BloodSpinnerAdapter(getActivity(), bloodItems);
        blood.setAdapter(bloodAdapter);
        districtDatabase = new DistrictDataSource(getActivity());
        districtDatabase.open();
        distItems = districtDatabase.getAllDistrictItem();
        districtDatabase.close();
        mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.actionbar_background);
        currentAlpha = 255;
        mActionBarBackgroundDrawable.setAlpha(currentAlpha);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.request_feed_actionbar, null);
        mTitle = (CustomTextView) mCustomView.findViewById(R.id.actionbar_title_text);
        mTitle.setText(R.string.blood_request);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
                "Blood Request");
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setBackgroundDrawable(mActionBarBackgroundDrawable);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowTitleEnabled(false);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowHomeEnabled(false);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setHomeButtonEnabled(false);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowCustomEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setCustomView(mCustomView);

        sc.setOnScrollChangedListener(new OnScrollChangedListener() {

            @Override
            public void onScrollChanged(ScrollView who, int l, int t, int oldl,
                                        int oldt) {
                final int headerHeight = rootView.findViewById(R.id.dummy_view)
                        .getHeight()
                        + rootView.findViewById(
                        R.id.blood_request_district_container)
                        .getHeight()
                        - ((ActionBarActivity) getActivity())
                        .getSupportActionBar().getHeight();
                final float ratio = 1.0f - ((float) Math.min(Math.max(t, 0),
                        headerHeight) / headerHeight);
                Log.d(RequestFragment.class.getSimpleName(), "" + ratio);
                final int newAlpha = (int) (ratio * 255);
                final int newAlphatext = (int) (ratio * 255);
                currentAlpha = newAlpha;
                mActionBarBackgroundDrawable.setAlpha(newAlpha);
                mTitle.setTextColor(Color.argb(newAlphatext, 0xff, 0xff, 0xff));
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mActionBarBackgroundDrawable.setCallback(new Callback() {

                @Override
                public void unscheduleDrawable(Drawable who, Runnable what) {

                }

                @Override
                public void scheduleDrawable(Drawable who, Runnable what,
                                             long when) {

                }

                @Override
                public void invalidateDrawable(Drawable who) {
                    ((ActionBarActivity) getActivity()).getSupportActionBar()
                            .setBackgroundDrawable(who);
                }
            });
        }
        districtAdapter = new DistrictSpinnerAdapter(getActivity(), distItems);
        district.setAdapter(districtAdapter);
        district.setSelection(districtAdapter.getItemPosition(userInfoItems
                .get(0).getDistId()));
        createHospitalList(distItems.get(district.getSelectedItemPosition())
                .getDistId());
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        doneButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (hospital.getSelectedItemId() == -1) {
                    Toast.makeText(
                            getActivity(),
                            "Sorry Blood Request is not availble for your city.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    // mobileNumber(String)
                    // groupId(Integer)
                    // amount(Integer)
                    // hospitalId(Integer)
                    // emergency(Integer 0,1)
                    // keyWord(String only A-Z, a-z, 0-9)
                    AlertDialog.Builder d = new AlertDialog.Builder(
                            getActivity());
                    d.setMessage("Are you sure about sending the request?");
                    d.setCancelable(false);
                    d.setTitle("Alert!");
                    d.setNegativeButton("No", null);
                    d.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    bloodRequest(createParams());
                                }
                            });
                    Dialog dialog = d.create();
                    dialog.show();
                }
            }
        });
        district.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                createHospitalList(distItems.get(
                        district.getSelectedItemPosition()).getDistId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private HashMap<String, String> createParams() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(URL.REQUEST_NAME, URL.ADD_BLOODREQUEST_PARAM);
        params.put(URL.MOBILE_TAG, userInfoItems.get(0).getMobileNumber());
        params.put(URL.GROUPID_TAG, blood.getSelectedItemId() + "");
        params.put(URL.AMOUNT_TAG, (String) amount.getSelectedItem());
        params.put(URL.HOSPITALID_TAG, hospital.getSelectedItemId() + "");
        params.put(URL.EMERGENCY_TAG, emergencyCheck.isChecked() ? "1" : "0");
        params.put(URL.PASSWORD_TAG, userInfoItems.get(0).getKeyWord());
        return params;
    }

    public void bloodRequest(HashMap<String, String> params) {

        CustomRequest jsonReq = new CustomRequest(Method.POST, URL.URL, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), response.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private void createHospitalList(int distId) {
        try {
            hospitalDatabase = new HospitalDataSource(getActivity());
            hospitalDatabase.open();
            hospitalItems = hospitalDatabase.getAllHospitalItem(distId);
            hospitalDatabase.close();
        } catch (Exception e) {
            hospitalItems = new ArrayList<HospitalItem>();
            HospitalItem item = new HospitalItem();
            item.setHospitalName(NO_HOSPITAL_TAG);
            item.setHospitalId(-1);
            hospitalItems.add(item);
            hospitalAdapter = new HospitalSpinnerAdapter(getActivity(),
                    hospitalItems);
            hospital.setAdapter(hospitalAdapter);
        }
        if (hospitalItems != null) {
            hospitalAdapter = new HospitalSpinnerAdapter(getActivity(),
                    hospitalItems);
            hospital.setAdapter(hospitalAdapter);
        }
    }

    @Override
    public void onDrawerSlide(float offset) {
        final int newAlpha = (int) (currentAlpha + (offset * (255 - currentAlpha)));
        final int newAlphaText = (int) (currentAlpha + (offset * (255 - currentAlpha)));
        if (mActionBarBackgroundDrawable != null && mTitle != null) {
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
            mTitle.setTextColor(Color.argb(newAlphaText, 0xff, 0xff, 0xff));
        }
    }
}