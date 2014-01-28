package com.galix.linguam.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.galix.linguam.pojo.TranslatedWord;
import com.galix.linguam.util.WordReferenceUtil;
import com.galix.linguam.util.WordReferenceUtil.Term;

public class Translate_act extends ListActivity {

	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	
	private static OriginalWordDBAdapter originalWordDB;
	private static TranslationDBAdapter translatedWordDB;
	
	private String url_base = "http://api.wordreference.com/0.8/6cd19/json";
	private String languageSource = "en";
	private String languageTo = "es";
	private WordReferenceUtil wrUtil;
	private HashMap<String, List<Term>> hashmapResponse;
	
	private ImageButton search_button;
	private ListView listview;
	ArrayList<Term> translateList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Get the message from the intent
		Intent intent = getIntent();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate_main_layout);
		
		wrUtil = new WordReferenceUtil();
		originalWordDB = new OriginalWordDBAdapter(LinguamApplication.getContext());
		translatedWordDB = new TranslationDBAdapter(LinguamApplication.getContext());
		LinguamApplication.init();

		// List & listview
		translateList = new ArrayList<Term>();
		listview = (ListView) findViewById(android.R.id.list);

		search_button = (ImageButton) findViewById(R.id.translate);
		search_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				EditText translate_caption = (EditText) (findViewById(R.id.search));
				String word_to_translate = translate_caption.getText()
						.toString();
				if (!word_to_translate.equals("")) {
					translate(word_to_translate);
				}
			}
		}); 
		
	
	}
	
	public JsonObjectRequest callWR(String word) {
		
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

								showResults(hashmapResponse.get("firstTranslation"),
										hashmapResponse.get("originalTerm").get(hashmapResponse.get("originalTerm").size()-1));
								//Term a = hashmapResponse.get("originalTerm").get(hashmapResponse.get("originalTerm").size()-1);
								/*
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
									toast.show();
								}*/

							} catch (Exception e) {
								// TODO Auto-generated catch block
								Log.v("Exception", e.toString());
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.v("onErrorResponse - Volley", error.toString());
						}
					});

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsObjRequest;
	}

	public void translate(String word) {

		RequestQueue queue = Volley.newRequestQueue(this);
		TranslatedWord is_translated = translatedWordDB.getTranslateByWord(word);
		
		if (is_translated == null) { //If word dosen't exist, search & save
			queue.add(this.callWR(word));
			//showResults(hashmapResponse.get("firstTranslation"));
		}else{ // If word exist, just show it
			Toast toast = Toast.makeText(LinguamApplication.getContext(), "Your translated word is:" + is_translated.getTerm(), Toast.LENGTH_LONG);			
			toast.show();
		}
		

	}
	
	private void showResults(final List<Term> translateList, final Term originalWord) {

		List<String> translationTerm = new ArrayList<String>(); 	
		
		for (Term term : translateList) {
			//translationTerm.add(createTerms("term", term.getTerm()));
		    translationTerm.add(term.getTerm());
		}
		
		// This is a simple adapter that accepts as parameter
		// Context
		// Data list
		// The row layout that is used during the row creation
		// The keys used to retrieve the data
		// The View id used to show the data. The key number and the view id must match
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.translate_row,R.id.tvTerm, translationTerm);
		
		listview.setAdapter(adapter);
		this.getListView().setSelector(R.drawable.translate_row_selector);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
			      String selectedFromList = (translateList.get(position).getTerm());
			      Toast toast = Toast.makeText(LinguamApplication.getContext(), "The item selected is: " + selectedFromList, Toast.LENGTH_SHORT);	
			      toast.show(); 
			      saveOriginalWord(originalWord);
			      saveTranslateWord(translateList.get(position),originalWord);
			      Toast toast2 = Toast.makeText(LinguamApplication.getContext(), "Orginal: " + originalWord.getTerm() + " AND Trans: " + translateList.get(position) + " saved", Toast.LENGTH_SHORT);	
			      toast2.show();
			}});
		
		/*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {

				final Term item = (Term) parent.getItemAtPosition(position);
				saveOriginalWord(hashmapResponse.get("originalTerm").get(
						hashmapResponse.get("originalTerm").size() - 1));
				saveTranslateWord(item, hashmapResponse.get("originalTerm")
						.get(hashmapResponse.get("originalTerm").size() - 1));

			}

		});*/

	}
	
	private HashMap<String, String> createTerms(String key, String name) {
		    HashMap<String, String> term = new HashMap<String, String>();
		    term.put(key, name);
		    return term;
		}

	
	private class StableArrayAdapter extends ArrayAdapter<Term> {

		List<Term> mIdMap = new ArrayList<Term>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<Term> translateList) {
			super(LinguamApplication.getContext(), textViewResourceId,
					translateList);
			for (int i = 0; i < translateList.size(); ++i) {
				mIdMap.add(translateList.get(i));
			}
		}

	}

	private void saveOriginalWord(Term originalWord) {
		originalWordDB.createOriginalWord(originalWord);
	}

	private void saveTranslateWord(Term translateWord, Term originalWord) {
		translatedWordDB.createTranslation(translateWord, true,
				originalWord.getTerm());
	}
}