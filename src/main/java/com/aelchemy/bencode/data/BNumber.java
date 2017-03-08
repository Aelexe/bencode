package com.aelchemy.bencode.data;

import com.aelchemy.bencode.Encode;

/**
 * {@link BNumber} represents a Bencoded number.
 * 
 * @author Aelexe
 *
 */
public class BNumber extends BData {

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
		return Encode.encodeNumber(value);
	}

}
