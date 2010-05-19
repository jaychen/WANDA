package com.ucla.WANDA;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ucla.WANDA.history.historypersonaldata;
import com.ucla.WANDA.record.advancedsetting;
import com.ucla.WANDA.record.recordpersonaldata;
import com.ucla.WANDA.services.ICallBack;
import com.ucla.WANDA.services.ISensorService2;

public class WANDA extends Activity implements Observer {
	private final int ACTIVITY_LOGIN = 0;
	private final int ACTIVITY_SPLASH = 1;
	public static final String PREFERENCES_USER = "user";
	public static final int RESULT_LOCATION_DISABLED = 1;
	public static final int RESULT_LOCATION_MISSING = 2;
	private static final int MESSAGE_COMPLETE_TAG = 55;
	private static final int MESSAGE_TIMEOUT_TAG = 56;
	private static final int MESSAGE_NO_RESPONSE_TAG = 57;
	private static final int WELCOMER_RESULT = 58;
	protected static final int HELP_IMAGE = 59;
	protected static final int CHANGE_GPS_SETTINGS = 23;
	protected static final int CHANGE_GPS_SETTINGS_2 = 24;
	protected static final int BLOCKING_TAG = 312;
	private boolean serviceStarted = false;
	
	AlertDialog.Builder ad;
	SharedPreferences preferences;
	
	File root = Environment.getExternalStorageDirectory();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		Intent intent = new Intent(this, WANDAmain.class);
		this.startActivityForResult(intent, ACTIVITY_SPLASH);

		setContentView(R.layout.main);
		setTitle("Welcome to WANDA");
		preferences = this.getSharedPreferences(PREFERENCES_USER, Activity.MODE_PRIVATE);
		
		Button recordBtn = (Button) findViewById(R.id.record_button);
	    Button historyBtn = (Button) findViewById(R.id.history_button);
	    
	    Intent myIntent = new Intent(WANDA.this, backgroundservice.class);
		startService(myIntent);
		
		// Start SensorService. This should be called at the beginning of the application
		
		Intent serviceIntent = new Intent();
		serviceIntent.setAction(Constants.SENSOR_SERVICE);
		startService(serviceIntent);				
		serviceStarted = true;
		
		recordBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(WANDA.this, recordpersonaldata.class);
					
					startActivity(intent);
				}
		});
	        
	    historyBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
			public void onClick(View v) {
					// TODO Auto-generated method stub
				Intent intent = new Intent(WANDA.this, historypersonaldata.class);
				startActivity(intent);				
			}
		});
	        
	    File dir = new File(root.getAbsolutePath(), "WANDA");
	    dir.mkdir();
	    initializeService();
	}
  
	@Override
	protected void onResume() {
			
		TextView textView = (TextView) this.findViewById(R.id.TextView03); 
		
		SharedPreferences preferences = this.getSharedPreferences(WANDA.PREFERENCES_USER, Activity.MODE_PRIVATE);
		String username_string = preferences.getString("username", "");
		
		if(!username_string.equals(""))
		{
			textView.setText(getString(R.string.login_username) + ": " + username_string);
		} 
		else 
			textView.setText(getString(R.string.whatsinvasive_notlogged));
		
		super.onResume();
		
		initializeService();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			mySensor2.unregisterCallback(mCallback);
		} catch (RemoteException re) {
			re.printStackTrace();
		}
		releaseService();
	}
	
	protected void showToast(String text,int length){
		Toast.makeText(this, text, length).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(requestCode){
		case ACTIVITY_LOGIN:
			if(resultCode == Activity.RESULT_OK || resultCode==Activity.RESULT_CANCELED){

				final SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_USER, Activity.MODE_PRIVATE);					
				preferences.edit().putBoolean("firstRun", false).commit();
	
			}
			else{
				this.finish();
			}
			break;
		case ACTIVITY_SPLASH:
			// Check user preferences
			final SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_USER, Activity.MODE_PRIVATE);		
			
			if(preferences.getString("username", null)==null || preferences.getString("password", null)==null){
				Intent intent = new Intent(this, Login.class);
				this.startActivityForResult(intent, ACTIVITY_LOGIN);
			}
			
			break;
		case WELCOMER_RESULT:
			//do nothing for now
			break;
		}
	}

	protected final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg){
			if(msg.arg1 == 5){
				WANDA.this.dismissDialog(BLOCKING_TAG);
				onResume();
			}
			else{
				switch(msg.what){
				case MESSAGE_COMPLETE_TAG:
					showToast(getString(R.string.tag_list_download_complete), Toast.LENGTH_SHORT);
					break;
				case MESSAGE_TIMEOUT_TAG:
					showToast(getString(R.string.tag_list_download_failed_http), Toast.LENGTH_LONG);
					break;
				case MESSAGE_NO_RESPONSE_TAG:
					showToast(getString(R.string.tag_list_download_failed_server), Toast.LENGTH_LONG);
					break;
				}
			}
		}
	};

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
	}
	
	/*
	 * Accelerometer Services
	 */
	/* Service */
	private void initializeService(){
		//Log.v("JAY", "bind with Service");
		bindService(new Intent(Constants.SENSOR_SERVICE), conn2, Context.BIND_AUTO_CREATE);			
	}
	
	private void releaseService(){
		if(serviceStarted)
			unbindService(conn2);		
	}
		
	private Handler accHandler = new Handler() {
		public void handleMessage(Message msg) {
			double temp;
			temp = (Double)msg.obj;
			
			if (msg.what == 0) {				
				//tv.setText("Current Activity Level: " + (int)temp);
				Toast.makeText(getBaseContext(), "Activity Level: " + (int)temp ,
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private ISensorService2 mySensor2;
	private ServiceConnection conn2 = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mySensor2 = ISensorService2.Stub.asInterface(service);	
			//Log.v("JAY", "Connection Started!");
			try {
				mySensor2.registerCallback(mCallback);
			} catch (RemoteException e) {				
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mySensor2 = null;			
		}		
	};
	
	private ICallBack mCallback = new ICallBack.Stub(){
		@Override
		public void onKmChanged(double value) throws RemoteException {	
			//Log.v("JAY","OverRide Call Back! Value:" + value);
			Message lmsg = new Message();
			lmsg.obj = new Double(value);
			lmsg.what = 0;								
			accHandler.sendMessage(lmsg);
		}		
	};
	
    public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		new MenuInflater(this).inflate(R.menu.settingmenu, menu);
		return result; 
	}
    
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.advancedsetting :
    		Intent intent = new Intent(WANDA.this, advancedsetting.class);
    		startActivity(intent);
    		break;
    	case R.id.closeprogram :
    		SharedPreferences preferences = this.getSharedPreferences(WANDA.PREFERENCES_USER, Activity.MODE_PRIVATE);
   
			Editor edit = preferences.edit();

			edit.putString("username", null);
			edit.putString("password", null);
			
			edit.commit();
    		//preferences.getString(key, defValue)
    		stopService(new Intent(this, backgroundservice.class));
    		finish(); // Close the program
    		break;
    	}
    	return (super.onOptionsItemSelected(item));
	}
}