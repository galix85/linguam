package com.galix.lafoska.service;

import java.util.HashMap;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import android.content.Context;
import android.util.Log;
import com.galix.lafoska.LaFoskaApplication;

/**
 * Service REST class
 * @author Jaume
 *
 */
public class ServiceClient {
	private static final String TAG="LaFoska: SerivceClient";
	private static ServiceClient instance;
	public static final String LAFOSKA_BASE_URL="http://lafosca-beach.herokuapp.com/api/v1";
	private RestAdapter mRestAdapter;
	private Map<String, Object> mClients = new HashMap<String, Object>();
	private RestAdapter mRestAuthAdapter;
	private Map<String, Object> mAuthClients = new HashMap<String, Object>();
	private String mBaseUrl = LAFOSKA_BASE_URL;

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
		Log.v(TAG, "getClient without auth");
		if (mRestAdapter == null) {
			mRestAdapter = new RestAdapter.Builder().setEndpoint(LAFOSKA_BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL).setRequestInterceptor(new RequestInterceptor() {
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
	
	/**
	 * Client with intercept auth header
	 * @param context
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAuthClient(Context context, Class<T> clazz) {
		Log.v(TAG, "getClient with auth");
		if (mRestAuthAdapter == null) {
			mRestAuthAdapter = new RestAdapter.Builder().setEndpoint(LAFOSKA_BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL).setRequestInterceptor(new RequestInterceptor() {
		        @Override
		        public void intercept(RequestFacade request) {
		            request.addHeader("Accept", "application/json; charset=UTF-8"); 
			            if(LaFoskaApplication.getToken() != null){
			                 request.addHeader("Authorization","Token token=" + LaFoskaApplication.getToken());
			            }                 
		      
		        }
		    }).build();
		}
		
		T client = null;
		
		if ((client = (T) mAuthClients.get(clazz.getCanonicalName())) != null) {
			return client;
		}
		
		client = mRestAuthAdapter.create(clazz);
		mAuthClients.put(clazz.getCanonicalName(), client);
		return client;
	}
	

	public void setRestAdapter(RestAdapter restAdapter) {
		mRestAdapter = restAdapter;
	}

	public String getBaseUrl(Context context) {
		return mBaseUrl;
	}
}
