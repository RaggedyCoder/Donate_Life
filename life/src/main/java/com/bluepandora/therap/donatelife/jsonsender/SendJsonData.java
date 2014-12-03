/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.jsonsender;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class SendJsonData {

    public static void sendJsonData(HttpServletRequest request, HttpServletResponse response, JSONObject object) {
        response.setContentType("application/json");
        System.out.println(object);
        try {
            PrintWriter out = response.getWriter();
            out.print(object);
            out.flush();
        } catch (IOException io) {
            io.printStackTrace();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
}
