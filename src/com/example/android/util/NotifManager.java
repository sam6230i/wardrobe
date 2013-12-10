package com.example.android.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.example.android.wardrobe.HomeActivity;
import com.example.android.wardrobe.R;

/**
 * NotifManager class will have all the methods required to handle Notifications in Notification Panel.
 * Currently single method is implemented to display notification.
 * Other methods like update existing, remove specific notification, remove all notifications etc can be listed down here.
 * 
 * @author Nishant Patil
 *
 */
public class NotifManager {

	public static final int NOTIF_ID = 1001;

	
	/**
	 * Displays a notification in notification panel with the help of 
	 * <b>NotificationCompat.Builder</b>(Added in revision10 Support Library Aug12) and <b>NotificationManager</b>.
	 * <br><br>
	 * Currently it is hard coded to show a fixed message when its called.
	 * 
	 * @param context : Context to use. Usually activity or the application's context.
	 * @author Nishant Patil
	 */
	public static void notify(Context context) {
		Intent intent = new Intent(context, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mNotifBuilder = new NotificationCompat.Builder(context)
				.setContentIntent(pIntent)
				.setSmallIcon(R.drawable.ic_notif_logo)
				.setContentTitle(context.getString(R.string.app_name))
				.setContentText(context.getString(R.string.notification_text)).setAutoCancel(true)
				.setLights(0, 100, 100).setOnlyAlertOnce(true)
				.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIF_ID);
		notificationManager.notify(NOTIF_ID, mNotifBuilder.build());
	}
	
}
