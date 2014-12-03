/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.jsonperser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class RequestNameAdderJson {

    public static JSONObject setRequestNameInJson(JSONObject jsonObject, String requestName) throws JSONException {
        jsonObject.put("requestName", requestName);
        return jsonObject;
    }
    
}
