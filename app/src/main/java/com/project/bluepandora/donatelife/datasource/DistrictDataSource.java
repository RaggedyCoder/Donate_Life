package com.project.bluepandora.donatelife.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.database.DataBaseOpenHelper;
import com.project.bluepandora.donatelife.exception.DistrictDatabaseException;

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
public class DistrictDataSource {

    private String[] allColumns = {DataBaseOpenHelper.DISTRICTID_COLOUMN,
            DataBaseOpenHelper.DISTRICTNAME_COLOUMN};
    protected Context context;
    protected SQLiteDatabase database;
    protected DataBaseOpenHelper dbHelper;

    public DistrictDataSource(Context context) {
        dbHelper = new DataBaseOpenHelper(context);
        this.context = context;
    }

    public DistrictItem createDistrictItem(int distId, String distName)
            throws DistrictDatabaseException {
        ContentValues values = new ContentValues();
        values.put(DataBaseOpenHelper.DISTRICTID_COLOUMN, distId);
        values.put(DataBaseOpenHelper.DISTRICTNAME_COLOUMN, distName);
        database.insert(DataBaseOpenHelper.DISTRICT_TABLE, null, values);
        Cursor cursor = database.query(DataBaseOpenHelper.DISTRICT_TABLE,
                allColumns, DataBaseOpenHelper.DISTRICTID_COLOUMN + " = "
                        + distId, null, null, null, null);
        cursor.moveToFirst();
        DistrictItem item = cursorToDistrictItem(cursor);
        return item;

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void deleteDistrictitem(DistrictItem item) {
        int distId = item.getDistId();
        database.delete(DataBaseOpenHelper.DISTRICT_TABLE,
                DataBaseOpenHelper.DISTRICTID_COLOUMN + " = " + distId, null);
    }

    public ArrayList<Item> getAllDistrictItem() {
        ArrayList<Item> items = new ArrayList<Item>();
        Cursor cursor = database.query(DataBaseOpenHelper.DISTRICT_TABLE,
                allColumns, null, null, null, null,
                DataBaseOpenHelper.DISTRICTNAME_COLOUMN);
        cursor.moveToFirst();
        do {
            DistrictItem item = cursorToDistrictItem(cursor);
            items.add(item);
        } while (cursor.moveToNext());
        return items;
    }

    public DistrictItem cursorToDistrictItem(Cursor cursor) {
        DistrictItem item = new DistrictItem();
        item.setDistId(cursor.getInt(cursor
                .getColumnIndex(DataBaseOpenHelper.DISTRICTID_COLOUMN)));
        item.setDistName(cursor.getString(cursor
                .getColumnIndex(DataBaseOpenHelper.DISTRICTNAME_COLOUMN)));
        return item;
    }

    public Cursor districtItemToCursor(DistrictItem item) {
        Cursor cursor = database.query(DataBaseOpenHelper.DISTRICT_TABLE,
                allColumns, DataBaseOpenHelper.DISTRICTID_COLOUMN + " = "
                        + item.getDistId(), null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }
}
