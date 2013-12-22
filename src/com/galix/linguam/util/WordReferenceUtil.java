package com.galix.linguam.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class WordReferenceUtil {

	public static class Term {
		String term;
		String POS;
		String sense;
		String usage;

		public String getTerm() {
			return term;
		}

		public void setTerm(String term) {
			this.term = term;
		}

		public String getPOS() {
			return POS;
		}

		public void setPOS(String pOS) {
			POS = pOS;
		}

		public String getSense() {
			return sense;
		}

		public void setSense(String sense) {
			this.sense = sense;
		}

		public String getUsage() {
			return usage;
		}

		public void setUsage(String usage) {
			this.usage = usage;
		}

		@Override
		public String toString() {
			return "Term [term=" + term + ", POS=" + POS + ", sense=" + sense
					+ ", usage=" + usage + "]";
		}

	}

	public static class Item {
		Term OriginalTerm;
		Term FirstTranslation;
		String Note;

		@Override
		public String toString() {
			return "Item [OriginalTerm=" + OriginalTerm + ", FirstTranslation="
					+ FirstTranslation + ", Note=" + Note + "]";
		}

	}
	
	public HashMap<String, List<Term>> hashmapResponse;

	public HashMap<String, List<Term>> parseJSON(
			String jsonLine) throws Exception {

		HashMap<String, List<WordReferenceUtil.Term>> hashRequestWRList = new HashMap<String, List<WordReferenceUtil.Term>>();

		// Parsing JSON
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonLine);
		JsonElement je2 = je.getAsJsonObject().get("term0");
		JsonElement je3 = je2.getAsJsonObject().get("PrincipalTranslations");

		// List of objects to persist
		List<WordReferenceUtil.Term> firstTranslationList = new ArrayList<WordReferenceUtil.Term>();
		List<WordReferenceUtil.Term> originalTermList = new ArrayList<WordReferenceUtil.Term>();

		Type mapType = new TypeToken<Map<String, Item>>() {
		}.getType();
		Map<String, Item> principalTranslation = new Gson().fromJson(je3,
				mapType);

		List<Item> list = new ArrayList<Item>(principalTranslation.values());

		for (Item item : list) {
			firstTranslationList.add(item.FirstTranslation);
			originalTermList.add(item.OriginalTerm);
			
		}

		// TODO hash-map with two lists
		if (firstTranslationList.size() > 0) {
			hashRequestWRList.put("firstTranslation", firstTranslationList);
		}
		if (originalTermList.size() > 0) {
			hashRequestWRList.put("originalTerm", originalTermList);
		}

		hashRequestWRList.keySet();
		
		return hashRequestWRList;
		//return firstTranslationList; 
		//return firstTranslationList.get(firstTranslationList.size()-1).getTerm().toString();

	}

	/*public void callWR(String word) {
		
		try {
			RequestQueue queue = Volley.newRequestQueue(LinguamApplication.appContext);
			// Encode word to translate
			String encoded_word = URLEncoder.encode(word, "UTF-8");
			// Call URL WR to translate
			String url = url_base + "/" + languageSource + languageTo + "/"
					+ encoded_word;
			
			
			JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET, 
					url,
                    null,
                    createMyReqSuccessListener(),
                    createMyReqErrorListener());

			/*jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							callbackResponse(response); 
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.v("onErrorResponse - Volley", error.toString());
							
						}
					});

			queue.add(myReq);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.v("UnsupportedEncodingException", e.toString());
			hashmapResponse.put("error", null);
		}
	}
	
	private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            	Log.v("onErrorResponse - Volley", error.toString());
            }
        };
    }

	 private Response.Listener<JSONObject> createMyReqSuccessListener() {
	        return new Response.Listener<JSONObject>() {
	            @Override
	            public void onResponse(JSONObject response) {
	                callbackResponse(response);
	            }
	        };
	    }

	public void callbackResponse(JSONObject response){
		//HashMap<String, List<WordReferenceUtil.Term>> hashRequestWRList = new HashMap<String, List<WordReferenceUtil.Term>>();
		//Send to parseJSON
		
		try {
			hashmapResponse =  parseJSON(response.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			hashmapResponse.put("error", null);
			Log.v("Exception Parsing JSON", e.toString());
			
		}
		
		
	}*/

}