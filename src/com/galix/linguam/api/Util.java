 package com.galix.linguam.api;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Util {
	
	private Gson gson;

	public String getTranslation(String jsonLine) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonLine);
		    JSONObject  term0 = jsonObject.getJSONObject("term0");
		    List<JSONObject>  principalTrans = (List<JSONObject>) term0.getJSONObject("PrincipalTranslations");
		    
		    JSONObject  firstTranslation = principalTrans.get(0).getJSONObject("FisrtTranslation");
		    Log.v("JSON", firstTranslation.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    
	    
	    
	    
	    
	    //System.out.println(principalTrans.toString());
	    
	    /*JsonObject  firstTranslation = jelement.getAsJsonObject();
	    JsonArray array = new JsonArray();
	    JsonArray jarray = principalTrans.getAsJsonArray();
	    
	    firstTranslation = principalTrans.getAsJsonObject("FirstTranslation");
	    //JsonArray jarray = principalTrans.getAsJsonArray("PrincipalTranslations");
	    
	    
	    /*JsonArray jarray = jobject.getAsJsonArray("AdditionalTranslations");
	    jobject = jarray.get(0).getAsJsonObject();
	    String result = jobject.get("translatedText").toString();*/
	    return "";
		
		    /*Gson gson = new GsonBuilder().create();
		    Data data = gson.fromJson(jsonLine, Data.class);

		    Translations translations = data.getTranslation();
		    TranslatedText[] arrayTranslatedText = translations.getArrayTranslatedText(); //this returns an array, based on json string

		    for(TranslatedText translatedText:arrayTranslatedText )
		    {
		           System.out.println(translatedText.getTranslatedText());
		    }
		    
		    return arrayTranslatedText[0].getTranslatedText();*/
	}
	
}
