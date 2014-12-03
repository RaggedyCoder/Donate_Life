/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.jsonperser;

import com.bluepandora.therap.donatelife.debug.Debug;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class BloodRequestJson {

    private static final String BLOOD_REQUEST = "bloodRequest";
    private static final String MOBILE_NUMBER = "mobileNumber";
    private static final String REQ_TIME = "reqTime";
    private static final String GROUP_ID = "groupId";
    private static final String AMOUNT = "amount";
    private static final String HOSP_ID = "hospitalId";
    private static final String EMERGENCY = "emergency";
    private static final String DONE = "done";

    public static JSONObject getBloodRequestJson(ResultSet result) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
   
        try {
            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(MOBILE_NUMBER, result.getString("mobile_number"));
                jsonObject.put(REQ_TIME, result.getString("req_time"));
                jsonObject.put(GROUP_ID, result.getString("group_id"));
                jsonObject.put(AMOUNT, result.getString("amount"));
                jsonObject.put(HOSP_ID, result.getString("hospital_id"));
                jsonObject.put(EMERGENCY, result.getString("emergency"));

                jsonArray.put(jsonObject);
            }
            jsonObject = new JSONObject();
            jsonObject.put(BLOOD_REQUEST, jsonArray);
            jsonObject.put(DONE, 1);

        } catch (SQLException error) {
            Debug.debugLog("BLOOD_GROUP RESULT SET: ", error);
            jsonObject = new JSONObject();
            jsonObject.put(DONE, 0);
        }
        return jsonObject;
    }
}
