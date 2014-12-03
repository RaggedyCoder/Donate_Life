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
public class HospitalJson {

    private static final String HOSPITAL = "hospital";
    private static final String DIST_ID = "distId";
    private static final String HOSP_ID = "hospitalId";
    private static final String HOSP_NAME = "hospitalName";
    private static final String DONE = "done";

    public static JSONObject getHospitalJson(ResultSet result) throws JSONException {
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        try {

            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(DIST_ID, result.getString("dist_id"));
                jsonObject.put(HOSP_ID, result.getString("hospital_id"));
                jsonObject.put(HOSP_NAME, result.getString("hospital_name"));
                jsonArray.put(jsonObject);
            }
            jsonObject = new JSONObject();
            jsonObject.put(HOSPITAL, jsonArray);
            jsonObject.put(DONE, 1);

        } catch (SQLException error) {

            Debug.debugLog("HOSPITAL JSON ERROR: ", error);
            jsonObject = new JSONObject();
            jsonObject.put(DONE, 0);
        }
        return jsonObject;
    }
}
