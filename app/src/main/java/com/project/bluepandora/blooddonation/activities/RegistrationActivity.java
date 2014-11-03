package com.project.bluepandora.blooddonation.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.project.bluepandora.blooddonation.fragments.CheckRegistrationFragment;
import com.project.bluepandora.blooddonation.fragments.RegistrationStepFragment;
import com.project.bluepandora.donatelife.R;

public class RegistrationActivity extends ActionBarActivity {
    public Fragment mContent;
    public static boolean backPressed = false;
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(
                    savedInstanceState, "regContent");
        }
        if (mContent == null) {
            mContent = new CheckRegistrationFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.reg_container, mContent).commit();
        } else {

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "regContent",
                mContent);
    }

    @Override
    public void onBackPressed() {

        if (backPressed) {
            finish();
            RegistrationActivity.this.overridePendingTransition(
                    R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
            super.onBackPressed();
        } else
            Toast.makeText(this, "Press Back again to Exit", Toast.LENGTH_SHORT)
                    .show();
        backPressed = true;
    }

    public void changeFragement(String mobileNumber) {
        mContent = new RegistrationStepFragment(mobileNumber);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.reg_container, mContent).commit();
    }
}
