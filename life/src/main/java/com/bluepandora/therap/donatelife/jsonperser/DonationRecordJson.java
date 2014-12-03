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

/**
 *
 * @author Biswajit Debnath
 */
public class DonationRecordJson {

    private static String DONATION_RECORD = "donationRecord";
    private static String DONATION_DATE = "donationDate";
    private static String DONATION_DETAIL = "donationDetail";
    private static String DONE = "done";

    public static JSONObject getDonationRecordJson(ResultSet result) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        try {

            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(DONATION_DATE, result.getString("donation_date"));
                jsonObject.put(DONATION_RECORD, result.getString("donation_detail"));
                jsonArray.put(jsonObject);
            }

            if (jsonArray.length() != 0) {
                jsonObject = new JSONObject();
                jsonObject.put(DONATION_RECORD, jsonArray);
                jsonObject.put(DONE, 1);
            } else {
                jsonObject = new JSONObject();
                jsonObject.put("message", Enum.MESSAGE_DONATION_NOT_FOUND);
                jsonObject.put(DONE, 0);
            }

        } catch (SQLException error) {
            Debug.debugLog("BLOOD_GROUP RESULT SET: ", error);
            jsonObject = new JSONObject();
            jsonObject.put(DONE, 0);

        }
        return jsonObject;

    }

}
