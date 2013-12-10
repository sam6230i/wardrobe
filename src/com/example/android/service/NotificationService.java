package com.example.android.service;

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
		NotifManager.notify(this);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

}
