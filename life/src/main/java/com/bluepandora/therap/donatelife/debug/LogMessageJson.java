/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.debug;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class LogMessageJson {

    public static JSONObject getLogMessageJson(int logValue, String logMessage) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("done", logValue);
        jsonObject.put("message", logMessage);
        return jsonObject;
    }

    public static JSONObject getLogMessageJson(int logValue, String logMessage, String requestName) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("done", logValue);
        jsonObject.put("message", logMessage);
        jsonObject.put("requestName", requestName);
        return jsonObject;
    }

    public static JSONObject getLogMessageJson(Object... logs) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if (logs.length % 2 == 0) {
            for (int index = 0; index < logs.length; index += 2) {
                jsonObject.put((String) logs[index], logs[index + 1]);
            }
        }
        return jsonObject;
    }
}
