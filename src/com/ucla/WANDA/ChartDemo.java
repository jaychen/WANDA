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
package com.ucla.WANDA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ucla.WANDA.chart.AverageTemperatureChart;
import com.ucla.WANDA.chart.BudgetDoughnutChart;
import com.ucla.WANDA.chart.BudgetPieChart;
import com.ucla.WANDA.chart.IChart;
import com.ucla.WANDA.chart.ProjectStatusChart;
import com.ucla.WANDA.chart.SalesBarChart;
import com.ucla.WANDA.chart.SalesComparisonChart;
import com.ucla.WANDA.chart.SalesGrowthChart;
import com.ucla.WANDA.chart.SalesStackedBarChart;
import com.ucla.WANDA.chart.ScatterChart;
import com.ucla.WANDA.chart.TrigonometricFunctionsChart;
import com.ucla.WANDA.history.chartWeight;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ChartDemo extends ListActivity {
  private IChart[] mCharts = new IChart[] {new AverageTemperatureChart(), new SalesStackedBarChart(),
		  new SalesBarChart(), new TrigonometricFunctionsChart(), new ScatterChart(), new SalesComparisonChart(),
          new ProjectStatusChart(), new SalesGrowthChart(), new BudgetPieChart(), new BudgetDoughnutChart()};
	
  private String[] mMenuText;

  private String[] mMenuSummary;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int length = mCharts.length;
    mMenuText = new String[length + 1];
    mMenuSummary = new String[length + 1];
    for (int i = 0; i < length; i++) {
      mMenuText[i] = mCharts[i].getName();
      mMenuSummary[i] = mCharts[i].getDesc();
    }
    mMenuText[length] = "Random values charts";
    mMenuSummary[length] = "Chart demos using randomly generated values";
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

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    if (position < mCharts.length) {
      Intent intent = mCharts[position].execute(this);
      startActivity(intent);
    } else {
      Intent intent = new Intent(this, chartWeight.class);
      startActivity(intent);
    }
  }
}