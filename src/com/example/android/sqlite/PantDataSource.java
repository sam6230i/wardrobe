package com.example.android.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PantDataSource {

	// Database fields
	private SQLiteDatabase database;
	private PantsSqliteHelper dbHelper;
	private String[] allColumns = { PantsSqliteHelper.COLUMN_ID,
			PantsSqliteHelper.COLUMN_IMAGE_PATH};

	public PantDataSource(Context context) {
		dbHelper = new PantsSqliteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Pant addPant(Pant pant) {
		ContentValues values = new ContentValues();
		values.put(PantsSqliteHelper.COLUMN_IMAGE_PATH, pant.getImagePath());
		long insertId = database.insert(PantsSqliteHelper.TABLE_NAME, null,
				values);
		Cursor cursor = database.query(PantsSqliteHelper.TABLE_NAME,
				allColumns, PantsSqliteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Pant newPant = cursorToPant(cursor);
		cursor.close();
		return newPant;
	}

	public void deletePant(Pant pant) {
		long id = pant.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(PantsSqliteHelper.TABLE_NAME, PantsSqliteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Pant> getAllPants() {
		List<Pant> Pants = new ArrayList<Pant>();

		Cursor cursor = database.query(PantsSqliteHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Pant pant = cursorToPant(cursor);
			Pants.add(pant);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return Pants;
	}

	private Pant cursorToPant(Cursor cursor) {
		Pant pant = new Pant();
		pant.setId(cursor.getLong(0));
		pant.setImagePath(cursor.getString(1));
		return pant;
	}
}