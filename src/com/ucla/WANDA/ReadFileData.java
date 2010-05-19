package com.ucla.WANDA;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import android.util.Log;

public class ReadFileData {
	public String fileName="";
	public static String SYSTOTIC = "SYSTOTIC";
	public static String DIASTOLIC = "DIASTOLIC";
	public static String HEART_RATE = "HEART_RATE";
	public static String MAP = "MAP";
	public static String WEIGHT = "WEIGHT";
	public static String BODY_FAT = "BODYFAT";
	public static String BODY_WATER = "BODY_WATER";
	public static String MUSCLE_MASS = "BODY_MASS";
	public static String METABOLIC_AGE = "METABOLIC_AGE";
	public static String BONE_MASS = "BONE_MASS";
	public static String VICERAL_RATING = "VICERAL_RATING";
	public static String ACTIVITY = "ACTIVITY";
	
	private Hashtable<String, Integer> Table; 		
	
	public ReadFileData(String fileN) {
		if(fileN == Constants.BP_FILE){
			Table = new Hashtable<String, Integer>(4);
			Table.put(ReadFileData.SYSTOTIC, new Integer(2));
			Table.put(ReadFileData.DIASTOLIC, new Integer(3));
			Table.put(ReadFileData.HEART_RATE, new Integer(4));
			Table.put(ReadFileData.MAP, new Integer(5));
			fileName = fileN;
		}
		else if(fileN == Constants.SCALE_FILE){
			Table = new Hashtable<String, Integer>(7);
			Table.put(ReadFileData.WEIGHT, new Integer(2));
			Table.put(ReadFileData.BODY_FAT, new Integer(3));
			Table.put(ReadFileData.BODY_WATER, new Integer(4));
			Table.put(ReadFileData.MUSCLE_MASS, new Integer(5));
			Table.put(ReadFileData.METABOLIC_AGE, new Integer(6));
			Table.put(ReadFileData.BONE_MASS, new Integer(7));
			Table.put(ReadFileData.VICERAL_RATING, new Integer(8));	
			fileName = fileN;
		}
		else if(fileN == Constants.ACTIVITY_FILE){
			Table = new Hashtable<String, Integer>(1);
			Table.put(ReadFileData.ACTIVITY, new Integer(2));
			fileName = fileN;
		}
		else
			Log.e("JAY", "Unknown File Name");
	}
	/**
	 * 
	 */
	public QueryObject parseFile(String field, Date startTime) {
		String existingFileName = "/sdcard/WANDA/" + fileName;		
		QueryObject o = new QueryObject();
				
		// Return the column index of the field in the file
		Integer key = (Integer)Table.get(field);
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		if(key == null){
			Log.e("JAY", "Wrong Field Name");
			return null;
		}
		
		try {
			FileInputStream fstream = new FileInputStream(new File(
					existingFileName));
			if (fstream == null) {
				Log.e("JAY", "File Do Not Exist");
				return null;
			}
			
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine = "";
			Date dt = new Date();
			
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				String[] tokens = strLine.split("[,]");
				dt = timeFormat.parse(tokens[0]);
				if(dt.after(startTime)){
					o.inserPair(dt, Float.valueOf(tokens[key].trim()).floatValue());
					//Log.v("JAY", dt.toString());
				}
			}
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		} 
		
		// If there is no values in teh specified period, return null
		if(o.getTime().size() < 1){
			Log.e("JAY", "No Matched Query");
			return null;
		}
		else
			return o;
	}
	
	/**
	 * 
	 * Return Data in One Day 
	 */
	public QueryObject getDay(String field){
		Date now = Calendar.getInstance().getTime();
		long msInPeriod = 24*60*60*1000;
		now.setTime(now.getTime()-msInPeriod);
		
		return parseFile(field, now);		
	}
	
	/**
	 * 
	 * Return Data in One Week 
	 */
	public QueryObject getWeek(String field){
		Date now = Calendar.getInstance().getTime();
		long msInPeriod = (long)7*24*60*60*1000;	// Pain in Ass... The right side need to be cast to Long
		now.setTime(now.getTime()-msInPeriod);
		
		return parseFile(field, now);		
	}
	/**
	 * 
	 * Return Data in One Month 
	 */
	public QueryObject getMonth(String field){
		Date now = Calendar.getInstance().getTime();		
		long msInPeriod = (long)30*24*60*60*1000;	// Pain in Ass... The right side need to be cast to Long
		now.setTime(now.getTime()-msInPeriod);
		
		return parseFile(field, now);	
	}
	
	/**
	 * 
	 * Return Data in One Year 
	 */
	public QueryObject getYear(String field){
		Date now = Calendar.getInstance().getTime();
		long msInPeriod = (long)365*24*60*60*1000;
		now.setTime(now.getTime()-msInPeriod);
		
		return parseFile(field, now);		
	}
	
	
	
	/**	 
	 *	Object used to stored the query values from files
	 */
	public class QueryObject {
		private ArrayList<Date> time;
		private ArrayList<Float> value;
		
		public QueryObject(){
			time = new ArrayList<Date>();
			value = new ArrayList<Float>();			
		}
		
		public void inserPair(Date d, Float f){
			time.add(d);
			value.add(f);
		}
		public ArrayList<Date> getTime(){
			return time;
		}
		public ArrayList<Float> getValue(){
			return value;
		}		
	}
	
	
}
