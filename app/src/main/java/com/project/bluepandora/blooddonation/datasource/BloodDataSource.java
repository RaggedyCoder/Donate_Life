package com.project.bluepandora.blooddonation.datasource;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.project.bluepandora.blooddonation.data.BloodItem;
import com.project.bluepandora.blooddonation.database.DataBaseOpenHelper;
import com.project.bluepandora.blooddonation.exception.BloodDatabaseException;

public class BloodDataSource {

	private String[] allColumns = { DataBaseOpenHelper.GROUPID_COLOUMN,
			DataBaseOpenHelper.GROUPNAME_COLOUMN };
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
