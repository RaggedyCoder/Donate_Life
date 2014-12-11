package com.project.bluepandora.donatelife.activities;

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

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class FeedbackActivity extends ActionBarActivity implements URL {


    private static final String TAG = FeedbackActivity.class.getSimpleName();
    /**
     * A {@link UserDataSource} for getting the user information.
     */
    UserDataSource userDatabase;
    /**
     * A {@link DistrictDataSource} for getting the district list.
     */
    DistrictDataSource districtDataSource;
    /**
     * An {@link ArrayList} of {@link String} for the subject.
     */
    ArrayList<String> subject;
    /**
     * An {@link ArrayList} of {@link DistrictItem} for the district list.
     */
    ArrayList<Item> districtList;
    /**
     * A {@link Spinner} for showing the subject of the feedback.
     */
    private Spinner subjectSpinner;
    /**
     * A {@link SpinnerAdapter} for the districtSpinner to have the data
     */
    private SpinnerAdapter districtSpinnerAdapter;
    /**
     * A {@link ArrayAdapter} for the subjectSpinner to have the data.
     */
    private ArrayAdapter<String> subjectSpinnerAdapter;
    /**
     * A {@link CustomTextView} for showing the user mobile number.
     */
    private CustomTextView mobileNumber;
    /**
     * A {@link CustomTextView} for showing the user feedback word limit.
     */
    private CustomTextView textCounter;
    /**
     * A {@link CustomEditText} for the feedback.
     */
    private CustomEditText feedback;
    /**
     * A {@link Spinner} for showing the subject of the district.
     */
    private Spinner districtSpinner;
    /**
     * A {@link  CustomButton} for sending the feedback.
     */
    private CustomButton sendFeedback;
    /**
     * A {@link LinearLayout} for holding the districtSpinner.
     */
    private LinearLayout districtHolder;
    /**
     * A {@link DialogBuilder} for showing alert dialog or the progress dialog.
     */
    private DialogBuilder mDialogBuilder;

    /**
     * A {@link DialogInterface.OnClickListener} for the neutral button of an alert dialog
     * This Alert Dialog is created by  mDialogBuilder{@link DialogBuilder}.
     */
    private DialogInterface.OnClickListener mOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = (CustomEditText) findViewById(R.id.feedback_edittext);
        mobileNumber = (CustomTextView) findViewById(R.id.mobile_number);
        textCounter = (CustomTextView) findViewById(R.id.counter);
        sendFeedback = (CustomButton) findViewById(R.id.send);
        districtSpinner = (Spinner) findViewById(R.id.district_spinner);
        subjectSpinner = (Spinner) findViewById(R.id.subject_spinner);
        districtHolder = (LinearLayout) findViewById(R.id.district_holder);
        districtDataSource = new DistrictDataSource(this);
        mDialogBuilder = new DialogBuilder(this, TAG);
        districtDataSource.open();
        subject = new ArrayList<String>();
        subject.add("App feedback");
        subject.add("Hospital Suggestion");
        subjectSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, subject);
        subjectSpinner.setAdapter(subjectSpinnerAdapter);
        districtList = districtDataSource.getAllDistrictItem();
        districtDataSource.close();
        districtSpinnerAdapter = new SpinnerAdapter(this, districtList);
        districtSpinner.setAdapter(districtSpinnerAdapter);
        userDatabase = new UserDataSource(this);
        userDatabase.open();
        mobileNumber.setText(userDatabase.getAllUserItem().get(0).getMobileNumber());
        userDatabase.close();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sendFeedback.setEnabled(false);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String string = subjectSpinnerAdapter.getItem(position);
                if (string.equals("App feedback")) {
                    districtHolder.setVisibility(View.GONE);
                    feedback.setHint("Write your feedback...");
                } else {
                    districtHolder.setVisibility(View.VISIBLE);
                    feedback.setHint("Suggest Hospital...");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCounter.setText(Integer.toString((400 - feedback.getText().toString().trim().length())));
                if (feedback.getText().toString().trim().length() == 0) {
                    sendFeedback.setEnabled(false);
                } else {
                    sendFeedback.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> params;
                if (subjectSpinner.getSelectedItem().equals("Hospital Suggestion")) {
                    DistrictItem item = (DistrictItem) districtSpinnerAdapter.getItem(
                            districtSpinner.getSelectedItemPosition());
                    params = ParamsBuilder.feedbackRequest(
                            mobileNumber.getText().toString(),
                            "New Hospital Suggestion",
                            "District: " + item.getDistName() +
                                    " Hospital: " + feedback.getText().toString().trim());
                } else {
                    params = ParamsBuilder.feedbackRequest(
                            mobileNumber.getText().toString(),
                            "App feedback",
                            feedback.getText().toString().trim());
                }
                mDialogBuilder.createProgressDialog(getResources().getString(R.string.loading));
                CustomRequest customRequest;
                customRequest = new CustomRequest(Request.Method.POST, URL, params,
                        new Response.Listener<JSONObject>() {
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
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mDialogBuilder.getProgressDialog().dismiss();
                        if (error.toString().contains("TimeoutError")) {
                            mDialogBuilder.createAlertDialog(
                                    getString(R.string.alert),
                                    getString(R.string.timeout_error));
                        } else if (error.toString().contains("UnknownHostException")) {
                            mDialogBuilder.createAlertDialog(
                                    getString(R.string.alert),
                                    getString(R.string.no_internet));
                        }
                        VolleyLog.e(TAG, error.getMessage());
                    }
                });
                AppController.getInstance().addToRequestQueue(customRequest);

            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
