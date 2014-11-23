package com.project.bluepandora.donatelife.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.adapter.AboutAdapter;
import com.project.bluepandora.donatelife.data.AboutItem;

import java.util.ArrayList;
import java.util.List;

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
public class AboutFragment extends ListFragment {

    private List<AboutItem> aboutItems;
    private View rootView;
    private AboutAdapter mAdapter;

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
        return super.onCreateView(inflater, container, savedInstanceState);
//        WebView wv = (WebView)findViewById(R.id.the_webview);
        //      wv.loadUrl("file:///android_asset/myweb.html");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
