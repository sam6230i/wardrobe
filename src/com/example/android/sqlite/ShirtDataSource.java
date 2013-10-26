package com.example.android.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShirtDataSource {

	// Database fields
	private SQLiteDatabase database;
	private ShirtsSqliteHelper dbHelper;
	private String[] allColumns = { ShirtsSqliteHelper.COLUMN_ID,
			ShirtsSqliteHelper.COLUMN_IMAGE_PATH};

	public ShirtDataSource(Context context) {
		dbHelper = new ShirtsSqliteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Shirt addShirt(Shirt shirt) {
		ContentValues values = new ContentValues();
		values.put(ShirtsSqliteHelper.COLUMN_IMAGE_PATH, shirt.getImagePath());
		long insertId = database.insert(ShirtsSqliteHelper.TABLE_NAME, null,
				values);
		Cursor cursor = database.query(ShirtsSqliteHelper.TABLE_NAME,
				allColumns, ShirtsSqliteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Shirt newShirt = cursorToShirt(cursor);
		cursor.close();
		return newShirt;
	}

	public void deleteShirt(Shirt shirt) {
		long id = shirt.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(ShirtsSqliteHelper.TABLE_NAME, ShirtsSqliteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Shirt> getAllShirts() {
		List<Shirt> shirts = new ArrayList<Shirt>();

		Cursor cursor = database.query(ShirtsSqliteHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Shirt shirt = cursorToShirt(cursor);
			shirts.add(shirt);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return shirts;
	}

	private Shirt cursorToShirt(Cursor cursor) {
		Shirt shirt = new Shirt();
		shirt.setId(cursor.getLong(0));
		shirt.setImagePath(cursor.getString(1));
		return shirt;
	}
}