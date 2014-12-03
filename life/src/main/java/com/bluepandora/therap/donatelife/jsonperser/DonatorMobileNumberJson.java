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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author Biswajit Debnath
 */
public class DonatorMobileNumberJson {

    public static JSONObject getDonatorMobileNumberJson(ResultSet result) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        try {

            while (result.next()) {
                jsonObject = new JSONObject();
                String mobileNumber = result.getString("mobile_number");
                jsonObject.put("mobileNumber", mobileNumber);
                jsonArray.put(jsonObject);
            }

            if (jsonArray.length() != 0) {
                jsonObject = new JSONObject();
                jsonObject.put("number", jsonArray);
                jsonObject.put("done", 1);
            } else {
                jsonObject = new JSONObject();
                jsonObject.put("message", Enum.MESSAGE_NO_SUITABLE_DONATOR_FOUND);
                jsonObject.put("done", 0);
            }

        } catch (SQLException error) {
            Debug.debugLog("FINDING DONATOR MOBILE NUMBER EXCPTION OCCURS!");
            jsonObject= new JSONObject();
            jsonObject.put("done", 0);
        }
        
        return jsonObject;
    }
}
