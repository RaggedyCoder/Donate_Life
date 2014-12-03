/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bluepandora.therap.donatelife.adminpanel;

import com.bluepandora.therap.donatelife.constant.DbConstant;

/**
 *
 * @author Biswajit Debnath
 */
public class AdminQuery extends DbConstant{
    
    public static String getMobileDetailQuery(String mobileNumber){
        return "select mobile_number, first_name, last_name, dist_name, group_name, gcm_id"
                + " from "+T_PERSON_INFO+" join "+T_PERSON+" using(person_id) join "+T_DISTRICT+" using(dist_id) "
                + "join "+T_BLOOD_GROUP+" using(group_id) where mobile_number like '%"+mobileNumber+"%'";   
    }
    public static String getDonatorListQuery(String groupName, String distName){
        return "select mobile_number, first_name, last_name, dist_name, group_name, gcm_id"
                + " from "+T_PERSON_INFO+" join "+T_PERSON+" using(person_id) join "+T_DISTRICT+" using(dist_id) "
                + "join "+T_BLOOD_GROUP+" using(group_id) where group_name like '"+groupName+"' and dist_name like '"+distName+"'";   
    }
    
    public static String getFeedBackQuery(){
        return "select * from " + T_FEEDBACK;
    }
    
    public static String getAccessKeyInfoQuery(String accessKey){
        return "select admin_name, mobile_number, email from "+T_ADMIN_PANEL + " where access_key='"+accessKey+"'";
    }
    
    public static String getAdminListQuery(){
        return "select * from "+T_ADMIN_PANEL;
    }
}
