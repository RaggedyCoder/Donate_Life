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
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.adapter.SpinnerAdapter;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.helpers.DialogBuilder;
import com.project.bluepandora.donatelife.helpers.ParamsBuilder;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.widget.CustomButton;
import com.widget.CustomEditText;
import com.widget.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FeedBackFragment extends Fragment implements URL {

    private static final String TAG = FeedBackFragment.class.getSimpleName();

    /**
     * A {@link com.project.bluepandora.donatelife.datasource.UserDataSource} for getting the user information.
     */
    UserDataSource userDatabase;
    /**
     * A {@link com.project.bluepandora.donatelife.datasource.DistrictDataSource} for getting the district list.
     */
    DistrictDataSource districtDataSource;
    /**
     * An {@link java.util.ArrayList} of {@link String} for the subject.
     */
    ArrayList<String> subject;
    /**
     * An {@link ArrayList} of {@link com.project.bluepandora.donatelife.data.DistrictItem} for the district list.
     */
    ArrayList<Item> districtList;
    /**
     * An {@link UserInfoItem} for the userInfo list.
     */
    UserInfoItem userInfo;
    /**
     * A {@link com.project.bluepandora.donatelife.adapter.SpinnerAdapter} for the districtSpinner to have the data
     */
    private SpinnerAdapter districtSpinnerAdapter;
    /**
     * A {@link android.widget.ArrayAdapter} for the subjectSpinner to have the data.
     */
    private ArrayAdapter<String> subjectSpinnerAdapter;
    /**
     * A {@link DialogBuilder} for showing alert dialog or the progress dialog.
     */
    private DialogBuilder mDialogBuilder;
    /**
     * A {@link DialogInterface.OnClickListener} for the neutral button of an alert dialog
     * This Alert Dialog is created by  mDialogBuilder{@link DialogBuilder}.
     */
    private DialogInterface.OnClickListener mOnClickListener;

    /**
     * A CustomRequest{@link CustomRequest} for the server.This is for sending the user feedback to
     * the sever.If the server receive the request.It will send a JSONObject.
     */
    private CustomRequest sendFeedbackRequest;
    /**
     * An ErrorListener{@link Response.ErrorListener} for the sendFeedbackRequest.
     * If there is any problem making a request to the server or during of it, mErrorListener
     * will receive the error message.
     */
    private Response.ErrorListener mErrorListener;

    /**
     * A Listener{@link Response.Listener<JSONObject>} for the sendFeedbackRequest.
     * After making a successful request to the server,server will send a {@link JSONObject}.
     * mJsonObjectListener will receive that JSONObject.
     */
    private Response.Listener<JSONObject> mJsonObjectListener;


    /**
     * A OnClickListener{@link View.OnClickListener} for the sendFeedbackButton from the
     * mainViewHolder{@link MainViewHolder}.This listener is for sending the feedback request
     * to the server.User will press the sendFeedbackButton. mSendFeedbackClickListener will be
     * used to receive that click.
     */
    private View.OnClickListener mSendFeedbackClickListener;

    /**
     * A TextWatcher{@link TextWatcher} for the feedbackEditText from the
     * mainViewHolder{@link MainViewHolder}.TextWatcher is for watching the what is user is writing
     * in feedbackEditText.It is for counting the feedbackEditText. It also for watching whether
     * if there is any valid character.valid character will enable the sendFeedbackButton.
     */
    private TextWatcher feedbackTextWatcher;

    /**
     * A MainViewHolder{@link MainViewHolder}. This is holding all the View of this Fragment.
     */
    private MainViewHolder mainViewHolder;

    /**
     * An OnItemSelectedListener{@link AdapterView.OnItemSelectedListener} for the subjectSpinner from
     * the mainViewHolder{@link MainViewHolder}. mSubjectSelectedListener is to
     * detect which subject is selected for the feedback request.
     */
    private AdapterView.OnItemSelectedListener mSubjectSelectedListener;

    /**
     * A whole View{@link View} of the fragment.
     */
    private View rootView;

    private HashMap<String, String> params;

    public FeedBackFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subject = new ArrayList<String>();
        subject.add("App feedback");
        subject.add("Hospital Suggestion");

        districtDataSource = new DistrictDataSource(getActivity());
        districtDataSource.open();
        districtList = districtDataSource.getAllDistrictItem();
        districtDataSource.close();

        userDatabase = new UserDataSource(getActivity());
        userDatabase.open();
        userInfo = userDatabase.getAllUserItem().get(0);
        userDatabase.close();

        mDialogBuilder = new DialogBuilder(getActivity(), TAG);
        subjectSpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, subject);
        districtSpinnerAdapter = new SpinnerAdapter(getActivity(), districtList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
            mainViewHolder = new MainViewHolder();
            mainViewHolder.feedbackEditText = (CustomEditText) rootView.findViewById(R.id.feedback_edittext);
            mainViewHolder.mobileNumberTextView = (CustomTextView) rootView.findViewById(R.id.mobile_number_text_view);
            mainViewHolder.textCounterTextView = (CustomTextView) rootView.findViewById(R.id.text_counter_text_view);
            mainViewHolder.sendFeedbackButton = (CustomButton) rootView.findViewById(R.id.send_feedback_button);
            mainViewHolder.districtSpinner = (Spinner) rootView.findViewById(R.id.district_spinner);
            mainViewHolder.subjectSpinner = (Spinner) rootView.findViewById(R.id.subject_spinner);
            mainViewHolder.districtHolderLayout = (LinearLayout) rootView.findViewById(R.id.district_holder_layout);
            rootView.setTag(mainViewHolder);
        } else {
            mainViewHolder = (MainViewHolder) rootView.getTag();
        }
        mainViewHolder.mobileNumberTextView.setText(userInfo.getMobileNumber());
        mainViewHolder.sendFeedbackButton.setEnabled(false);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewHolder.subjectSpinner.setAdapter(subjectSpinnerAdapter);
        mainViewHolder.districtSpinner.setAdapter(districtSpinnerAdapter);
        mainViewHolder.subjectSpinner.setOnItemSelectedListener(mSubjectSelectedListener);
        mainViewHolder.feedbackEditText.addTextChangedListener(feedbackTextWatcher);
        mainViewHolder.sendFeedbackButton.setOnClickListener(mSendFeedbackClickListener);
        sendFeedbackRequest = new CustomRequest(Request.Method.POST, URL, mJsonObjectListener, mErrorListener);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        };
        mSendFeedbackClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainViewHolder.subjectSpinner.getSelectedItem().equals("Hospital Suggestion")) {
                    DistrictItem item = (DistrictItem) districtSpinnerAdapter.getItem(
                            mainViewHolder.districtSpinner.getSelectedItemPosition());
                    params = ParamsBuilder.feedbackRequest(
                            mainViewHolder.mobileNumberTextView.getText().toString(),
                            "New Hospital Suggestion",
                            "District: " + item.getDistName() +
                                    " Hospital: " + mainViewHolder.feedbackEditText.getText().toString().trim());

                } else if (mainViewHolder.subjectSpinner.getSelectedItem().equals("App feedback")) {
                    params = ParamsBuilder.feedbackRequest(
                            mainViewHolder.mobileNumberTextView.getText().toString(),
                            "App feedback",
                            mainViewHolder.feedbackEditText.getText().toString().trim());

                }
                sendFeedbackRequest.setParams(params);
                mDialogBuilder.createProgressDialog(getResources().getString(R.string.loading));
                AppController.getInstance().addToRequestQueue(sendFeedbackRequest);
            }
        };
        feedbackTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int feedBackTextSize = mainViewHolder.feedbackEditText.getText().toString().trim().length();
                final String feedBackTextCounter = Integer.toString((400 - feedBackTextSize));
                mainViewHolder.textCounterTextView.setText(feedBackTextCounter);
                if (mainViewHolder.feedbackEditText.getText().toString().trim().length() == 0) {
                    mainViewHolder.sendFeedbackButton.setEnabled(false);
                } else {
                    mainViewHolder.sendFeedbackButton.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mJsonObjectListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                mDialogBuilder.getProgressDialog().dismiss();
                try {
                    if (response.getInt("done") == 1) {
                        mDialogBuilder.createAlertDialog(
                                getString(R.string.info),
                                getString(R.string.feedback_thank_you),
                                mOnClickListener);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialogBuilder.getProgressDialog().dismiss();
                if (error.toString().contains("TimeoutError")) {
                    mDialogBuilder.createAlertDialog(getString(R.string.alert), getString(R.string.timeout_error));
                } else if (error.toString().contains("UnknownHostException")) {
                    mDialogBuilder.createAlertDialog(getString(R.string.alert), getString(R.string.no_internet));
                }
                VolleyLog.e(TAG, error.getMessage());
            }
        };
        mSubjectSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String string = subjectSpinnerAdapter.getItem(position);
                if (string.equals("App feedback")) {
                    mainViewHolder.districtHolderLayout.setVisibility(View.GONE);
                    mainViewHolder.feedbackEditText.setHint("Write your feedback...");
                } else {
                    mainViewHolder.districtHolderLayout.setVisibility(View.VISIBLE);
                    mainViewHolder.feedbackEditText.setHint("Suggest Hospital...");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private static class MainViewHolder {
        /**
         * A {@link android.widget.Spinner} for showing the subject of the feedback.
         */
        private Spinner subjectSpinner;
        /**
         * A {@link com.widget.CustomTextView} for showing the user mobile number.
         */
        private CustomTextView mobileNumberTextView;
        /**
         * A {@link CustomTextView} for showing the user feedback word limit.
         */
        private CustomTextView textCounterTextView;
        /**
         * A {@link com.widget.CustomEditText} for the feedback.
         */
        private CustomEditText feedbackEditText;
        /**
         * A {@link Spinner} for showing the subject of the district.
         */
        private Spinner districtSpinner;
        /**
         * A {@link  CustomButton} for sending the feedback.
         */
        private CustomButton sendFeedbackButton;
        /**
         * A {@link LinearLayout} for holding the districtSpinner.
         */
        private LinearLayout districtHolderLayout;
    }
}
