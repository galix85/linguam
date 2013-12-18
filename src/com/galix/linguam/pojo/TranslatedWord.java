package com.galix.linguam.pojo;

public class TranslatedWord {
	
	int id;
	String term;
    String POS;
    String sense;
    String usage;
	
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
    
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
		return "TranslationWord [term=" + term + ", POS=" + POS + ", sense="
				+ sense + ", usage=" + usage + "]";
	}
    
} 