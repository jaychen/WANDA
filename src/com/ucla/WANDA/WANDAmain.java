package com.ucla.WANDA;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

public class WANDAmain extends Activity{
	private static final int WELCOMER_RESULT = 0;
	protected static final int NOGPS = 1;
	protected static final int YESGPS = 2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = this.getIntent().getExtras();

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.wandamain);
		
		//SharedPreferences preferences = this.getSharedPreferences(WANDA.PREFERENCES_USER, Activity.MODE_PRIVATE); // DO NOT ERASE
		//final boolean firstRun = preferences.getBoolean("firstRun", true); //DO NOT ERASE

		// DO NOT ERASE BELOW
		if(extras == null || !extras.containsKey("noTimer")){
			// Start exit timer
			Timer timer = new Timer();
			TimerTask task = new TimerTask(){

				@Override
				public void run() {
					showWelcome();
				}};
				timer.schedule(task, 4000);
		}
	}
	
	private void showWelcome(){
		Intent intent = new Intent(this, MainScreen.class);
		intent.putExtra("setupMode", true);
		this.startActivityForResult(intent, WELCOMER_RESULT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case WELCOMER_RESULT:
			if(resultCode==Activity.RESULT_OK){
				WANDAmain.this.finish();
				break;
			}else if(resultCode==Activity.RESULT_CANCELED){			
				WANDAmain.this.setResult(RESULT_CANCELED);
				WANDAmain.this.finish();
				break;
			}
			else{			
				WANDAmain.this.finish();
				break;
			}			
		}
	}
}
