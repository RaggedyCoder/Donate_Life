package com.project.bluepandora.donatelife.helpers;
/*
 * Copyright (C) 2014 The Blue Pandora Project Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.util.Log;

import java.util.HashMap;

/**
 * A class for creating the {@link HashMap} for the different request to the server.
 * This class has a implementation of {@link URL} interface to use the pre defined
 * Tags and Values.
 */
public final class ParamsBuilder implements URL {

    //Local static parameter for the server request.
    private static HashMap<String, String> params;

    /**
     * Get the parameter of register request for the server.
     *
     * @param firstName    The First Name of the user.
     * @param lastName     The Last Name of the user.
     * @param distId       The District ID of the user where live.
     * @param groupId      The Blood Group ID of the user.
     * @param mobileNumber The Mobile Number of the user.
     * @param password     The Password of the user.
     * @return The params which is hash mapped.
     */
    public static HashMap<String, String> registerRequest(String firstName, String lastName,
                                                          String distId, String groupId,
                                                          String mobileNumber, String password) {
        /*
        * Tags and Values for Registration Request
        *
        * static final TAG->requestName.  static final Value->register
        *
        * static final TAG->firstName.    Value will be determined by the user.
        *
        * static final TAG->lastName.     Value will be determined by the user.
        *
        * static final TAG->groupId.      Value will be determined by the user.
        *
        * static final TAG->distId.       Value will be determined by the user.
        *
        * static final TAG->mobileNumber. Value will be determined by the user.
        *
        * static final TAG->keyWord.      Value will be determined by the user
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, REGISTER_REQUEST_PARAM);
        params.put(FIRST_NAME_KEY, firstName);
        params.put(LAST_NAME_KEY, lastName);
        params.put(DISTRICTID_TAG, distId);
        params.put(GROUPID_TAG, groupId);
        params.put(MOBILE_TAG, mobileNumber);
        params.put(PASSWORD_TAG, password);
        Log.i("ParamBuilder", params.toString());
        return params;
    }

    /**
     * Get the parameter of blood group list request for the server.
     *
     * @return The params which is hash mapped.
     */
    public static HashMap<String, String> bloodGroupList() {
        /*
        * Tags and Values for Blood Group List Request
        *
        * static final TAG->requestName.  static final Value->bloodGroupList
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, BLOODLIST_PARAM);
        Log.i("ParamBuilder", params.toString());
        return params;
    }

    /**
     * Get the parameter of district list request for the server.
     *
     * @return The params which is hash mapped.
     */
    public static HashMap<String, String> districtList() {
        /*
        * Tags and Values for District List Request
        *
        * static final TAG->requestName.  static final Value->districtList
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, DISTRICTLIST_PARAM);
        Log.i("ParamBuilder", params.toString());
        return params;
    }

    /**
     * Get the parameter of hospital list request for the server.
     *
     * @return The params which is hash mapped.
     */
    public static HashMap<String, String> hospitalList() {
        /*
        * Tags and Values for Hospital List Request
        *
        * static final TAG->requestName.  static final Value->hospitalList
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, HOSPITALLIST_PARAM);
        return params;
    }

    /**
     * Get the parameter of checking  is the user already registered the for the application.
     * The parameter will be send to the server.
     *
     * @param mobileNumber The Mobile Number of the user.
     * @return The params which is hash mapped.
     */
    public static HashMap<String, String> registerCheckRequest(String mobileNumber) {
        /*
        * Tags and Values for Registered User Check  Request
        *
        * static final TAG->requestName.  static final Value->isRegistered
        *
        * static final TAG->mobileNumber.  Value will be determined by the user
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, REGISTER_CHECK);
        params.put(MOBILE_TAG, mobileNumber);
        Log.i("ParamBuilder", params.toString());
        return params;
    }

    /**
     * Get the parameter of donation record of the user who is already registered.
     * The parameter will be send to the server.
     *
     * @param mobileNumber The Mobile Number of the user.
     * @return The params which is hash mapped.
     */
    public static HashMap<String, String> donationRecord(String mobileNumber) {
        /*
        * Tags and Values for User Donation Record List Request
        *
        * static final TAG->requestName.  static final Value->getDonationRecord
        *
        * static final TAG->mobileNumber.  Value will be determined by the user
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, GET_DONATION_RECORD_PARAM);
        params.put(MOBILE_TAG, mobileNumber);
        Log.i("ParamBuilder", params.toString());
        return params;
    }

    /**
     * Get the parameter of blood feed list request for the server.
     *
     * @return The params which is hash mapped.
     */
    public static HashMap<String, String> bloodRequestFeed() {
        /*
        * Tags and Values for Blood Request Feed
        *
        * static final TAG->requestName.  static final Value->getBloodRequest
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, BLOODREQUEST_PARAM);
        return params;
    }

    /**
     * Get the parameter of update user info request for the server.
     * <p/>
     * <pre class="prettyprint">
     * HashMap<String,String> params= updateUserRequest("firstName","lastName",
     * Integer.parseInt(1),Integer.parseInt(1),
     * "01*********","ABab12");
     * </pre>
     *
     * @param firstName    The First Name of the user.
     * @param lastName     The Last Name of the user.
     * @param distId       The District ID of the user where live.
     * @param groupId      The Blood Group ID of the user.
     * @param mobileNumber The Mobile Number of the user.
     * @param password     The Password of the user.
     * @return The params which is hash mapped.
     */
    public static HashMap<String, String> updateUserRequest(String firstName, String lastName,
                                                            String distId, String groupId,
                                                            String mobileNumber, String password) {

        /*
        * Tags and Values for Registration Request
        *
        * static final TAG->requestName.  static final Value->updateUserInfo
        *
        * static final TAG->firstName.    Value will be determined by the user.
        *
        * static final TAG->lastName.     Value will be determined by the user.
        *
        * static final TAG->groupId.      Value will be determined by the user.
        *
        * static final TAG->distId.       Value will be determined by the user.
        *
        * static final TAG->mobileNumber. Value will be determined by the user.
        *
        * static final TAG->keyWord.      Value will be determined by the user
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, USERINFOUPDATE_PARAM);
        params.put(FIRST_NAME_KEY, firstName);
        params.put(LAST_NAME_KEY, lastName);
        params.put(DISTRICTID_TAG, distId);
        params.put(GROUPID_TAG, groupId);
        params.put(MOBILE_TAG, mobileNumber);
        params.put(PASSWORD_TAG, password);
        Log.i("ParamBuilder", "updateUserRequest:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of feedback request from the user.This parameter will be send to the server.
     * <p/>
     * <pre class="prettyprint">
     * HashMap<String,String> params= feedbackRequest("01*********","Subject","Comment");
     * </pre>
     *
     * @param mobileNumber The Mobile Number of the user.
     * @param subject      The Subject of the feedback.This subject is needed for filtering all the
     *                     feedback request.
     * @param comment      The comment from the user.This part contains the main feedback.
     * @return The parameter for the server request.
     */
    public static HashMap<String, String> feedbackRequest(String mobileNumber, String subject, String comment) {

        /*
        * Tags and Values for Registration Request
        *
        * static final TAG->requestName.  static final Value->feedback
        *
        * static final TAG->subject.      Subject of the feedback.Value will be
        *                                 determined by the user.
        *
        * static final TAG->comment.     Value will be determined by the user.
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, FEEDBACK_REQUEST);
        params.put(FEEDBACK_USERNAME_PARAM, mobileNumber);
        params.put(FEEDBACK_SUBJECT_PARAM, subject);
        params.put(FEEDBACK_COMMENT_PARAM, comment);
        Log.i("ParamBuilder", "donatorMobileNumber:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of donner mobile number request for the server.
     * Server will send the donner number to the user.
     * <p/>
     * <pre class="prettyprint">
     * HashMap<String,String> params= donatorMobileNumber(Integer.parseInt(1),Integer.parseInt(1),
     * "01*********","ABab12");
     * </pre>
     *
     * @param hospitalId   The Hospital ID of the user where the blood is needed.Each
     *                     hospital ID is an integer type.It must have to parsed to String.
     * @param groupId      The Blood Group ID of the user.Each group ID is an integer type.
     *                     It must have to parsed to String.
     * @param mobileNumber The Mobile Number of the user.
     * @param password     The Password of the user.
     * @return The parameter for the server request.
     */
    public static HashMap<String, String> donatorMobileNumber(String hospitalId, String groupId,
                                                              String mobileNumber, String password) {

        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, EMERGENCY_SMS_REQUEST);
        params.put(MOBILE_TAG, mobileNumber);
        params.put(HOSPITALID_TAG, hospitalId);
        params.put(GROUPID_TAG, groupId);
        params.put(PASSWORD_TAG, password);
        Log.i("ParamBuilder", "feedbackRequest:" + params.toString());
        return params;
    }
}