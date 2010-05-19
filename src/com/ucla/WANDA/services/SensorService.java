package com.ucla.WANDA.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.ucla.WANDA.Constants;
import com.ucla.WANDA.SampleValues;
import com.ucla.WANDA.WandaFile;

public class SensorService extends Service {
	// private TextView accText;
	private SensorManager myManager;
	private List<Sensor> sensors;
	private Sensor accSensor;
	private long lastUpdate = 0;

	private int sampleCounter = 0;
	private float sumAcc[] = new float[3];
	private float sumAccSquare[] = new float[3];

	private double currentKm = 1;
	private final RemoteCallbackList<ICallBack> mcallbacks = new RemoteCallbackList<ICallBack>();
	//private SharedPreferences preferences;


	@Override
	public IBinder onBind(Intent arg0) {
		if (ISensorService2.class.getName().equals(arg0.getAction())) {
			Log.v("JAY", "accBinder is Bound");
			return accBinder;
		} else
			return accBinder;
	}

	private final ISensorService2.Stub accBinder = new ISensorService2.Stub() {

		@Override
		public double getKm() throws RemoteException {
			return currentKm;
		}

		@Override
		public void registerCallback(ICallBack cb) throws RemoteException {
			if (cb != null)
				mcallbacks.register(cb);
		}

		@Override
		public void unregisterCallback(ICallBack cb) throws RemoteException {
			if (cb != null)
				mcallbacks.unregister(cb);
		}

		@Override
		public void startAcc() throws RemoteException {
			// TODO Auto-generated method stub
			myManager.registerListener(mySensorListener, accSensor,
					SensorManager.SENSOR_DELAY_GAME);
		}

		@Override
		public void stopAcc() throws RemoteException {
			// TODO Auto-generated method stub
			myManager.unregisterListener(mySensorListener);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		lastUpdate = System.currentTimeMillis();
		//preferences = this.getSharedPreferences(Constants.PREFS_NAME, Activity.MODE_PRIVATE);
		//boolean tmp = preferences.getBoolean(Constants.PREFS_ACC_REC, false);
		
		myManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensors = myManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) {
			accSensor = sensors.get(0);
		}
		
		// Start the Acc Sensor only if the advanced Setting is true		
		myManager.registerListener(mySensorListener, accSensor,
				SensorManager.SENSOR_DELAY_GAME);
				
		// Start the Upload Timer Service. By default, upload in every 60 minute
		UploadTimer uploadTask = new UploadTimer(Constants.UPLOAD_PERIOD);
		uploadTask.start();		
		
	}

	private void callBackKm(double value) {
		final int callbacks = mcallbacks.beginBroadcast();
		for (int i = 0; i < callbacks; i++) {
			try {
				mcallbacks.getBroadcastItem(i).onKmChanged(value);
				// Log.v("JAY", "callBackKm done!");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		mcallbacks.finishBroadcast();
	}

	private void processData(float x, float y, float z) {
		long curTime = System.currentTimeMillis();
		sampleCounter++;
		sumAcc[0] += x;
		sumAcc[1] += y;
		sumAcc[2] += z;
		sumAccSquare[0] += x * x;
		sumAccSquare[1] += y * y;
		sumAccSquare[2] += z * z;
		if (curTime - lastUpdate >= Constants.ACC_SAMPLE_WINDOW * 1000) {
			double km = 0;
			km = Math.pow(sumAcc[0], 2) + Math.pow(sumAcc[1], 2)
					+ Math.pow(sumAcc[2], 2);
			km /= (sampleCounter);
			km = (sumAccSquare[0] + sumAccSquare[1] + sumAccSquare[2]) - km;
			km /= (sampleCounter - 1);
			km = Math.sqrt(km);
			currentKm = km;
			callBackKm(km);

			SampleValues v = new SampleValues(
					Constants.DEVICE_TYPE_ACCELEROMETER);
			v.setAcc(km);
			WandaFile f = new WandaFile(this, v);
			f.autoWrite();
			// f.upload();

			sumAcc[0] = 0;
			sumAcc[1] = 0;
			sumAcc[2] = 0;
			sumAccSquare[0] = 0;
			sumAccSquare[1] = 0;
			sumAccSquare[2] = 0;
			sampleCounter = 0;
			lastUpdate = curTime;
			// closeSampleFile();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		myManager.unregisterListener(mySensorListener);
	}

	private final SensorEventListener mySensorListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				processData(event.values[0], event.values[1], event.values[2]);
			}
		}
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
	
	private class UploadTimer {
	    private final Timer timer = new Timer();
	    private final int seconds;

	    public UploadTimer(int minutes) {
	        this.seconds = minutes;
	    }

	    public void start() {
	        timer.scheduleAtFixedRate(new TimerTask() {
	            public void run() {
	                WandaFile f = new WandaFile();
	                f.uploadAll();
	                //Log.v("JAY", "Trying to Upload!");
	                //timer.cancel();
	            }	           
	        }, 1000, seconds * 1000);
	    }
	    /*public static void main(String[] args) {
	        EggTimer eggTimer = new EggTimer(2);
	        eggTimer.start();*/
	 }

}