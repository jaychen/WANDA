package com.ucla.WANDA.record;

import com.ucla.WANDA.R;
import com.ucla.WANDA.R.drawable;
import com.ucla.WANDA.R.raw;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AlarmService_Service extends BroadcastReceiver {

	private Notification notify 			= null;
	private NotificationManager notifier	= null;
	private static final int NOTIFY_4 = 0x1004;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		notifier = (NotificationManager)     context.getSystemService(Context.NOTIFICATION_SERVICE);
		notify = new Notification(R.drawable.star, "You've got a message", System.currentTimeMillis());
		
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, questionaire.class), 0);
		notify.setLatestEventInfo(context, "Doctor's Message!", "Please do answer the questionnaire if you don't", contentIntent);
		notify.flags |= Notification.FLAG_AUTO_CANCEL; // when clicked the notification, off the notification
		notify.sound = Uri.parse("android.resource://com.ucla.WANDA/" + R.raw.smallalarm);  
		//notify.vibrate = (long[]) intent.getExtras().get("vibrationPatern");

		// The PendingIntent to launch our activity if the user selects this notification
		notifier.notify(NOTIFY_4, notify);
	}
	
}
