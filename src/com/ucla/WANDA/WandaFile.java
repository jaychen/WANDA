package com.ucla.WANDA;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

public class WandaFile {
	private File sdRoot;
	private File tmpRoot;

	private String fileName;
	private SampleValues value = null;
	private Context cont = null;

	public WandaFile() {
	}

	public WandaFile(Context context, String name) {
		cont = context;
		fileName = name;
		createSampleFile(fileName);
	}

	public WandaFile(Context context, SampleValues s) {
		cont = context;
		value = s;
	}

	public void setSampleValues(SampleValues s) {
		value = s;
	}

	public void autoWrite() {
		File tmpFile;
		FileWriter tmpWriter;
		String writeFileName;
		BufferedWriter tmpOut;
		String str;
		SimpleDateFormat timeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String timeStamp = timeFormat.format(Calendar.getInstance().getTime());
		SharedPreferences settings;
		String userID = "001";
		if (cont != null) {
			settings = cont.getSharedPreferences(WANDA.PREFERENCES_USER,
					Activity.MODE_PRIVATE);
			userID = settings.getString("username", "001");
		}

		if (value.getDeviceType() == Constants.DEVICE_TYPE_BP) {
			writeFileName = Constants.BP_FILE;
			str = timeStamp + ", " + userID + ", " + value.getSystolic() + ", ";
			str += value.getDiastolic() + ", " + value.getHeartRate() + ", "
					+ value.getMean() + "\n";
		} else if (value.getDeviceType() == Constants.DEVICE_TYPE_SCALE) {
			writeFileName = Constants.SCALE_FILE;
			str = timeStamp + ", " + userID + ", ";
			str += value.getWeight() + ", " + value.getBodyFat() + ", "
					+ value.getBodyWater() + ", " + value.getMuscleMass();
			str += ", " + value.getMetabolicAge() + ", " + value.getBoneMass()
					+ ", " + value.getViceralRating() + "\n";
		} else if (value.getDeviceType() == Constants.DEVICE_TYPE_ACCELEROMETER) {
			writeFileName = Constants.ACTIVITY_FILE;
			str = timeStamp + ", " + userID + ", " + (int) value.getAcc()
					+ "\n";
		} else
			return;

		// Create Sample File and Write File
		createSampleFile(writeFileName);

		// Write History File
		tmpFile = new File(sdRoot.getPath(), writeFileName);
		try {
			tmpWriter = new FileWriter(tmpFile, true);
			tmpOut = new BufferedWriter(tmpWriter);
			tmpOut.append(str);
			tmpOut.close();
			tmpWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Write Temp File
		tmpFile = new File(tmpRoot.getPath(), writeFileName);
		try {
			tmpWriter = new FileWriter(tmpFile, true);
			tmpOut = new BufferedWriter(tmpWriter);
			tmpOut.append(str);
			tmpOut.close();
			tmpWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int createSampleFile(String name) {
		File smpfile;
		// Environment.getExternalStorageState());
		// sdRoot = Environment.getExternalStorageDirectory();
		// Create a directory. Directory is like a file in the systerm
		sdRoot = new File(Environment.getExternalStorageDirectory(), "WANDA");
		if (!sdRoot.exists()) {
			sdRoot.mkdir();
		}

		tmpRoot = new File(sdRoot.getPath(), "TEMP");
		if (!tmpRoot.exists()) {
			tmpRoot.mkdir();
		}

		smpfile = new File(sdRoot.getPath(), name);
		if (!smpfile.exists() && sdRoot.exists()) {
			try {
				smpfile.createNewFile();
			} catch (IOException e) {
				Log.d("JAY", "File creation failed in "
						+ tmpRoot.getPath().toString() + "\\" + smpfile);
				return -1;
			}
		}
		smpfile = new File(tmpRoot.getPath(), name);
		if (!smpfile.exists() && tmpRoot.exists()) {
			try {
				smpfile.createNewFile();
			} catch (IOException e) {
				Log.d("JAY", "File creation failed in " + tmpRoot.getPath()
						+ "\\" + smpfile);
				return -1;
			}
		}
		// Log.v("JAY", "Have Write Permission: " +
		// Boolean.toString(sdRoot.canWrite()));
		return 0;
	}

	public boolean upload(String fileN) {

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1024;
		boolean success = false;

		// "http://www.lecs.cs.ucla.edu/~zenithhan/upload.php";
		String urlString = Constants.UPLOAD_ADDRESS;
		// String urlString =
		// "http://www.lecs.cs.ucla.edu/~zenithhan/upload.php";
		String existingFileName = Constants.TEMP_FOLDER_PATH + fileN;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "dkjsei40f9844-------djs8dviw--4-s-df-";
		String Tag = "JAY";

		try {

			FileInputStream fis = new FileInputStream(
					new File(existingFileName));

			if (fis == null) {
				Log.e(Tag, "File Do Not Exist");
				return false;
			}

			URL url = new URL(urlString);

			conn = (HttpURLConnection) url.openConnection();

			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			if (conn != null) {
				Log.v(Tag, "Network Connection Established");
			}

			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos
					.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
							+ existingFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size

			bytesAvailable = fis.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form
			bytesRead = fis.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				// Upload file part(s)

				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fis.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fis.read(buffer, 0, bufferSize);
			}

			// send multipart form data necessary after file data...

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			fis.close();
			dos.flush();

			// retrieve the response from server
			InputStream is = conn.getInputStream();
			if (is != null) {
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				String s = b.toString();
				//Log.i(Tag, "Response: " + s);
				if (s.indexOf("UPLOAD_OK") != -1) {
					success = true;
					Log.v(Tag, "Upload Success!");
				} else {
					success = false;
					Log.v(Tag, "Upload Fail!");
				}

			}
			dos.close();

		} catch (MalformedURLException ex) {
			Log.e(Tag, "error: " + ex.getMessage(), ex);
		}

		catch (IOException io) {
			Log.e(Tag, "error: " + io.getMessage(), io);
		}

		if (success)
			deleteFile(fileN);

		return success;
	}

	/*
	 * Overload Method
	 */
	public boolean upload() {
		if (value != null) {
			String name;
			if (value.getDeviceType() == Constants.DEVICE_TYPE_BP)
				name = Constants.BP_FILE;
			else if (value.getDeviceType() == Constants.DEVICE_TYPE_SCALE)
				name = Constants.SCALE_FILE;
			else if (value.getDeviceType() == Constants.DEVICE_TYPE_ACCELEROMETER)
				name = Constants.ACTIVITY_FILE;
			else
				return false;
			boolean tmp = upload(name);
			return tmp;
		} else
			return false;
	}

	/**
	 * Upload all the files inside TEMP
	 */
	public void uploadAll() {
		File folder = new File(Constants.TEMP_FOLDER_PATH);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				//Log.v("JAY", "File " + listOfFiles[i].getName());
				upload(listOfFiles[i].getName());				
			} 
			else if (listOfFiles[i].isDirectory()) {
				/*Do Nothing*/
			}
		}		
	}

	public void deleteFile(String fileN) {
		File smpFile = new File(Constants.TEMP_FOLDER_PATH, fileN);
		Log.v("JAY", "Files Pointer Created!");
		if (smpFile.exists()) {
			smpFile.delete();
			Log.v("JAY", "Files Deleted!");
		} else {
			Log.v("JAY", "Can't Locate File!");
		}
	}
}
