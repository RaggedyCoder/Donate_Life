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
public class DistrictJson {

    private static final String DIST_ID = "distId";
    private static final String DIST_NAME = "distName";
    private static final String DISTRICT = "district";
    private static final String DONE = "done";

    public static JSONObject getDistrictJson(ResultSet result) throws JSONException {
        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray();
        try {

            while (result.next()) {
                jsonObject = new JSONObject();
                jsonObject.put(DIST_ID, result.getString("dist_id"));
                jsonObject.put(DIST_NAME, result.getString("dist_name"));
                jsonArray.put(jsonObject);
            }
            jsonObject = new JSONObject();
            jsonObject.put(DISTRICT, jsonArray);
            jsonObject.put(DONE, 1);

        } catch (SQLException error) {

            Debug.debugLog("DISTRICT JSON ERROR: ", error);
            jsonObject = new JSONObject();
            jsonObject.put(DONE, 0);
        }
        return jsonObject;

    }
}
