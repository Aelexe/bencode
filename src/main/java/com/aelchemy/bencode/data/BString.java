package com.aelchemy.bencode.data;

import com.aelchemy.bencode.Bencode;

/**
 * {@link BString} represents a Bencoded string.
 * 
 * @author Aelexe
 * 
 */
public class BString extends BType {

	private String value;

	public BString(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	@Override
	public String encode() {
		return Bencode.encodeString(value);
	}

}
