/**
 * Copyright (C) 2009 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ucla.WANDA.history;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import com.ucla.WANDA.Constants;
import com.ucla.WANDA.ReadFileData;
import com.ucla.WANDA.chart.IChart;
import com.ucla.WANDA.ReadFileData.QueryObject;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class chartWeight extends ListActivity {

  private String[] mMenuText;
  private String[] mMenuSummary;
  private Intent intent 			= null;
  private final int WEEK			= 0;
  private final int MONTH			= 1;
  private QueryObject object		= null;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // I know, I know, this should go into strings.xml and accessed using
    // getString(R.string....)
    mMenuText = new String[] { "Week", "Month" };
    mMenuSummary = new String[] { "See the graph of one week", "See the graph of one month" };
    setListAdapter(new SimpleAdapter(this, getListValues(), android.R.layout.simple_list_item_2,
        new String[] { IChart.NAME, IChart.DESC }, new int[] { android.R.id.text1, android.R.id.text2 }));
  }

  private List<Map<String, String>> getListValues() {
    List<Map<String, String>> values = new ArrayList<Map<String, String>>();
    int length = mMenuText.length;
    for (int i = 0; i < length; i++) {
      Map<String, String> v = new HashMap<String, String>();
      v.put(IChart.NAME, mMenuText[i]);
      v.put(IChart.DESC, mMenuSummary[i]);
      values.add(v);
    }
    return values;
  }

  private XYMultipleSeriesDataset getDemoDataset(int choice) {
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    XYSeries series = new XYSeries("Weight");
    
    ReadFileData readfile = new ReadFileData(Constants.SCALE_FILE);
    
    /*
     *  See the graph in week or in month?
     */
    switch(choice) {
    case 0 : // Week 
    	 object = readfile.getWeek(ReadFileData.WEIGHT);
    	 if (object != null)
    	 {
        	ArrayList<Date> time = object.getTime();
        	ArrayList<Float> value = object.getValue();
        	
    	    for(int i = 0; i< time.size(); i++) {
    	    	series.add(i, (double)(value.get(i)));
    	 	    Log.v("TAG","" + time.get(i) + ", " + value.get(i) );
    	 	}
    	    dataset.addSeries(series);
    	 }	
    	 break;
    case 1 : // Month
    	object = readfile.getMonth(ReadFileData.WEIGHT);
    	if (object != null)
   	 	{
	       	ArrayList<Date> time = object.getTime();
	       	ArrayList<Float> value = object.getValue();
	       	
	   	    for(int i = 0; i< time.size(); i++) {
	   	    	series.add(i, (double)(value.get(i)));
    	 	    Log.v("TAG","" + time.get(i) + ", " + value.get(i) );
	   	    }
   	    dataset.addSeries(series);
   	 	}	
    	break;
    }
    return dataset;
  }

  private XYMultipleSeriesRenderer getDemoRenderer() {
    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
    XYSeriesRenderer r = new XYSeriesRenderer();
    r.setColor(Color.YELLOW);
    r.setPointStyle(PointStyle.CIRCLE);
    //r.setFillBelowLine(true);
    //r.setFillBelowLineColor(Color.WHITE);
    r.setFillPoints(true);
    renderer.addSeriesRenderer(r);
    return renderer;
  }

  private void setChartSettings(XYMultipleSeriesRenderer renderer, int choice) {
	  switch(choice) {
	  case 0 :
		  renderer.setChartTitle("WEIGHT - WEEK");
		  break;
	  case 1 :
		  renderer.setChartTitle("WEIGHT - MONTH");
		  break;
	  }
	  
    renderer.setYTitle("Weight");
    renderer.setYAxisMin(50);
    renderer.setYAxisMax(120);
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    
    XYMultipleSeriesRenderer renderer = getDemoRenderer();
    
    switch (position) {
    case 0:
  	  intent = ChartFactory.getLineChartIntent(chartWeight.this, getDemoDataset(WEEK), renderer);
  	  setChartSettings(renderer, WEEK);
  
      break;
    case 1:
  	  intent = ChartFactory.getLineChartIntent(chartWeight.this, getDemoDataset(MONTH), renderer);
  	  setChartSettings(renderer, MONTH);

      break;
    }
    
    startActivity(intent);
  }
}