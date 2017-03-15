package com.aelchemy.bencode;

import com.aelchemy.bencode.data.BData;
import com.aelchemy.bencode.data.BDictionary;
import com.aelchemy.bencode.data.BList;

/**
 * Contains methods for the encoding of data into Bencoded data.
 * 
 * @author Aelexe
 * 
 */
class Encode {

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

	/**
	 * Encodes the {@link BList} argument into a Bencoded list.
	 * 
	 * @param list The {@link BList} to encode.
	 * @return The Bencoded list.
	 */
	public static String encodeList(final BList list) {
		StringBuilder bencodedList = new StringBuilder();
		bencodedList.append("l");
		for (BData data : list) {
			bencodedList.append(data.encode());
		}
		bencodedList.append("e");

		return bencodedList.toString();
	}

	/**
	 * Encodes the {@link BDictionary} argument into a Bencoded dictionary.
	 * 
	 * @param list The {@link BDictionary} to encode.
	 * @return The Bencoded dictionary.
	 */
	public static String encodeDictionary(final BDictionary dictionary) {
		StringBuilder bencodedDictionary = new StringBuilder();
		bencodedDictionary.append("d");
		for (String key : dictionary.keySet()) {
			bencodedDictionary.append(encodeString(key));
			bencodedDictionary.append(dictionary.get(key).encode());
		}
		bencodedDictionary.append("e");

		return bencodedDictionary.toString();
	}
}
