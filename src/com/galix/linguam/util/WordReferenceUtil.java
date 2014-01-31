package com.galix.linguam.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.pojo.OriginalWord;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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
			String jsonLine)  {

		HashMap<String, List<WordReferenceUtil.Term>> hashRequestWRList = new HashMap<String, List<WordReferenceUtil.Term>>();
		
		try {
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

		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashRequestWRList;
	}

}