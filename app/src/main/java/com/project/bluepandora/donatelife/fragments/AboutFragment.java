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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.AboutActivity;
import com.project.bluepandora.donatelife.adapter.AboutAdapter;
import com.project.bluepandora.donatelife.data.AboutItem;
import com.widget.CustomButton;

import java.util.ArrayList;
import java.util.List;


public class AboutFragment extends Fragment implements AboutActivity.BackPressImp, AdapterView.OnItemClickListener {

    // Defines a tag for identifying log entries
    private static final String TAG = AboutFragment.class.getSimpleName();

    // Defines the Subject when a mail will be send to the developers
    private static final String DEVELOPER_CONTACT_MAIL_SUBJECT = "Donate Life:Contact";

    // Defines the path of the license view url.
    private static final String LICENSE_URL = "file:///android_asset/licences.html";


    private static final int VERSION_DETAIL = 0;
    private static final int TERMS_DETAIL = 1;
    private static final int PRIVACY_DETAIL = 2;
    private static final int USAGE_DETAIL = 3;
    private static final int LICENSE_DETAIL = 4;
    private static final int DEVELOPERS_DETAIL = 5;

    private static boolean licenseViewVisibility = false;
    private List<AboutItem> aboutItems;
    private View rootView;
    private View developerView;
    private View versionView;

    private AboutAdapter mAdapter;
    private Animation animOpen;
    private Animation animClose;

    private Animation.AnimationListener mAnimationListener;

    private MainViewHolder mainViewHolder;
    private DeveloperDialogViewHolder developerDialogViewHolder;
    private VersionViewHolder versionViewHolder;

    private OnClickListeners onClickListeners;

    public AboutFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutItems = new ArrayList<AboutItem>();
        String[] header = getResources().getStringArray(R.array.about_list_header);
        String[] body = getResources().getStringArray(R.array.about_list_body);
        for (int i = 0; i < header.length; i++) {
            AboutItem aboutItem = new AboutItem();
            aboutItem.setHeader(header[i]);
            aboutItem.setBody(body[i]);
            aboutItems.add(aboutItem);
        }
        Log.i(TAG, "onCreate Method Called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.about_list, container, false);
            mainViewHolder = new MainViewHolder();
            mainViewHolder.licenseView = (WebView) rootView.findViewById(R.id.license_view);
            mainViewHolder.aboutListView = (ListView) rootView.findViewById(R.id.about_list_view);
            rootView.setTag(mainViewHolder);
        } else {
            mainViewHolder = (MainViewHolder) rootView.getTag();
        }

        if (developerView == null) {
            developerView = inflater.inflate(R.layout.developer_info, container, false);
            developerDialogViewHolder = new DeveloperDialogViewHolder();
            developerDialogViewHolder.theDoctorFacebook = (CustomButton) developerView.findViewById(R.id.the_doctor_facebook);
            developerDialogViewHolder.coderBdFacebook = (CustomButton) developerView.findViewById(R.id.coder_bd_facebook);
            developerDialogViewHolder.theDoctorEmail = (CustomButton) developerView.findViewById(R.id.the_doctor_email);
            developerDialogViewHolder.coderBdEmail = (CustomButton) developerView.findViewById(R.id.coder_bd_email);
            developerDialogViewHolder.developerDialogBuilder = new ProgressDialog.Builder(getActivity());
            developerDialogViewHolder.developerDialogBuilder.setView(developerView);
            developerDialogViewHolder.developerDialog = developerDialogViewHolder.developerDialogBuilder.create();
            developerView.setTag(developerDialogViewHolder);
        } else {
            developerDialogViewHolder = (DeveloperDialogViewHolder) developerView.getTag();
        }

