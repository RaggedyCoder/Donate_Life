package com.project.bluepandora.blooddonation.fragments;
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

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.project.bluepandora.blooddonation.data.BloodItem;
import com.project.bluepandora.blooddonation.data.DistrictItem;
import com.project.bluepandora.blooddonation.data.UserInfoItem;
import com.project.bluepandora.blooddonation.datasource.BloodDataSource;
import com.project.bluepandora.blooddonation.datasource.DistrictDataSource;
import com.project.bluepandora.blooddonation.datasource.UserDataSource;
import com.project.bluepandora.donatelife.R;
import com.widget.CustomScrollView;
import com.widget.CustomTextView;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private float density;
    private UserDataSource userDatabase;
    private ArrayList<UserInfoItem> userInfo;
    private BloodDataSource bloodDatabase;
    private DistrictDataSource districtDatabase;
    private DistrictItem distItem;
    private BloodItem bloodItem;
    private TextView avatar;
    private TextView userName;
    private TextView mobileNumber;
    private TextView districtName;
    private TextView bloodGroup;
    private TextView profileDetail;
    private TextView profileEdit;
    private CustomScrollView scrollView;

    private TextView fullName;

    private TextView mobileNumber_show;
    private TextView districtName_show;
    private TextView bloodGroup_show;
    private TextView fullName_show;

    private CustomTextView mTitle;
    private Drawable mActionBarBackgroundDrawable;
    private View mCustomView;

    public ProfileFragment() {
        // TODO Auto-generated constructor stub

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container,
                false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),
                "MuseoSansRounded-700.otf");
        userDatabase = new UserDataSource(getActivity());
        userDatabase.open();
        bloodDatabase = new BloodDataSource(getActivity());
        bloodDatabase.open();
        districtDatabase = new DistrictDataSource(getActivity());
        districtDatabase.open();
        userInfo = userDatabase.getAllUserItem();
        bloodItem = new BloodItem();
        bloodItem.setBloodId(userInfo.get(0).getGroupId());
        bloodItem = bloodDatabase.cursorToBloodItem(bloodDatabase
                .bloodItemToCursor(bloodItem));
        distItem = new DistrictItem();
        distItem.setDistId(userInfo.get(0).getDistId());
        distItem = districtDatabase.cursorToDistrictItem(districtDatabase
                .districtItemToCursor(distItem));
        bloodDatabase.close();
        userDatabase.close();
        districtDatabase.close();
        avatar = (TextView) rootView.findViewById(R.id.avatar);
        avatar.setTypeface(tf);
        avatar.setText(bloodItem.getBloodName());
        scrollView = (CustomScrollView) rootView.findViewById(R.id.profile_details_scroll_view);

        userName = (TextView) rootView.findViewById(R.id.username);
        userName.setText(userInfo.get(0).getFirstName());
        userName.setTypeface(tf);

        mobileNumber = (TextView) rootView
                .findViewById(R.id.profile_mobile_number);
        mobileNumber.setTypeface(tf);
        mobileNumber.setText(userInfo.get(0).getMobileNumber());

        districtName = (TextView) rootView.findViewById(R.id.profile_area_name);
        districtName.setText(distItem.getDistName());
        districtName.setTypeface(tf);

        bloodGroup = (TextView) rootView.findViewById(R.id.profile_blood_group);
        bloodGroup.setText(bloodItem.getBloodName());
        bloodGroup.setTypeface(tf);

        fullName = (TextView) rootView.findViewById(R.id.profile_full_name);
        fullName.setText(userInfo.get(0).getFirstName() + " "
                + userInfo.get(0).getLastName());
        fullName.setTypeface(tf);

        bloodGroup_show = (TextView) rootView
                .findViewById(R.id.profile_blood_group_show);
        bloodGroup_show.setTypeface(tf);
        mobileNumber_show = (TextView) rootView
                .findViewById(R.id.profile_mobile_number_show);
        mobileNumber_show.setTypeface(tf);

        districtName_show = (TextView) rootView
                .findViewById(R.id.profile_area_name_show);
        districtName_show.setTypeface(tf);
        fullName_show = (TextView) rootView
                .findViewById(R.id.profile_full_name_show);
        fullName_show.setTypeface(tf);

        profileDetail = (TextView) rootView.findViewById(R.id.profile_details);
        profileDetail.setTypeface(tf);

        profileEdit = (TextView) rootView.findViewById(R.id.profile_edit);
        profileEdit.setTypeface(tf);
        mActionBarBackgroundDrawable = getResources().getDrawable(
                R.drawable.actionbar_background);

        mActionBarBackgroundDrawable.setAlpha(255);

        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        mCustomView = mInflater.inflate(R.layout.request_feed_actionbar, null);

        mTitle = (CustomTextView) mCustomView
                .findViewById(R.id.actionbar_title_text);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowTitleEnabled(false);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setHomeButtonEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowCustomEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowHomeEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setCustomView(mCustomView);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .hide();
        density = getActivity().getResources().getDisplayMetrics().density;
        final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.profile_details_header_holder);
        scrollView.setOnScrollChangedListener(new CustomScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                final int paddingTop = 225;
                float ratio = 1.0f - (Math.min(Math.max(t, 0), paddingTop) / paddingTop);
                Log.e(ProfileFragment.class.getSimpleName(), "" + ratio + " " + t);
                final int newPaddingTop = (int) (ratio * 225);
                layout.setPadding(0, (int) (newPaddingTop * density), 0, 0);
            }
        });
        mTitle.setText(R.string.profile);
        return rootView;
    }

}
