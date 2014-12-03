/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluepandora.therap.donatelife.constant;

/**
 *
 * @author Biswajit Debnath
 */
public class DbUser {

    /**
     * This is for local access
     */
//    public static final String USERNAME = "root";
//    public static final String PASSWORD = "coderbd";
//    public static final String DATABASETYPE = "com.mysql.jdbc.Driver";
//    public static final String DATABASEURL = "jdbc:mysql://localhost:3306/";
//  
    
    /**
     * This is for openshift access
     */
   
    public static final String USERNAME = "adminzeiBGMn";
    public static final String PASSWORD = "ZAh4_LlqmuMn";
    public static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    public static final String OPENSHIFT_MYSQL_DB_HOST= System.getenv("OPENSHIFT_MYSQL_DB_HOST");
    public static final String OPENSHIFT_MYSQL_DB_PORT= System.getenv("OPENSHIFT_MYSQL_DB_PORT");
    public static final String DATABASEURL = "jdbc:mysql://"+OPENSHIFT_MYSQL_DB_HOST+":"+OPENSHIFT_MYSQL_DB_PORT+"/";

}
