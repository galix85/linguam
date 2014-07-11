package com.galix.linguam.service;

import java.util.HashMap;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import android.content.Context;

/**
 * Service REST class
 * @author Jaume
 *
 */
public class ServiceClient {
	
	private static final String TAG="Liguam: SerivceClient";
	private static ServiceClient instance;
	
	public static final String URL_BASE = "http://api.wordreference.com/0.8/6cd19/json/";
	public String langFrom = "en";
	public String langTo = "es";
	private RestAdapter mRestAdapter;
	private Map<String, Object> mClients = new HashMap<String, Object>();

	//Exclude this attribute from JSON
	//@Expose private Long term0;
	
	private ServiceClient() {
	}

	public static ServiceClient getInstance() {
		if (null == instance) {
			instance = new ServiceClient();
		}
		return instance;
	}

	/**
	 * Client without intercept auth header
	 * @param context
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getClient(Context context, Class<T> clazz) {

		if (mRestAdapter == null) {
			mRestAdapter = new RestAdapter.Builder().setEndpoint(URL_BASE).setLogLevel(RestAdapter.LogLevel.FULL)
					.setRequestInterceptor(new RequestInterceptor() {
		        @Override
		        public void intercept(RequestFacade request) {
		            request.addHeader("Accept", "application/json; charset=UTF-8");
		        }
		    }).build();
		}
		
		T client = null;
		
		if ((client = (T) mClients.get(clazz.getCanonicalName())) != null) {
			return client;
		}
		
		client = mRestAdapter.create(clazz);
		mClients.put(clazz.getCanonicalName(), client);
		return client;
	}
	
}
