package com.example.android.sqlite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WardrobeSqliteHelper extends SQLiteOpenHelper {

	public static final String SHIRTS_TABLE_NAME = "shirts";
	public static final String FAVOURITES_TABLE_NAME = "favourites";
	public static final String WORE_TABLE_NAME = "wore";
	public static final String PANTS_TABLE_NAME = "pants";
	public static final String SETTINGS_TABLE_NAME = "settings";
	public static final String COLUMN_ID = "_id";
	public static final String PANT_ID = "pant_id";
	public static final String SHIRT_ID = "shirt_id";
	public static final String WORE_DATE = "wore_date";
	public static final String COLUMN_IMAGE_PATH = "path";
	public static final String SETTINGS_KEY = "key";
	public static final String SETTINGS_VALUE = "value";

	private static final String DATABASE_NAME = "wardrobe.db";
	private static final int DATABASE_VERSION = 2; // Upgrading DB version as new table is introduced.

	// Database creation sql statement
	private static final String PANTS_TABLE_CREATE = "create table "
			+ PANTS_TABLE_NAME + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_IMAGE_PATH
			+ " text not null);";

	private static final String SHIRTS_TABLE_CREATE = "create table "
			+ SHIRTS_TABLE_NAME + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_IMAGE_PATH
			+ " text not null);";

	private static final String FAVOURITES_TABLE_CREATE = "create table "
			+ FAVOURITES_TABLE_NAME + "(" + COLUMN_ID
			+ " integer primary key autoincrement, "
			+ SHIRT_ID + " integer, "
			+ PANT_ID + " integer, "
			+ " FOREIGN KEY(" + SHIRT_ID + ") REFERENCES " + SHIRTS_TABLE_NAME + "(" + COLUMN_ID + ") "
			+ " FOREIGN KEY(" + PANT_ID + ") REFERENCES " + PANTS_TABLE_NAME + "(" + COLUMN_ID + ") "
			+ " UNIQUE(" + SHIRT_ID + "," +  PANT_ID + ") ON CONFLICT IGNORE "
			+ ")";

	private static final String WORE_TABLE_CREATE = "create table "
			+ WORE_TABLE_NAME + "(" + COLUMN_ID
			+ " integer primary key autoincrement, "
			+ SHIRT_ID + " integer, "
			+ PANT_ID + " integer, "
			+ WORE_DATE + " integer, "
			+ " FOREIGN KEY(" + SHIRT_ID + ") REFERENCES " + SHIRTS_TABLE_NAME + "(" + COLUMN_ID + ") "
			+ " FOREIGN KEY(" + PANT_ID + ") REFERENCES " + PANTS_TABLE_NAME + "(" + COLUMN_ID + ") "
			+ " UNIQUE(" + SHIRT_ID + ", " +  PANT_ID + ", " + WORE_DATE + ") ON CONFLICT IGNORE "
			+ ")";
	
	public WardrobeSqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(PANTS_TABLE_CREATE);
		database.execSQL(SHIRTS_TABLE_CREATE);
		database.execSQL(FAVOURITES_TABLE_CREATE);
		database.execSQL(WORE_TABLE_CREATE);
		
		
		/*
		 * I would prefer using StringBuilder to build a query and create a table.
		 * Keeping all create table queries as static does not make sense as they will never be used in apps lifetime again (Except in upgrade cases, still...)  
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("create table if not exists ")
		.append(SETTINGS_TABLE_NAME)
		.append("(")
		.append(SETTINGS_KEY).append(" text primary key, ")
		.append(SETTINGS_VALUE).append(" text ")
		.append(")");
		
		database.execSQL(sb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(WardrobeSqliteHelper.class.getName(),	"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + FAVOURITES_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + WORE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + SHIRTS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + PANTS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + SETTINGS_TABLE_NAME);
		
		
		// This call is must in onUpgrade. Otherwise it will never create db again.
		onCreate(db);

	}

}