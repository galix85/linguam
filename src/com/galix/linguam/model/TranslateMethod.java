package com.galix.linguam.model;

import java.util.HashMap;
import java.util.List;

import com.galix.linguam.db.OriginalWordDBAdapter;
import com.galix.linguam.db.TranslationDBAdapter;
import com.galix.linguam.pojo.OriginalWord;
import com.galix.linguam.util.WordReferenceUtil.Term;

public class TranslateMethod {

	public String saveTranslateResult(HashMap<String, List<Term>> hashmapResponse, OriginalWordDBAdapter originalWordDB, TranslationDBAdapter translatedWordDB){
		

		//Get both response WR lists from hashmap
		List<Term> firstTranslation = hashmapResponse.get("firstTranslation");
		List<Term> originalTerm = hashmapResponse.get("originalTerm");
		String translated_word = null;
		
		int firstTranslationSize = firstTranslation.size();
		//int originalTermSize = originalTerm.size();
		
		OriginalWord originalword = originalWordDB.createOriginalWord(originalTerm.get(originalTerm.size()-1));
		
		if (originalword != null){
			
			//Insert to DB
			for (Term translation : firstTranslation) {
				 if (--firstTranslationSize == 0) {
					 translatedWordDB.createTranslation(translation,true,originalword.getTerm());
				 }else{
					 translatedWordDB.createTranslation(translation,false,originalword.getTerm());
				 }
			}
		
			return translated_word = hashmapResponse.get("firstTranslation").get(firstTranslation.size()-1).getTerm().toString();
		}else{
			return translated_word;
		}
		
	}
	
	
}
