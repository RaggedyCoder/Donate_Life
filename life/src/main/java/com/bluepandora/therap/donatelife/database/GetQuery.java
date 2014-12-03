/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.database;

import com.bluepandora.therap.donatelife.constant.DbConstant;
import com.bluepandora.therap.donatelife.constant.Enum;

/**
 *
 * @author Biswajit Debnath
 */
public class GetQuery extends DbConstant {

    public static String getBloodGroupListQuery() {
        return "select * from " + T_BLOOD_GROUP + " order by group_id";
    }

    public static String getHospitalListQuery() {
        return "select * from " + T_HOSPITAL + " order by hospital_name";
    }

    public static String getDistrictListQuery() {
        return "select * from " + T_DISTRICT + " order by dist_name";
    }

    public static String getBloodRequestListQuery() {
        return "select * from " + T_BLOOD_REQUEST + " order by req_time desc";
    }

    public static String getBloodGroupNameQuery(String groupId) {
        return "select * from " + T_BLOOD_GROUP + " where group_id=" + groupId;
    }

    public static String getHospitalNameQuery(String hospitalId) {
        return "select * from " + T_HOSPITAL + " where hospital_id=" + hospitalId;
    }

    public static String getUserProfileQuery(String mobileNumber, String keyWord) {
        return "select * from ("
                + "(select * from " + T_PERSON_INFO + " where mobile_number='" + mobileNumber + "' and key_word='" + keyWord + "')"
                + "as inf join (select * from " + T_PERSON + " where person_id ="
                + "(select person_id from " + T_PERSON_INFO + " where mobile_number='" + mobileNumber + "' and key_word='" + keyWord + "') "
                + ") as pname)";
    }

    public static String getDonationRecordQuery(String mobileNumber) {
        return "select * from " + T_DONATION_RECORD + " where mobile_number='" + mobileNumber + "'";
    }

    public static String mobileNumberUsedQuery(String mobileNumber) {
        return "select * from " + T_PERSON_INFO + " where mobile_number='" + mobileNumber + "'";
    }

    public static String addPersonNameQuery(String firstName, String lastName) {
        return "INSERT INTO " + T_PERSON + " (first_name, last_name) VALUES ('" + firstName + "','" + lastName + "')"
                + "  ON DUPLICATE KEY UPDATE person_id=LAST_INSERT_ID(person_id)";
    }

    public static String addPersonInfo(String mobileNumber, String groupId, String distId, String keyWord, String firstName, String lastName) {
        return "insert into " + T_PERSON_INFO + " (mobile_number, group_id, dist_id, key_word, person_id)( "
                + " select '" + mobileNumber + "'," + groupId + " , " + distId + ",'" + keyWord + "', person_id from " + T_PERSON + " where first_name='" + firstName + "' and last_name='" + lastName + "')";
    }

    public static String addFeedback(String id_user, String subject, String comment) {
        return "insert into " + T_FEEDBACK + "(id_user, subject, comment) values('" + id_user + "','" + subject + "','" + comment + "')";
    }

    public static String updateGCMIdQuery(String mobileNumber, String gcmId) {
        return "update " + T_PERSON_INFO + " set gcm_id='" + gcmId + "' where mobile_number='" + mobileNumber + "'";
    }

    public static String getValidUserQuery(String mobileNumber, String keyWord) {
        return "select * from " + T_PERSON_INFO + " where mobile_number='" + mobileNumber + "' and key_word='" + keyWord + "'";
    }

    public static String getDuplicateBloodGroupQuery(String mobileNumber, String groupId) {
        return "select * from " + T_BLOOD_REQUEST + " where mobile_number='" + mobileNumber + "' and group_id=" + groupId;
    }

    public static String getDuplicateHospitalGroupQuery(String mobileNumber, String hospitalId) {
        return "select * from " + T_BLOOD_REQUEST + " where mobile_number='" + mobileNumber + "' and hospital_id=" + hospitalId;
    }

