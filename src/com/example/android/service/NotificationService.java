package com.example.android.service;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.android.sqlite.AppPreferences;
import com.example.android.util.NotifManager;

/**
 * NotificationService will just give a call to NotifManager to display notification.
 * Service will register next day alarm at set time to give notification again
 * 
 * @author Nishant Patil
 * 
 */

public class NotificationService extends Service
{
	String TAG = "NotificationService";

	@Override
	public void onCreate()
	{
		super.onCreate();

		NotifManager.notify(this);

		Intent i = new Intent(this, NotificationService.class);
		AlarmManager manager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 6);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		if (cal.getTimeInMillis() <= System.currentTimeMillis())
		{
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
		}

		manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

		AppPreferences.setNotificationTimestamp(this, cal.getTimeInMillis());

		stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

}
