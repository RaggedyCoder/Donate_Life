package com.project.bluepandora.donatelife.datasource;

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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.project.bluepandora.donatelife.data.DRItem;
import com.project.bluepandora.donatelife.database.DataBaseOpenHelper;

import java.util.ArrayList;

public class DRDataSource {

    protected Context context;
    protected SQLiteDatabase database;
    protected DataBaseOpenHelper dbHelper;
    private String[] allColumns = {DataBaseOpenHelper.DONATION_TIME_COLOUMN,
            DataBaseOpenHelper.DONATION_DETAILS_COLOUMN};

    public DRDataSource(Context context) {
        dbHelper = new DataBaseOpenHelper(context);
        this.context = context;
    }

    public DRItem createDRItem(String donationTime, String donationDetails) {
        ContentValues values = new ContentValues();
        values.put(DataBaseOpenHelper.DONATION_TIME_COLOUMN, donationTime);
        values.put(DataBaseOpenHelper.DONATION_DETAILS_COLOUMN, donationDetails);
        database.insert(DataBaseOpenHelper.DONATION_RECORD_TABLE, null, values);
        Cursor cursor = database.query(DataBaseOpenHelper.DISTRICT_TABLE,
                allColumns, DataBaseOpenHelper.DONATION_TIME_COLOUMN + " LIKE "
                        + donationTime, null, null, null, null);
        cursor.moveToFirst();
        DRItem item = cursorToDRItem(cursor);
        return item;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteDistrictitem(DRItem item) {
        String donationTime = item.getDonationTime();
        database.delete(DataBaseOpenHelper.DONATION_RECORD_TABLE,
                DataBaseOpenHelper.DONATION_TIME_COLOUMN + " = " + donationTime, null);
    }

    public ArrayList<DRItem> getAllDRItem() {
        ArrayList<DRItem> items = new ArrayList<DRItem>();
        Cursor cursor = database.query(DataBaseOpenHelper.DONATION_RECORD_TABLE,
                allColumns, null, null, null, null,
                DataBaseOpenHelper.DONATION_TIME_COLOUMN);
        if (cursor.moveToFirst()) {
            do {
                DRItem item = cursorToDRItem(cursor);
                items.add(item);
            } while (cursor.moveToNext());
        }
        return items;
    }

    private DRItem cursorToDRItem(Cursor cursor) {
        DRItem item = new DRItem();
        item.setDonationTime(cursor.getString(cursor
                .getColumnIndex(DataBaseOpenHelper.DONATION_TIME_COLOUMN)));
        item.setDonationDetails(cursor.getString(cursor
                .getColumnIndex(DataBaseOpenHelper.DONATION_DETAILS_COLOUMN)));
        return item;
    }

    private Cursor dritemToCursor(DRItem item) {
        Cursor cursor = database.query(DataBaseOpenHelper.DONATION_RECORD_TABLE,
                allColumns, DataBaseOpenHelper.DONATION_TIME_COLOUMN + " = "
                        + item.getDonationTime(), null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }
}
