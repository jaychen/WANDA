package com.ucla.WANDA;

import java.util.ArrayList;

import android.util.Log;

import com.ucla.WANDA.ReadFileData.QueryObject;

public class StatAnalysis {

	public StatAnalysis() {
		
	}
	/**	  
	 * @param period
	 *            Number of days used to calculate the linear Regression
	 * @param device
	 * @param field
	 *            The parameter of the device
	 * @return Expectation Value. 
	 * Return -1 if error occurs 
	 */
	public float getMean(int period, String fName, String field){
		ReadFileData r = new ReadFileData(fName);
		QueryObject o;
		if (period <= 7)
			o = r.getWeek(field);
		else if (period <= 31)
			o = r.getMonth(field);
		else
			o = r.getYear(field);
		
		float mean = 0;
		if(o!=null){
			ArrayList<Float> aValue = o.getValue();

			for (int i = 0; i < aValue.size(); i++) {				
				mean += aValue.get(i);				
			}
			mean/=aValue.size();
		}
		else{
			Log.e("JAY", "Empty Query in getMean()");
			return -1;
		}		
		return mean;
	}
	/**
	 * 
	 * @param period
	 * @param fName
	 * @param field
	 * @return Variance. -1 if any error occurs
	 */
	public float getVariance(int period, String fName, String field){
		ReadFileData r = new ReadFileData(fName);
		QueryObject o;
		float var=0;
		if (period <= 7)
			o = r.getWeek(field);
		else if (period <= 31)
			o = r.getMonth(field);
		else
			o = r.getYear(field);
		float mean=getMean(period, fName, field);
		if(o!=null){
			ArrayList<Float> aValue = o.getValue();
			for (int i = 0; i < aValue.size(); i++) {				
				var += Math.pow(aValue.get(i)-mean,2);				
			}
			var/=aValue.size();
		}
		else{
			Log.e("JAY", "Empty Query in getVariance()");
			return -1;
		}
		return var;		
	}
	
	/**
	 * 
	 * @param period
	 *            Number of days used to calculate the linear Regression
	 * @param device
	 * @param field
	 *            The parameter of the device
	 * @return The slope of the linear regression line. -999 if any error occurs
	 */
	public float regressionTest(int period, String fName, String field) {
		float ySum = 0;
		float xSum = 0;
		float xySum = 0;
		float slope = 0;
		// Declare a object with the file name as the input parameter. It can be
		// Constants.BP_FILE, Constants.SCALE_FILE, or Constants.ACTIVITY_FILE
		ReadFileData r = new ReadFileData(fName);

		QueryObject o;
		if (period <= 7)
			o = r.getWeek(field);
		else if (period <= 31)
			o = r.getMonth(field);
		else
			o = r.getYear(field);

		if (o != null) {
			ArrayList<Integer> index = new ArrayList<Integer>();
			ArrayList<Float> aValue = o.getValue();

			for (int i = 0; i < aValue.size(); i++) {
				// Log.v("TAG", "" + aTime.get(i) + ", " + aValue.get(i));
				index.add(i);
				ySum += aValue.get(i);
				xSum += i;
				xySum += aValue.get(i) * i;
			}
			float meanX = xSum / aValue.size();
			float sdSum = 0;
			for (int i = 0; i < aValue.size(); i++) {
				sdSum += Math.pow(index.get(i) - meanX, 2);
			}
			slope = xySum - ySum * xSum / aValue.size();
			slope = slope / sdSum;
			// Log.v("JAY", "Slope: " + slope);
		} else {
			Log.e("JAY", "Wrong Regression Field!");
			return -9999;
		}
		return slope;
	}

}