        if (versionView == null) {
            versionView = inflater.inflate(R.layout.version_info, container, false);
            versionViewHolder = new VersionViewHolder();
            versionViewHolder.versionDialogBuilder = new ProgressDialog.Builder(getActivity());
            versionViewHolder.versionDialogBuilder.setView(versionView);
            versionViewHolder.versionDialog = versionViewHolder.versionDialogBuilder.create();
            versionView.setTag(versionViewHolder);
        } else {
            versionViewHolder = (VersionViewHolder) versionView.getTag();
        }
        mAdapter = new AboutAdapter(getActivity(), aboutItems);
        animOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.dim_grow);
        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.dim);
        licenseViewVisibility = getViewVisibility(mainViewHolder.licenseView);
        mainViewHolder.licenseView.loadUrl(LICENSE_URL);
        Log.i(TAG, "onCreateView Method Called");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewHolder.aboutListView.setAdapter(mAdapter);
        animClose.setAnimationListener(mAnimationListener);
        mainViewHolder.aboutListView.setOnItemClickListener(this);
        developerDialogViewHolder.theDoctorFacebook.setOnClickListener(onClickListeners);
        developerDialogViewHolder.coderBdFacebook.setOnClickListener(onClickListeners);
        developerDialogViewHolder.theDoctorEmail.setOnClickListener(onClickListeners);
        developerDialogViewHolder.coderBdEmail.setOnClickListener(onClickListeners);
        Log.i(TAG, "onActivityCreated Method Called");
    }

    @Override
    public boolean onBackPressed() {
        if (getViewVisibility(mainViewHolder.licenseView)) {
            licenseViewVisibility = false;
            mainViewHolder.licenseView.startAnimation(animClose);
        } else {
            licenseViewVisibility = true;
        }
        return licenseViewVisibility;
    }

    private boolean getViewVisibility(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    private boolean getViewEquals(View view1, View view2) {
        return view1.equals(view2);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onClickListeners = new OnClickListeners();
        mAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainViewHolder.licenseView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        Log.i(TAG, "onAttach Method Called");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "aboutListView Item Clicked where-");
        switch (position) {
            case VERSION_DETAIL:
                versionViewHolder.versionDialog.show();
                Log.i(TAG, "Donate Life selected");
                break;
            case TERMS_DETAIL:
                Log.i(TAG, "Terms of service selected");
                break;
            case PRIVACY_DETAIL:
                Log.i(TAG, "Privacy Policy selected");
                break;
            case USAGE_DETAIL:
                Log.i(TAG, "Usage Policy selected");
                break;
            case LICENSE_DETAIL:
                mainViewHolder.licenseView.setVisibility(View.VISIBLE);
                mainViewHolder.licenseView.startAnimation(animOpen);
                licenseViewVisibility = true;
                Log.i(TAG, "Open Source Licenses selected");
                break;
            case DEVELOPERS_DETAIL:
                developerDialogViewHolder.developerDialog.show();
                Log.i(TAG, "About Developers selected");
                break;
        }
    }

    private static class MainViewHolder {
        private WebView licenseView;
        private ListView aboutListView;
    }

    private static class VersionViewHolder {
        private ProgressDialog.Builder versionDialogBuilder;
        private Dialog versionDialog;
    }

    private static class DeveloperDialogViewHolder {
        private CustomButton theDoctorFacebook;
        private CustomButton coderBdFacebook;
        private CustomButton theDoctorEmail;
        private CustomButton coderBdEmail;
        private ProgressDialog.Builder developerDialogBuilder;
        private Dialog developerDialog;
    }

    private class OnClickListeners implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (getViewEquals(view, developerDialogViewHolder.theDoctorFacebook)) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.the_doctor_facebook)));
                startActivity(browserIntent);
                Log.i(TAG, "Facebook for the doctor selected");
            } else if (getViewEquals(view, developerDialogViewHolder.theDoctorEmail)) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.the_doctor_email));
                intent.putExtra(Intent.EXTRA_SUBJECT, DEVELOPER_CONTACT_MAIL_SUBJECT);
                startActivity(Intent.createChooser(intent, ""));
                Log.i(TAG, "Email for the doctor selected");
            } else if (getViewEquals(view, developerDialogViewHolder.coderBdFacebook)) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.coder_bd_facebook)));
                startActivity(browserIntent);
                Log.i(TAG, "Facebook for coder.bd selected");
            } else if (getViewEquals(view, developerDialogViewHolder.coderBdEmail)) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.coder_bd_email));
                intent.putExtra(Intent.EXTRA_SUBJECT, DEVELOPER_CONTACT_MAIL_SUBJECT);
                startActivity(Intent.createChooser(intent, ""));
                Log.i(TAG, "Email for coder.bd selected");
            }
        }
    }
}
