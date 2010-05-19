package com.ucla.WANDA;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.view.KeyEvent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private Button button	= null;
	private final String VALIDATE_URL = "http://wanda.webege.com/authentication.php";
	private final int DIALOG_LOGGING = 0;
	private final int DIALOG_TIMEOUT = 1;
	private final int MESSAGE_INVALID_LOGIN = 0;
	private final int MESSAGE_LOGIN = 1;
	private final int MESSAGE_LOGIN_TIMEOUT = 2;
	private final int MESSAGE_LOGIN_RETRY = 3;
	private final int MESSAGE_LOGIN_CANCEL = 4;
	protected static final int BACKBUTTONPRESSED = 999;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login);	
	    // TODO Auto-generated method stub
	    
	    button = (Button) this.findViewById (R.id.ButtonLogin);
	    button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				validateLogin();
			}
		});
	}
	
	private void validateLogin(){
		final String username = ((EditText) this.findViewById(R.id.EditText01)).getText().toString();
		final String password = ((EditText) this.findViewById(R.id.EditText02)).getText().toString();
		
		if(username.equals("") || password.equals("")){
			Toast.makeText(this, getString(R.string.login_username_must_not_empty), Toast.LENGTH_LONG).show();
		}
		else
		{
			this.showDialog(DIALOG_LOGGING);
			
			Thread thread = new Thread() {
				@Override
				public void run() {
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet request = new HttpGet(VALIDATE_URL +"?username=" + username + "&password=" + password);
					
					try {
						HttpResponse response = httpClient.execute(request);
						
						if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
							
							HttpEntity entity = response.getEntity();
							if(entity != null) {
								InputStream instream = entity.getContent();
								byte[] tempByte = new byte[10000];
								instream.read(tempByte);
								String contents = new String(tempByte);
								
								String[] splits = contents.split("/"); // need to split first time
								String[] splitsAgain = splits[0].split(","); // need to split again for each attributes
								
								
								System.out.println(splits[0]);
								
								// There are five attributes 
								// Name, Sex, Age, Height(Ft), Last upload time 
								// for example, Frazier Hellings, Male, 35, 5.7, Last upload time
								// Let's put that in SharedPreferences
								SharedPreferences preferences = Login.this.getSharedPreferences(WANDA.PREFERENCES_USER, Activity.MODE_PRIVATE);					      
								Editor edit = preferences.edit();
								
								edit.putString("sirname", splitsAgain[0]);
								//edit.putString("sex", splitsAgain[1]);
								//edit.putString("age", splitsAgain[2]);
								//edit.putString("height", splitsAgain[3]);
								//edit.putString("weight", splitsAgain[4]);
								//edit.putString("lasttime", splitsAgain[4]);
								
								edit.commit();
								/////////////////////////////////////////////////////////////////////////////////////////////////////
							}

							Message msg = new Message();
							msg.what = MESSAGE_LOGIN;
							msg.obj = new String[]{username, password};
							
							Login.this.handler.sendMessage(msg);
						}
						else{
							// Failed to get data
							Login.this.handler.sendEmptyMessage(MESSAGE_INVALID_LOGIN);
						}
					} 
					catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						Login.this.handler.sendEmptyMessage(MESSAGE_LOGIN_TIMEOUT);
						e.printStackTrace();
					}
					catch (SocketTimeoutException e){
						//java.net.SocketTimeoutException: The operation timed out
						//java.net.SocketTimeoutException: Socket is not connected
						Login.this.handler.sendEmptyMessage(MESSAGE_LOGIN_TIMEOUT);
					} 
					catch (IOException e) {
						Login.this.handler.sendEmptyMessage(MESSAGE_LOGIN_TIMEOUT);
						e.printStackTrace();
					}
				}
			};
			thread.start();
		}
	}
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
				case MESSAGE_INVALID_LOGIN:
					Login.this.dismissDialog(DIALOG_LOGGING);
					Toast.makeText(Login.this, getString(R.string.login_invalid), Toast.LENGTH_SHORT).show();
					
					break;
				case MESSAGE_LOGIN:
					SharedPreferences preferences = Login.this.getSharedPreferences(WANDA.PREFERENCES_USER, Activity.MODE_PRIVATE);
			        
					Editor edit = preferences.edit();
					
					String[] values = (String[]) msg.obj;
					
					edit.putString("username", values[0]);
					edit.putString("password", values[1]);
					
					edit.commit();
					Login.this.finish();
					break;
				case MESSAGE_LOGIN_TIMEOUT:
					Login.this.dismissDialog(DIALOG_LOGGING);
					Login.this.showDialog(DIALOG_TIMEOUT);
					break;
				case MESSAGE_LOGIN_RETRY:
					Login.this.dismissDialog(DIALOG_TIMEOUT);
					validateLogin();
					
					break;
				case MESSAGE_LOGIN_CANCEL:
					Login.this.finish();
					
					break;
			}
		}
	};
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		
		switch(id){
			case DIALOG_LOGGING:
				ProgressDialog progress = new ProgressDialog(this);
				
				progress.setTitle(getString(R.string.login_loading));
				progress.setMessage(getString(R.string.login_please_wait));
				progress.setIndeterminate(true);
				progress.setCancelable(false);
				
				dialog = progress;
				
				break;
			case DIALOG_TIMEOUT:
				dialog = new AlertDialog.Builder(this)
					.setTitle(getString(R.string.login_timeout_title))
					.setMessage(getString(R.string.login_timeout_msg))
					.setPositiveButton(getString(R.string.login_retry_button), new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Login.this.handler.sendEmptyMessage(MESSAGE_LOGIN_RETRY);
						}})
					.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Login.this.handler.sendEmptyMessage(MESSAGE_LOGIN_CANCEL);
						}})
					
					.create();
				break;
		}
			
		return dialog;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	this.setResult(Login.BACKBUTTONPRESSED);
        	this.finish();
        }
        return false;
    }
}