    public static String addBloodRequestQuery(String reqTime, String mobileNumber, String groupId, String amount, String hospitalId, String emergency) {
        return "insert into " + T_BLOOD_REQUEST + " (req_time, mobile_number, group_id, amount, hospital_id, emergency)"
                + " values ('" + reqTime + "', '" + mobileNumber + "'," + groupId + "," + amount + "," + hospitalId + "," + emergency + ")";
    }

    public static String addBloodRequestTrackerQuery(String mobileNumber, String reqTime) {
        return "insert into " + T_REQUEST_TRACKER + " (mobile_number, req_time) values ('" + mobileNumber + "','" + reqTime + "')";
    }

    public static String getPersonRequestTrackerQuery(String mobileNumber, String date) {
        return "select * from (select mobile_number, date(req_time) as daily_date, count(*) as daily_request from "
                + T_REQUEST_TRACKER + " group by mobile_number, date(req_time)) as trs"
                + " where mobile_number='" + mobileNumber + "' and daily_date like date('" + date + "')";
    }

    public static String removePersonBloodRequestTrackerQuery(String mobileNumber) {
        return "delete from " + T_REQUEST_TRACKER + " where mobile_number='" + mobileNumber + "'";
    }

    public static String addDonationRecordQuery(String mobileNumber, String donationDate, String donationDetail) {
        return "insert into " + T_DONATION_RECORD + " values('" + mobileNumber + "','" + donationDate + "','" + donationDetail + "')";
    }

    public static String removeDonationRecordQuery(String mobileNumber, String donationDate) {
        return "delete from " + T_DONATION_RECORD + " where mobile_number='" + mobileNumber + "' and donation_date='" + donationDate + "'";
    }

    public static String removeBloodRequestQuery(String mobileNumber, String reqTime) {
        return "delete from " + T_BLOOD_REQUEST + " where mobile_number='" + mobileNumber + "' and req_time='" + reqTime + "'";
    }

    public static String getGcmIdOfDonatorQuery(String groupId, String hospitalId, String mobileNumber) {
        return "select mobile_number, gcm_id from " + T_PERSON_INFO + " where group_id = " + groupId + " and dist_id in("
                + "select dist_id from " + T_HOSPITAL + " where hospital_id=" + hospitalId + ") and gcm_id is not null and mobile_number!='" + mobileNumber + "'";
    }

    public static String getGcmIdOfDonatorQuery(String mobileNumber) {
        return "select mobile_number, gcm_id from " + T_PERSON_INFO + " where mobile_number='" + mobileNumber + "' and gcm_id is not null";
    }

    public static String updatePersonInfoQuery(String mobileNumber, String keyWord, String firstName, String lastName, String groupId, String distId) {
        return "update " + T_PERSON_INFO + " set person_id = (select person_id from " + T_PERSON + " where first_name='" + firstName + "' and last_name='" + lastName + "'), group_id=" + groupId + ", dist_id=" + distId + " where mobile_number='" + mobileNumber + "' and key_word='" + keyWord + "'";
    }

    public static String getPersonNameIdQuery(String firstName, String lastName) {
        return "select * from " + T_PERSON + " where first_name='" + firstName + "' and last_name='" + lastName + "'";
    }

    public static String deleteBloodRequestBeforeQuery(int day) {
        return "DELETE FROM " + T_BLOOD_REQUEST + " WHERE req_time < (NOW()-interval " + day + " day)";
    }

    public static String deleteBloodRequestTrackerQuery(int day) {
        return "delete from " + T_REQUEST_TRACKER + " where req_time <(NOW()-interval " + day + " day)";
    }

    public static String findBestDonatorQuery(String groupId, String hospitalId) {
        return "select * from "+T_PERSON_INFO+" where mobile_number not in ("
                + "select mobile_number from "+T_DONATION_RECORD+" where donation_date > (now()-interval 3 month) "
                + "group by mobile_number) and group_id="+groupId+" and dist_id=(select dist_id from "+T_HOSPITAL+" where hospital_id="+hospitalId+")";
    }
}
