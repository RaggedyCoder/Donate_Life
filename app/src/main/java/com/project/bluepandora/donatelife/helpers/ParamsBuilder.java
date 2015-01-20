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

import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.Item;

import java.util.HashMap;


/**
 * A class for creating the {@link HashMap} for the different request to the server.
 * This class has a implementation of {@link URL} interface to use the pre defined
 * Tags and Values.
 * <p/>
 * <table border="2" width="85%" align="center" frame="hsides" rules="rows">
 * <colgroup align="left" span="3" />
 * <colgroup align="left" />
 * <colgroup align="center" />
 * <colgroup align="center" />
 * <p/>
 * <thead>
 * <tr><th colspan="3">Method</th> <th colspan="25">Description</th></tr>
 * </thead>
 * <p/>
 * <tbody>
 * <tr><th colspan="3" align="left" border="1">{@link #registerRequest(String, String, String, String, String, String)}</th>
 * <td colspan="25"align="right" border="1" >This method is called when the user registration parameter is required.</td>
 * </tr>
 * <p/>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #bloodGroupList()}</th>
 * <td colspan="25"align="right" border="1">This method is called when blood group list parameter is required.</td>
 * </tr>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #districtList()}</th>
 * <td colspan="25"align="right" border="1">This method is called when district list parameter is required.</td>
 * </tr>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #hospitalList()}</th>
 * <td colspan="25"align="right" border="1">This method is called when hospital list parameter is required.</td>
 * </tr>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #registerCheckRequest(String)}</th>
 * <td colspan="25"align="right" border="1">This method is called when is the mobile number already registered
 * in server check parameter is required.</td>
 * </tr>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #userInfoRequest(String, String)}</th>
 * <td colspan="25"align="right" border="1">When a registered user is trying to log in his/her user profile is needed.
 * To get that from the server the parameter which is needed acquired by
 * calling this method.</td>
 * </tr>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #donationRecord(String)}</th>
 * <td colspan="25"align="right" border="1">When a user profile is acquired successfully from the server the user blood donation
 * record is need.This method is called is needed to to call at that time to get
 * parameter for the donation record..</td>
 * </tr>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #bloodRequestFeed(com.project.bluepandora.donatelife.data.Item...)}</th>
 * <td colspan="25"align="right" border="1">This method is called to get the parameter of the blood feed.This method works three different way.
 * If The user choose to filter his/her blood feed then his/her group id or the district id will also
 * be send to this method.</td>
 * </tr>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #updateUserRequest(String, String, String, String, String, String)}</th>
 * <td colspan="25"align="right" border="1">This method is called to get the parameter to update user profile.</td>
 * </tr>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #feedbackRequest(String, String, String)}</th>
 * <td colspan="25"align="right" border="1">This method is called to get the parameter when user is trying to send a feedback.</td>
 * </tr>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #donatorMobileNumber(String, String, String, String)}</th>
 * <td colspan="25"align="right" border="1">This method is called to get the parameter when user is trying to get available donor to
 * send him/her sms.</td>
 * </tr>
 * <p/>
 * <tr><th colspan="3" align="left" border="1">{@link #bloodRequest(String, String, String, String, String, String, String)}</th>
 * <td colspan="25"align="right" border="1">This method is called when the blood request parameter is required.</td>
 * </tr>
 * </tbody>
 * </table>
 * <p/>
 * For example a user is requesting for register for the code snippet will look like this.
 * <pre class="prettyprint">
 * public class RegisterUser{
 * <p/>
 * public sendServerUserInfo(){
 * HashMap  params=
 * ParamsBuilder.registerRequest(
 * "firstName",
 * "lastName",
 * Integer.toString(1),
 * Integer.toString(1),
 * "01*********",
 * "ABab12");
 * CustomRequest registerRequest=
 * new CustomRequest(
 * Method.POST,
 * URL.URL,
 * params,
 * jsonListeners,
 * ErrorListeners);
 * }
 * }
 * </pre>
 */
public final class ParamsBuilder implements URL {

    /**
     * Local static parameter for the server request.
     */
    private static HashMap<String, String> params;

