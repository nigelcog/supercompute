package com.fit.supercomp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	public int NOTIFICATION_ID = 1;
	public static NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public static String enrollList[];
	
	public  static NotificationCompat.Builder mBuilder;

	public GcmIntentService() {

		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		String message = extras.getString("message");
		String enro = extras.getString("enrollments");

		Log.d("gcm", "gcm");

		enrollList = enro.split(",");
		
		new back().execute("0");

		for (int i = 0; i < enrollList.length; i++) {
			Log.d("enroll number", "e: " + enrollList[i]);

		}

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		sendNotification();
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification() {
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		String enr = "";
		for (int i = 0; i < enrollList.length; i++) {

			enr = enr + " " + enrollList[i];
		}

		 mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.scloud_ic)
				.setContentTitle("Processing")
//				.setStyle(new NotificationCompat.BigTextStyle().bigText(enr))
				.setStyle(new NotificationCompat.BigTextStyle().bigText("Right now your device is processing some data. We will notifiy you once it is done."))
//				 .setContentText("Right now we are using your device for processing our data");
//				.setContentText(enr);
				.setContentText("Right now your device is processing some data. We will notifiy you once it is done.");
		mBuilder.setAutoCancel(true);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}
