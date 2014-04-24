package com.galix.linguam.pojo;

import java.util.ArrayList;

public class GameData {

	private PairWord pairWord;
	private ArrayList<String> paddingWordList;
	
	/**
	 * @return the pairWordList
	 */
	public PairWord getPairWord() {
		return pairWord;
	}
	/**
	 * @param pairWordList the pairWordList to set
	 */
	public void setPairWord(PairWord pairWord) {
		this.pairWord = pairWord;
	}
	/**
	 * @return the paddingWordList
	 */
	public ArrayList<String> getPaddingWordList() {
		return paddingWordList;
	}
	/**
	 * @param paddingWordList the paddingWordList to set
	 */
	public void setPaddingWordList(ArrayList<String> paddingWordList) {
		this.paddingWordList = paddingWordList;
	}
	
		
}
