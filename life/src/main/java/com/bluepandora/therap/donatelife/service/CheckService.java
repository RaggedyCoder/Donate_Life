/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.service;

import com.bluepandora.therap.donatelife.constant.DbUser;
import com.bluepandora.therap.donatelife.database.DatabaseService;
import com.bluepandora.therap.donatelife.database.GetQuery;
import com.bluepandora.therap.donatelife.debug.Debug;
import com.bluepandora.therap.donatelife.debug.LogMessageJson;
import com.bluepandora.therap.donatelife.jsonsender.SendJsonData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Biswajit Debnath
 */
public class CheckService {

    private static DatabaseService dbService = new DatabaseService(
            DbUser.DRIVER_NAME,
            DbUser.DATABASEURL,
            DbUser.USERNAME,
            DbUser.PASSWORD
    );

    public static boolean isMobileNumberTaken(String mobileNumber) {
        String query = GetQuery.mobileNumberUsedQuery(mobileNumber);
        ResultSet result = dbService.getResultSet(query);

        boolean mobileNumberRegistered = false;

        try {
            while (result.next()) {
                mobileNumberRegistered = true;
            }
        } catch (SQLException error) {
            Logger.getLogger(CheckService.class.getName()).log(Level.SEVERE, null, error);
        }

        return mobileNumberRegistered;
    }

    public static boolean isValidUser(String mobileNumber, String hashKey) {
        String query = GetQuery.getValidUserQuery(mobileNumber, hashKey);
       // Debug.debugLog("VALID USER QUERY: ", query);
        ResultSet result = dbService.getResultSet(query);
        boolean USER_VALID = false;
        try {
            while (result.next()) {
                USER_VALID = true;
            }
        } catch (SQLException error) {
            Logger.getLogger(CheckService.class.getName()).log(Level.SEVERE, null, error);
        }
        return USER_VALID;
    }

    public static boolean isDuplicateBloodGroup(String mobileNumber, String groupId) {
        String query = GetQuery.getDuplicateBloodGroupQuery(mobileNumber, groupId);
       // Debug.debugLog("DUPLICATE BLOOD GROUP QUERY: ", query);
        ResultSet result = dbService.getResultSet(query);
        boolean VALID_GROUP = true;
        try {
            while (result.next()) {
                VALID_GROUP = false;
            }
        } catch (SQLException err) {
            Logger.getLogger(CheckService.class.getName()).log(Level.SEVERE, null, err);
        }
        return VALID_GROUP;
    }

    public static boolean isDuplicateHospital(String mobileNumber, String hospitalId) {
        String query = GetQuery.getDuplicateHospitalGroupQuery(mobileNumber, hospitalId);
        //Debug.debugLog("DUPLICATE HOSPITAL QUERY: ", query);
        ResultSet result = dbService.getResultSet(query);
        boolean VALID_HOSPITAL = true;
        try {
            while (result.next()) {
                VALID_HOSPITAL = false;
            }
        } catch (SQLException error) {
            Logger.getLogger(CheckService.class.getName()).log(Level.SEVERE, null, error);
        }

        return VALID_HOSPITAL;
    }

    public static int requestTracker(String mobileNumber, String date) {
        String query = GetQuery.getPersonRequestTrackerQuery(mobileNumber,date);
       // Debug.debugLog("GET REQUEST: ", query);
        ResultSet result = dbService.getResultSet(query);
        int totalRequestFound = 0;
        String dailyRequest = "0";
        try {
            while (result.next()) {
                dailyRequest = (String) result.getString("daily_request");
            }
            totalRequestFound = Integer.parseInt(dailyRequest);
        } catch (SQLException error) {
            Logger.getLogger(CheckService.class.getName()).log(Level.SEVERE, null, error);
        }
        return totalRequestFound;
    }

    public static boolean isNameAlreadyAdded(String firstName, String lastName) {
        String query = GetQuery.getPersonNameIdQuery(firstName, lastName);
        ResultSet result = dbService.getResultSet(query);

        boolean nameTaken = false;

        try {
            while (result.next()) {
                nameTaken = true;
            }
        } catch (SQLException error) {
            Logger.getLogger(CheckService.class.getName()).log(Level.SEVERE, null, error);
        }

        return nameTaken;
    }
}
