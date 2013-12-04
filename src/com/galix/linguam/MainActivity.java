package com.galix.linguam;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galix.linguam.R;
import com.galix.linguam.api.ParseJSONWordReference;
import com.galix.linguam.api.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
	}
	
	public JsonObjectRequest callWR(String word){
		final ParseJSONWordReference parsedWR = new ParseJSONWordReference();
		String url = "http://api.wordreference.com/0.8/6cd19/json/enes/"+ word;

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				String translatedWord = parsedWR.getTranslation(response.toString());
				TextView result_translate = (TextView)findViewById(R.id.result_translate);
				result_translate.setText(translatedWord.toString());
				result_translate.setVisibility(1);
				//findViewById(R.id.progressBar1).setVisibility(View.GONE);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub

			}
		});
		
		return jsObjRequest;
	}
	
	
	public void translate(View view){
		
		RequestQueue queue = Volley.newRequestQueue(this);
		EditText translate_caption = (EditText)(findViewById(R.id.search));
		String word_to_translate = translate_caption.getText().toString();
		
		final Util util = new Util();
		String url = "http://api.wordreference.com/0.8/6cd19/json/enes/"+ word_to_translate;

		new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

			private String translated_word;

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				//translated_word = util.getTranslation(response.toString());
				
		/*		    JsonElement jelement = new JsonParser().parse(response.toString());
				    JsonObject  jobject = jelement.getAsJsonObject();
				    jobject = jobject.getAsJsonObject("data");
				    JsonArray jarray = jobject.getAsJsonArray("translations");
				    jobject = jarray.get(0).getAsJsonObject();
				
				TextView result_translate = (TextView)findViewById(R.id.result_translate);
				result_translate.setText(result_translate.toString());
				/*txtDisplay.setText("Response => " + translated_word);
				findViewById(R.id.progressBar1).setVisibility(View.GONE);*/
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		queue.add(this.callWR(word_to_translate));
		
	}

}
