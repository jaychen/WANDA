package com.ucla.WANDA.record;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ucla.WANDA.Constants;
import com.ucla.WANDA.R;
import com.ucla.WANDA.WANDA;

public class recordpersonaldata extends Activity {

	private Button weightBtn = null;
	private Button bloodpressureBtn = null;
	private Button questionBtn = null;
	private Button activityBtn = null;
	private Button gobackBtn = null;
	private String username_string = "";
	SharedPreferences preferences;
	
	private AudioRecorder rec;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recordpersonaldata);

		weightBtn = (Button) findViewById(R.id.weight);
		bloodpressureBtn = (Button) findViewById(R.id.bloodpressure);
		questionBtn = (Button) findViewById(R.id.dailyquestion);
		activityBtn = (Button) findViewById(R.id.activity);
		gobackBtn = (Button) findViewById(R.id.goback);
		rec = new AudioRecorder("/audioTest2.3gp");

		new AddStringTask().execute();

		weightBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Connect to the BT
				Intent intent = new Intent(recordpersonaldata.this,
						Measurement.class);
				intent.putExtra("DeviceType", Constants.DEVICE_TYPE_SCALE);
				startActivity(intent);
			}
		});

		bloodpressureBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Connect to the BT
				Intent intent = new Intent(recordpersonaldata.this,
						Measurement.class);
				intent.putExtra("DeviceType", Constants.DEVICE_TYPE_BP);
				startActivity(intent);
			}
		});

		questionBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences preferences = recordpersonaldata.this
						.getSharedPreferences(WANDA.PREFERENCES_USER,
								Activity.MODE_PRIVATE);
				username_string = preferences.getString("username", "");

				Intent intent = new Intent(recordpersonaldata.this,
						questionaire.class);
				intent.putExtra("userid", username_string);
				startActivity(intent);
			}
		});

		activityBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Intent intent = new Intent(recordpersonaldata.this,
						advancedsetting.class);
				startActivity(intent);*/
				
				Intent intent = new Intent(recordpersonaldata.this,
						Measurement.class);
				intent.putExtra("DeviceType", Constants.DEVICE_TYPE_MGH);
				startActivity(intent);				
			}
		});

		gobackBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// TODO Auto-generated method stub
	}

	/** Creates the menu items **/
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Record");
		menu.add(0, 1, 0, "Stop Recording");
		menu.add(0, 2, 0, "Play");
		menu.add(0, 3, 0, "Stop Playing");
		return true;
	}

	/** Handles item selections **/
	public boolean onOptionsItemSelected(MenuItem item) {
		
		String text="";
		/* Launch Activity According to Menu Selection */
		switch (item.getItemId()) {
		case 0:
			text="Start Recording...";
			try {
				rec.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case 1:
			text="Stop Recording...";
			try {
				rec.stop();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			text="Play Recording...";
			rec.play();
			break;
		case 3:
			text="Stop Playing...";
			rec.stopPlay();
			break;
		}
		Log.v("JAY", "Opening: " + text);
		Toast.makeText(getBaseContext(), text,
				Toast.LENGTH_LONG).show();
		return false;
	}

	class AddStringTask extends AsyncTask<Void, String, Void> {
		private final ProgressDialog dialog = new ProgressDialog(
				recordpersonaldata.this);

		@Override
		protected Void doInBackground(Void... unused) {
			// for (String item : items) {
			// publishProgress(item);

			// SystemClock.sleep(200);

			// }
			publishProgress();
			SystemClock.sleep(500);

			return (null);
		}

		@Override
		protected void onProgressUpdate(String... item) {
			// ((ArrayAdapter)getListAdapter()).add(item[0]);
			dialog.setTitle("Loading Page");
			dialog.setMessage("Please wait......");
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void unused) {
			// Toast.makeText(AddStringTask.this, "Done!",
			// Toast.LENGTH_SHORT).show();
			Toast.makeText(recordpersonaldata.this, "Done", Toast.LENGTH_SHORT);

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}
}
