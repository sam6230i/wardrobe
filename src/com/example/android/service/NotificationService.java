package com.example.android.service;

import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.android.sqlite.AppPreferences;
import com.example.android.util.NotifManager;

/**
 * NotificationService will just give a call to NotifManager to display notification.
 * This service will be called everyday at set time.
 * 
 * @author Nishant Patil
 * 
 */

public class NotificationService extends Service
{

	@Override
	public void onCreate()
	{
		super.onCreate();

		// TODO : Nishant : Adding this at last moment. Need to keep it at right place.
		// Time : 06:20AM 11Dec.

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		long lastSet = 0;
		if (AppPreferences.getLastNotificationTimestamp(this) > 0)
		{
			lastSet = AppPreferences.getLastNotificationTimestamp(this);
		}
		else
		{
			AppPreferences.setLastNotificationTimestamp(this, System.currentTimeMillis());
		}

		cal.setTimeInMillis(lastSet);
		Log.d("Nish", "Last Alart set at : " + cal.toString());
		NotifManager.notify(this);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

}
