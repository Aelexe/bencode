package com.aelchemy.bencode.data;

import com.aelchemy.bencode.Encode;

/**
 * {@link BString} represents a Bencoded string.
 * 
 * @author Aelexe
 *
 */
public class BString extends BData {

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
		return Encode.encodeString(value);
	}

}
