package com.aelchemy.bencode.data;

/**
 * Abstract {@link BData} is a wrapper for {@link BType}.
 * 
 * @author Aelexe
 *
 */
public class BData {

	private BType data;

	public BData(BType data) {
		this.data = data;
	}

	public boolean isString() {
		return data instanceof BString;
	}

	public BString asString() {
		return (BString) data;
	}

	public boolean isNumber() {
		return data instanceof BNumber;
	}

	public BNumber asNumber() {
		return (BNumber) data;
	}

	public boolean isList() {
		return data instanceof BList;
	}

	public BList asList() {
		return (BList) data;
	}

	public boolean isDictionary() {
		return data instanceof BDictionary;
	}

	public BDictionary asDictionary() {
		return (BDictionary) data;
	}

	public String encode() {
		return data.encode();
	}

}
