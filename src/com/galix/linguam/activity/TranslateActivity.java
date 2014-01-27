package com.galix.linguam.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.db.OriginalWordDBAdapter;
import com.galix.linguam.db.TranslationDBAdapter;
import com.galix.linguam.pojo.OriginalWord;
import com.galix.linguam.util.WordReferenceUtil;
import com.galix.linguam.util.WordReferenceUtil.Term;

public class TranslateActivity extends ListActivity {

	private ImageButton search_button;
	private ListView listview;
	ArrayList<Term> translateList;

	private static OriginalWordDBAdapter originalWordDB;
	private static TranslationDBAdapter translatedWordDB;

	private String url_base = "http://api.wordreference.com/0.8/6cd19/json";
	private String languageSource = "en";
	private String languageTo = "es";
	private WordReferenceUtil wrUtil;
	private static HashMap<String, List<Term>> hashmapResponse;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate_main_layout);

		originalWordDB = new OriginalWordDBAdapter(
				LinguamApplication.getContext());
		translatedWordDB = new TranslationDBAdapter(
				LinguamApplication.getContext());

		// List & listview
		translateList = new ArrayList<Term>();
		listview = (ListView) findViewById(android.R.id.list);

		// Get the message from the intent
		Intent intent = getIntent();

		search_button = (ImageButton) findViewById(R.id.translate);
		final RequestQueue queue = Volley.newRequestQueue(this);
		search_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				EditText translate_caption = (EditText) (findViewById(R.id.search));
				String word_to_translate = translate_caption.getText()
						.toString();
				if (!word_to_translate.equals("")) {
					// hashmapResponse = RequestWR.callWR(word_to_translate);
					//queue.add(RequestWR.callWR(word_to_translate));
					queue.add(callWR(word_to_translate));
					translate_caption.setText("");
				}
			}
		}); 
	}

	private void saveOriginalWord(Term originalWord) {
		originalWordDB.createOriginalWord(originalWord);
	}

	private void saveTranslateWord(Term translateWord, Term originalWord) {
		translatedWordDB.createTranslation(translateWord, true,
				originalWord.getTerm());
	}

	private void showResults(final HashMap<String, List<Term>> hashmapResponse) {

		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.id.list, translateList);

		listview.setAdapter(adapter);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {

				final Term item = (Term) parent.getItemAtPosition(position);
				saveOriginalWord(hashmapResponse.get("originalTerm").get(
						hashmapResponse.get("originalTerm").size() - 1));
				saveTranslateWord(item, hashmapResponse.get("originalTerm")
						.get(hashmapResponse.get("originalTerm").size() - 1));

			}

		});

	}
	
	private JsonObjectRequest callWR(String word) {
		
		JsonObjectRequest jsObjRequest = null;

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
							
							Toast toast = null;
							
							try {
								hashmapResponse = wrUtil
										.parseJSON(response.toString());
								//Get both response WR lists from hashmap
								List<Term> firstTranslation = hashmapResponse.get("firstTranslation");
								List<Term> originalTerm = hashmapResponse.get("originalTerm");
								
								int firstTranslationSize = firstTranslation.size();
								//int originalTermSize = originalTerm.size();
								
								OriginalWord originalword = originalWordDB.createOriginalWord(originalTerm.get(originalTerm.size()-1));
								
								if (originalword != null){
									//Insert to DB
									for (Term translation : firstTranslation) {
										 if (--firstTranslationSize == 0) {
											 translatedWordDB.createTranslation(translation,true,originalword.getTerm());
										 }else{
											 translatedWordDB.createTranslation(translation,false,originalword.getTerm());
										 }
									}
								
								String translated_word = hashmapResponse.get("firstTranslation").get(firstTranslation.size()-1).getTerm().toString();
								    toast = Toast.makeText(LinguamApplication.getContext(), "Your translated word is: " + translated_word, Toast.LENGTH_LONG);
								}else{
									toast = Toast.makeText(LinguamApplication.getContext(), "This word is already in your translated collection words", Toast.LENGTH_LONG);
								}
								
								toast.show();
								
								
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

	private class StableArrayAdapter extends ArrayAdapter<Term> {

		HashMap<Term, Integer> mIdMap = new HashMap<Term, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				ArrayList<Term> translateList) {
			super(LinguamApplication.getContext(), textViewResourceId,
					translateList);
			for (int i = 0; i < translateList.size(); ++i) {
				mIdMap.put(translateList.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			Term item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}
}