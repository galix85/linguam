package com.galix.linguam.RESTinterface;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

import com.google.gson.JsonObject;

	/**
	 * Interface of client / user operations
	 * @author Jaume
	 *
	 */
	public interface WordReferenceClient {
			
		    @GET("/{lang}/{word}")
			void translate(@Path("lang") String lang,@Path("word") String word, Callback<JsonObject> callback);
 
	}

	
	
