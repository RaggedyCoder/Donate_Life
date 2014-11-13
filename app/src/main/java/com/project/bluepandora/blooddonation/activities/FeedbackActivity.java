package com.project.bluepandora.blooddonation.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.project.bluepandora.blooddonation.datasource.UserDataSource;
import com.project.bluepandora.donatelife.R;
import com.widget.CustomEditText;
import com.widget.CustomTextView;


public class FeedbackActivity extends ActionBarActivity {


    private CustomTextView mobileNumber;
    private CustomTextView textCounter;
    private CustomEditText feedback;
    private UserDataSource userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback = (CustomEditText) findViewById(R.id.feedback_edittext);
        mobileNumber = (CustomTextView) findViewById(R.id.mobile_number);
        textCounter = (CustomTextView) findViewById(R.id.counter);
        userDatabase = new UserDataSource(this);
        userDatabase.open();
        mobileNumber.setText(userDatabase.getAllUserItem().get(0).getMobileNumber());
        userDatabase.close();

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
