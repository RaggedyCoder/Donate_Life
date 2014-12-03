/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.gcmservice;

import com.bluepandora.therap.donatelife.constant.DbUser;
import com.bluepandora.therap.donatelife.database.DatabaseService;
import com.bluepandora.therap.donatelife.database.GetQuery;
import com.bluepandora.therap.donatelife.debug.Debug;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;

import com.google.android.gcm.server.Sender;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Biswajit Debnath
 */
public class GcmService {

    private static final String GOOGLE_SERVER_KEY = "AIzaSyA_1Xk1GFUu_T6bth1erowm4hD6nTCAoFw";
    private static final String MESSAGE_KEY = "message";

    private static DatabaseService dbService = new DatabaseService(
            DbUser.DRIVER_NAME,
            DbUser.DATABASEURL,
            DbUser.USERNAME,
            DbUser.PASSWORD
    );

    public static void giveGCMService(HttpServletRequest request, HttpServletResponse response) {

        if (request.getParameter("groupId") != null && request.getParameter("hospitalId") != null && request.getParameter("mobileNumber") != null) {
            String groupId = request.getParameter("groupId");
            String hospitalId = request.getParameter("hospitalId");
            String mobileNumber = request.getParameter("mobileNumber");

            String donatorMessage = getMessage(groupId, hospitalId);
            Debug.debugLog("Donator Message:", donatorMessage);
            List gcmIDList = FindDonator.findDonatorGCMId(groupId, hospitalId);
            Debug.debugLog(gcmIDList);

            int donatorCount = gcmIDList.size();

            if (donatorCount != 0) {
                sendNotificationToDonator(request, response, gcmIDList, donatorMessage);
            }

            gcmIDList = FindDonator.findDonatorGCMId(mobileNumber);

            if (gcmIDList.size() != 0) {

                if (donatorCount <= 1) {
                    sendNotificationToDonator(request, response, gcmIDList, "NOTIFIED " + donatorCount + " PERSON");
                } else {
                    sendNotificationToDonator(request, response, gcmIDList, "NOTIFIED " + donatorCount + " PERSONS");
                }

            }
        }
    }

    private static String getMessage(String groupId, String hospitalId) {
        String query = GetQuery.getBloodGroupNameQuery(groupId);
        ResultSet result = dbService.getResultSet(query);

        String groupName = null;
        String hospitalName = null;
        String message = null;

        try {

            while (result.next()) {
                groupName = result.getString("group_name");
            }
            query = GetQuery.getHospitalNameQuery(hospitalId);

            result = dbService.getResultSet(query);

            while (result.next()) {
                hospitalName = result.getString("hospital_name");
            }

        } catch (SQLException error) {
            Debug.debugLog("BLOOD GROUP NAME SQL ERROR", error);
        }

        if (groupName != null && hospitalName != null) {
            message = "NEED " + groupName + " IN " + hospitalName;
        }

        return message;
    }

    private static void sendNotificationToDonator(HttpServletRequest request, HttpServletResponse response, List donatorList, String donatorMessage) {

        try {
            Sender sender = new Sender(GOOGLE_SERVER_KEY);
            Message message = new Message.Builder()
                    .delayWhileIdle(true).addData(MESSAGE_KEY, donatorMessage)
                    .build();
            sender.send(message, donatorList, 1);
        } catch (IOException ioe) {
            Debug.debugLog("SENDING NOTIFICATION IO EXCEPTION!");
        } catch (Exception error) {
            Debug.debugLog("SENDING NOTIFICATION EXCEPTION!");
        }
    }
}
