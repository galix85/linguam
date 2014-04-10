package com.galix.linguam.pojo;

public class Item {

	Term OriginalTerm;
	Term FirstTranslation;
	String Note;
	
	/**
	 * @return the originalTerm
	 */
	public Term getOriginalTerm() {
		return OriginalTerm;
	}

	/**
	 * @param originalTerm the originalTerm to set
	 */
	public void setOriginalTerm(Term originalTerm) {
		OriginalTerm = originalTerm;
	}

	/**
	 * @return the firstTranslation
	 */
	public Term getFirstTranslation() {
		return FirstTranslation;
	}

	/**
	 * @param firstTranslation the firstTranslation to set
	 */
	public void setFirstTranslation(Term firstTranslation) {
		FirstTranslation = firstTranslation;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return Note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		Note = note;
	}

	
	@Override
	public String toString() {
		return "Item [OriginalTerm=" + OriginalTerm + ", FirstTranslation="
				+ FirstTranslation + ", Note=" + Note + "]";
	}
	
}

