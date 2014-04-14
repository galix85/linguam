package com.galix.linguam.activity;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.RESTinterface.WordReferenceClient;
import com.galix.linguam.adapter.TermAdapter;
import com.galix.linguam.pojo.Item;
import com.galix.linguam.pojo.OriginalWord;
import com.galix.linguam.pojo.Term;
import com.galix.linguam.service.ServiceClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class Translate_Activity extends ListActivity {
	
	private static final String TAG = "Linguam: Translate Activity";
	private WordReferenceClient wordReferenceClient;
			
	private ImageButton ib_searchButton;
	private ListView lv;
	private InputMethodManager imm;
	ArrayList<Term> translateList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Input Method Manager
		this.imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		//Progress Icon
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// Get the message from the intent
		getIntent();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate_main_layout);
		
		// Client object with actions interface of beach.
		wordReferenceClient = ServiceClient.getInstance().getClient(this,
						WordReferenceClient.class);

		// List & listview
		translateList = new ArrayList<Term>();
		lv = (ListView) findViewById(android.R.id.list);

		ib_searchButton = (ImageButton) findViewById(R.id.translate);
		ib_searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				//Make visible progress icon
				setProgressBarIndeterminateVisibility(Boolean.TRUE);
				
				EditText etTranslateCaption = (EditText) (findViewById(R.id.search));
				String word_to_translate;
				try {
					
					word_to_translate = URLEncoder.encode(etTranslateCaption.getText().toString(), "UTF-8");
					if (!word_to_translate.equals("")) {
						//translate(word_to_translate);
						getTranslation(word_to_translate);
					}
				
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					Log.v(TAG, e.getCause().toString());
				}
				
			}
		}); 
		
	
	}

	private void showResults(final List<Term> translateList, final Term originalWord) {

		//Make visible title of search "Possible Translations:"
		TextView tvTitleTerm = (TextView)(findViewById(R.id.tvTitleTerm));
		tvTitleTerm.setText(getString(R.string.translate_possible_translation));
		tvTitleTerm.setVisibility(1);
		
		List<String> translationTerm = new ArrayList<String>(); 	
		for (Term term : translateList) {
		    translationTerm.add(term.getTerm());
		}
		
		// This is a simple adapter that accepts as parameter
		// Context
		// Data list
		// The row layout that is used during the row creation
		// The keys used to retrieve the data
		// The View id used to show the data. The key number and the view id must match
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.translate_row,R.id.tvTerm, translationTerm);
		TermAdapter adapter = new TermAdapter(LinguamApplication.getContext(), translateList);
		lv.setAdapter(adapter);
		
		//
		this.getListView().setSelector(R.drawable.translate_row_selector);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				Toast toast;
				if (saveOriginalWord(originalWord) != null) {
					saveTranslateWord(translateList.get(position),originalWord);
					toast = Toast.makeText(LinguamApplication.getContext(), "TranslationOrginal: " + originalWord.getTerm() + " AND Trans: " + translateList.get(position).getTerm() + " saved", Toast.LENGTH_SHORT);
				}else{
					toast = Toast.makeText(LinguamApplication.getContext(), "You can only have a translation for '" + originalWord.getTerm() + "'", Toast.LENGTH_SHORT);
				}
				
				toast.show();
				
				//Reset layout to init of activity
				EditText translate_caption = (EditText) (findViewById(R.id.search));
				translate_caption.setText(null);
				
				TextView tvTitleTerm = (TextView)(findViewById(R.id.tvTitleTerm));
				tvTitleTerm.setVisibility(-1);

				lv.setAdapter(null);
			}});
		
	}

	private OriginalWord saveOriginalWord(Term originalWord) {
		OriginalWord newOriginalWord = LinguamApplication.originalWordDB.createOriginalWord(originalWord);
		return newOriginalWord;
	}

	private void saveTranslateWord(Term translateWord, Term originalWord) {
		LinguamApplication.translatedWordDB.createTranslation(translateWord, true,
				originalWord.getTerm());
	}
	
	/**
	 * Get all information about beach
	 */
	private void getTranslation(String word) {

		
		wordReferenceClient.translate(word,new Callback<JsonObject>() {

			@Override
			public void failure(RetrofitError err) {
				setProgressBarIndeterminateVisibility(Boolean.FALSE);
				
				Log.e(TAG, err.toString(), err);
				// TODO Auto-generated catch block
				TextView tvTitleTerm = (TextView)(findViewById(R.id.tvTitleTerm));
				tvTitleTerm.setText(getString(R.string.translate_possible_translation) +" "+ getString(R.string.translate_no_result));
				tvTitleTerm.setVisibility(1);
				
			}

			@Override
			public void success(JsonObject response, retrofit.client.Response arg1) {
				setProgressBarIndeterminateVisibility(Boolean.FALSE);
				
				// List of objects to persist
				List<Term> firstTranslationList = new ArrayList<Term>();
				List<Term> originalTermList = new ArrayList<Term>();
				HashMap<String, List<Term>> hashRequestWRList = new HashMap<String, List<Term>>();
				
				
				if (response.getAsJsonObject().get("Error") != null){
					
					TextView tvTitleTerm = (TextView)(findViewById(R.id.tvTitleTerm));
					tvTitleTerm.setText(getString(R.string.translate_possible_translation) +" "+ getString(R.string.translate_no_result));
					tvTitleTerm.setVisibility(1);
					
				}else{
					
					TextView tvTitleTerm = (TextView)(findViewById(R.id.tvTitleTerm));
					tvTitleTerm.setText(getString(R.string.translate_possible_translation));
					tvTitleTerm.setVisibility(-1);
					
					// Parsing JSON
					JsonElement je2 = response.getAsJsonObject().get("term0");
					JsonElement je3 = je2.getAsJsonObject().get("PrincipalTranslations");

					Type mapType = new TypeToken<Map<String, Item>>() {}.getType();
					
					Map<String, Item> principalTranslation = new Gson().fromJson(je3,
							mapType);

					List<Item> list = new ArrayList<Item>(principalTranslation.values());

					for (Item item : list) {
						firstTranslationList.add(item.getFirstTranslation());
						originalTermList.add(item.getOriginalTerm());
					}

					// Add translate word to hashmap
					if (firstTranslationList.size() > 0) {
						hashRequestWRList.put("firstTranslation", firstTranslationList);
					}
					// Add original word to hashmap
					if (originalTermList.size() > 0) {
						hashRequestWRList.put("originalTerm", originalTermList);
					}

					showResults(hashRequestWRList.get("firstTranslation"),
							hashRequestWRList.get("originalTerm").get(hashRequestWRList.get("originalTerm").size()-1));
				}
				
			}
		
		});

	}

}