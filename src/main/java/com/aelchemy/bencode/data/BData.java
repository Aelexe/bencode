package com.aelchemy.bencode.data;

/**
 * Abstract {@link BData} represents a Bencode data type.
 * 
 * @author Aelexe
 *
 */
public abstract class BData {

	public boolean isString() {
		return this instanceof BString;
	}

	public BString getAsString() {
		return (BString) this;
	}

	public boolean isNumber() {
		return this instanceof BNumber;
	}

	public BNumber getAsNumber() {
		return (BNumber) this;
	}

	public abstract String encode();

}
