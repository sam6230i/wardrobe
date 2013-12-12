package com.example.android.sqlite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WardrobeDataSource {

	// Database fields
	private SQLiteDatabase database;
	private WardrobeSqliteHelper dbHelper;
	private String[] allColumns = { WardrobeSqliteHelper.COLUMN_ID,
			WardrobeSqliteHelper.COLUMN_IMAGE_PATH};

	private String[] favourtiesAllColumns = { WardrobeSqliteHelper.COLUMN_ID,
			WardrobeSqliteHelper.SHIRT_ID, WardrobeSqliteHelper.PANT_ID};

	private String[] woreAllColumns = { WardrobeSqliteHelper.COLUMN_ID,
			WardrobeSqliteHelper.SHIRT_ID, WardrobeSqliteHelper.PANT_ID, WardrobeSqliteHelper.WORE_DATE};

	public WardrobeDataSource(Context context) {
		dbHelper = new WardrobeSqliteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	//pant database methods
	public Pant addPant(Pant pant) {
		ContentValues values = new ContentValues();
		values.put(WardrobeSqliteHelper.COLUMN_IMAGE_PATH, pant.getImagePath());
		long insertId = database.insert(WardrobeSqliteHelper.PANTS_TABLE_NAME, null,
				values);
		Pant newPant = getPant(insertId);
		return newPant;
	}

	public Pant getPant(long pantId) {
		Cursor cursor = database.query(WardrobeSqliteHelper.PANTS_TABLE_NAME,
				allColumns, WardrobeSqliteHelper.COLUMN_ID + " = " + pantId, null,
				null, null, null);
		cursor.moveToFirst();
		Pant newPant = cursorToPant(cursor);
		cursor.close();
		return newPant;
	}

	public void deletePant(Pant pant) {
		long id = pant.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(WardrobeSqliteHelper.PANTS_TABLE_NAME, WardrobeSqliteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Pant> getAllPants() {
		List<Pant> Pants = new ArrayList<Pant>();

		Cursor cursor = database.query(WardrobeSqliteHelper.PANTS_TABLE_NAME,
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


	//Shirt database methods
	public Shirt addShirt(Shirt shirt) {
		ContentValues values = new ContentValues();
		values.put(WardrobeSqliteHelper.COLUMN_IMAGE_PATH, shirt.getImagePath());
		long insertId = database.insert(WardrobeSqliteHelper.SHIRTS_TABLE_NAME, null,
				values);
		Shirt newShirt = getShirt(insertId);
		return newShirt;
	}

	public Shirt getShirt(long shirtId) {
		Cursor cursor = database.query(WardrobeSqliteHelper.SHIRTS_TABLE_NAME,
				allColumns, WardrobeSqliteHelper.COLUMN_ID + " = " + shirtId, null,
				null, null, null);
		cursor.moveToFirst();
		Shirt newShirt = cursorToShirt(cursor);
		cursor.close();
		return newShirt;
	}

	private Shirt cursorToShirt(Cursor cursor) {
		Shirt shirt = new Shirt();
		shirt.setId(cursor.getLong(0));
		shirt.setImagePath(cursor.getString(1));
		return shirt;
	}

	public void deleteShirt(Shirt shirt) {
		long id = shirt.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(WardrobeSqliteHelper.SHIRTS_TABLE_NAME, WardrobeSqliteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Shirt> getAllShirts() {
		List<Shirt> shirts = new ArrayList<Shirt>();

		Cursor cursor = database.query(WardrobeSqliteHelper.SHIRTS_TABLE_NAME,
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

	//Favourites database methods

	public long addFavourite(long shirtId, long pantId) {
		ContentValues values = new ContentValues();
		values.put(WardrobeSqliteHelper.SHIRT_ID, shirtId);
		values.put(WardrobeSqliteHelper.PANT_ID, pantId);
		long insertId = database.insertWithOnConflict(WardrobeSqliteHelper.FAVOURITES_TABLE_NAME, null,
				values, SQLiteDatabase.CONFLICT_IGNORE);
		return insertId;
	}

	public void deleteFavourite(long shirtId, long pantId) {
		database.delete(WardrobeSqliteHelper.FAVOURITES_TABLE_NAME,
				WardrobeSqliteHelper.SHIRT_ID + " =  ? "
				+ " and " + WardrobeSqliteHelper.PANT_ID + " = ?",
				new String[] {Long.toString(shirtId),
				Long.toString(pantId)}
				);
	}

	public boolean isFavourite(long shirtId, long pantId) {
		Cursor cursor = database.query(WardrobeSqliteHelper.FAVOURITES_TABLE_NAME,
				favourtiesAllColumns,
				WardrobeSqliteHelper.SHIRT_ID + " =  ? "
						+ " and " + WardrobeSqliteHelper.PANT_ID + " = ?",
				new String[]{Long.toString(shirtId),
						Long.toString(pantId)}
				, null, null, null
		);

		cursor.moveToFirst();
		return cursor.getCount() > 0;
	}

	public List<Favourite> getAllFavourites() {
		List<Favourite> favourites = new ArrayList<Favourite>();

		Cursor cursor = database.query(WardrobeSqliteHelper.FAVOURITES_TABLE_NAME,
				favourtiesAllColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Favourite favourite = new Favourite();
			favourite.setId(cursor.getLong(0));
			favourite.setShirtId(cursor.getLong(1));
			favourite.setPantId(cursor.getLong(2));

			Shirt shirt = getShirt(favourite.getShirtId());
			if (shirt != null) {
				favourite.setShirtPath(shirt.getImagePath());
			}

			Pant pant = getPant(favourite.getPantId());
			if (pant != null) {
				favourite.setPantPath(pant.getImagePath());
			}

			favourites.add(favourite);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return favourites;
	}

	//wore database methods

	public long addToWore(long shirtId, long pantId) {

		ContentValues values = new ContentValues();
		values.put(WardrobeSqliteHelper.SHIRT_ID, shirtId);
		values.put(WardrobeSqliteHelper.PANT_ID, pantId);
		values.put(WardrobeSqliteHelper.WORE_DATE, getStartOfDayTime());
		long insertId = database.insertWithOnConflict(WardrobeSqliteHelper.WORE_TABLE_NAME, null,
				values, SQLiteDatabase.CONFLICT_IGNORE);
		return insertId;
	}

	public void deleteFromWore(long shirtId, long pantId) {
		long startOfDayTime = getStartOfDayTime();
		database.delete(WardrobeSqliteHelper.WORE_TABLE_NAME,
				WardrobeSqliteHelper.SHIRT_ID + " =  ? "
						+ " and " + WardrobeSqliteHelper.PANT_ID + " = ?"
						+ " and " + WardrobeSqliteHelper.WORE_DATE + " = ?",
				new String[] {Long.toString(shirtId),
						Long.toString(pantId), Long.toString(startOfDayTime)}
		);
	}

	public boolean isWornToday(long shirtId, long pantId) {
		long startOfDayTime = getStartOfDayTime();
		Cursor cursor = database.query(WardrobeSqliteHelper.WORE_TABLE_NAME,
				woreAllColumns,
				WardrobeSqliteHelper.SHIRT_ID + " =  ? "
						+ " and " + WardrobeSqliteHelper.PANT_ID + " = ?"
						+ " and " + WardrobeSqliteHelper.WORE_DATE + " = ?",
				new String[]{Long.toString(shirtId),
						Long.toString(pantId), Long.toString(startOfDayTime)}
				, null, null, null
		);

		cursor.moveToFirst();
		return cursor.getCount() > 0;
	}

	public List<Wore> getWhatIWore() {
		List<Wore> woreList = new ArrayList<Wore>();

		Cursor cursor = database.query(WardrobeSqliteHelper.WORE_TABLE_NAME,
				woreAllColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Wore wore = new Wore();
			wore.setId(cursor.getLong(0));
			wore.setShirtId(cursor.getLong(1));
			wore.setPantId(cursor.getLong(2));
			wore.setWoreDate(createDate(cursor.getLong(3)));

			Shirt shirt = getShirt(wore.getShirtId());
			if (shirt != null) {
				wore.setShirtPath(shirt.getImagePath());
			}

			Pant pant = getPant(wore.getPantId());
			if (pant != null) {
				wore.setPantPath(pant.getImagePath());
			}

			woreList.add(wore);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return woreList;
	}

	public long getStartOfDayTime() {
		//calculating start of the day timestamp in seconds
		Calendar instance = Calendar.getInstance();
		int year = instance.get(Calendar.YEAR);
		int month = instance.get(Calendar.MONTH);
		int day = instance.get(Calendar.DATE);
		instance.clear();
		instance.set(year, month, day);
		long time = instance.getTimeInMillis() / 1000;
		return time;
	}

	public Date createDate(long timeInSeconds) {
		long timeInMills = timeInSeconds * 1000;
		return new Date(timeInMills);
	}

}