package com.example.android.service;

import java.util.Calendar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
		
		//TODO : Nishant : Adding this at last moment. Need to keep it at right place.
		// Time : 06:20AM 11Dec.
		
		Calendar cal=Calendar.getInstance();
	    cal.set(Calendar.HOUR_OF_DAY, 06);
	    cal.set(Calendar.MINUTE,00);
	    cal.set(Calendar.SECOND, 00);
	    if(System.currentTimeMillis() <= cal.getTimeInMillis()) {
	    	NotifManager.notify(this);
	    }
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

}
