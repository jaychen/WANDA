package com.ucla.WANDA.record;

import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ucla.WANDA.BTService;
import com.ucla.WANDA.Constants;
import com.ucla.WANDA.R;
import com.ucla.WANDA.SampleValues;
import com.ucla.WANDA.WandaFile;

public class Measurement extends Activity {

	private static final int REQUEST_ENABLE_BT = 2;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	private static final int CONNECTION_LOST = 4;

	private TextView bpTV, scaleTV;	
	private String scaleStr = "";
	// Create a SampleValues object used to pass data
	private SampleValues scaleV = new SampleValues(Constants.DEVICE_TYPE_SCALE);
	
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BTService mChatService = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		savedInstanceState = this.getIntent().getExtras();
		int flag = savedInstanceState.getInt("DeviceType");
		
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
		}
		//Log.v("JAY", "Flag: " + flag);
		if(flag == Constants.DEVICE_TYPE_BP)
		{
			setContentView(R.layout.bp_measurement);
			bpTV = (TextView) findViewById(R.id.scaleView);
			ensureDiscoverable();
			
			if(mChatService != null){
				mChatService.stop();
				mChatService = null;
			}			
			mChatService = new BTService(this, mHandler, Constants.DEVICE_TYPE_BP);
			mChatService.start();
			
			/*StatAnalysis s = new StatAnalysis();
			String tp = "";
			tp+="Slope: " + s.regressionTest(7, Constants.BP_FILE, ReadFileData.DIASTOLIC);
			tp+="\nMean: " + s.getMean(7, Constants.BP_FILE, ReadFileData.DIASTOLIC);
			tp+="\nVariance: " + s.getVariance(7, Constants.BP_FILE, ReadFileData.DIASTOLIC);
			Log.v("JAY", tp);*/
					
		}
		else if(flag == Constants.DEVICE_TYPE_SCALE)
		{
			setContentView(R.layout.w_measurement);
			scaleTV = (TextView) findViewById(R.id.scaleView);

			if(mChatService != null){
				mChatService.stop();
				mChatService = null;
			}
			mChatService = new BTService(this, mHandler, Constants.DEVICE_TYPE_SCALE);
			mChatService.start();			
		}
		else if(flag == Constants.DEVICE_TYPE_MGH)
		{
			setContentView(R.layout.wgh_measurement);
			scaleTV = (TextView) findViewById(R.id.wghView);

			if(mChatService != null){
				mChatService.stop();
				mChatService = null;
			}
			mChatService = new BTService(this, mHandler, Constants.DEVICE_TYPE_MGH);
			mChatService.start();			
		}
		
		Button gobackBtn = (Button) findViewById(R.id.goback);
		gobackBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
			

	}

	@Override
	public void onStart() {
		super.onStart();
		// If BT is not on, request that it be enabled.
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			// if (mChatService == null) setupChat();
		}

	}

	/** The Handler to receive information back from the BluetoothChatService*/
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				Log.v("JAY", "Write: " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;

				//Log.v("JAY", "Read " + msg.arg1 + " Bytes");				
				if (msg.arg2 == Constants.DEVICE_TYPE_BP)
					parseBP(readBuf, msg.arg1);
				else if (msg.arg2 == Constants.DEVICE_TYPE_SCALE)
					parseScale(readBuf, msg.arg1);
				break;
			case CONNECTION_LOST:
				Toast.makeText(getBaseContext(), "Connection Lost",
						Toast.LENGTH_SHORT).show();
				// Write File
				// weightFile.writeData(weightData);
				mChatService.stop();
				//mChatService.start();
				break;
			}
		}
	};

	private void parseBP(byte[] in, int length) {
		SampleValues v = new SampleValues(Constants.DEVICE_TYPE_BP);
		String tmp = bpTV.getText().toString();
		int valid = 0;
		int diff = 0;
		int diastolic = 0;
		int pulseRate = 0;
		int map = 0;
		Log.v("JAY", getHexString(in, length));

		valid = Integer.parseInt(new String(in, 60, 2), 16);
		diff = Integer.parseInt(new String(in, 62, 2), 16);
		diastolic = Integer.parseInt(new String(in, 64, 2), 16);
		pulseRate = Integer.parseInt(new String(in, 66, 2), 16);
		map = Integer.parseInt(new String(in, 68, 2), 16);
		
		v.setSystolic(diff + diastolic);
		v.setDiastolic(diastolic);
		v.setHeartRate(pulseRate);
		v.setMean(map);
		
		if (valid == 128)
			tmp += "\nValid Data\n";

		tmp += "systolic: " + (diff + diastolic);
		tmp += "\nDiastolic: " + diastolic;
		tmp += "\nPulse Rate: " + pulseRate;
		tmp += "\nmap: " + map;

		bpTV.setText(tmp);
		popAlert(v.validateData());
		WandaFile f = new WandaFile(this,v);
		f.autoWrite();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sendMessage("PWA3");
		f.uploadAll();		// Try to upload the file immediately
	}

	private String getHexString(byte[] b, int length) {
		String result = "";
		for (int i = 0; i < length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	/** Need to Convert Lb to Kg */
	private void parseScale(byte[] in, int length) {
		// construct a string from the valid bytes in the buffer		
		String parse = new String(in, 0, length);
		scaleStr += parse;
		// Because data is chopped to multiple sections, wait until all data are received to write the file
		if((parse.indexOf("\r")==-1) && (parse.indexOf("\n")==-1) )			
			return;					
		
		String tmp = scaleTV.getText().toString();
		String[] tokens = scaleStr.split("[,]");
	
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokens[i].toUpperCase();
			if (tokens[i].equals("WK")) {
				tmp += "\nWeight: " + tokens[++i]	+ "Kg\n";				
				scaleV.setWeight(Float.valueOf(tokens[i].trim()).floatValue()); 
				// not a good way. Need to
				// Validate the data.
			} else if (tokens[i].equals("FW")) {
				tmp += "Body Fat: " + tokens[++i] + "% \n";
				scaleV.setBodyFat(Float.valueOf(tokens[i].trim()).floatValue()); 				
			} else if (tokens[i].equals("WW")) {
				tmp += "Body Water: " + tokens[++i] + "% \n";
				scaleV.setBodyWater(Float.valueOf(tokens[i].trim()).floatValue()); 				
			} else if (tokens[i].equals("MW")) {
				tmp += "Muscle Mass: " + tokens[++i] + "Kg\n";
				scaleV.setMuscleMass(Float.valueOf(tokens[i].trim()).floatValue());				
			} else if (tokens[i].equals("RA")) {
				tmp += "Metabolic Age: " + tokens[++i] + "\n";
				scaleV.setMetabolicAge(Integer.valueOf(tokens[i].trim()).intValue());				
			} else if (tokens[i].equals("BW")) {
				tmp += "Bone Mass: " + tokens[++i] + "Kg\n";
				scaleV.setBoneMass(Float.valueOf(tokens[i].trim()).floatValue());				
			} else if (tokens[i].equals("PR")) {
				tmp += "Viceral Rating: " + tokens[++i] + "\n";
				scaleV.setViceralRating(Float.valueOf(tokens[i].trim()).floatValue());				
			}					
			scaleTV.setText(tmp);			
		}		
		String str = scaleV.validateData();
		popAlert(str);
		if(str.indexOf("Wrong User") < 0){
			WandaFile f = new WandaFile(this, scaleV);
			f.autoWrite();	// Write scaleV into file
			f.uploadAll();		// Try to upload the file immediately
		}
		// Re-Initialize the variables
		scaleStr = "";		
		scaleV = new SampleValues(Constants.DEVICE_TYPE_SCALE);	
		
	}

	/**
	 * The Android phone must be discoverable before the BP Monitor can connect to it.
	 */
	private void ensureDiscoverable() {
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Need this method when the Android device is has not been paired with other Medical devices
	 */
	private void searchDevices() {
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice devices : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
				/*
				 * mArrayAdapter .add(device.getName() + "\n" +
				 * device.getAddress());
				 */
				Log.v("JAY", "Parid Device: " + devices.getName() + "\n"
						+ devices.getAddress());
			}
		}

		// Create a BroadcastReceiver for ACTION_FOUND
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

					Log.v("JAY", "Discover " + device.getName() + "\n"
							+ device.getAddress());
				}
			}
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister
		// during onDestroy

		if (mBluetoothAdapter.startDiscovery()) {
			Log.v("JAY", "Found Device. True");
		}
	}

	/** 
	 * @param message
	 * A string of text to send.
	 */
	private void sendMessage(String message) {

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);
		}
	}
	private void popAlert(String tmp){
		AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle("Summary");
		alert.setButton("OK", new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		        return;
		      } }); 
		alert.setMessage(tmp);
		alert.show();
	}



	@Override
	public synchronized void onResume() {
		super.onResume();

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.

	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		//mChatService.stop();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
	}
}
