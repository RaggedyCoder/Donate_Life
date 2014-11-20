package com.project.bluepandora.donatelife.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.project.bluepandora.donatelife.data.DistrictItem;
import com.project.bluepandora.donatelife.database.DataBaseOpenHelper;
import com.project.bluepandora.donatelife.exception.DistrictDatabaseException;

import java.util.ArrayList;

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

    public ArrayList<DistrictItem> getAllDistrictItem() {
        ArrayList<DistrictItem> items = new ArrayList<DistrictItem>();
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
