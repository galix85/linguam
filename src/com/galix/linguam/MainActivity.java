package com.galix.linguam;

import com.galix.linguam.api.Wordreference;
import com.galix.lingus.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void saveWord(View view){
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.editText);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	public void translateButtonAction(View view){
		EditText editText = (EditText) findViewById(R.id.editText);
		String word = editText.getText().toString();
		Wordreference wordrefCon = new Wordreference("");
	    String translatedWord = wordrefCon.callWordReferenceTranslation(word);
		TextView resultTranslate = (TextView) findViewById(R.id.textView);
		resultTranslate.setText(translatedWord);
	}
	
}
