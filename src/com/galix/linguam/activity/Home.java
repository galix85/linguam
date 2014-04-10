package com.galix.linguam.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;

public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
	}

	public void to_translate_activity(View view){
		Intent i = new Intent(this, Translate_Activity.class);
		startActivity(i); 
	}
	
	public void to_play_activity(View view){
		Intent i = new Intent(this, Translate_Activity.class);
		startActivity(i); 
	}

}