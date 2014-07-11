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
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.RESTinterface.WordReferenceClient;
import com.galix.linguam.adapter.DrawableAdapter;
import com.galix.linguam.adapter.TermAdapter;
import com.galix.linguam.pojo.Item;
import com.galix.linguam.pojo.Language;
import com.galix.linguam.pojo.OriginalWord;
import com.galix.linguam.pojo.Term;
import com.galix.linguam.pojo.TranslatedWord;
import com.galix.linguam.service.ServiceClient;
import com.galix.linguam.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class Translate_Activity extends ListActivity implements OnItemSelectedListener {

	private static final String TAG = "Linguam: Translate Activity";
	private WordReferenceClient wordReferenceClient;

	private EditText etTranslateCaption;
	private TextView tvTitleTerm;

	private ImageButton ib_searchButton;
	private ListView lv;
	private Spinner spinner;
	private InputMethodManager imm;
	ArrayList<Term> translateList;
	
	private String langFrom = null;
	private String langTo = null;
	
	private List<Language> drawerListViewLang;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawableAdapter adapter;
	

	/**
	 * @return the langFrom
	 */
	public String getLangFrom() {
		return langFrom;
	}

	/**
	 * @param langFrom the langFrom to set
	 */
	public void setLangFrom(String langFrom) {
		this.langFrom = langFrom;
	}

	/**
	 * @return the langTo
	 */
	public String getLangTo() {
		return langTo;
	}

	/**
	 * @param langTo the langTo to set
	 */
	public void setLangTo(String langTo) {
		this.langTo = langTo;
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	    	
	    	Language select_language = drawerListViewLang.get(position);
	    	LinguamApplication.languageDB.setSelectedLanguage(select_language.getId());
	    
	    	Toast.makeText(LinguamApplication.getContext(),"Select language: "+ select_language.getTitle(), Toast.LENGTH_LONG).show();
	    	drawerLayout.closeDrawer(drawerListView);

	    }
	}

	
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
         actionBarDrawerToggle.syncState();
    }

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
    }

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		 // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        // then it has handled the app icon touch event

		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Input Method Manager
		this.imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		// Progress Icon
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// Get the message from the intent
		getIntent();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate_layout);
		
		// get list items from db
		drawerListViewLang = LinguamApplication.languageDB.getActiveLanguage();
		// get ListView defined in activity_main.xml
		drawerListView = (ListView) findViewById(R.id.left_drawer);
       
		adapter = new DrawableAdapter(LinguamApplication.getContext(), drawerListViewLang);
		
		 // Set the adapter for the list view
		drawerListView.setAdapter(adapter);

		// 2. App Icon 
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// 2.1 create ActionBarDrawerToggle
		actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                );

        // 2.2 Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        
        // 2.3 enable and show "up" arrow
        getActionBar().setDisplayHomeAsUpEnabled(true); 
        
        // just styling option
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		drawerListView.setOnItemClickListener(new DrawerItemClickListener());

		// Client object with actions interface of beach.
		wordReferenceClient = ServiceClient.getInstance().getClient(this,
				WordReferenceClient.class);
		

		// Init UI
		this.translateList = new ArrayList<Term>();
		this.lv = (ListView) findViewById(android.R.id.list);
		this.tvTitleTerm = (TextView) (findViewById(R.id.tvTitleTerm));
		this.etTranslateCaption = (EditText) (findViewById(R.id.search));
		this.ib_searchButton = (ImageButton) findViewById(R.id.translate);
		this.spinner = (Spinner) findViewById(R.id.spinner);

		// Spinner click listener
        spinner.setOnItemSelectedListener((OnItemSelectedListener) this);
 
        // Loading spinner data from database
        loadSpinnerData();

		ib_searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				String word_to_translate;
				
				try {

					word_to_translate = URLEncoder.encode(etTranslateCaption
							.getText().toString(), "UTF-8");
					if (!word_to_translate.equals("")) {
						// Make visible progress icon
						setProgressBarIndeterminateVisibility(Boolean.TRUE);
						// Hide keyboard
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
						// Call to Translate Method
						getTranslation(word_to_translate);
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					Log.v(TAG, e.getCause().toString());
				}

			}
		});

	}

	private void showResults(final List<Term> translateList,
			final Term originalWord) {

		// Make visible title of search "Possible Translations:"
		tvTitleTerm.setText(getString(R.string.translate_possible_translation));
		tvTitleTerm.setVisibility(1);

		List<String> translationTerm = new ArrayList<String>();
		for (Term term : translateList) {
			translationTerm.add(term.getTerm());
		}

		TermAdapter adapter = new TermAdapter(LinguamApplication.getContext(),
				translateList);
		lv.setAdapter(adapter);

		//
		this.getListView().setSelector(R.drawable.translate_row_selector);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast toast;
				if (saveOriginalWord(originalWord) != null) {
					if (saveTranslateWord(translateList.get(position),
							originalWord) != null) {
						saveOtherTranslationWord(translateList, originalWord);
						String strTranslationSavedOriginal = getResources()
								.getString(R.string.translation_saved);
						String strTranslationSavedFinal = String.format(
								strTranslationSavedOriginal,
								originalWord.getTerm(),
								translateList.get(position).getTerm());
						toast = Toast.makeText(LinguamApplication.getContext(),
								strTranslationSavedFinal, Toast.LENGTH_SHORT);
						toast.show();
					}
				} else {
					String strAlreadyTranslatedOriginal = getResources()
							.getString(R.string.already_traslated);
					String strAlreadyTranslatedFinal = String.format(
							strAlreadyTranslatedOriginal,
							originalWord.getTerm());
					toast = Toast.makeText(LinguamApplication.getContext(),
							strAlreadyTranslatedFinal, Toast.LENGTH_SHORT);
					toast.show();
				}

				// Reset layout to init of activity
				etTranslateCaption.setText(null);
				tvTitleTerm.setVisibility(-1);
				lv.setAdapter(null);

			}
		});

	}

	private OriginalWord saveOriginalWord(Term originalWord) {
	
		originalWord.setTerm(Util.trimWord(originalWord.getTerm()));
		OriginalWord newOriginalWord = LinguamApplication.originalWordDB
				.createOriginalWord(originalWord);
		return newOriginalWord;
	}

	private TranslatedWord saveTranslateWord(Term translateWord,
			Term originalWord) {
		translateWord.setTerm(Util.trimWord(translateWord.getTerm()));
		return LinguamApplication.translatedWordDB.createTranslation(
				translateWord, true, originalWord.getTerm());
	}

	private void saveOtherTranslationWord(List<Term> translateWord,
			Term originalWord) {

		for (Term term : translateWord) {
			term.setTerm(Util.trimWord(term.getTerm()));
			LinguamApplication.translatedWordDB.createTranslation(term, false,
					originalWord.getTerm());
		}

	}

	/**
	 * Get all information about beach
	 */
	private void getTranslation(String word) {

		wordReferenceClient.translate(langFrom+langTo,word, new Callback<JsonObject>() {

			@Override
			public void failure(RetrofitError err) {
				setProgressBarIndeterminateVisibility(Boolean.FALSE);

				Log.e(TAG, err.toString(), err);
				// TODO Auto-generated catch block
				tvTitleTerm
						.setText(getString(R.string.translate_possible_translation)
								+ " " + getString(R.string.translate_no_result));
				tvTitleTerm.setVisibility(1);

			}

			@Override
			public void success(JsonObject response,
					retrofit.client.Response arg1) {
				setProgressBarIndeterminateVisibility(Boolean.FALSE);

				// List of objects to persist
				List<Term> firstTranslationList = new ArrayList<Term>();
				List<Term> originalTermList = new ArrayList<Term>();
				HashMap<String, List<Term>> hashRequestWRList = new HashMap<String, List<Term>>();

				if (response.getAsJsonObject().get("Error") != null) {

					tvTitleTerm
							.setText(getString(R.string.translate_possible_translation)
									+ " "
									+ getString(R.string.translate_no_result));
					tvTitleTerm.setVisibility(1);

				} else {

					tvTitleTerm
							.setText(getString(R.string.translate_possible_translation));
					tvTitleTerm.setVisibility(-1);

					try {

						// Parsing JSON
						JsonElement je2 = response.getAsJsonObject().get(
								"term0");
						JsonElement je3 = je2.getAsJsonObject().get(
								"PrincipalTranslations");

						Type mapType = new TypeToken<Map<String, Item>>() {
						}.getType();

						Map<String, Item> principalTranslation = new Gson()
								.fromJson(je3, mapType);

						List<Item> list = new ArrayList<Item>(
								principalTranslation.values());

						for (Item item : list) {
							firstTranslationList.add(item.getFirstTranslation());
							originalTermList.add(item.getOriginalTerm());
						}
						
						firstTranslationList = Util.removeDuplicates(firstTranslationList);
						originalTermList = Util.removeDuplicates(originalTermList);

						// Add translate word to hashmap
						if (firstTranslationList.size() > 0) {
							hashRequestWRList.put("firstTranslation",
									firstTranslationList);
						}
						// Add original word to hashmap
						if (originalTermList.size() > 0) {
							hashRequestWRList.put("originalTerm",
									originalTermList);
						}

						showResults(
								hashRequestWRList.get("firstTranslation"),
								hashRequestWRList.get("originalTerm").get(
										hashRequestWRList.get("originalTerm")
												.size() - 1));

					} catch (Exception e) {
						tvTitleTerm
								.setText(getString(R.string.translate_possible_translation)
										+ " "
										+ getString(R.string.translate_no_result));
						tvTitleTerm.setVisibility(1);
						Log.v(TAG, e.toString());
					}

				}

			}

		});

	}
	
	/**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
   
    	// Spinner Drop down elements
        List<Language> languages = LinguamApplication.languageDB.getActiveLanguage();
 
        // Creating adapter for spinner
        ArrayAdapter<Language> dataAdapter = new ArrayAdapter<Language>(this,
                android.R.layout.simple_spinner_item, languages);
 
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
 
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {

    	Language language = (Language)parent.getItemAtPosition(position);
        
        //Set langs
        setLangFrom(language.getLangFrom());
        setLangTo(language.getLangTo());
       
        // Showing selected spinner item
        Log.v(TAG ,"Lang to translate: " + language.getLangFrom() + " to " + language.getLangTo());
 
    }
 
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
 
    }

}