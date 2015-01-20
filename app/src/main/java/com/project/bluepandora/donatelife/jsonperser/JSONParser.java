package com.project.bluepandora.donatelife.jsonperser;
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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.project.bluepandora.donatelife.activities.SettingsActivity;
import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DRItem;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.FeedItem;
import com.project.bluepandora.donatelife.data.HospitalItem;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DRDataSource;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.datasource.HospitalDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.exception.BloodDatabaseException;
import com.project.bluepandora.donatelife.exception.DistrictDatabaseException;
import com.project.bluepandora.donatelife.exception.HospitalDatabaseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class JSONParser {

    private static final int DONE = 1;
    private static final int ERROR = 0;
    private static final String NO_DONATION_RECORD = "DONATION RECORD NOT FOUND!";
    private Context context;
    private String TAG;
    private BloodDataSource bloodDatabase;
    private DistrictDataSource districtDatabase;
    private HospitalDataSource hospitalDatabase;
    private UserInfoItem userInfoItem;
    private boolean distFilter;
    private boolean groupFilter;
    private boolean banglaFilter;

    public JSONParser(Context context, String TAG, boolean filter) {
        this.context = context;
        this.TAG = TAG;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (filter) {
            distFilter = preferences.getBoolean(SettingsActivity.DISTRICT_FILTER_TAG, false);
            groupFilter = preferences.getBoolean(SettingsActivity.GROUP_FILTER_TAG, false);
            banglaFilter = preferences.getBoolean(SettingsActivity.LANGUAGE_TAG, false);
            bloodDatabase = new BloodDataSource(context);
            bloodDatabase.open();
            districtDatabase = new DistrictDataSource(context);
            districtDatabase.open();
            hospitalDatabase = new HospitalDataSource(context);
            hospitalDatabase.open();
            UserDataSource userDataSource = new UserDataSource(context);
            userDataSource.open();
            userInfoItem = userDataSource.getAllUserItem().get(0);
            userDataSource.close();
        }
    }

    public JSONParser(Context context, String TAG) {
        this.context = context;
        this.TAG = TAG;
    }


    public ArrayList<String> getMobileNumber(JSONObject response) {
        ArrayList<String> mobileNumber = new ArrayList<String>();
        try {
            JSONArray numbers = response.getJSONArray("number");
            Log.i(TAG, "Total mobile numbers-" + numbers.length());
            for (int i = 0; i < numbers.length(); i++) {
                JSONObject number = numbers.getJSONObject(i);
                mobileNumber.add(number.getString("mobileNumber"));
                Log.i(TAG, i + ". mobile number-" + numbers.get(i));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return mobileNumber;
    }

    public void parseJsonBlood(JSONObject response) {
        BloodDataSource bloodDatabase = new BloodDataSource(context);
        bloodDatabase.open();
        try {

            JSONArray feedArray = response.getJSONArray("bloodGroup");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject temp = (JSONObject) feedArray.get(i);
                int groupId = Integer.parseInt(temp.getString("groupId"));
                String groupName = temp.getString("groupName");
                String banglaGroupName = temp.getString("groupBName");
                try {
                    bloodDatabase.createBloodItem(groupId, groupName, banglaGroupName);
                } catch (BloodDatabaseException e) {
                    Log.e(BloodDatabaseException.BLOODDATABASEEXCEPTION_TAG,
                            e.getMessage());
                }
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
        bloodDatabase.close();
    }

    public void parseJsonDistrict(JSONObject response) {

        DistrictDataSource districtDatabase = new DistrictDataSource(context);
        districtDatabase.open();
        try {
            JSONArray feedArray = response.getJSONArray("district");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject temp = (JSONObject) feedArray.get(i);
                int distId = Integer.parseInt(temp.getString("distId"));
                String distName = temp.getString("distName");
                String banglaDistName = temp.getString("distBName");
                try {
                    districtDatabase.createDistrictItem(distId, distName, banglaDistName);
                } catch (DistrictDatabaseException e) {
                    Log.e(DistrictDatabaseException.DISTRICTDATABASE_EXCEPTION_TAG,
                            e.getMessage());
                }

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        districtDatabase.close();
    }

    public void parseJsonHospital(JSONObject response) {

        HospitalDataSource hospitalDatabase = new HospitalDataSource(context);
        hospitalDatabase.open();
        try {
            JSONArray feedArray = response.getJSONArray("hospital");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject temp = (JSONObject) feedArray.get(i);
                int distId = Integer.parseInt(temp.getString("distId"));
                int hospitalId = Integer.parseInt(temp.getString("hospitalId"));
                String hospitalName = temp.getString("hospitalName");
                String banglaHospitalName = temp.getString("hospitalBName");
                try {
                    hospitalDatabase.createHospitalItem(distId, hospitalId,
                            hospitalName, banglaHospitalName);
                } catch (HospitalDatabaseException e) {

                    Log.e(HospitalDatabaseException.HOSPITALDATABASE_EXCEPTION_TAG,
                            e.getMessage());
                }

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
        hospitalDatabase.close();
    }

    public boolean parseRegistrationCheck(JSONObject response) {
        try {
            int done = response.getInt("done");
            switch (done) {
                case DONE:
                    return true;
                case ERROR:
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseDonationInfo(JSONObject response) {
        DRDataSource drDataSource = new DRDataSource(context);
        drDataSource.open();
        try {
            int done = response.getInt("done");
            switch (done) {
                case DONE:
                    JSONArray array = response.getJSONArray("donationRecord");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        DRItem item = new DRItem();
                        item.setDonationTime(obj.getString("donationDate"));
                        item.setDonationDetails(obj.getString("donationRecord"));
                        drDataSource.createDRItem(item);
                    }
                    drDataSource.close();
                    return true;
                case ERROR:
                    final String message = response.getString("message");
                    return message.equals(NO_DONATION_RECORD);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parseUserInfo(JSONObject response, String mobileNumber, String keyWord) {
        UserDataSource userDataBase = new UserDataSource(context);
        userDataBase.open();
        UserInfoItem item = new UserInfoItem();
        try {
            int done = response.getInt("done");
            switch (done) {
                case DONE:
                    JSONArray feedArray = response.getJSONArray("profile");
                    for (int i = 0; i < feedArray.length(); i++) {
                        JSONObject temp = feedArray.getJSONObject(i);
                        item.setFirstName(temp.getString("firstName"));
                        item.setLastName(temp.getString("lastName"));
                        item.setGroupId(temp.getInt("groupId"));
                        item.setDistId(temp.getInt("distId"));
                        item.setMobileNumber(mobileNumber);
                        item.setKeyWord(keyWord);
                        userDataBase.createUserInfoItem(item);
                        userDataBase.close();
                    }
                    return true;
                case ERROR:
                    return false;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    public void closeAlldatabase() {
        bloodDatabase.close();
        districtDatabase.close();
        hospitalDatabase.close();
    }

    public FeedItem parseJsonFeed(JSONObject jsonObject) throws JSONException {
        FeedItem item = new FeedItem();
        String mobile = jsonObject.getString("mobileNumber");
        String timestamp = jsonObject.getString("reqTime");
        String emergency = jsonObject.getString("emergency").compareTo("1") == 0 ? "yes" : null;
        int amount = jsonObject.getInt("amount");
        BloodItem bloodItem = new BloodItem();
        bloodItem.setBloodId(Integer.parseInt(jsonObject.getString("groupId")));
        if (groupFilter && userInfoItem.getGroupId() != bloodItem.getBloodId()) {
            return null;
        }
        bloodItem = bloodDatabase.cursorToBloodItem(bloodDatabase.bloodItemToCursor(bloodItem));
        HospitalItem hospitalItem = new HospitalItem();
        hospitalItem.setHospitalId(Integer.parseInt(jsonObject.getString("hospitalId")));
        hospitalItem = hospitalDatabase.cursorToHospitalItem(hospitalDatabase.hospitalItemToCursor(hospitalItem));

        DistrictItem districtItem = new DistrictItem();
        districtItem.setDistId(hospitalItem.getDistId());
        districtItem = districtDatabase.cursorToDistrictItem(districtDatabase.districtItemToCursor(districtItem));
        if (distFilter && userInfoItem.getDistId() != districtItem.getDistId()) {
            return null;
        }
        item.setContact(mobile);
        item.setTimeStamp(timestamp);
        item.setEmergency(emergency);
        item.setBloodAmount(amount);
        if (banglaFilter) {
            item.setBloodGroup(bloodItem.getBanglaBloodName());
            item.setHospital(hospitalItem.getBanglaHospitalName());
            item.setArea(districtItem.getBanglaDistName());
        } else {
            item.setBloodGroup(bloodItem.getBloodName());
            item.setHospital(hospitalItem.getHospitalName());
            item.setArea(districtItem.getDistName());
        }
        return item;
    }
}