package com.ucla.WANDA.record;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class AudioRecorder {
	MediaRecorder recorder = new MediaRecorder();
	MediaPlayer mp = new MediaPlayer();
	String path;

	/**
	 * Creates a new audio recording at the given path (relative to root of SD
	 * card).
	 */
	public AudioRecorder(String path) {
		this.path = sanitizePath(path);
	}

	private String sanitizePath(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (!path.contains(".")) {
			path += ".3gp";
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/WANDA/VOICE"	+ path;
	}

	/**
	 * Starts a new recording.
	 */
	public void start() throws IOException {
		String state = android.os.Environment.getExternalStorageState();
		if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
			Log.e("JAY", "SD Card is not mounted.");
			throw new IOException("SD Card is not mounted.  It is " + state
					+ ".");
		}

		// make sure the directory we plan to store the recording in exists
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			Log.e("JAY", "Path to file could not be created.");
			throw new IOException("Path to file could not be created.");
		}
		recorder.reset();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(path);
		recorder.prepare();
		recorder.start();   // Recording is now started
		Log.v("JAY", "Recorder Start()");
	}

	/**
	 * Stops a recording that has been previously started.
	 */
	public void stop() throws IOException {
		recorder.stop();
		//recorder.reset(); // You can reuse the object by going back to
							// setAudioSource() step
		//recorder.release(); // Now the object cannot be reused
	}
	/**
	 * Play the recorded audio
	 */
	public void play(){
		
		try {
			mp.reset();
			mp.setDataSource(path);
			mp.prepare();
		    mp.start();
		} catch (IllegalArgumentException e) {			
			e.printStackTrace();
		} catch (IllegalStateException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}		 
	}
	/**
	 * Stop Playing the recorded audio
	 */
	public void stopPlay(){
		
		mp.stop();
		//mp.release();
	}

}
