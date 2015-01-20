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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.MainActivity;
import com.project.bluepandora.donatelife.activities.SettingsActivity;
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
import com.project.bluepandora.donatelife.helpers.NumericalExchange;
import com.project.bluepandora.donatelife.helpers.ParamsBuilder;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.jsonperser.JSONParser;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.project.bluepandora.util.Utils;
import com.widget.CustomButton;
import com.widget.CustomCheckBox;
import com.widget.CustomEditText;
import com.widget.CustomScrollView;
import com.widget.CustomScrollView.OnScrollChangedListener;
import com.widget.CustomSwitch;
import com.widget.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RequestFragment extends Fragment implements MainActivity.DrawerSlideListeners, URL {

    private static final String TAG = RequestFragment.class.getSimpleName();
    HospitalDataSource hospitalDatabase;
    DistrictDataSource districtDatabase;
    BloodDataSource bloodDatabase;
    UserDataSource userDatabase;
    ArrayList<String> amounts;
    private ArrayList<Item> hospitalItems;
    private ArrayList<Item> distItems;
    private UserInfoItem userInfoItem;
    private ArrayAdapter<String> amountAdapter;
    private SpinnerAdapter bloodAdapter;
    private SpinnerAdapter districtAdapter;

    private boolean banglaFilter;
    private SpinnerAdapter hospitalAdapter;
    private ArrayList<String> mobileNumber;
    private View sendSmsDialogView;
    private View rootView;
    private View mCustomView;
    private ButtonOnClickListener buttonOnClickListener;

    private MainViewHolder mainViewHolder;
    private DialogViewHolder dialogViewHolder;
    /**
     * A CustomRequest{@link CustomRequest} for the server.This is for sending the user blood request to
     * the sever.If the server receive the request.It will send a JSONObject.
     */
    private CustomRequest bloodRequest;
    /**
     * An ErrorListener{@link Response.ErrorListener} for the bloodRequest.
     * If there is any problem making a request to the server or during of it, mErrorListener
     * will receive the error message.
     */
    private Response.ErrorListener mErrorListener;

    /**
     * A Listener{@link Response.Listener<JSONObject>} for the bloodRequest.
     * After making a successful request to the server,server will send a {@link JSONObject}.
     * mJsonObjectListener will receive that JSONObject.
     */
    private Response.Listener<JSONObject> mJsonObjectListener;

    private DialogInterface.OnClickListener smsPositiveClickListener;
    private DialogInterface.OnClickListener serverPositiveClickListener;

    private OnScrollChangedListener mOnScrollChangedListener;
    private OnItemSelectedListener mDistrictItemSelectedListener;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private Callback mDrawableCallback;

    private TextWatcher numberTextWatcher;

    private DialogBuilder dialogBuilder;

    private Drawable mActionBarBackgroundDrawable;
    private CustomTextView mTitle;
    private int currentAlpha;

    private JSONParser jsonParser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogBuilder = new DialogBuilder(getActivity(), TAG);
        userInfoItem = new UserInfoItem();
        userDatabase = new UserDataSource(getActivity());
        userDatabase.open();
        userInfoItem = userDatabase.getAllUserItem().get(0);
        userDatabase.close();

        bloodDatabase = new BloodDataSource(getActivity());
        bloodDatabase.open();
        ArrayList<Item> bloodItems;
        bloodItems = bloodDatabase.getAllBloodItem();
        bloodDatabase.close();
        bloodAdapter = new SpinnerAdapter(getActivity(), bloodItems);
        banglaFilter = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(SettingsActivity.LANGUAGE_TAG, false);

        amounts = new ArrayList<String>();
        for (int i = 1; i <= 12; i++) {
            if (banglaFilter) {
                amounts.add(NumericalExchange.toBanglaNumerical(Integer.toString(i)));
            } else {
                amounts.add(NumericalExchange.toEnglishNumerical(Integer.toString(i)));
            }
        }
        amountAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, amounts);

        districtDatabase = new DistrictDataSource(getActivity());
        districtDatabase.open();
        distItems = districtDatabase.getAllDistrictItem();
        districtDatabase.close();
        districtAdapter = new SpinnerAdapter(getActivity(), distItems);

        hospitalItems = new ArrayList<Item>();
        hospitalAdapter = new SpinnerAdapter(getActivity(), hospitalItems);
        jsonParser = new JSONParser(getActivity(), TAG);
        buttonOnClickListener = new ButtonOnClickListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_request,
                    container, false);
            mainViewHolder = new MainViewHolder();
            mainViewHolder.districtSpinner = (Spinner) rootView.findViewById(R.id.district_spinner);
            mainViewHolder.bloodSpinner = (Spinner) rootView.findViewById(R.id.blood_group_spinner);
            mainViewHolder.hospitalSpinner = (Spinner) rootView.findViewById(R.id.hospital_spinner);
            mainViewHolder.amountSpinner = (Spinner) rootView.findViewById(R.id.amount_spinner);
            mainViewHolder.doneButton = (CustomButton) rootView.findViewById(R.id.done_button);
            mainViewHolder.scrollView = (CustomScrollView) rootView.findViewById(R.id.scrollview);
            mainViewHolder.emergencyCheck = (CustomCheckBox) rootView.findViewById(R.id.emergency_checkBox);
            if (Utils.hasICS()) {
                mainViewHolder.emergencySmsSwitch = (CustomSwitch) rootView.findViewById(R.id.emergency_sms_switch);
            } else {
                mainViewHolder.emergencySmsCheckBox = (CustomCheckBox) rootView.findViewById(R.id.emergency_sms_checkbox);
            }
            rootView.setTag(mainViewHolder);
        } else {
            mainViewHolder = (MainViewHolder) rootView.getTag();
        }
        if (sendSmsDialogView == null) {
            sendSmsDialogView = inflater.inflate(R.layout.send_sms_dialog, container, false);
            dialogViewHolder = new DialogViewHolder();
            dialogViewHolder.donorSmsBodyTextView = (CustomTextView) sendSmsDialogView.findViewById(R.id.donor_sms_body_text_view);
            dialogViewHolder.donorFinderTextView = (CustomTextView) sendSmsDialogView.findViewById(R.id.donor_finder_text_view);
            dialogViewHolder.donorNumberEditText = (CustomEditText) sendSmsDialogView.findViewById(R.id.donor_number_edit_text);
            dialogViewHolder.confirmSmsButton = (CustomButton) sendSmsDialogView.findViewById(R.id.confirm_sms_button);
            sendSmsDialogView.setTag(dialogViewHolder);
        } else {
            dialogViewHolder = (DialogViewHolder) sendSmsDialogView.getTag();
        }
        dialogViewHolder.sendSmsDialogBuilder = new ProgressDialog.Builder(getActivity());
        dialogViewHolder.sendSmsDialogBuilder.setView(sendSmsDialogView);
        dialogViewHolder.sendSmsDialog = dialogViewHolder.sendSmsDialogBuilder.create();
        mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.actionbar_background);
        mCustomView = inflater.inflate(R.layout.request_feed_actionbar, container, false);
        currentAlpha = 255;
        mActionBarBackgroundDrawable.setAlpha(currentAlpha);
        mTitle = (CustomTextView) mCustomView.findViewById(R.id.actionbar_title_text);
        mTitle.setText(R.string.blood_request);
        customizeActionbar();
        mainViewHolder.scrollView.setOnScrollChangedListener(mOnScrollChangedListener);
        return rootView;
    }

    private void customizeActionbar() {
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
                R.string.blood_request);
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewHolder.amountSpinner.setAdapter(amountAdapter);
        mainViewHolder.bloodSpinner.setAdapter(bloodAdapter);
        mainViewHolder.districtSpinner.setAdapter(districtAdapter);
        mainViewHolder.hospitalSpinner.setAdapter(hospitalAdapter);
        int selectedItemId = districtAdapter.getItemPosition(userInfoItem.getDistId());
        mainViewHolder.districtSpinner.setSelection(selectedItemId);
        DistrictItem districtItem = ((DistrictItem) mainViewHolder.districtSpinner.getSelectedItem());
        createHospitalList(districtItem.getDistId());
        if (!Utils.hasJB_MR1()) {
            mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
        }
        dialogViewHolder.donorNumberEditText.addTextChangedListener(numberTextWatcher);
        if (Utils.hasICS()) {
            mainViewHolder.emergencySmsSwitch.setOnCheckedChangeListener(mOnCheckedChangeListener);
        } else {
            mainViewHolder.emergencySmsCheckBox.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
        mainViewHolder.doneButton.setOnClickListener(buttonOnClickListener);
        dialogViewHolder.confirmSmsButton.setOnClickListener(buttonOnClickListener);
        mainViewHolder.districtSpinner.setOnItemSelectedListener(mDistrictItemSelectedListener);
        bloodRequest = new CustomRequest(Request.Method.POST, URL, mJsonObjectListener, mErrorListener);
    }

    private boolean getViewEquals(View view1, View view2) {
        return view1.equals(view2);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("", "" + isChecked);
                if (isChecked) {
                    mainViewHolder.doneButton.setText(getResources().getString(R.string.request_sms));
                } else {
                    mainViewHolder.doneButton.setText(getResources().getString(R.string.request_server));
                }
            }
        };
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogBuilder.getProgressDialog().dismiss();
                if (error.toString().contains("TimeoutError")) {
                    dialogBuilder.createAlertDialog(getString(R.string.alert), getResources().getString(R.string.timeout_error));
                } else if (error.toString().contains("UnknownHostException")) {
                    dialogBuilder.createAlertDialog(getString(R.string.alert), getResources().getString(R.string.no_internet));
                }
            }
        };
        mJsonObjectListener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("TAG", response.toString());
                dialogBuilder.getProgressDialog().dismiss();
                try {
                    if (response.getString("requestName").equals("addBloodRequest")) {
                        dialogBuilder.createAlertDialog(response.getString("message"));
                    } else if (response.getString("requestName").equals("donatorMobileNumber")) {
                        if (response.getInt("done") == 1) {
                            mobileNumber = jsonParser.getMobileNumber(response);
                            if (mobileNumber.size() == 0) {
                                dialogBuilder.createAlertDialog(getString(R.string.no_donor_availibility));
                            } else {
                                showSmsDialog();
                            }
                        } else {
                            dialogBuilder.createAlertDialog(getString(R.string.no_donor_availibility));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        numberTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    int number = Integer.parseInt(s.toString());
                    if (number == 0) {
                        dialogViewHolder.donorNumberEditText.setText("1");
                    } else if (number > mobileNumber.size()) {
                        dialogViewHolder.donorNumberEditText.setText(Integer.toString(mobileNumber.size()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mDrawableCallback = new Callback() {

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
        };
        serverPositiveClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String mobileNumber = userInfoItem.getMobileNumber();
                final String bloodId = Long.toString(mainViewHolder.bloodSpinner.getSelectedItemId());
                String amount = (String) mainViewHolder.amountSpinner.getSelectedItem();
                if (banglaFilter) {
                    amount = NumericalExchange.toEnglishNumerical(amount);
                }
                final String hospitalId = Long.toString(mainViewHolder.hospitalSpinner.getSelectedItemId());
                final String emergency = mainViewHolder.emergencyCheck.isChecked() ? "1" : "0";
                final String keyWord = userInfoItem.getKeyWord();
                Date dateNow = new Date();
                String reqTime = isoFormat.format(dateNow);
                if (banglaFilter) {
                    reqTime = NumericalExchange.toEnglishNumerical(reqTime);
                }
                HashMap<String, String> params = ParamsBuilder.bloodRequest(
                        mobileNumber,
                        bloodId,
                        amount,
                        hospitalId,
                        emergency,
                        keyWord,
                        reqTime
                );
                bloodRequest.setParams(params);
                AppController.getInstance().addToRequestQueue(bloodRequest);
                dialogBuilder.createProgressDialog(getResources().getString(R.string.processing));
            }
        };
        smsPositiveClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
                final String hospitalId = Long.toString(mainViewHolder.hospitalSpinner.getSelectedItemId());
                final String bloodId = Long.toString(mainViewHolder.bloodSpinner.getSelectedItemId());
                final String mobileNumber = userInfoItem.getMobileNumber();
                final String keyWord = userInfoItem.getKeyWord();
                HashMap<String, String> params = ParamsBuilder.donatorMobileNumber(
                        hospitalId,
                        bloodId,
                        mobileNumber,
                        keyWord);
                bloodRequest.setParams(params);
                AppController.getInstance().addToRequestQueue(bloodRequest);
                dialogBuilder.createProgressDialog(getResources().getString(R.string.processing));
            }
        };
        mDistrictItemSelectedListener = new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                createHospitalList(((DistrictItem) distItems.get(
                        mainViewHolder.districtSpinner.getSelectedItemPosition())).getDistId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        mOnScrollChangedListener = new OnScrollChangedListener() {

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
                final int newAlphaText = (int) (ratio * 255);
                currentAlpha = newAlpha;
                mActionBarBackgroundDrawable.setAlpha(newAlpha);
                mTitle.setTextColor(Color.argb(newAlphaText, 0xff, 0xff, 0xff));
            }
        };
    }

    private void showSmsDialog() {
        int bloodAmount = Integer.parseInt(((String) mainViewHolder.amountSpinner.getSelectedItem()));
        String bloodGroup = ((BloodItem) mainViewHolder.bloodSpinner.getSelectedItem()).getBloodName();
        String hospitalName = ((HospitalItem) mainViewHolder.hospitalSpinner.getSelectedItem()).getHospitalName();
        final String smsBody = "!!Please Help!!\n Need"
                + ((bloodAmount == 1) ? "" : "(one Bag)" + "(" + bloodAmount + " Bags)")
                + bloodGroup + " in " + hospitalName + "\n-Donate Life";
        String title = getResources().getQuantityString(R.plurals.donor_title, mobileNumber.size(), mobileNumber.size());
        if (title.equals(getString(R.string.donor_zero_grammar_error))) {
            title = getString(R.string.donor_zero_grammar_correct);
        }
        dialogViewHolder.donorSmsBodyTextView.setText(smsBody);
        dialogViewHolder.donorFinderTextView.setText(title);
        dialogViewHolder.donorNumberEditText.setText(String.valueOf(mobileNumber.size()));
        dialogViewHolder.sendSmsDialog.show();
    }

    private void createHospitalList(int distId) {
        hospitalItems.clear();
        try {
            hospitalDatabase = new HospitalDataSource(getActivity());
            hospitalDatabase.open();
            hospitalItems = hospitalDatabase.getAllHospitalItem(distId);
            hospitalDatabase.close();
        } catch (Exception e) {
            HospitalItem item = new HospitalItem();
            item.setHospitalName(getString(R.string.no_hospital_tag));
            item.setHospitalId(-1);
            hospitalItems.add(item);

        }
        hospitalAdapter.setItems(hospitalItems);
        hospitalAdapter.notifyDataSetChanged();
    }


    private boolean isSmsRequest() {
        if (Utils.hasICS()) {
            return mainViewHolder.emergencySmsSwitch.isChecked();
        } else {
            return mainViewHolder.emergencySmsCheckBox.isChecked();
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

    private static class MainViewHolder {
        CustomScrollView scrollView;
        private Spinner districtSpinner;
        private Spinner bloodSpinner;
        private Spinner hospitalSpinner;
        private Spinner amountSpinner;
        private CustomButton doneButton;
        private CustomCheckBox emergencyCheck;
        private CustomCheckBox emergencySmsCheckBox;
        private CustomSwitch emergencySmsSwitch;
    }


    private static class DialogViewHolder {
        private ProgressDialog.Builder sendSmsDialogBuilder;
        private Dialog sendSmsDialog;
        private CustomTextView donorFinderTextView;
        private CustomEditText donorNumberEditText;
        private CustomTextView donorSmsBodyTextView;
        private CustomButton confirmSmsButton;
    }

    private class ButtonOnClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (getViewEquals(view, mainViewHolder.doneButton)) {
                if (mainViewHolder.hospitalSpinner.getSelectedItemId() == -1) {
                    dialogBuilder.createAlertDialog(getString(R.string.request_not_possible));
                } else {
                    if (isSmsRequest()) {
                        dialogBuilder.createAlertDialog(getString(R.string.alert), getString(R.string.sms_alert),
                                smsPositiveClickListener, null);
                    } else {
                        dialogBuilder.createAlertDialog(getString(R.string.alert), getString(R.string.request_alert),
                                serverPositiveClickListener, null);
                    }
                }
            } else if (getViewEquals(view, dialogViewHolder.confirmSmsButton)) {
                if (dialogViewHolder.donorNumberEditText.length() == 0) {
                    dialogBuilder.createAlertDialog(getString(R.string.one_donor_must));
                } else {
                    for (int i = 0; i < Integer.parseInt(dialogViewHolder.donorNumberEditText.getText().toString()); i++) {
                        SmsManager.getDefault().sendTextMessage(mobileNumber.get(i),
                                null, dialogViewHolder.donorSmsBodyTextView.getText().toString().trim(), null, null);
                    }
                }
            }
        }
    }
}