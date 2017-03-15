package com.aelchemy.bencode.data;

import com.aelchemy.bencode.Bencode;

/**
 * {@link BNumber} represents a Bencoded number.
 * 
 * @author Aelexe
 * 
 */
public class BNumber extends BType {

	private long value;

	public BNumber(final long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(final long value) {
		this.value = value;
	}

	@Override
	public String encode() {
		return Bencode.encodeNumber(value);
	}

}
