package com.example.android.sqlite;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
	public static String WORDROBE_PREF = "com.example.android.wordrobe";
	
	private static final String NOTIFICATION_SET = "notif_set";
	private static final String LAST_NOTIFICATION_TIMESTAMP = "last_notif_timestamp";
	
	public static void setNotificationSetCheck( Context context, boolean isSet){
		SharedPreferences mSettings = context.getSharedPreferences(WORDROBE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mSettings.edit();
		editor.putBoolean(NOTIFICATION_SET, isSet);
		editor.commit();
		return;
	}
	
	public static boolean getNotificationSetCheck(Context context) {
		SharedPreferences mSettings = context.getSharedPreferences(WORDROBE_PREF, Context.MODE_PRIVATE);
		return mSettings.getBoolean(NOTIFICATION_SET, false);
	}
	
	public static void setLastNotificationTimestamp(Context context, long timestamp){
		SharedPreferences mSettings = context.getSharedPreferences(WORDROBE_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mSettings.edit();
		editor.putLong(LAST_NOTIFICATION_TIMESTAMP, timestamp);
		editor.commit();
		return;
	}
	public static long getLastNotificationTimestamp(Context context) {
		SharedPreferences mSettings = context.getSharedPreferences(WORDROBE_PREF, Context.MODE_PRIVATE);
		return mSettings.getLong(LAST_NOTIFICATION_TIMESTAMP, 0);
	}
	
}
