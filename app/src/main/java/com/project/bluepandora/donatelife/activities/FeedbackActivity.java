package com.project.bluepandora.donatelife.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.widget.CustomButton;
import com.widget.CustomEditText;
import com.widget.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
public class FeedbackActivity extends ActionBarActivity {


    private CustomTextView mobileNumber;
    private CustomTextView textCounter;
    private CustomEditText feedback;
    private CustomButton sendFeedback;
    protected UserDataSource userDatabase;
    /**
     * A {@link ProgressDialog} for showing the user background work is going on.
     */
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = (CustomEditText) findViewById(R.id.feedback_edittext);
        mobileNumber = (CustomTextView) findViewById(R.id.mobile_number);
        textCounter = (CustomTextView) findViewById(R.id.counter);
        sendFeedback = (CustomButton) findViewById(R.id.send);
        userDatabase = new UserDataSource(this);
        userDatabase.open();
        mobileNumber.setText(userDatabase.getAllUserItem().get(0).getMobileNumber());
        userDatabase.close();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sendFeedback.setEnabled(false);
        feedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textCounter.setText("" + (400 - feedback.getText().length()));
                if (feedback.getText().length() == 0) {
                    sendFeedback.setEnabled(false);
                } else if (feedback.getText().length() != 0) {
                    if (feedback.getText().toString().replaceAll("\n", "").length() == 0) {
                        sendFeedback.setEnabled(false);

                    } else if (feedback.getText().toString().replaceAll(" ", "").length() == 0) {
                        sendFeedback.setEnabled(false);
                    } else {
                        sendFeedback.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(URL.REQUEST_NAME, "feedback");
                params.put("username", mobileNumber.getText().toString());
                params.put("comments", feedback.getText().toString());
                createProgressDialog();
                CustomRequest customRequest;
                customRequest = new CustomRequest(Request.Method.POST, URL.URL, params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Toast.makeText(FeedbackActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                try {
                                    if (response.getInt("done") == 1) {
                                        createAlertDialog("Thank you for your feedback");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Toast.makeText(FeedbackActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        VolleyLog.e("Feedback", volleyError.getMessage());
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

    private void createProgressDialog() {
        pd = new ProgressDialog(this);
        pd.setMessage(this.getResources().getString(R.string.loading));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
    }

    private void createAlertDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Info");
        alertDialog.setMessage(message);
        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }
}