    /**
     * Get the parameter of register request for the server.
     * <p/>
     * <pre class="prettyprint">
     * HashMap<String,String> params= ParamsBuilder.registerRequest("firstName","lastName",
     * Integer.toString(1),Integer.toString(1),"01*********","ABab12");
     * </pre>
     *
     * @param firstName    The First Name of the user.
     * @param lastName     The Last Name of the user.
     * @param distId       The District ID of the user where live.
     * @param groupId      The Blood Group ID of the user.
     * @param mobileNumber The Mobile Number of the user.
     * @param keyWord      The Password of the user.
     * @return {@link #params} The parameter for the server request.
     */
    public static HashMap<String, String> registerRequest(String firstName, String lastName,
                                                          String distId, String groupId,
                                                          String mobileNumber, String keyWord) {
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
        params.put(PASSWORD_TAG, keyWord);
        Log.i("ParamBuilder", "register:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of blood group list request for the server.
     *
     * @return {@link #params} The parameter for the server request.
     */
    public static HashMap<String, String> bloodGroupList() {
        /*
        * Tags and Values for Blood Group List Request
        *
        * static final TAG->requestName.  static final Value->bloodGroupList
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, BLOODLIST_PARAM);
        Log.i("ParamBuilder", "bloodGroupList:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of district list request for the server.
     *
     * @return {@link #params} The parameter for the server request.
     */
    public static HashMap<String, String> districtList() {
        /*
        * Tags and Values for District List Request
        *
        * static final TAG->requestName.  static final Value->districtList
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, DISTRICTLIST_PARAM);
        Log.i("ParamBuilder", "districtList:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of hospital list request for the server.
     *
     * @return {@link #params} The parameter for the server request.
     */
    public static HashMap<String, String> hospitalList() {
        /*
        * Tags and Values for Hospital List Request
        *
        * static final TAG->requestName.  static final Value->hospitalList
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, HOSPITALLIST_PARAM);
        Log.i("ParamBuilder", "hospitalList:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of checking  is the user already registered the for the application.
     * The parameter will be send to the server.
     *
     * @param mobileNumber The Mobile Number of the user.
     * @return {@link #params} The parameter for the server request.
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
        Log.i("ParamBuilder", "isRegistered:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of is the userInfo whom are already registered the for the application.
     * If the user is not registered or the password/userName was entered wrong a message
     * "USER NOT FOUND OR INVALID ID or PASSWORD!" will be send back
     * <p/>
     * The parameter will be send to the server.
     *
     * @param mobileNumber The Mobile Number of the user.this will be taken from the mobileNumberEditText
     *                     from the LogInFragment MainViewHolder.
     * @param keyWord      The Password of the user profile.this will be taken from the passwordEditText
     *                     from the LogInFragment MainViewHolder.
     * @return {@link #params} The parameter for the server request.
     */
    public static HashMap<String, String> userInfoRequest(String mobileNumber, String keyWord) {
        /*
        * Tags and Values for Registered User Check  Request
        *
        * static final TAG->requestName.  static final Value->isRegistered
        *
        * static final TAG->mobileNumber.  Value will be determined by the user
        *
        * static final TAG->keyWord.  Value will be determined by the user
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, USER_INFO);
        params.put(MOBILE_TAG, mobileNumber);
        params.put(PASSWORD_TAG, keyWord);
        Log.i("ParamBuilder", "isRegistered:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of donation record of the user who is already registered.
     * The parameter will be send to the server.
     *
     * @param mobileNumber The Mobile Number of the user.
     * @return {@link #params} The parameter for the server request.
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
        Log.i("ParamBuilder", "getDonationRecord:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of blood feed list request for the server.
     *
     * @return {@link #params} The parameter for the server request.
     */
    public static HashMap<String, String> bloodRequestFeed(Item... items) {
        /*
        * Tags and Values for Blood Request Feed
        *
        * static final TAG->requestName.  static final Value->getBloodRequest
        *
        * static final TAG->groupId.      If the blood group filter is open.User groupId
        *                                 will be send.
        *
        * static final TAG->distId.       If the district filter is open.User distId
        *                                 will be send.
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, BLOODREQUEST_PARAM);
        for (Item item : items) {
            if (item instanceof BloodItem) {
                String groupId = Integer.toString(((BloodItem) item).getBloodId());
                params.put(GROUPID_TAG, groupId);
            } else if (item instanceof DistrictItem) {
                String distId = Integer.toString(((DistrictItem) item).getDistId());
                params.put(DISTRICTID_TAG, distId);
            }
        }
        Log.i("ParamBuilder", "getBloodRequest:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of update user info request for the server.
     * <p/>
     * <pre class="prettyprint">
     * HashMap<String,String> params= ParamsBuilder.updateUserRequest("firstName","lastName",
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
     * @return {@link #params} The parameter for the server request.
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
     * HashMap<String,String> params= ParamsBuilder.feedbackRequest("01*********","Subject","Comment");
     * </pre>
     *
     * @param mobileNumber The Mobile Number of the user.
     * @param subject      The Subject of the feedback.This subject is needed for filtering all the
     *                     feedback request.
     * @param comment      The comment from the user.This part contains the main feedback.
     * @return {@link #params} The parameter for the server request.
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
        Log.i("ParamBuilder", "feedbackRequest:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of donor mobile number request for the server.
     * Server will send the donor number to the user.
     * <p/>
     * <pre class="prettyprint">
     * HashMap<String,String> params= ParamsBuilder.donatorMobileNumber(Integer.toString(1),Integer.toString(1),
     * "01*********","ABab12");
     * </pre>
     *
     * @param hospitalId   The Hospital ID of the user where the blood is needed.Each
     *                     hospital ID is an integer type.It must have to parsed to String.
     * @param groupId      The Blood Group ID of the user.Each group ID is an integer type.
     *                     It must have to parsed to String.
     * @param mobileNumber The Mobile Number of the user.
     * @param keyWord      The Password of the user.
     * @return {@link #params} The parameter for the server request.
     */
    public static HashMap<String, String> donatorMobileNumber(String hospitalId, String groupId,
                                                              String mobileNumber, String keyWord) {

        /*
        * Tags and Values for Donor Mobile Number Request
        *
        * static final TAG->requestName.  static final Value->donatorMobileNumber
        *
        * static final TAG->hospitalId    The id of the hospital where the blood is needed.
        *
        * static final TAG->groupId       The id of the blood group which blood is needed.
        *
        * static final TAG->mobileNumber. The mobile number of the requested user.
        *                                 determined by the user.
        * static final TAG->keyWord.      The keyWord of the request user.
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, EMERGENCY_SMS_REQUEST);
        params.put(MOBILE_TAG, mobileNumber);
        params.put(HOSPITALID_TAG, hospitalId);
        params.put(GROUPID_TAG, groupId);
        params.put(PASSWORD_TAG, keyWord);
        Log.i("ParamBuilder", "feedbackRequest:" + params.toString());
        return params;
    }

    /**
     * Get the parameter of needed blood request.
     * Server will send is the request valid or not.
     * <p/>
     * <pre class="prettyprint">
     * HashMap<String,String> params= ParamsBuilder.bloodRequest("01*********",Integer.toString(1),
     * Integer.toString(1),Integer.toString(1),Integer.toString(1),"ABab12","1971:12:16 05:00:00");
     * </pre>
     *
     * @param mobileNumber The Mobile Number of the user.
     * @param groupId      The Blood Group ID of the user.Each group ID is an integer type.
     *                     It must have to parsed to String.
     * @param amount       The amount of the bag of of blood needed.Amount is used as Integer.
     *                     So It must have to be parsed to String.
     * @param hospitalId   The Hospital ID of the user where the blood is needed.Each
     *                     hospital ID is an integer type.It must have to parsed to String.
     * @param emergency    The 0 or 1 binary emergency value.Emergency value is for the emergency
     *                     request to set is the request emergency or not.As it is an integer it must
     *                     have to be parsed to String.
     * @param keyWord      The Password of the user.
     * @param reqTime      The request time when the request was made.
     * @return {@link #params} The parameter for the server request.
     */
    public static HashMap<String, String> bloodRequest(String mobileNumber,
                                                       String groupId,
                                                       String amount,
                                                       String hospitalId,
                                                       String emergency,
                                                       String keyWord,
                                                       String reqTime) {
        /*
        * Tags and Values for Donor Mobile Number Request
        *
        * static final TAG->requestName.  static final Value->addBloodRequest
        *
        * static final TAG->mobileNumber. The mobile number of the requested user.
        *                                 determined by the user.
        * static final TAG->groupId       The id of the blood group which blood is needed.
        *
        * static final TAG->amount.       The number of blood bags needed. determined by the user.
        *
        * static final TAG->hospitalId    The id of the hospital where the blood is needed.
        *
        * static final TAG->keyWord.      The keyWord of the request user.
        *
        * static final TAG->reqTime.      The time when the blood request made.
        */
        params = new HashMap<String, String>();
        params.put(REQUEST_NAME, ADD_BLOODREQUEST_PARAM);
        params.put(MOBILE_TAG, mobileNumber);
        params.put(GROUPID_TAG, groupId);
        params.put(AMOUNT_TAG, amount);
        params.put(HOSPITALID_TAG, hospitalId);
        params.put(EMERGENCY_TAG, emergency);
        params.put(PASSWORD_TAG, keyWord);
        params.put(REQUESTTIME_TAG, reqTime);
        Log.i("ParamBuilder", "bloodRequest:" + params.toString());
        return params;
    }
}