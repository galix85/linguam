package com.galix.linguam.api;

import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class ParseJSONWordReference {

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
         return "Term [term=" + term + ", POS=" + POS + ", sense=" + sense + ", usage=" + usage + "]";
      }
      
   }

   public static class Item {
      Term OriginalTerm;
      Term FirstTranslation;
      String Note;

      @Override
      public String toString() {
         return "Item [OriginalTerm=" + OriginalTerm + ", FirstTranslation=" + FirstTranslation + ", Note=" + Note + "]";
      }


   }

   public String getTranslation(String jsonLine) {
         
      JsonParser jp = new JsonParser();
      JsonElement je = jp.parse(jsonLine);
      JsonElement je2 = je.getAsJsonObject().get("term0");
      JsonElement je3 = je2.getAsJsonObject().get("PrincipalTranslations");

      //List of objects to persist
      List<ParseJSONWordReference.Term> firstTranslationList = new ArrayList<ParseJSONWordReference.Term>();
      List<ParseJSONWordReference.Term> originalTermList = new ArrayList<ParseJSONWordReference.Term>();
          
      Type mapType = new TypeToken<Map<String, Item>>() {}.getType();
      Map<String, Item> principalTranslation = new Gson().fromJson(je3, mapType);
      
      List<Item> list = new ArrayList<Item>(principalTranslation.values());

      for (Item item : list) {
  		 firstTranslationList.add(item.FirstTranslation);
  		 originalTermList.add(item.OriginalTerm);
  	  }
      
      return firstTranslationList.get(0).getTerm().toString();
   }

}