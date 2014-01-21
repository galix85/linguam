package com.galix.linguam.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.galix.linguam.util.WordReferenceUtil.Term;

public class RequestWR {
	
	static String url_base = "http://api.wordreference.com/0.8/6cd19/json";
	static String languageSource = "en";
	static String languageTo = "es";
	
	private static WordReferenceUtil wrUtil;
	private static HashMap<String, List<Term>> hashmapResponse;
	
	static JsonObjectRequest jsObjRequest = null;
	
	public static HashMap<String, List<Term>> callWR(String word) {
			
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
								
								try {
									hashmapResponse = wrUtil
											.parseJSON(response.toString());

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
	
			return hashmapResponse;
		}

	
	
}
