package com.ucla.WANDA.history;

import com.ucla.WANDA.R;
import com.ucla.WANDA.R.id;
import com.ucla.WANDA.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class historypersonaldata extends Activity {

	private Button weightBtn		= null;
	private Button bloodpressureBtn = null;
	private Button activityBtn		= null;
	private Button gobackBtn 		= null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.historypersonaldata);
	
	    // TODO Auto-generated method stub
	    new AddStringTask().execute();
	    
	    weightBtn = (Button) findViewById(R.id.weighthistory);
	    bloodpressureBtn = (Button) findViewById(R.id.bloodpressurehistory);
	    activityBtn = (Button) findViewById(R.id.activity);
	    gobackBtn = (Button) findViewById(R.id.goback);
	   
	    
	    weightBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(historypersonaldata.this, chartWeight.class);
				startActivity(intent);
			}
		});
	    
	    bloodpressureBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(historypersonaldata.this, chartBloodPressure.class);
				startActivity(intent);
			}
		});
	    
	    activityBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(historypersonaldata.this, chartActivity.class);
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
	}
	

	class AddStringTask extends AsyncTask<Void, String, Void> {
		private final ProgressDialog dialog = new ProgressDialog(historypersonaldata.this);
		
		@Override
		protected Void doInBackground(Void... unused) {
			//for (String item : items) {
			//	publishProgress(item);
				
			//	SystemClock.sleep(200);
				
			//}
			publishProgress();
			SystemClock.sleep(1000);
			
			return(null);
		}
		
		@Override
		protected void onProgressUpdate(String... item) {
			//((ArrayAdapter)getListAdapter()).add(item[0]);
			dialog.setTitle("Downloading contents");
			dialog.setMessage("Please wait......");
			dialog.show();
		}
		
		@Override
		protected void onPostExecute(Void unused) {
			//Toast.makeText(AddStringTask.this, "Done!", Toast.LENGTH_SHORT).show();
			Toast.makeText(historypersonaldata.this, "Done", Toast.LENGTH_SHORT);
			
			if(this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}
}
