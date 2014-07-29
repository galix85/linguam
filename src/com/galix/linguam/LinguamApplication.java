package com.galix.linguam;


import java.util.Locale;

import com.galix.linguam.db.LanguageDBAdapter;
import com.galix.linguam.db.OriginalWordDBAdapter;
import com.galix.linguam.db.StatisticDBAdapter;
import com.galix.linguam.db.TranslationDBAdapter;
import com.galix.linguam.pojo.Language;
import com.galix.linguam.util.Util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


public class LinguamApplication extends Application {
	
	public static final String PREFS_NAME = "PreferencesFile";
	public static SharedPreferences preferences = null;
	
	
	public static Context appContext;
	private static LinguamApplication instance = null;
	public static OriginalWordDBAdapter originalWordDB = null;
	public static TranslationDBAdapter translatedWordDB = null;
	public static StatisticDBAdapter statisticDB = null;
	public static LanguageDBAdapter languageDB = null;
	public static Locale spanish_locale;
	public static Language selectedLanguage = null;
	
	public static final int CONSTANT_SCORE = 10;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		appContext = getApplicationContext();
		originalWordDB = new OriginalWordDBAdapter(appContext);
		translatedWordDB = new TranslationDBAdapter(appContext);
		statisticDB = new StatisticDBAdapter(appContext);
		languageDB = new LanguageDBAdapter(appContext);
		spanish_locale = new Locale("es", "ES");
		
		//get default language
		setSelectedLanguage(Util.getSelectedLanguage());
		
		// Restore preferences
	    this.preferences = getSharedPreferences(PREFS_NAME, 0);
	}
	
	
	public static LinguamApplication getInstance() {
		if(instance != null) {
			return instance;
		} else {
			return new LinguamApplication();
		}
	}

	/**
	 * Returns the application context
	 *
	 * @return application context
	 */
	public static Context getContext() {
	    return appContext;
	}

	public static void setSelectedLanguage(Language language){
		selectedLanguage = language;
	}
		
	public static Language getSelectedLanguage(){
		return selectedLanguage;
	}
}

