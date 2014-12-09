package com.project.bluepandora.donatelife.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.database.DataBaseOpenHelper;

import java.util.ArrayList;

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
public class UserDataSource {
    private String[] allColumns = {DataBaseOpenHelper.FIRST_NAME_COLOUMN,
            DataBaseOpenHelper.LAST_NAME_COLOUMN,
            DataBaseOpenHelper.MOBILE_NUMBER_COLOUMN,
            DataBaseOpenHelper.KEY_WORD_COLOUMN,
            DataBaseOpenHelper.DISTRICTID_COLOUMN,
            DataBaseOpenHelper.GROUPID_COLOUMN,};

    protected Context context;
    protected SQLiteDatabase database;
    protected DataBaseOpenHelper dbHelper;

    public UserDataSource(Context context) {
        dbHelper = new DataBaseOpenHelper(context);
        this.context = context;
    }

    public UserInfoItem createUserInfoItem(String firstName, String lastName,
                                           String keyWord, String mobileNumber, int distId, int groupId) {
        ContentValues values = null;

        values = new ContentValues();
        values.put(DataBaseOpenHelper.FIRST_NAME_COLOUMN, firstName);
        values.put(DataBaseOpenHelper.LAST_NAME_COLOUMN, lastName);
        values.put(DataBaseOpenHelper.MOBILE_NUMBER_COLOUMN, mobileNumber);
        values.put(DataBaseOpenHelper.KEY_WORD_COLOUMN, keyWord);
        values.put(DataBaseOpenHelper.DISTRICTID_COLOUMN, distId);
        values.put(DataBaseOpenHelper.GROUPID_COLOUMN, groupId);

        database.insert(DataBaseOpenHelper.USER_TABLE, null, values);
        Cursor cursor = database.query(DataBaseOpenHelper.USER_TABLE,
                allColumns, DataBaseOpenHelper.MOBILE_NUMBER_COLOUMN
                        + " LIKE '" + mobileNumber + "'", null, null, null,
                null);
        cursor.moveToFirst();
        UserInfoItem item = cursorToUserInfoItem(cursor);
        return item;
    }

    public UserInfoItem createUserInfoItem(UserInfoItem item) {
        UserInfoItem returnItem = createUserInfoItem(item.getFirstName(),
                item.getLastName(), item.getKeyWord(), item.getMobileNumber(),
                item.getDistId(), item.getGroupId());
        return returnItem;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteUserInfoitem(UserInfoItem item) {
        String mobileNumber = item.getMobileNumber();
        database.delete(DataBaseOpenHelper.USER_TABLE,
                DataBaseOpenHelper.MOBILE_NUMBER_COLOUMN + " LIKE '"
                        + mobileNumber + "'", null);
    }

    public void updateUserInfoitem(UserInfoItem item) {
        ContentValues values = null;

        values = new ContentValues();
        values.put(DataBaseOpenHelper.FIRST_NAME_COLOUMN, item.getFirstName());
        values.put(DataBaseOpenHelper.LAST_NAME_COLOUMN, item.getLastName());
        values.put(DataBaseOpenHelper.MOBILE_NUMBER_COLOUMN, item.getMobileNumber());
        values.put(DataBaseOpenHelper.KEY_WORD_COLOUMN, item.getKeyWord());
        values.put(DataBaseOpenHelper.DISTRICTID_COLOUMN, item.getDistId());
        values.put(DataBaseOpenHelper.GROUPID_COLOUMN, item.getGroupId());

        database.update(DataBaseOpenHelper.USER_TABLE, values, DataBaseOpenHelper.MOBILE_NUMBER_COLOUMN + " LIKE '"
                + item.getMobileNumber() + "'", null);
    }

    public ArrayList<UserInfoItem> getAllUserItem() {
        ArrayList<UserInfoItem> items = new ArrayList<UserInfoItem>();
        Cursor cursor = database.query(DataBaseOpenHelper.USER_TABLE,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        do {
            UserInfoItem item = cursorToUserInfoItem(cursor);
            items.add(item);
        } while (cursor.moveToNext());
        return items;
    }

    public UserInfoItem cursorToUserInfoItem(Cursor cursor) {
        UserInfoItem item = new UserInfoItem();
        item.setFirstName(cursor.getString(cursor
                .getColumnIndex(DataBaseOpenHelper.FIRST_NAME_COLOUMN)));
        item.setLastName(cursor.getString(cursor
                .getColumnIndex(DataBaseOpenHelper.LAST_NAME_COLOUMN)));
        item.setMobileNumber(cursor.getString(cursor
                .getColumnIndex(DataBaseOpenHelper.MOBILE_NUMBER_COLOUMN)));
        item.setKeyWord(cursor.getString(cursor
                .getColumnIndex(DataBaseOpenHelper.KEY_WORD_COLOUMN)));
        item.setDistId(cursor.getInt(cursor
                .getColumnIndex(DataBaseOpenHelper.DISTRICTID_COLOUMN)));
        item.setGroupId(cursor.getInt(cursor
                .getColumnIndex(DataBaseOpenHelper.GROUPID_COLOUMN)));
        return item;
    }

    public Cursor UserInfoItemToCursor(UserInfoItem item) {
        Cursor cursor = database.query(DataBaseOpenHelper.USER_TABLE,
                allColumns, DataBaseOpenHelper.MOBILE_NUMBER_COLOUMN
                        + " LIKE '" + item.getMobileNumber() + "'", null, null,
                null, null);
        cursor.moveToFirst();
        return cursor;
    }

}
