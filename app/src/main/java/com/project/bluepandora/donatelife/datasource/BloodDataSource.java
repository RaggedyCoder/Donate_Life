package com.project.bluepandora.donatelife.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.database.DataBaseOpenHelper;
import com.project.bluepandora.donatelife.exception.BloodDatabaseException;

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
public class BloodDataSource {

    private String[] allColumns = {DataBaseOpenHelper.GROUPID_COLOUMN,
            DataBaseOpenHelper.GROUPNAME_COLOUMN};
    protected Context context;
    protected SQLiteDatabase database;
    protected DataBaseOpenHelper dbHelper;

    public BloodDataSource(Context context) {
        dbHelper = new DataBaseOpenHelper(context);
        this.context = context;
    }

    public BloodItem createBloodItem(int bloodId, String bloodName)
            throws BloodDatabaseException {
        ContentValues values = null;

        values = new ContentValues();
        values.put(DataBaseOpenHelper.GROUPID_COLOUMN, bloodId);
        values.put(DataBaseOpenHelper.GROUPNAME_COLOUMN, bloodName);

        database.insert(DataBaseOpenHelper.BLOOD_TABLE, null, values);

        Cursor cursor = database.query(DataBaseOpenHelper.BLOOD_TABLE,
                allColumns, DataBaseOpenHelper.GROUPID_COLOUMN + " = "
                        + bloodId, null, null, null, null);
        cursor.moveToFirst();
        BloodItem item = cursorToBloodItem(cursor);
        return item;
    }

    public BloodItem createBloodItem(BloodItem item)
            throws BloodDatabaseException {

        BloodItem returnItem = createBloodItem(item.getBloodId(),
                item.getBloodName());
        return returnItem;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteBlooditem(BloodItem item) {
        int bloodId = item.getBloodId();
        database.delete(DataBaseOpenHelper.BLOOD_TABLE,
                DataBaseOpenHelper.GROUPID_COLOUMN + " = " + bloodId, null);
    }

    public ArrayList<BloodItem> getAllBloodItem() {
        ArrayList<BloodItem> items = new ArrayList<BloodItem>();
        Cursor cursor = database.query(DataBaseOpenHelper.BLOOD_TABLE,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        do {
            BloodItem item = cursorToBloodItem(cursor);
            items.add(item);
        } while (cursor.moveToNext());
        return items;
    }

    public BloodItem cursorToBloodItem(Cursor cursor) {
        BloodItem item = new BloodItem();
        item.setBloodId(cursor.getInt(cursor
                .getColumnIndex(DataBaseOpenHelper.GROUPID_COLOUMN)));
        item.setBloodName(cursor.getString(cursor
                .getColumnIndex(DataBaseOpenHelper.GROUPNAME_COLOUMN)));
        return item;
    }

    public Cursor bloodItemToCursor(BloodItem item) {
        Cursor cursor = database.query(DataBaseOpenHelper.BLOOD_TABLE,
                allColumns,
                DataBaseOpenHelper.GROUPID_COLOUMN + " = " + item.getBloodId(),
                null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }
}
