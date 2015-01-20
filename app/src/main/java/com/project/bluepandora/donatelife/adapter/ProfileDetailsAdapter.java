package com.project.bluepandora.donatelife.adapter;
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
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.SettingsActivity;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.fragments.ProfileDetailsFragment;
import com.project.bluepandora.donatelife.helpers.DialogBuilder;
import com.project.bluepandora.donatelife.helpers.NumericalExchange;
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
import java.util.List;


public class ProfileDetailsAdapter extends BaseAdapter {

    private static final int NAME = 0;
    private static final int MOBILE = 1;
    private static final int DISTRICT = 2;
    private static final int GROUP = 3;
    private static final int RECORD = 4;
    UserInfoItem infoItem;
    List<String> header;
    private ProfileDetailsFragment fragment;
    private DistrictItem distItem;
    private BloodItem bloodItem;
    private Activity activity;
    private LayoutInflater inflater;
    private BloodDataSource bloodDatabase;
    private Resources resources;
    private DistrictDataSource districtDatabase;
    private SpinnerAdapter districtSpinnerAdapter;
    private ArrayList<Item> districtItems;
    private ProgressDialog.Builder editProfileDialogBuilder;
    private Dialog editProfileDialog;
    private DialogBuilder dialogBuilder;
    private View editProfile;
    private boolean banglaFilter;

    public ProfileDetailsAdapter(
            Activity activity,
            ProfileDetailsFragment fragment,
            UserInfoItem infoItem,
            List<String> header) {
        this.activity = activity;
        this.fragment = fragment;
        this.infoItem = infoItem;
        this.header = header;
        bloodDatabase = new BloodDataSource(activity);
        bloodDatabase.open();
        districtDatabase = new DistrictDataSource(activity);
        districtDatabase.open();
        bloodItem = new BloodItem();
        bloodItem.setBloodId(infoItem.getGroupId());
        bloodItem = bloodDatabase.cursorToBloodItem(bloodDatabase
                .bloodItemToCursor(bloodItem));
        distItem = new DistrictItem();
        distItem.setDistId(infoItem.getDistId());
        distItem = districtDatabase.cursorToDistrictItem(districtDatabase
                .districtItemToCursor(distItem));
        districtItems = districtDatabase.getAllDistrictItem();
        districtSpinnerAdapter = new SpinnerAdapter(activity, districtItems);
        bloodDatabase.close();
        districtDatabase.close();
        resources = activity.getResources();
        editProfileDialogBuilder = new ProgressDialog.Builder(activity);
        dialogBuilder = new DialogBuilder(activity, "");
        banglaFilter = PreferenceManager.getDefaultSharedPreferences(
                activity).getBoolean(SettingsActivity.LANGUAGE_TAG, false);

    }

    @Override
    public int getCount() {
        return header.size();
    }


