package com.galix.linguam.pojo;

public class PairWord {

	private String originalWord;
	private String translateWord;
	private int level;
	
	/**
	 * @return the originalWord
	 */
	public String getOriginalWord() {
		return originalWord;
	}
	/**
	 * @param originalWord the originalWord to set
	 */
	public void setOriginalWord(String originalWord) {
		this.originalWord = originalWord;
	}
	/**
	 * @return the translateWord
	 */
	public String getTranslateWord() {
		return translateWord;
	}
	/**
	 * @param translateWord the translateWord to set
	 */
	public void setTranslateWord(String translateWord) {
		this.translateWord = translateWord;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
