package com.ucla.WANDA;

public final class Constants {
	// Device Number
	public static int DEVICE_TYPE_BP = 0;
	public static int DEVICE_TYPE_SCALE = 1;
	public static int DEVICE_TYPE_ACCELEROMETER = 2;
	public static int DEVICE_TYPE_MGH = 3;
	public static String SENSOR_SERVICE = "com.ucla.WANDA.services.SENSOR_SERVICE";
	
	// SharedPreference
	public static String PREFS_NAME = "WANDAPREF";
	public static String PREFS_ACC_REC = "PREFS_ACC_REC";
	
	
	// File Name
	public static String ACTIVITY_FILE = "activity.csv";
	public static String SCALE_FILE = "scale.csv";
	public static String BP_FILE = "bp.csv";

	// Bluetooth Devices Address
	public static String SCALE_MAC = "00:A0:96:26:D0:CD";
	public static String BP_MAC = "00:A0:96:2C:C7:87";
	public static String MGH_MAC = "00:13:7B:50:AA:90";
	public static String AP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	public static String SCALE_SERVICE_NAME = "Bluetooth Serial Port";
	public static String MGH_SERVICE_NAME = "Bluetooth Serial Port";
	public static String BP_SERVICE_NAME = "PWAccessP";
	
	// File Path
	public static String UPLOAD_ADDRESS = "http://wanda.webege.com/upload.php";
	public static String TEMP_FOLDER_PATH = "/sdcard/WANDA/TEMP/";
	public static String AUDIO_FOLDER_PATH = "/WANDA/VOICE";
	
	// Accelerometer Sampling rate
	public static int ACC_SAMPLE_WINDOW = 60;	// Sampling Period. Seconds
	public static int UPLOAD_PERIOD = 300;		// Seconds
	
	// Rules
	public static int LOW_SYSTOLIC=90;
	public static int HIGH_SYSTOLIC=120;
	public static int LOW_DIASTOLIC=60;
	public static int HIGH_DIASTOLIC=80;
	
}
