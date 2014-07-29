package com.galix.linguam.pojo;

public class Language {

	private int languageId;
	private String langFrom;
	private String langTo;
	private String isActive;
	private String isSelected;
	private String title;
	private String subtitle;
	private String imgSrc;
	
	/**
	 * @return the langFrom
	 */
	public String getLangFrom() {
		return langFrom;
	}
	/**
	 * @param langFrom the langFrom to set
	 */
	public void setLangFrom(String langFrom) {
		this.langFrom = langFrom;
	}
	/**
	 * @return the langTo
	 */
	public String getLangTo() {
		return langTo;
	}
	/**
	 * @param langTo the langTo to set
	 */
	public void setLangTo(String langTo) {
		this.langTo = langTo;
	}
	/**
	 * @return the imgSrc
	 */
	public String getImgSrc() {
		return imgSrc;
	}
	/**
	 * @param imgSrc the imgSrc to set
	 */
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	
	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getIsSelected() {
		return isSelected;
	}
	
	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}
	
	public String getSubtitle() {
		return subtitle;
	}
	
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public int getId() {
		return languageId;
	}
	public void setId(int id) {
		this.languageId = id;
	}
	
	@Override
	public String toString() {
	    return this.subtitle;
	}

		
}
