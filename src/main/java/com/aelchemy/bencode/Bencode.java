package com.aelchemy.bencode;

import com.aelchemy.bencode.data.BList;
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

	/**
	 * Encodes the string argument into a Bencoded string. <br>
	 * Null will be treated as an empty string.
	 * 
	 * @param string The string to encode.
	 * @return The Bencoded string.
	 */
	public static String encodeString(final String string) {
		return Encode.encodeString(string);
	}

	/**
	 * Decodes the Bencoded data argument as a number.
	 * 
	 * @param bData The Bencoded data containing the number.
	 * @return The number contained in the Bencoded data argument.
	 * @throws InvalidFormatException Thrown if the Bencoded data argument is an invalid format.
	 */
	public static long decodeNumber(final String bData) throws InvalidFormatException {
		return Decode.decodeNumber(bData);
	}

	/**
	 * Encodes the number argument into a Bencoded number.
	 * 
	 * @param number The number to encode.
	 * @return The Bencoded number.
	 */
	public static String encodeNumber(final long number) {
		return encodeNumber(number);
	}

	/**
	 * Decodes the Bencoded data argument as a list.
	 * 
	 * @param bData The Bencoded data containing the list.
	 * @return The {@link BList} contained in the Bencoded data argument.
	 * @throws InvalidFormatException Thrown if the Bencoded data argument is an invalid format.
	 */
	public static BList decodeList(final String bData) throws InvalidFormatException {
		return Decode.decodeList(bData);
	}

}
