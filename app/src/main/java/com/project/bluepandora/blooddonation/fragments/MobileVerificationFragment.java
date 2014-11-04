package com.project.bluepandora.blooddonation.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.project.bluepandora.donatelife.R;
import com.widget.CustomButton;

/**
 * <p/>
 * This fragment is for the user to sign up.
 * They can sign up by their mobile number.
 * <p/>
 */
public class MobileVerificationFragment extends Fragment {

    /**
     * Defines a tag for identifying log entries
     */
    private static final String TAG = MobileVerificationFragment.class.getSimpleName();
    /**
     * A {@link android.widget.Spinner} for showing the country's list
     */
    private Spinner countryNameSpinner;

    /**
     * A Button {@link com.widget.CustomButton} for the un registered user to sign up.
     */
    private CustomButton signUpButton;
    /**
     * A {@link View} for the whole fragment view.
     */
    private View rootView;

    public MobileVerificationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_mobileverification, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
