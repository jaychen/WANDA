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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import com.ucla.WANDA.chart.IChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class chartActivity extends ListActivity {

  private String[] mMenuText;
  private String[] mMenuSummary;
  private File rfile 				= null;
  private Intent intent 			= null;
  private final int DAY				= 0;
  private final int WEEK			= 1;
  private final int MONTH			= 2;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // I know, I know, this should go into strings.xml and accessed using
    // getString(R.string....)
    mMenuText = new String[] { "Day", "Week", "Month" };
    mMenuSummary = new String[] { "See the graph of today", "See the graph of one week", "See the graph of one month" };
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
    XYSeries series = new XYSeries("Activity");
    
    rfile = new File("/sdcard/WANDA/blood pressure.txt");
    FileReader reader;
	
    
    switch(choice) {
    case 0 :
        for (int k = 0; k < 10; k++) {
            series.add(k, k);
        }
        
        dataset.addSeries(series);
    	break;
    case 1 :
    	
    	try {
    		reader = new FileReader(rfile);
    	
    		BufferedReader br = new BufferedReader(reader);
    			
    		String temp = "";
    					
    		while((temp = br.readLine())!= null) {
    			//Log.i("SSSSSS", temp);
    			String[] splitter = temp.split(",");
    			series.add(Double.parseDouble(splitter[0]), Double.parseDouble(splitter[1]));
    		}
    	
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        
        dataset.addSeries(series);
    	break;
    case 2 :
        for (int k = 0; k < 10; k++) {
            series.add(k, k);
        }
        
        dataset.addSeries(series);
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
		  renderer.setChartTitle("ACTIVITY - TODAY");
		  break;
	  case 1 :
		  renderer.setChartTitle("ACTIVITY - WEEK");
		  break;
	  case 2 :
		  renderer.setChartTitle("ACTIVITY - MONTH");
		  break;
	  }
 
    renderer.setXTitle("time");
    renderer.setYTitle("activity");
    //renderer.setXAxisMin(0);
    //renderer.setXAxisMax(10);
    renderer.setYAxisMin(50);
    renderer.setYAxisMax(70);
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    
    XYMultipleSeriesRenderer renderer = getDemoRenderer();
    
    switch (position) {
    case 0:
	  setChartSettings(renderer, DAY);
	  intent = ChartFactory.getLineChartIntent(chartActivity.this, getDemoDataset(DAY), renderer);
	  
      break;
    case 1:
  	  setChartSettings(renderer, WEEK);
  	  intent = ChartFactory.getLineChartIntent(chartActivity.this, getDemoDataset(WEEK), renderer);
  
      break;
    case 2:
  	  setChartSettings(renderer, MONTH);
  	  intent = ChartFactory.getLineChartIntent(chartActivity.this, getDemoDataset(MONTH), renderer);

      break;
    }
    
    startActivity(intent);
  }
}