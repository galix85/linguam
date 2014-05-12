package com.galix.linguam.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.pojo.TranslatedWord;

public class Home_Activity extends Activity {

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

		Toast noWords = null;
		
		if (LinguamApplication.translatedWordDB.getAllTranslates().size() >= 10){
			Intent i = new Intent(this, Game_Activity.class);
			startActivity(i);
		}else{
			noWords = Toast.makeText(LinguamApplication.getContext(), R.string.no_words, Toast.LENGTH_LONG);
			noWords.show();
		}
		 
	}

}