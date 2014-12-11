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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.MainActivity;
import com.project.bluepandora.donatelife.adapter.SpinnerAdapter;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.HospitalItem;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.datasource.HospitalDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.helpers.DialogBuilder;
import com.project.bluepandora.donatelife.helpers.ParamsBuilder;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.project.bluepandora.util.Utils;
import com.widget.CustomButton;
import com.widget.CustomCheckBox;
import com.widget.CustomEditText;
import com.widget.CustomScrollView;
import com.widget.CustomScrollView.OnScrollChangedListener;
import com.widget.CustomSwitch;
import com.widget.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@SuppressLint("InflateParams")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RequestFragment extends Fragment implements MainActivity.DrawerSlideListeners, URL {

    private static final String TAG = RequestFragment.class.getSimpleName();
    private static final String NO_HOSPITAL_TAG = "Sorry No Hospital Name is available for this City";
    private Spinner district;
    private Spinner blood;
    private Spinner hospital;
    private Spinner amount;

    private CustomButton doneButton;
    private CustomCheckBox emergencyCheck;
    private CustomCheckBox emergencySmsCheckBox;
    private CustomSwitch emergencySmsSwitch;


    private ArrayList<Item> distItems;
    private ArrayList<Item> bloodItems;
    private ArrayList<Item> hospitalItems;
    private ArrayList<UserInfoItem> userInfoItems;
    private ArrayList<String> amounts;

    private ArrayAdapter<String> amountAdapter;

    private HospitalDataSource hospitalDatabase;
    private DistrictDataSource districtDatabase;
    private BloodDataSource bloodDatabase;
    private UserDataSource userDatase;

    private SpinnerAdapter bloodAdapter;
    private SpinnerAdapter districtAdapter;
    private SpinnerAdapter hospitalAdapter;
    private ArrayList<String> mobileNumber;
    private ProgressDialog.Builder sendSmsDialogBuilder;
    private Dialog sendSmsDialog;
    private CustomTextView donorFinderTextView;
    private CustomEditText donorNumberEditText;
    private CustomTextView donorSmsBodyTextView;
    private CustomButton confirmSmsButton;
    private View sendSmsDialogView;


    private DialogBuilder dialogBuilder;

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
        emergencyCheck = (CustomCheckBox) rootView.findViewById(R.id.emergency_checkBox);
        sendSmsDialogBuilder = new ProgressDialog.Builder(getActivity());
        sendSmsDialogView = inflater.inflate(R.layout.send_sms_dialog, container, false);
        donorSmsBodyTextView = (CustomTextView) sendSmsDialogView.findViewById(R.id.donor_sms_body_text_view);
        donorFinderTextView = (CustomTextView) sendSmsDialogView.findViewById(R.id.donor_finder_text_view);
        donorNumberEditText = (CustomEditText) sendSmsDialogView.findViewById(R.id.donor_number_edit_text);
        confirmSmsButton = (CustomButton) sendSmsDialogView.findViewById(R.id.confirm_sms_button);
        sendSmsDialogBuilder.setView(sendSmsDialogView);
        sendSmsDialog = sendSmsDialogBuilder.create();
        if (Utils.hasICS()) {
            emergencySmsSwitch = (CustomSwitch) rootView.findViewById(R.id.emergency_sms_switch);
        } else {
            emergencySmsCheckBox = (CustomCheckBox) rootView.findViewById(R.id.emergency_sms_checkbox);
        }
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
        for (int i = 1; i <= 12; i++) {
            amounts.add(Integer.toString(i));
        }
        amountAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, amounts);
        amount.setAdapter(amountAdapter);
        bloodAdapter = new SpinnerAdapter(getActivity(), bloodItems);
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
        districtAdapter = new SpinnerAdapter(getActivity(), distItems);
        district.setAdapter(districtAdapter);
        district.setSelection(districtAdapter.getItemPosition(userInfoItems
                .get(0).getDistId()));
        createHospitalList(((DistrictItem) distItems.get(district.getSelectedItemPosition()))
                .getDistId());
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        confirmSmsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (donorNumberEditText.length() == 0) {
                    dialogBuilder.createAlertDialog("You must add 1 donor.");
                } else {
                    for (int i = 0; i < Integer.parseInt(donorNumberEditText.getText().toString()); i++) {
                        SmsManager.getDefault().sendTextMessage(mobileNumber.get(i),
                                null, donorSmsBodyTextView.getText().toString().trim(), null, null);
                    }
                }
            }
        });
        donorNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    int number = Integer.parseInt(s.toString());
                    if (number == 0) {
                        donorNumberEditText.setText("1");
                    } else if (number > mobileNumber.size()) {
                        donorNumberEditText.setText(Integer.toString(mobileNumber.size()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialogBuilder = new DialogBuilder(getActivity(), TAG);
        if (Utils.hasICS()) {
            emergencySmsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.e("", "" + isChecked);
                    if (isChecked) {
                        doneButton.setText(getResources().getString(R.string.request_sms));
                    } else {
                        doneButton.setText(getResources().getString(R.string.request_server));
                    }
                }
            });
        } else {
            emergencySmsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        doneButton.setText(getResources().getString(R.string.request_sms));
                    } else {
                        doneButton.setText(getResources().getString(R.string.request_server));
                    }
                }
            });
        }
        doneButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (hospital.getSelectedItemId() == -1) {
                    Toast.makeText(
                            getActivity(),
                            "Sorry Blood Request is not available for your city.",
                            Toast.LENGTH_SHORT).show();

                } else {
                    DialogInterface.OnClickListener serverPositiveClickListener = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            bloodRequest(createParams());
                            dialogBuilder.createProgressDialog(getResources().getString(R.string.processing));
                        }
                    };
                    DialogInterface.OnClickListener smsPositiveClickListener = new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            HashMap<String, String> params = ParamsBuilder.donatorMobileNumber(
                                    Long.toString(hospital.getSelectedItemId()),
                                    Long.toString(blood.getSelectedItemId()),
                                    userInfoItems.get(0).getMobileNumber(),
                                    userInfoItems.get(0).getKeyWord());
                            bloodRequest(params);
                            dialogBuilder.createProgressDialog(getResources().getString(R.string.processing));
                        }
                    };
                    if (isSmsRequest()) {
                        dialogBuilder.createAlertDialog("Alert!", "Are you sure about the sms request?",
                                smsPositiveClickListener, null);
                    } else {
                        dialogBuilder.createAlertDialog("Alert!", "Are you sure about sending the request?",
                                serverPositiveClickListener, null);
                    }
                }
            }
        });
        district.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                createHospitalList(((DistrictItem) distItems.get(
                        district.getSelectedItemPosition())).getDistId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private HashMap<String, String> createParams() {
        // mobileNumber(String)
        // groupId(Integer)
        // amount(Integer)
        // hospitalId(Integer)
        // emergency(Integer 0,1)
        // keyWord(String only A-Z, a-z, 0-9)
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateNow = new Date();
        String reqTime = isoFormat.format(dateNow);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(REQUEST_NAME, ADD_BLOODREQUEST_PARAM);
        params.put(MOBILE_TAG, userInfoItems.get(0).getMobileNumber());
        params.put(GROUPID_TAG, blood.getSelectedItemId() + "");
        params.put(AMOUNT_TAG, (String) amount.getSelectedItem());
        params.put(HOSPITALID_TAG, hospital.getSelectedItemId() + "");
        params.put(EMERGENCY_TAG, emergencyCheck.isChecked() ? "1" : "0");
        params.put(PASSWORD_TAG, userInfoItems.get(0).getKeyWord());
        params.put(REQUESTTIME_TAG, reqTime);
        Log.e("MSG", reqTime);
        return params;
    }

    public void bloodRequest(HashMap<String, String> params) {

        CustomRequest jsonReq = new CustomRequest(Method.POST, URL, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("TAG", response.toString());
                        dialogBuilder.getProgressDialog().dismiss();
                        try {
                            if (response.getString("requestName").equals("addBloodRequest")) {
                                dialogBuilder.createAlertDialog(response.getString("message"));
                            } else if (response.getString("requestName").equals("donatorMobileNumber")) {
                                if (response.getInt("done") == 1) {
                                    getMobileNumber(response);
                                    if (mobileNumber.size() == 0) {
                                        dialogBuilder.createAlertDialog("Sorry!No donor is available at this moment");
                                    } else {
                                        int bloodAmount = Integer.parseInt(((String) amount.getSelectedItem()));
                                        String bloodGroup = ((BloodItem) blood.getSelectedItem()).getBloodName();
                                        String hospitalName = ((HospitalItem) hospital.getSelectedItem()).getHospitalName();
                                        final String smsBody = "Please Help Need "
                                                + getResources().getQuantityString(R.plurals.bags, bloodAmount, bloodAmount) + " "
                                                + bloodGroup + " in " + hospitalName + "\n-Donate Life";
                                        String title = getResources().getQuantityString(R.plurals.donor_title, mobileNumber.size(), mobileNumber.size());
                                        if (title.equals(getString(R.string.donor_zero_grammar_error))) {
                                            title = getString(R.string.donor_zero_grammar_correct);
                                        }
                                        donorSmsBodyTextView.setText(smsBody);
                                        donorFinderTextView.setText(title);
                                        donorNumberEditText.setText(String.valueOf(mobileNumber.size()));
                                        sendSmsDialog.show();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.toString().contains("TimeoutError")) {
                    dialogBuilder.createAlertDialog("Alert", getResources().getString(R.string.timeout_error));
                } else if (error.toString().contains("UnknownHostException")) {
                    dialogBuilder.createAlertDialog("Alert", getResources().getString(R.string.no_internet));
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private void getMobileNumber(JSONObject response) {

        mobileNumber = new ArrayList<String>();
        try {
            JSONArray numbers = response.getJSONArray("number");
            for (int i = 0; i < numbers.length(); i++) {
                JSONObject number = numbers.getJSONObject(i);
                mobileNumber.add(number.getString("mobileNumber"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createHospitalList(int distId) {
        try {
            hospitalDatabase = new HospitalDataSource(getActivity());
            hospitalDatabase.open();
            hospitalItems = hospitalDatabase.getAllHospitalItem(distId);
            hospitalDatabase.close();
        } catch (Exception e) {
            hospitalItems = new ArrayList<Item>();
            HospitalItem item = new HospitalItem();
            item.setHospitalName(NO_HOSPITAL_TAG);
            item.setHospitalId(-1);
            hospitalItems.add(item);
            hospitalAdapter = new SpinnerAdapter(getActivity(),
                    hospitalItems);
            hospital.setAdapter(hospitalAdapter);
        }
        if (hospitalItems != null) {
            hospitalAdapter = new SpinnerAdapter(getActivity(),
                    hospitalItems);
            hospital.setAdapter(hospitalAdapter);
        }
    }


    private boolean isSmsRequest() {
        if (Utils.hasICS()) {
            return emergencySmsSwitch.isChecked();
        } else {
            return emergencySmsCheckBox.isChecked();
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