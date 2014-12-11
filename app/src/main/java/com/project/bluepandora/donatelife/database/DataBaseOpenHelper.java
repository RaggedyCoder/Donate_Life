package com.project.bluepandora.donatelife.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
public class DataBaseOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "BloodDonate.db";
    public static final int DB_VERSION = 1;
    public static final String CREATE = "CREATE";
    public static final String COLOUMN = "COLOUMN";
    public static final String TABLE = "TABLE";
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String FOREIGN_KEY = "FOREIGN KEY";
    public static final String REFERENCES = "REFERENCES";
    public static final String DROP = "DROP";
    public static final String IF_NOT_EXISTS = "IF NOT EXISTS";
    public static final String HOSPITAL_TABLE = "hospital";
    public static final String BLOOD_TABLE = "blood";
    public static final String DISTRICT_TABLE = "district";
    public static final String USER_TABLE = "info";
    public static final String DONATION_RECORD_TABLE = "donner";
    public static final String HOSPITALID_COLOUMN = "hospital_id";
    public static final String HOSPITALNAME_COLOUMN = "hospital_name";
    public static final String BANGLA_HOSPITALNAME_COLOUMN = "bangla_hospital_name";
    public static final String DISTRICTID_COLOUMN = "district_id";
    public static final String DISTRICTNAME_COLOUMN = "district_name";
    public static final String BANGLA_DISTRICTNAME_COLOUMN = "bangla_district_name";
    public static final String GROUPID_COLOUMN = "group_id";
    public static final String GROUPNAME_COLOUMN = "group_name";
    public static final String BANGLA_GROUPNAME_COLOUMN = "bangla_group_name";
    public static final String MOBILE_NUMBER_COLOUMN = "mobile_number";
    public static final String KEY_WORD_COLOUMN = "key_word";
    public static final String FIRST_NAME_COLOUMN = "first_name";
    public static final String LAST_NAME_COLOUMN = "last_name";
    public static final String DONATION_TIME_COLOUMN = "donation_time";
    public static final String DONATION_DETAILS_COLOUMN = "donation_details";
    public static final String INTEGER = "INTEGER";
    public static final String VARCHAR = "VARCHAR";
    private static final String HOSPITAL_CREATE = CREATE + " " + TABLE + " "
            + IF_NOT_EXISTS + " " + HOSPITAL_TABLE + "(" + " "
            + DISTRICTID_COLOUMN + " " + INTEGER + "," + HOSPITALID_COLOUMN
            + " " + INTEGER + "," + HOSPITALNAME_COLOUMN + " " + VARCHAR + ","
            + BANGLA_HOSPITALNAME_COLOUMN + " " + VARCHAR + ","
            + PRIMARY_KEY + "(" + HOSPITALID_COLOUMN + ")" + "," + FOREIGN_KEY
            + "(" + DISTRICTID_COLOUMN + ")" + " " + REFERENCES + " "
            + DISTRICT_TABLE + "(" + DISTRICTID_COLOUMN + ")" + ");";
    private static final String DISTRICT_CREATE = CREATE + " " + TABLE + " "
            + IF_NOT_EXISTS + " " + DISTRICT_TABLE + "(" + " "
            + DISTRICTID_COLOUMN + " " + INTEGER + "," + DISTRICTNAME_COLOUMN
            + " " + VARCHAR + "," + BANGLA_DISTRICTNAME_COLOUMN
            + " " + VARCHAR + "," + PRIMARY_KEY + "(" + DISTRICTID_COLOUMN
            + ")" + ");";
    private static final String BLOOD_CREATE = CREATE + " " + TABLE + " "
            + IF_NOT_EXISTS + " " + BLOOD_TABLE + "(" + " " + GROUPID_COLOUMN
            + " " + INTEGER + "," + GROUPNAME_COLOUMN + " " + VARCHAR + ","
            + BANGLA_GROUPNAME_COLOUMN + " " + VARCHAR + ","
            + PRIMARY_KEY + "(" + GROUPID_COLOUMN + ")" + ");";
    private static final String USER_CREATE = CREATE + " " + TABLE + " "
            + IF_NOT_EXISTS + " " + USER_TABLE + "(" + " " + FIRST_NAME_COLOUMN
            + " " + VARCHAR + "," + " " + LAST_NAME_COLOUMN + " " + VARCHAR
            + "," + MOBILE_NUMBER_COLOUMN + " " + VARCHAR + ","
            + KEY_WORD_COLOUMN + " " + VARCHAR + "," + DISTRICTID_COLOUMN + " "
            + INTEGER + "," + GROUPID_COLOUMN + " " + INTEGER + ","
            + PRIMARY_KEY + "(" + MOBILE_NUMBER_COLOUMN + ")" + ","
            + FOREIGN_KEY + "(" + DISTRICTID_COLOUMN + ")" + " " + REFERENCES
            + " " + DISTRICT_TABLE + "(" + DISTRICTID_COLOUMN + ")" + ","
            + FOREIGN_KEY + "(" + GROUPID_COLOUMN + ")" + " " + REFERENCES
            + " " + BLOOD_TABLE + "(" + GROUPID_COLOUMN + ")" + ");";

    private static final String DONATION_RECORD_CREATE = CREATE + " " + TABLE + " "
            + IF_NOT_EXISTS + " " + DONATION_RECORD_TABLE + "(" + DONATION_TIME_COLOUMN +
            " " + VARCHAR + "," + " " + DONATION_DETAILS_COLOUMN + " " + VARCHAR + "," + " "
            + PRIMARY_KEY + "(" + DONATION_TIME_COLOUMN + ")" + ");";
    private Context context;


    public DataBaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DISTRICT_CREATE);
            Log.i("Database Create", "Created District");
        } catch (Exception e) {
            Log.e("Database Create", e.getMessage());
        }
        try {
            db.execSQL(HOSPITAL_CREATE);
            Log.i("Database Create", "Created hospital");
        } catch (Exception e) {
            Log.e("Database Create", e.getMessage());
        }
        try {
            db.execSQL(BLOOD_CREATE);
            Log.i("Database Create", "Created Blood");
        } catch (Exception e) {
            Log.e("Database Create", e.getMessage());
        }
        try {
            db.execSQL(USER_CREATE);
            Log.i("Database Create", "Created info");
        } catch (Exception e) {
            Log.e("Database Create", e.getMessage());
        }
        try {
            db.execSQL(DONATION_RECORD_CREATE);
            Log.i("Database Create", "Created donner.");
        } catch (Exception e) {
            Log.e("Database Create", e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP + " " + TABLE + " "
                    + DISTRICT_TABLE);
            Log.i("Database Create", "Dropped District");
        } catch (Exception e) {
            Log.e("Database Drop", e.getMessage());
        }
        try {
            db.execSQL(DROP + " " + TABLE + " "
                    + HOSPITAL_TABLE);
            Log.i("Database Create", "Dropped Hospital");
        } catch (Exception e) {
            Log.e("Database Drop", e.getMessage());
        }
        try {
            db.execSQL(DROP + " " + TABLE + " "
                    + BLOOD_TABLE);
            Log.i("Database Create", "Dropped Blood");
        } catch (Exception e) {
            Log.e("Database Drop", e.getMessage());
        }
        try {
            db.execSQL(DROP + " " + TABLE + " "
                    + USER_TABLE);
            Log.i("Database Create", "Dropped info");
        } catch (Exception e) {
            Log.e("Database Drop", e.getMessage());
        }
        try {
            db.execSQL(DROP + " " + TABLE + " "
                    + DONATION_RECORD_TABLE);
            Log.i("Database Create", "Dropped Donation Record");
        } catch (Exception e) {
            Log.e("Database Drop", e.getMessage());
        }
        onCreate(db);
    }

}
