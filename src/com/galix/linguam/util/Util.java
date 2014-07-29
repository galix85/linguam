package com.galix.linguam.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.pojo.Language;
import com.galix.linguam.pojo.Term;

public class Util {

	static HashMap<String, Integer> langMapping = new HashMap<String, Integer>();
	
	public static int randomNumber(int min, int max) {
		Random rand = new Random();
		return rand.nextInt(max - min + 1) + min;
	}

	public static ArrayList<Integer> generateRandomNumber(int min, int max) {
		
		int rnd;
		int[] randNo = new int[max+1];
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int k = min; k <= max; k++) {
			rnd = randomNumber(min,max);

			if (k == min) {
				randNo[min] = rnd;
				numbers.add(randNo[min]);
			} else {
				while (numbers.contains(Integer.valueOf(rnd))) {
					rnd = randomNumber(min,max);
				}
				randNo[k] = rnd;
				numbers.add(randNo[k]);
			}
		}
		return numbers;	}

	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int getWidth(Context ctxt){

		int width=0;
		
	    WindowManager wm = (WindowManager) ctxt.getSystemService(Context.WINDOW_SERVICE);
	    Display display = wm.getDefaultDisplay();
	    
	    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.HONEYCOMB){                   
	        Point size = new Point();
	        display.getSize(size);
	        width = size.x;
	    }
	    else{
	        width = display.getWidth();  // deprecated
	    }
	    return width;
		
	}
	
	public static boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) LinguamApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static String trimWord(String originalWord) {
		
		String[] originalWordArray = originalWord.split(",");
		if (originalWordArray != null && originalWordArray.length > 0)
			originalWord = originalWordArray[0];
			originalWord = originalWord.replaceAll("\\s+$", "");
		
		return originalWord;
	}

	public static List<Term> removeDuplicates(List<Term> originalList) {
		
		Set<Term> setItems = new LinkedHashSet<Term>(originalList);
		originalList.clear();
		originalList.addAll(setItems);
		
		return originalList;
	}
	
	public static String getActionBarTitle() {
		
		populateLangMapping();
		Language selectedLanguage = LinguamApplication.languageDB.getSelectedLanguage();
		if (langMapping.containsKey(selectedLanguage.getLangFrom())){
			return LinguamApplication.getContext().getResources().getString(langMapping.get(selectedLanguage.getLangFrom()));
		}else{
			return "Playing with: " + selectedLanguage.getTitle();
		}
		
	}
	
	public static Language getSelectedLanguage() {
		return LinguamApplication.languageDB.getSelectedLanguage();
	}
	
	private static void populateLangMapping(){
		
		langMapping.put("es", R.string.es);
		langMapping.put("en", R.string.en);
		langMapping.put("pt", R.string.pt);
		langMapping.put("it", R.string.it);
		langMapping.put("fr", R.string.fr);
	
	}
}
