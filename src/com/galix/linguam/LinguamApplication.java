package com.galix.linguam;


import java.util.Locale;

import com.galix.linguam.db.OriginalWordDBAdapter;
import com.galix.linguam.db.StatisticDBAdapter;
import com.galix.linguam.db.TranslationDBAdapter;

import android.app.Application;
import android.content.Context;


public class LinguamApplication extends Application {
	
	public static Context appContext;
	private static LinguamApplication instance = null;
	public static OriginalWordDBAdapter originalWordDB = null;
	public static TranslationDBAdapter translatedWordDB = null;
	public static StatisticDBAdapter statisticDB = null;
	public static Locale spanish_locale;
	
	public static final int CONSTANT_SCORE = 10;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		appContext = getApplicationContext();
		originalWordDB = new OriginalWordDBAdapter(appContext);
		translatedWordDB = new TranslationDBAdapter(appContext);
		statisticDB = new StatisticDBAdapter(appContext);
		spanish_locale = new Locale("es", "ES");
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

}

