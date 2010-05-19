package com.ucla.WANDA;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainScreen extends Activity {
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.mainscreen);
		
		setTitle(R.string.title_welcomer);
	
		this.findViewById(R.id.Button01).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub					
		        	MainScreen.this.finish();

			}
			
		});
		
	}

}