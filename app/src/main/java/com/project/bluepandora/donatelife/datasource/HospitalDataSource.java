package com.project.bluepandora.donatelife.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.project.bluepandora.donatelife.data.HospitalItem;
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
public class HospitalDataSource {

    private String[] allColumns = {DataBaseOpenHelper.DISTRICTID_COLOUMN,
            DataBaseOpenHelper.HOSPITALID_COLOUMN,
            DataBaseOpenHelper.HOSPITALNAME_COLOUMN};
    protected Context context;
    protected SQLiteDatabase database;
    protected DataBaseOpenHelper dbHelper;

    public HospitalDataSource(Context context) {
        dbHelper = new DataBaseOpenHelper(context);
        this.context = context;
    }

    public HospitalItem createHospitalItem(int distId, int hospitalId,
                                           String hospitalName) {
        ContentValues values = new ContentValues();
        values.put(DataBaseOpenHelper.DISTRICTID_COLOUMN, distId);
        values.put(DataBaseOpenHelper.HOSPITALID_COLOUMN, hospitalId);
        values.put(DataBaseOpenHelper.HOSPITALNAME_COLOUMN, hospitalName);
        database.insert(DataBaseOpenHelper.HOSPITAL_TABLE, null, values);
        Cursor cursor = database.query(DataBaseOpenHelper.HOSPITAL_TABLE,
                allColumns, DataBaseOpenHelper.HOSPITALID_COLOUMN + " = "
                        + hospitalId, null, null, null, null);
        cursor.moveToFirst();
        HospitalItem item = cursorToHospitalItem(cursor);
        return item;

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteHospitalitem(HospitalItem item) {
        int hospitalId = item.getHospitalId();
        database.delete(DataBaseOpenHelper.HOSPITAL_TABLE,
                DataBaseOpenHelper.HOSPITALID_COLOUMN + " = " + hospitalId,
                null);
    }

    public ArrayList<HospitalItem> getAllHospitalItem() {
        ArrayList<HospitalItem> items = new ArrayList<HospitalItem>();
        Cursor cursor = database.query(DataBaseOpenHelper.HOSPITAL_TABLE,
                allColumns, null, null, null, null,
                DataBaseOpenHelper.HOSPITALNAME_COLOUMN);
        cursor.moveToFirst();
        do {
            HospitalItem item = cursorToHospitalItem(cursor);
            items.add(item);
        } while (cursor.moveToNext());
        return items;
    }

    public ArrayList<HospitalItem> getAllHospitalItem(int distId) {
        ArrayList<HospitalItem> items = new ArrayList<HospitalItem>();
        Cursor cursor = database.query(DataBaseOpenHelper.HOSPITAL_TABLE,
                allColumns, DataBaseOpenHelper.DISTRICTID_COLOUMN + " = "
                        + distId, null, null, null, DataBaseOpenHelper.HOSPITALNAME_COLOUMN);
        if (cursor == null) {

            return null;
        }
        cursor.moveToFirst();
        do {
            HospitalItem item = cursorToHospitalItem(cursor);
            items.add(item);
        } while (cursor.moveToNext());
        return items;
    }

    public HospitalItem cursorToHospitalItem(Cursor cursor) {
        HospitalItem item = new HospitalItem();
        item.setDistId(cursor.getInt(cursor
                .getColumnIndex(DataBaseOpenHelper.DISTRICTID_COLOUMN)));
        item.setHospitalId(cursor.getInt(cursor
                .getColumnIndex(DataBaseOpenHelper.HOSPITALID_COLOUMN)));
        item.setHospitalName(cursor.getString(cursor
                .getColumnIndex(DataBaseOpenHelper.HOSPITALNAME_COLOUMN)));
        return item;
    }

    public Cursor hospitalItemToCursor(HospitalItem item) {
        Cursor cursor = database.query(DataBaseOpenHelper.HOSPITAL_TABLE,
                allColumns, DataBaseOpenHelper.HOSPITALID_COLOUMN + " = "
                        + item.getHospitalId(), null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }
}