    @Override
    public Object getItem(int position) {
        return header.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final DialogViewHolder editProfileHolder;
        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.profile_details, parent, false);
            holder = new ViewHolder();
            holder.profileHeaderHolder = (RelativeLayout) convertView.findViewById(R.id.profile_header_holder);
            holder.profileEdit = (ImageButton) convertView.findViewById(R.id.profile_edit);
            holder.header = (CustomTextView) convertView.findViewById(R.id.details_header);
            holder.description = (CustomTextView) convertView.findViewById(R.id.details_body);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (editProfile == null) {
            editProfile = inflater.inflate(R.layout.edit_profile, parent, false);
            editProfileHolder = new DialogViewHolder();
            editProfileHolder.districtSpinner = (Spinner) editProfile.findViewById(R.id.district_spinner);
            editProfileHolder.districtSpinner.setAdapter(districtSpinnerAdapter);
            editProfileHolder.firstNameEditText = (CustomEditText) editProfile.findViewById(R.id.first_name_edit_text);
            editProfileHolder.lastNameEditText = (CustomEditText) editProfile.findViewById(R.id.last_name_edit_text);
            editProfileHolder.confirmButton = (CustomButton) editProfile.findViewById(R.id.confirm_button);
            editProfileDialogBuilder.setView(editProfile);
            editProfileHolder.confirmPassword = (CustomEditText) editProfile.findViewById(R.id.confirm_password);
            editProfileDialog = editProfileDialogBuilder.create();
            editProfileDialog.setCanceledOnTouchOutside(false);
            editProfileDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                    notifyDataSetChanged();
                }
            });
            editProfile.setTag(editProfileHolder);
        } else {
            editProfileHolder = (DialogViewHolder) editProfile.getTag();
        }
        if (position != 0) {
            holder.profileHeaderHolder.setVisibility(View.GONE);
            holder.profileEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editProfileHolder.firstNameEditText.setText(infoItem.getFirstName());
                    editProfileHolder.lastNameEditText.setText(infoItem.getLastName());
                    editProfileHolder.confirmButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!editProfileHolder.confirmPassword.getText().toString().equals(infoItem.getKeyWord())) {
                                dialogBuilder.createAlertDialog("Wrong Password!");
                            } else {
                                HashMap<String, String> params = ParamsBuilder.updateUserRequest(
                                        editProfileHolder.firstNameEditText.getText().toString(),
                                        editProfileHolder.lastNameEditText.getText().toString(),
                                        Integer.toString(((DistrictItem) editProfileHolder.districtSpinner.getSelectedItem()).getDistId()),
                                        Integer.toString(infoItem.getGroupId()),
                                        infoItem.getMobileNumber(),
                                        infoItem.getKeyWord());
                                updateUserInfo(params);
                                dialogBuilder.createProgressDialog(resources.getString(R.string.processing));
                            }
                        }
                    });
                    editProfileHolder.districtSpinner.setSelection(
                            districtSpinnerAdapter.getItemPosition(infoItem.getDistId()));
                    editProfileDialog.show();
                }
            });
        } else {
            holder.profileHeaderHolder.setVisibility(View.VISIBLE);
        }
        holder.header.setText(header.get(position));
        holder.description.setText(getDescription(position));
        return convertView;
    }

    private String getDescription(int position) {
        switch (position) {
            case NAME:
                return infoItem.getFirstName() + " " + infoItem.getLastName();
            case MOBILE:
                if (banglaFilter) {
                    return NumericalExchange.toBanglaNumerical(infoItem.getMobileNumber());
                } else {
                    return infoItem.getMobileNumber();
                }
            case DISTRICT:
                if (banglaFilter) {
                    return distItem.getBanglaDistName();
                } else {
                    return distItem.getDistName();
                }

            case GROUP:
                if (banglaFilter) {
                    return bloodItem.getBanglaBloodName();
                } else {
                    return bloodItem.getBloodName();
                }

            case RECORD:
                int amount = Integer.parseInt(infoItem.getTotalDonation());
                String quantity = resources.getQuantityString(R.plurals.donation, amount, amount);
                if (quantity.equals(resources.getString(R.string.zero_grammar_error))) {
                    return resources.getString(R.string.zero_donation_record);
                } else {
                    return quantity;
                }
            default:
                return null;
        }
    }

    private void updateUserInfo(final HashMap<String, String> params) {
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, URL.URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                dialogBuilder.getProgressDialog().dismiss();
                try {
                    Log.e("TAG", jsonObject.toString(1) + params.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (jsonObject.getInt("done") == 1) {
                        dialogBuilder.createAlertDialog("Alert!", "Profile is updated.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editProfileDialog.dismiss();
                                notifyDataSetChanged();
                            }
                        });
                        notifyFragment();
                        Log.e("TAG", jsonObject.toString(1) + params.toString());
                        dialogBuilder.getProgressDialog().dismiss();
                        infoItem.setFirstName(params.get("firstName"));
                        infoItem.setLastName(params.get("lastName"));
                        infoItem.setDistId(Integer.parseInt(params.get("distId")));
                        districtDatabase.open();
                        distItem = districtDatabase.cursorToDistrictItem(districtDatabase.districtItemToCursor(new DistrictItem(infoItem.getDistId(), "", "")));
                        UserDataSource userDataBase = new UserDataSource(activity);
                        userDataBase.open();
                        userDataBase.open();
                        userDataBase.updateUserInfoitem(infoItem);
                        userDataBase.close();
                        ProfileDetailsAdapter.this.notifyDataSetChanged();
                    } else {
                        dialogBuilder.createAlertDialog("Alert!", "Something went wrong!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editProfileDialog.dismiss();
                                notifyDataSetChanged();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialogBuilder.getProgressDialog().dismiss();
                dialogBuilder.createAlertDialog("Alert!", "Please check your internet connection", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editProfileDialog.dismiss();
                        notifyDataSetChanged();
                    }
                });
            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private void notifyFragment() {
        fragment.getmHeaderTitle().setText(infoItem.getFirstName());
        fragment.getUsernameTitle().setText(infoItem.getFirstName());
    }

    static class DialogViewHolder {
        private Spinner districtSpinner;
        private CustomButton confirmButton;
        private CustomEditText firstNameEditText;
        private CustomEditText lastNameEditText;
        private CustomEditText confirmPassword;
    }

    static class ViewHolder {
        RelativeLayout profileHeaderHolder;
        CustomTextView header;
        CustomTextView description;
        ImageButton profileEdit;
    }
}
