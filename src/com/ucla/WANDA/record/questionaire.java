package com.ucla.WANDA.record;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.ucla.WANDA.R;

public class questionaire extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	public static final String KEY_LIST_PREF_1 = "list_preference1";
	public static final String KEY_LIST_PREF_2 = "list_preference2";
	public static final String KEY_LIST_PREF_3 = "list_preference3";
	public static final String KEY_LIST_PREF_4 = "list_preference4";
	public static final String KEY_LIST_PREF_5 = "list_preference5";
	public static final String KEY_LIST_PREF_6 = "list_preference6";
	public static final String KEY_LIST_PREF_7 = "list_preference7";
	public static final String KEY_LIST_PREF_8 = "list_preference8";
	public static final String KEY_LIST_PREF_9 = "list_preference9";
	public static final String KEY_LIST_PREF_10 = "list_preference10";
	public static final String KEY_LIST_PREF_11 = "list_preference11";
	public static final String KEY_LIST_PREF_12 = "list_preference12";
	public static final String KEY_LIST_PREF_13 = "list_preference13";
	
	private boolean question1 = false;
	private boolean question2 = false;
	private boolean question3 = false;
	private boolean question4 = false;
	private boolean question5 = false;
	private boolean question6 = false;
	private boolean question7 = false;
	private boolean question8 = false;
	private boolean question9 = false;
	private boolean question10 = false;
	private boolean question11 = false;
	private boolean question12 = false;
	private boolean question13 = false;
	
	private String answer1 = "";
	private String answer2 = "";
	private String answer3 = "";
	private String answer4 = "";
	private String answer5 = "";
	private String answer6 = "";
	private String answer7 = "";
	private String answer8 = "";
	private String answer9 = "";
	private String answer10 = "";
	private String answer11 = "";
	private String answer12 = "";
	private String answer13 = "";
	
	private Preference Preference1;
	private Preference Preference2;
	private Preference Preference3;
	private Preference Preference4;
	private Preference Preference5;
	private Preference Preference6;
	private Preference Preference7;
	private Preference Preference8;
	private Preference Preference9;
	private Preference Preference10;
	private Preference Preference11;
	private Preference Preference12;
	private Preference Preference13;
	
	private String questionnaire_result = "";
	private String user_name = "";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    savedInstanceState = this.getIntent().getExtras(); // getting intent
	    user_name = savedInstanceState.getString("userid");
	    
	    addPreferencesFromResource(R.layout.questionaire);
	    
	    question1 = false;
	    question2 = false;
	    question3 = false;
	    question4 = false;
	    question5 = false;
	    question6 = false;
	    question7 = false;
	    question8 = false;
	    question9 = false;
	    question10 = false;
	    question11 = false;
	    question12 = false;
	    question13 = false;
 	   
	    // TODO Auto-generated method stub*/
	    Preference1 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_1);
	    Preference2 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_2);
	    Preference3 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_3);
	    Preference4 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_4);
	    Preference5 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_5);
	    Preference6 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_6);
	    Preference7 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_7);
	    Preference8 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_8);
	    Preference9 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_9);
	    Preference10 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_10);
	    Preference11 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_11);
	    Preference12 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_12);
	    Preference13 = (Preference) getPreferenceScreen().findPreference(KEY_LIST_PREF_13);
	    
	    //Log.i("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", Preference1.getKey().toString());
	    
	    // TODO Auto-generated method stub
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			
		if(key.equals(KEY_LIST_PREF_1)) {
			answer1 = sharedPreferences.getString(key, "");
			Preference1.setSummary(answer1);
			question1 = true;
		}
		else if(key.equals(KEY_LIST_PREF_2)) {
			answer2 = sharedPreferences.getString(key, "");
			Preference2.setSummary(answer2);
			question2 = true;
		}
		else if(key.equals(KEY_LIST_PREF_3)) {
			answer3 = sharedPreferences.getString(key, "");
			Preference3.setSummary(answer3);
			question3 = true;
		}
		else if(key.equals(KEY_LIST_PREF_4)) {
			answer4 = sharedPreferences.getString(key, "");
			Preference4.setSummary(answer4);
			question4 = true;
		}
		else if(key.equals(KEY_LIST_PREF_5)){
			answer5 = sharedPreferences.getString(key, "");
			Preference5.setSummary(answer5);
			question5 = true;
		}
		else if(key.equals(KEY_LIST_PREF_6)){
			answer6 = sharedPreferences.getString(key, "");
			Preference6.setSummary(answer6);
			question6 = true;
		}
		else if(key.equals(KEY_LIST_PREF_7)){
			answer7 = sharedPreferences.getString(key, "");
			Preference7.setSummary(answer7);
			question7 = true;
		}
		else if(key.equals(KEY_LIST_PREF_8)){
			answer8 = sharedPreferences.getString(key, "");
			Preference8.setSummary(answer8);
			question8 = true;
		}
		else if(key.equals(KEY_LIST_PREF_9)){
			answer9 = sharedPreferences.getString(key, "");
			Preference9.setSummary(answer9);
			question9 = true;
		}
		else if(key.equals(KEY_LIST_PREF_10)){
			answer10 = sharedPreferences.getString(key, "");
			Preference10.setSummary(answer10);
			question10 = true;
		}
		else if(key.equals(KEY_LIST_PREF_11)){
			answer11 = sharedPreferences.getString(key, "");
			Preference11.setSummary(answer11);
			question11 = true;
		}
		else if(key.equals(KEY_LIST_PREF_12)){
			answer12 = sharedPreferences.getString(key, "");
			Preference12.setSummary(answer12);
			question12 = true;
		}
		else {
			answer13 = sharedPreferences.getString(key, "");
			Preference13.setSummary(answer13);
			question13 = true;
		}
			
		if(question1 == true && 
				question2 == true &&
				question3 == true &&
				question4 == true &&
				question5 == true &&
				question6 == true &&
				question7 == true &&
				question8 == true &&
				question9 == true &&
				question10 == true &&
				question11 == true &&
				question12 == true &&
				question13 == true){
			new AlertDialog.Builder(this)
			.setIcon(R.drawable.dialogquestion)
			.setTitle("Done All Questions. Send to Server?")			
			.setPositiveButton("Send", new DialogInterface.OnClickListener() {
		     @Override
		     public void onClick(DialogInterface dialog, int which) {
		    	 SimpleDateFormat timeFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
		    	 String timeStamp = timeFormat.format(Calendar.getInstance().getTime());
		    	
		    	//String myDate = DateFormat.format("yyyy-mm-dd hh:mm:ss", Calendar.getInstance().getTime()).toString(); 
		    	
		    	Log.i("AAA", timeStamp);
		    	
		    	questionnaire_result = user_name + "\n";
		    	questionnaire_result += timeStamp + "\n"; 
		    	questionnaire_result += answer1 + "\n" + answer2 + "\n" + answer3 + "\n" + answer4 + "\n" + answer5 + "\n" + answer6 + "\n" + answer7 + "\n" + answer8 + "\n"
		    	+ answer9 + "\n" + answer10 + "\n" + answer11 + "\n" + answer12 + "\n" + answer13;
		    	 
		    	 
		    	new AddStringTask().execute();
			  }
		     }
		    )
		    .setNegativeButton("Not yet", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			}).show();
			
		}
		//Intent myIntent = new Intent(advancedsetting.this, backgroundservice.class);
		//myIntent.putExtra("selected", selectedAlarm);
			
		//startService(myIntent);

	}
	
	class AddStringTask extends AsyncTask<Void, String, Void> {
		private final ProgressDialog dialog = new ProgressDialog(questionaire.this);
		
		@Override
		protected Void doInBackground(Void... unused) {
			
			fileManage fm = new fileManage();
			publishProgress();
			SystemClock.sleep(2000);
			
			fm.fileSaved(questionnaire_result);
			fm.fileUpload();
			
			return(null);
		}
		
		@Override
		protected void onProgressUpdate(String... item) {
			dialog.setTitle("Upload to Server");
			dialog.setMessage("Please wait......");
			dialog.show();
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			
			if(this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			Toast.makeText(questionaire.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
			Log.i("username", user_name + "");
			SystemClock.sleep(1000);
			finish();
		}
	}	

}
