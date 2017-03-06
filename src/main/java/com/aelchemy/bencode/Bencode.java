package com.aelchemy.bencode;

import com.aelchemy.bencode.exception.InvalidFormatException;

/**
 * Contains methods for encoding and decoding of Bencoded data.
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/Bencode">Bencode - Wikipedia</a>
 * 
 * @author Aelexe
 *
 */
public class Bencode {

	/**
	 * Decodes the Bencoded data argument as a string.
	 * 
	 * @param bData The Bencoded data containing the string.
	 * @return The string contained in the Bencoded data argument.
	 * @throws InvalidFormatException Thrown if the Bencoded data argument is an invalid format.
	 */
	public static String decodeString(final String bData) throws InvalidFormatException {
		return Decode.decodeString(bData);
	}

}
