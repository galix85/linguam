package com.galix.linguam.pojo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Term {

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
		return "Term [term=" + term + ", POS=" + POS + ", sense=" + sense
				+ ", usage=" + usage + "]";
	}
	
	public int hashCode() {
		  // return (POS.hashCode() + sense.hashCode() + term.hashCode() + usage.hashCode());
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(POS);
		builder.append(sense);
		builder.append(term);
		builder.append(usage);
		return builder.toHashCode();
	}

	   public boolean equals(Object obj) {
		   
		   /*if (obj == null) return false;
		   if (obj == this) return true;
		   if (!(obj instanceof TermWrapper))return false;*/
		   if (obj instanceof Term) {
			   Term otherTerm = ((Term)obj);
		       return new EqualsBuilder().
		           append(POS, otherTerm.POS).
		           append(sense, otherTerm.sense).
		           append(term, otherTerm.term).
		           append(usage, otherTerm.usage).
		           isEquals();
		   }
		   return false;
	   }
}

