package com.project.bluepandora.donatelife.jsonperser;

import android.content.Context;
import android.util.Log;

import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DistrictDataSource;
import com.project.bluepandora.donatelife.datasource.HospitalDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.exception.BloodDatabaseException;
import com.project.bluepandora.donatelife.exception.DistrictDatabaseException;
import com.project.bluepandora.donatelife.exception.HospitalDatabaseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class JSONParser {

    private Context context;

    public JSONParser(Context context) {
        this.context = context;
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
                try {
                    bloodDatabase.createBloodItem(groupId, groupName);
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
                try {
                    districtDatabase.createDistrictItem(distId, distName);
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
                try {
                    hospitalDatabase.createHospitalItem(distId, hospitalId,
                            hospitalName);
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

    public void parseUserInfo(JSONObject response, String mobileNumber,
                              String password) {
        UserDataSource userDataBase = new UserDataSource(context);
        userDataBase.open();
        UserInfoItem item = new UserInfoItem();
        JSONArray feedArray = null;
        try {
            feedArray = response.getJSONArray("profile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (feedArray != null) {
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject temp = null;
                try {
                    temp = (JSONObject) feedArray.get(i);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
                if (temp != null) {
                    try {
                        item.setFirstName(temp.getString("firstName"));
                        item.setLastName(temp.getString("lastName"));
                        item.setKeyWord(password);
                        item.setMobileNumber(mobileNumber);
                        item.setGroupId(Integer.parseInt(temp
                                .getString("groupId")));
                        item.setDistId(Integer.parseInt(temp
                                .getString("distId")));
                        try {
                            userDataBase.createUserInfoItem(item);
                            userDataBase.close();

                        } catch (Exception e) {
                            // Toast.makeText(getActivity(), e.getMessage(),
                            // Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }
        }
    }
}