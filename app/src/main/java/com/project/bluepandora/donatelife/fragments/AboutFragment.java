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
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.AboutActivity;
import com.project.bluepandora.donatelife.adapter.AboutAdapter;
import com.project.bluepandora.donatelife.data.AboutItem;
import com.widget.CustomButton;

import java.util.ArrayList;
import java.util.List;


public class AboutFragment extends ListFragment implements AboutActivity.BackPressImp {

    private static boolean licenseView = false;
    private List<AboutItem> aboutItems;
    private View rootView;

    private AboutAdapter mAdapter;
    private WebView wv;
    private Animation animOpen;
    private Animation animClose;
    private ProgressDialog.Builder versionDialogBuilder;
    private ProgressDialog.Builder developerDialogBuilder;
    private Dialog versionDialog;
    private Dialog developerDialog;
    private Animation.AnimationListener mAnimationListener;

    private CustomButton theDoctorFacebook;
    private CustomButton coderBdFacebook;
    private CustomButton theDoctorEmail;
    private CustomButton coderBdEmail;


    public AboutFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.about_list, container, false);
        aboutItems = new ArrayList<AboutItem>();
        String[] header = getResources().getStringArray(R.array.about_list_header);
        String[] body = getResources().getStringArray(R.array.about_list_body);
        for (int i = 0; i < header.length; i++) {
            AboutItem aboutItem = new AboutItem();
            aboutItem.setHeader(header[i]);
            aboutItem.setBody(body[i]);
            aboutItems.add(aboutItem);
        }
        mAdapter = new AboutAdapter(getActivity(), aboutItems);
        animOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.dim_grow);
        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.dim);
        View developerView = inflater.inflate(R.layout.developer_info, null);
        versionDialogBuilder = new ProgressDialog.Builder(getActivity());
        View versionView = inflater.inflate(R.layout.version_info, null);
        versionDialogBuilder.setView(versionView);
        developerDialogBuilder = new ProgressDialog.Builder(getActivity());
        developerDialogBuilder.setView(developerView);
        theDoctorFacebook = (CustomButton) developerView.findViewById(R.id.the_doctor_facebook);
        coderBdFacebook = (CustomButton) developerView.findViewById(R.id.coder_bd_facebook);
        theDoctorEmail = (CustomButton) developerView.findViewById(R.id.the_doctor_email);
        coderBdEmail = (CustomButton) developerView.findViewById(R.id.coder_bd_email);
        versionDialog = versionDialogBuilder.create();
        developerDialog = developerDialogBuilder.create();
        wv = (WebView) rootView.findViewById(R.id.license_view);
        licenseView = getViewVisibility(wv);
        wv.loadUrl("file:///android_asset/licences.html");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setAdapter(mAdapter);
        animClose.setAnimationListener(mAnimationListener);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    versionDialog.show();
                } else if (position == 4) {
                    wv.setVisibility(View.VISIBLE);
                    wv.startAnimation(animOpen);
                    licenseView = true;
                } else if (position == 5) {
                    developerDialog.show();
                }
            }
        });
        theDoctorFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tumanisdevil"));
                startActivity(browserIntent);
            }
        });
        coderBdFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=100000043940885"));
                startActivity(browserIntent);
            }
        });
        theDoctorEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sajid.sust.cse@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Donate Life:Contact");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
        coderBdEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"biswajit.sust@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Donate Life:Contact");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (wv.getVisibility() == View.VISIBLE) {
            licenseView = false;
            wv.startAnimation(animClose);
        } else {
            licenseView = true;
        }
        return licenseView;
    }

    private boolean getViewVisibility(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                wv.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }

}
