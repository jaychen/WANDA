package com.ucla.WANDA;

import android.util.Log;


public class SampleValues {
	private int deviceType;
	// BP
	private int systolic;
	private int diastolic;
	private int heartRate;
	private int mean;

	// Weight Scale
	private float weight;
	private float bodyFat;
	private float bodyWater;
	private float muscleMass;
	private int metabolicAge;
	private float boneMass;
	private float viceralRating;

	// Accelerometer
	private double acc;

	public SampleValues() {
	}

	public SampleValues(int type) {
		if (type == Constants.DEVICE_TYPE_BP) {
			deviceType = Constants.DEVICE_TYPE_BP;
			systolic = 0;
			diastolic = 0;
			heartRate = 0;
			mean = 0;
		} else if (type == Constants.DEVICE_TYPE_SCALE) {
			deviceType = Constants.DEVICE_TYPE_SCALE;
			weight = 0;
			bodyFat = 0;
			bodyWater = 0;
			muscleMass = 0;
			metabolicAge = 0;
			boneMass = 0;
			viceralRating = 0;
		} else if (type == Constants.DEVICE_TYPE_ACCELEROMETER) {
			deviceType = Constants.DEVICE_TYPE_ACCELEROMETER;
			acc = 0;
		}
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public int getSystolic() {
		return systolic;
	}

	public void setSystolic(int systolic) {
		this.systolic = systolic;
	}

	public int getDiastolic() {
		return diastolic;
	}

	public void setDiastolic(int diastolic) {
		this.diastolic = diastolic;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public int getMean() {
		return mean;
	}

	public void setMean(int mean) {
		this.mean = mean;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getBodyFat() {
		return bodyFat;
	}

	public void setBodyFat(float bodyFat) {
		this.bodyFat = bodyFat;
	}

	public float getBodyWater() {
		return bodyWater;
	}

	public void setBodyWater(float bodyWater) {
		this.bodyWater = bodyWater;
	}

	public float getMuscleMass() {
		return muscleMass;
	}

	public void setMuscleMass(float muscleMass) {
		this.muscleMass = muscleMass;
	}

	public int getMetabolicAge() {
		return metabolicAge;
	}

	public void setMetabolicAge(int metabolicAge) {
		this.metabolicAge = metabolicAge;
	}

	public float getBoneMass() {
		return boneMass;
	}

	public void setBoneMass(float boneMass) {
		this.boneMass = boneMass;
	}

	public float getViceralRating() {
		return viceralRating;
	}

	public void setViceralRating(float viceralRating) {
		this.viceralRating = viceralRating;
	}

	public double getAcc() {
		return acc;
	}

	public void setAcc(double acc) {
		this.acc = acc;
	}

	public String validateData() {
		String results = "";
		int sysLevel = 0; // 0 is Low, 1 is normal, 2-4 is high
		int diaLevel = 0; // 0 is Low, 1 is normal, 2-4 is high
		StatAnalysis s = new StatAnalysis();
		// Basic Range Test
		if (deviceType == Constants.DEVICE_TYPE_BP) {
			if (this.systolic > Constants.HIGH_SYSTOLIC + 40)
				sysLevel = 4;
			else if (this.systolic > Constants.HIGH_SYSTOLIC + 20)
				sysLevel = 3;
			else if (this.systolic > Constants.HIGH_SYSTOLIC)
				sysLevel = 2;
			else if (this.systolic >= Constants.LOW_SYSTOLIC)
				sysLevel = 1;
			else
				sysLevel = 0;

			if (this.diastolic > Constants.HIGH_DIASTOLIC + 40)
				diaLevel = 4;
			else if (this.diastolic > Constants.HIGH_DIASTOLIC + 20)
				diaLevel = 3;
			else if (this.diastolic > Constants.HIGH_DIASTOLIC)
				diaLevel = 2;
			else if (this.diastolic >= Constants.LOW_DIASTOLIC)
				diaLevel = 1;
			else
				diaLevel = 0;

			if (sysLevel == 1 && diaLevel == 1) {
				// Normal Range
				results += "Your Blood Pressure is in Perfect Range\n";
			} else if (sysLevel == 0 && diaLevel == 0) {
				// Low BP Pressure
				results += "Your Blood Pressure is Too LOW!\n";
			} else if (sysLevel > 3 && diaLevel > 3) {
				// Danger BP Pressure
				results += "Your Blood Pressure is Too HIGH!\n";
				results += "Pleae Consult a doctor immediately!\n";
			} else if (sysLevel >= 2 && diaLevel >= 2) {
				// High BP Pressure
				results += "Your Blood Pressure is a little bit High!\n";
				results += "I recoomend you to take some rest!\n";
			} else {
				results += "You are in good shape!";
			}
			
			// Regression Test			
			float slp = s.regressionTest(7, Constants.BP_FILE,
					ReadFileData.SYSTOTIC);
			if (slp > 2)
				results += "Your blood pressure has been increased within the week";
			else if (slp < -2)
				results += "Your blood pressure has been decreased within the week";

		} else if (deviceType == Constants.DEVICE_TYPE_SCALE) {
			
			// Regression Test
			float slp = s.regressionTest(7, Constants.SCALE_FILE,
					ReadFileData.WEIGHT);
			
			if(Math.abs(weight-s.getMean(7, Constants.SCALE_FILE,
					ReadFileData.WEIGHT))>5){
				results += "Wrong User";
				return results;
			}
			
			if (slp > 1)
				results += "Your Weight has been increased within the week";
			else if (slp < -1)
				results += "Your Weight has been decreased within the week";
			else
				results += "Your Weight is in normal range";

		} else if (deviceType == Constants.DEVICE_TYPE_ACCELEROMETER) {
			float slp = s.regressionTest(7,
					Constants.ACTIVITY_FILE, ReadFileData.ACTIVITY);
			if (slp > 2)
				results += "Your Activity Level has been increased within the week";
			else if (slp < -2)
				results += "Your Activity Level has been decreased within the week";
		}
		return results;
	}
	
	public float BMIPrime(float w){
		float height = 1.75f;
		float BMI=0;
		if(height >0)
			BMI = (float)(w/Math.pow(height, 2));
		else{
			Log.e("JAY", "Zero Height in BMI");
			return -1;
		}
		BMI=BMI/25;
		
		return BMI;			
	}
}
