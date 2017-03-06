package com.aelchemy.bencode;

import org.apache.commons.lang3.StringUtils;

import com.aelchemy.bencode.exception.InvalidFormatException;

/**
 * Contains methods for the decoding of Bencoded data.
 * 
 * @author Aelexe
 *
 */
class Decode {

	/**
	 * Decodes the Bencoded data argument as a string.
	 * 
	 * @param bData The Bencoded data containing the string.
	 * @return The string contained in the Bencoded data argument.
	 * @throws InvalidFormatException Thrown if the Bencoded data argument is an invalid format.
	 */
	public static String decodeString(final String bData) throws InvalidFormatException {
		// Validate the data isn't empty.
		if (StringUtils.isBlank(bData)) {
			throw new InvalidFormatException("Data is null or empty: \"" + bData + "\"");
		}

		// Split the data into the length and data parts.
		String[] bStringSplit = bData.split(":", 2);

		// Validate there are two parts and the length isn't empty.
		if (bStringSplit.length != 2 || StringUtils.isBlank(bStringSplit[0])) {
			throw new InvalidFormatException("Data is missing the length, data or colon: \"" + bData + "\"");
		}
		int length = 0;
		String data = bStringSplit[1];

		// Parse the length.
		try {
			length = Integer.parseInt(bStringSplit[0]);
		} catch (NumberFormatException e) {
			throw new InvalidFormatException("Data length could not be parsed to an integer: \"" + bData + "\"");
		}

		// Validate the length is a positive integer and the data's length matches it.
		if (length < 0 || data.length() != length) {
			throw new InvalidFormatException("Data length and declared length do not match: \"" + bData + "\"");
		}

		return data;
	}

	/**
	 * Decodes the Bencoded data argument as a number.
	 * 
	 * @param bData The Bencoded data containing the number.
	 * @return The number contained in the Bencoded data argument.
	 * @throws InvalidFormatException Thrown if the Bencoded data argument is an invalid format.
	 */
	public static long decodeNumber(final String bData) throws InvalidFormatException {
		// Validate the data isn't empty.
		if (bData == null || bData.length() < 3) {
			throw new InvalidFormatException("Data is null or doesn't contain a number: \"" + bData + "\"");
		}

		// Validate the data starts with i and ends with e.
		if (!bData.startsWith("i") || !bData.endsWith("e")) {
			throw new InvalidFormatException("Data does not start with i or end with e: \"" + bData + "\"");
		}

		// Validate the data doesn't have leading zeros, unless it is zero.
		if ((bData.startsWith("i0") && !bData.equals("i0e")) || bData.startsWith("i-0")) {
			throw new InvalidFormatException("Data contains leading zeros: \"" + bData + "\"");
		}

		// Parse the number.
		long number;
		try {
			number = Long.parseLong(bData.substring(1, bData.length() - 1));
		} catch (NumberFormatException e) {
			throw new InvalidFormatException("Data could not be parsed to a long: \"" + bData + "\"");
		}

		return number;
	}

}
