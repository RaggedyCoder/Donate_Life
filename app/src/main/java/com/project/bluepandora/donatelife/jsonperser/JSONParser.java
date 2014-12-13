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
import android.util.Log;

import com.project.bluepandora.donatelife.data.DRItem;
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
}