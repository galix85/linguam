package com.galix.linguam.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.db.OriginalWordDBAdapter;
import com.galix.linguam.db.TranslationDBAdapter;
import com.galix.linguam.util.WordReferenceUtil;
import com.galix.linguam.util.WordReferenceUtil.Term;

public class Home extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	private String translatedWord;
	private static OriginalWordDBAdapter originalWordDB;
	private static TranslationDBAdapter translatedWordDB;
	private String url_base = "http://api.wordreference.com/0.8/6cd19/json";
	private String languageSource = "en";
	private String languageTo = "es";
	private WordReferenceUtil wrUtil;
	private HashMap<String, List<Term>> hashmapResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		wrUtil = new WordReferenceUtil();
		originalWordDB = new OriginalWordDBAdapter(
				LinguamApplication.appContext);
		translatedWordDB = new TranslationDBAdapter(
				LinguamApplication.appContext);
		LinguamApplication.init();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
	}

	public JsonObjectRequest callWR(String word) {
		
		JsonObjectRequest jsObjRequest = null;
		translatedWord = null;

		try {
			// Encode word to translate
			String encoded_word = URLEncoder.encode(word, "UTF-8");
			// Call URL WR to translate
			String url = url_base + "/" + languageSource + languageTo + "/"
					+ encoded_word;

			jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {

							try {
								translatedWord = wrUtil
										.parseJSON(response.toString());
								TextView result_translate = (TextView) findViewById(R.id.result_translate);
								result_translate.setText(translatedWord
										.toString());
								result_translate.setVisibility(1);
								// findViewById(R.id.progressBar1).setVisibility(View.GONE);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Log.v("Exception", e.toString());
								TextView result_translate = (TextView) findViewById(R.id.result_translate);
								result_translate.setText("No results");
								result_translate.setVisibility(1);
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.v("onErrorResponse - Volley", error.toString());
							TextView result_translate = (TextView) findViewById(R.id.result_translate);
							result_translate
									.setText("There is any network problem - Please check if you have interent access");
							result_translate.setVisibility(1);
						}
					});

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsObjRequest;
	}

	public void translate(View view) {

		RequestQueue queue = Volley.newRequestQueue(this);
		EditText translate_caption = (EditText) (findViewById(R.id.search));
		String word_to_translate = translate_caption.getText().toString();
		queue.add(this.callWR(word_to_translate));

		// Instance of adapter

		// TODO insert parsing hasmap of request
		// translateTableAdapter.createTranslation(new(Term()))();
	}

}
