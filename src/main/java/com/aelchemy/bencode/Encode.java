package com.aelchemy.bencode;

/**
 * Contains methods for the encoding of data into Bencoded data.
 * 
 * @author Aelexe
 *
 */
public class Encode {

	/**
	 * Encodes the string argument into a Bencoded string. <br>
	 * Null will be treated as an empty string.
	 * 
	 * @param string The string to encode.
	 * @return The Bencoded string.
	 */
	public static String encodeString(String string) {
		if (string == null) {
			string = "";
		}

		return string.length() + ":" + string;
	}

	/**
	 * Encodes the number argument into a Bencoded number.
	 * 
	 * @param number The number to encode.
	 * @return The Bencoded number.
	 */
	public static String encodeNumber(final long number) {
		return "i" + number + "e";
	}
}
