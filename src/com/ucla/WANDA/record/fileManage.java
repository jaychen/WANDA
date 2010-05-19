package com.ucla.WANDA.record;

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
import android.os.Environment;
import android.util.Log;

public class fileManage {
	
	File root = Environment.getExternalStorageDirectory();
	
	public void fileUpload() {
    
		HttpURLConnection conn = null; 
		DataOutputStream dos = null;
	    int bytesRead, bytesAvailable, bufferSize;    
		byte [] buffer;
		int maxBufferSize = 1024;
		//String temp = file.getName();
		 
		String urlString = "http://wanda.webege.com/upload.php";
		String existingFileName = "/sdcard/WANDA/questionnaire.txt";
		 
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "dkjsei40f9844-------djs8dviw--4-s-df-";
		String Tag="3rd";
		    
		try {
		    FileInputStream fis = new FileInputStream(new File(existingFileName));
	
		    if(fis != null) {
		    		Log.e("what???", fis.toString());
		    		Log.e("FIS?", "Existed");
		    }
		    	
		    URL url = new URL(urlString);
		    	
		    conn = (HttpURLConnection) url.openConnection();
		    	
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
		    conn.setUseCaches(false);
		    //conn.connect();
		    	
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Connection","Keep-Alive"); 
		    conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary); 
		    	
	         
		    if(conn != null) {
	         	Log.e("connection status?", "Succeed");
	        }
	          
	        dos = new DataOutputStream( conn.getOutputStream() ); 
	
	        dos.writeBytes(twoHyphens + boundary + lineEnd); 
	        dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" 
	          + existingFileName +"\"" + lineEnd); 
	        dos.writeBytes(lineEnd); 
		    	
		    	
	        Log.e(Tag, "Headers are written");
	        // create a buffer of maximum size
	         
		    bytesAvailable = fis.available();
		    bufferSize = Math.min(bytesAvailable, maxBufferSize);
		    buffer = new byte[bufferSize];
		    	
		    	
		    // read file and write it into form
		    	
		    bytesRead = fis.read(buffer, 0, bufferSize);
		    	
		    while( bytesRead > 0 ) {
		    	// Upload file part(s)
		    
		    	dos.write(buffer, 0, bufferSize); 
		        bytesAvailable = fis.available(); 
		        bufferSize = Math.min(bytesAvailable, maxBufferSize); 
		        bytesRead = fis.read(buffer, 0, bufferSize); 
		    }
		    	
		    // send multipart form data necessary after file data...
		    	
		    dos.writeBytes(lineEnd); 
		    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		        
		    // close streams
		    Log.e(Tag,"File is written");
		    fis.close();
		    dos.flush();
		        
		    //////////////////////////////////////////////////////////////////////////////////
		    //retrieve the response from server for checking if the server gets the file well
		    InputStream is = conn.getInputStream();
		    if(is != null) {
		     	Log.e("inputstream", "is ok");
		    }
		    int ch;
		        
		    StringBuffer b = new StringBuffer();
		    while( (ch = is.read()) != -1) {
		      	b.append((char) ch);
		    }
		        
		    String s = b.toString();
		    Log.i("Response", s);
		        
		    dos.close();
				
		    }
		    catch (MalformedURLException ex)
		    {
		    	Log.e(Tag, "error: " + ex.getMessage(), ex);
		    }
	
		    catch(IOException io) {
		    	Log.e(Tag, "error: " + io.getMessage(), io);
		    }
	}
	
	public void fileSaved(String filename) {
		
		try {
			Log.v("AAA", filename);
			File gpxfile = new File("/sdcard/WANDA/", "questionnaire.txt");
	        FileWriter gpxwriter;
	        gpxwriter = new FileWriter(gpxfile);
	        BufferedWriter out = new BufferedWriter(gpxwriter);
	        
	        // TODO :
	        out.write(filename);
	    	out.close();
		}
		catch (IOException ie) {
			ie.printStackTrace();
		}

	}
}
