package com.ucla.WANDA;

import com.ucla.WANDA.record.AlarmService_Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;


public class backgroundservice extends Service {
	private PendingIntent sender			= null;
	private static AlarmManager am			= null;
	
	
	public static final String BG_SERVICE = "com.ucla.backgroundservice.SERVICE";
	public static final String EXTRA_UPDATE_RATE = "update-rate";

	public void onCreate() {
		super.onCreate();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onStart(Intent intent, int startId) {
	    super.onStart(intent, startId);
	   
	    Intent in = new Intent(backgroundservice.this, AlarmService_Service.class);
	    //in.putExtra("vibrationPatern", new long[] { 200, 300 }); // For vibrate, FUTURE WORK
        PendingIntent sender = PendingIntent.getBroadcast(backgroundservice.this, 0, in, 0);
        
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000*10*60*6, 1000*10*60*6, sender);
        
	}	
	
	public void onDestroy() {
		super.onDestroy();
		stopSelf();
		//am.cancel(sender);
	}
}
