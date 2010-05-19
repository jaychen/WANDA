package com.ucla.WANDA.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.ucla.WANDA.R;
import com.ucla.WANDA.R.id;
import com.ucla.WANDA.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebView;
import android.widget.TextView;


public class weighthistory extends Activity {

	 //private String file="/sdcard/WANDA/weight.txt";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.weighthistory);
	    
	    
	    File rfile = new File("/sdcard/WANDA/weight.txt");
	    FileReader reader;
		try {
			reader = new FileReader(rfile);
		
			BufferedReader br = new BufferedReader(reader);
				
			String temp = "";
			String wholeData = "";
				
			while((temp = br.readLine())!= null) {
				wholeData += temp + "\n";
			}
			
			
			String[] splitter = wholeData.split("/");
			
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    TextView tv = (TextView) findViewById(R.id.titlehistory);
	    tv.setText("Body Weight Variation");
	    
		String img = "http://chart.apis.google.com/chart?cht=bvg&chs=250x150&chd=s:Monkeys&chxt=x,y&chxs=0,ff0000,12,0,lt|1,0000ff,10,1,lt";
		WebView mCharView = (WebView) findViewById(R.id.showgraph);
			
		mCharView.loadUrl(img);
	
	    // TODO Auto-generated method stu
	}
}
