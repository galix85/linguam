package com.galix.linguam.api;

public class Wordreference {

	String url;
	
	public Wordreference(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	//TODO Conexxio real
	//api.wordreference.com/0.8/6cd19/json/enes/fallen
	public String callWordReferenceTranslation(String word){
		return "Word translated";
	}
	
}
