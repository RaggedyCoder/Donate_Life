/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.jsonperser;

import com.bluepandora.therap.donatelife.debug.Debug;
import com.bluepandora.therap.donatelife.constant.Enum;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileJson {

    private static final String PROFILE = "profile";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String GROUP_ID = "groupId";
    private static final String DIST_ID = "distId";
    private static final String DONE = "done";

    public static JSONObject getUserProfileJson(ResultSet result) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        try {
            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(FIRST_NAME, result.getString("first_name"));
                jsonObject.put(LAST_NAME, result.getString("last_name"));
                jsonObject.put(GROUP_ID, result.getString("group_id"));
                jsonObject.put(DIST_ID, result.getString("dist_id"));
                jsonArray.put(jsonObject);
            }
            if (jsonArray.length() != 0) {
                jsonObject = new JSONObject();
                jsonObject.put(PROFILE, jsonArray);
                jsonObject.put(DONE, 1);
            } else {
                jsonObject = new JSONObject();
                jsonObject.put("message", Enum.MESSAGE_INVALID_USER);
                jsonObject.put(DONE, 0);
            }
        } catch (SQLException error) {
            Debug.debugLog("USER PROFILE RESULT DATA : ", error);
            jsonObject = new JSONObject();
            jsonObject.put(DONE, 0);
        }
        return jsonObject;
    }
}
