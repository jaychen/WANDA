package com.ucla.WANDA.record;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import com.ucla.WANDA.Constants;
import com.ucla.WANDA.R;
import com.ucla.WANDA.backgroundservice;
import com.ucla.WANDA.services.ISensorService2;

public class advancedsetting extends PreferenceActivity implements OnSharedPreferenceChangeListener{

	public static final String KEY_CHECKBOX_PREF = "checkbox_preference";
	public static final String KEY_CHECKBOX_PREF2 = "checkbox_preference2";
	public MediaPlayer player = null;
	private Preference mPreference;
	private Preference alarmPreference;
	

	NotificationManager notifier = null;
	Notification notify = null;

	//private static final int NOTIFY_2 = 0x1002;
	//private static final int NOTIFY_3 = 0x1003;
	//private static final int NOTIFY_4 = 0x1004;

	
	//private int alarmSelection = 0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.layout.advancedsetting);
	    	   
	    // TODO Auto-generated method stub*/
	    mPreference = (Preference) getPreferenceScreen().findPreference(KEY_CHECKBOX_PREF);
	    alarmPreference = (Preference) getPreferenceScreen().findPreference(KEY_CHECKBOX_PREF2);
	    
	    notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	    
	    notify = new Notification(R.drawable.star, "New Message Just Arrived!", System.currentTimeMillis());
	    //using currentTimeMillis pops up the notification ASAP   
	    
	    initializeService();
	}
	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		initializeService();
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		if(key.equals(KEY_CHECKBOX_PREF)) {
			Toast.makeText(this, "KEY: " + key + 
					"\ngetBoolean(): " + sharedPreferences.getBoolean(key, false), Toast.LENGTH_SHORT).show();
			mPreference.setSummary(sharedPreferences.getBoolean(key, false) ? 
						"Recording : Enabled" : "Recording : Disabled"); 
			if(sharedPreferences.getBoolean(key, false)){				
				try {
					mySensor2.startAcc();
				} catch (RemoteException e) {					
					e.printStackTrace();
				}
			}
			else{				
				try {
					mySensor2.stopAcc();
				} catch (RemoteException e) {					
					e.printStackTrace();
				}
			}			
		}
		
		if(key.equals(KEY_CHECKBOX_PREF2)) {
			alarmPreference.setSummary(sharedPreferences.getBoolean(key, false) ? 
					"Alarm : On" : "Alarm : Off"); 
			
			if(sharedPreferences.getBoolean(key, false)) {
				Intent myIntent = new Intent(advancedsetting.this, backgroundservice.class);
				myIntent.putExtra(backgroundservice.EXTRA_UPDATE_RATE, 5000);
				startService(myIntent);
			}
		}
	}
	
	private ISensorService2 mySensor2;
	private ServiceConnection conn2 = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mySensor2 = ISensorService2.Stub.asInterface(service);				
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mySensor2 = null;			
		}		
	};
	
	/*
	 * Accelerometer Services
	 */
	/* Service */
	private void initializeService(){
		Log.v("JAY", "bind with Service");
		bindService(new Intent(Constants.SENSOR_SERVICE), conn2, Context.BIND_AUTO_CREATE);			
	}
	
	private void releaseService(){
		unbindService(conn2);		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();		
		releaseService();
	}
	
	
	// TODO : Be Specific on each part
	/*
	private void soundOnly(){
		 notify.flags |= Notification.FLAG_AUTO_CANCEL;

		 //player = MediaPlayer.create(this, R.raw.braincandy);
		 //player.start();
		 //notify.sound =
		 notify.sound = Uri.parse("android.resource://com.ucla.WANDA/" + R.raw.smallalarm);
         
         Intent toLaunch = new Intent (advancedsetting.this, WANDA.class);
         PendingIntent intentBack = PendingIntent.getActivity(advancedsetting.this, 0, toLaunch, 0);
         
         notify.setLatestEventInfo(advancedsetting.this, "Doctor's Message!", "Please do answer the questionnaire", intentBack);
         
         notifier.notify(NOTIFY_4, notify);
	}
	private void vibrateOnly(){
		 Notification notify = new Notification(android.R.drawable.stat_notify_chat, "Vibrate!", System.currentTimeMillis());
         
         notify.flags |= Notification.FLAG_AUTO_CANCEL;
         
         notify.vibrate = new long[] {200, 200,  600, 600,   600, 200,  200, 600,   600, 200, 200, 200, 200, 600,    200,200, 600,200, 200, 600,    600, 200, 600, 200, 600, 600,   200, 200, 200, 600,   600, 200, 200, 200, 200, 600,}; 
         
         Intent toLaunch = new Intent (advancedsetting.this, WANDA.class);
         PendingIntent intentBack = PendingIntent.getActivity(advancedsetting.this, 0, toLaunch, 0);
         
         notify.setLatestEventInfo(advancedsetting.this, "Bzzt!", "This vibrated your phone.", intentBack);
         
         notifier.notify(NOTIFY_2, notify);
         
         Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
         vibe.vibrate(500);
	}
	private void blinkingOnly(){
		 notify.flags |= Notification.FLAG_AUTO_CANCEL;
         notify.number++;
         
         notify.flags |= Notification.FLAG_SHOW_LIGHTS;
         
         if (notify.number < 2) {
             notify.ledARGB = Color.GREEN;
             notify.ledOnMS = 1000;
             notify.ledOffMS = 1000;
         } else if (notify.number < 3) {
             notify.ledARGB = Color.BLUE;
             notify.ledOnMS = 750;
             notify.ledOffMS = 750;
         } else if (notify.number < 4) {
                 notify.ledARGB = Color.WHITE;
                 notify.ledOnMS = 500;
                 notify.ledOffMS = 500;
         } else {
             notify.ledARGB = Color.RED;
             notify.ledOnMS = 50;
             notify.ledOffMS = 50;
         }

         Intent toLaunch = new Intent (advancedsetting.this, WANDA.class);
         PendingIntent intentBack = PendingIntent.getActivity(advancedsetting.this, 0, toLaunch, 0);
         
         notify.setLatestEventInfo(advancedsetting.this, "Bright!", "This lit up your phone.", intentBack);
         
         notifier.notify(NOTIFY_3, notify);
	}
	*/
}
