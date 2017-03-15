package com.aelchemy.bencode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.aelchemy.bencode.data.BData;
import com.aelchemy.bencode.data.BDictionary;
import com.aelchemy.bencode.data.BList;
import com.aelchemy.bencode.data.BNumber;
import com.aelchemy.bencode.data.BString;
import com.aelchemy.bencode.exception.InvalidFormatException;

/**
 * Contains methods for the decoding of Bencoded data.
 * 
 * @author Aelexe
 * 
 */
class Decode {

	private static final Pattern LEADING_NUMBER_PATTERN = Pattern.compile("\\d*");

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
	 * Decodes the Bencoded data argument as a string.
	 * 
	 * @param bData The Bencoded data containing the string.
	 * @return {@link BString} representing the string contained in the Bencoded data argument.
	 * @throws InvalidFormatException Thrown if the Bencoded data argument is an invalid format.
	 */
	public static BString decodeBString(final String bData) throws InvalidFormatException {
		return new BString(decodeString(bData));
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
			throw new InvalidFormatException("Data does not start with i and end with e: \"" + bData + "\"");
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

	/**
	 * Decodes the Bencoded data argument as a number.
	 * 
	 * @param bData The Bencoded data containing the number.
	 * @return {@link BNumber} representing the number contained in the Bencoded data argument.
	 * @throws InvalidFormatException Thrown if the Bencoded data argument is an invalid format.
	 */
	public static BNumber decodeBNumber(final String bData) throws InvalidFormatException {
		return new BNumber(decodeNumber(bData));
	}

	/**
	 * Decodes the Bencoded data argument as a list.
	 * 
	 * @param bData The Bencoded data containing the list.
	 * @return The list contained in the Bencoded data argument.
	 * @throws InvalidFormatException Thrown if the Bencoded data argument is an invalid format.
	 */
	public static BList decodeList(String bData) throws InvalidFormatException {
		// Validate the data isn't empty.
		if (bData == null || bData.length() < 2) {
			throw new InvalidFormatException("Data is null or doesn't contain a list: \"" + bData + "\"");
		}

		// Validate the data starts with l and ends with e.
		if (!bData.startsWith("l") || !bData.endsWith("e")) {
			throw new InvalidFormatException("Data does not start with l and end with e: \"" + bData + "\"");
		}

		// Drop the leading l.
		bData = bData.substring(1);

		// Initialise the list.
		BList list = new BList();

		// Repeatedly extract and decode each Bencoded value until none are left, signaling the completion of the list parsing.
		String extract;
		while ((extract = extractNextBDataString(bData)) != null) {
			list.add(decode(extract));
			bData = trimBData(extract, bData);
		}

		// Validate the last remaining character is the end of the list.
		if (!bData.equals("e")) {
			throw new InvalidFormatException("Data does not end with e: \"" + bData + "\"");
		}

		return list;
	}

	/**
	 * Decodes the Bencoded data argument as a dictionary.
	 * 
	 * @param bData The Bencoded data containing the dictionary.
	 * @return The dictionary contained in the Bencoded data argument.
	 * @throws InvalidFormatException Thrown if the Bencoded data argument is an invalid format.
	 */
	public static BDictionary decodeDictionary(String bData) throws InvalidFormatException {
		// Validate the data isn't empty.
		if (bData == null || bData.length() < 2) {
			throw new InvalidFormatException("Data is null or doesn't contain a dictionary: \"" + bData + "\"");
		}

		// Validate the data starts with d and ends with e.
		if (!bData.startsWith("d") || !bData.endsWith("e")) {
			throw new InvalidFormatException("Data does not start with d and end with e: \"" + bData + "\"");
		}

		// Drop the leading d.
		bData = bData.substring(1);

		// Initialise the dictionary.
		BDictionary dictionary = new BDictionary();

		// Repeatedly extract and decode each Bencoded value until none are left, signaling the completion of the list parsing.
		String keyExtract;
		while ((keyExtract = extractNextBDataString(bData)) != null) {
			String key = decodeString(keyExtract);
			bData = trimBData(keyExtract, bData);
			String valueExtract = extractNextBDataString(bData);
			if (valueExtract == null) {
				throw new InvalidFormatException("Data does not contain a value for key: \"" + key + "\"");
			}
			dictionary.put(key, decode(valueExtract));
			bData = trimBData(valueExtract, bData);
		}

		// Validate the last remaining character is the end of the dictionary.
		if (!bData.equals("e")) {
			throw new InvalidFormatException("Data does not end with e: \"" + bData + "\"");
		}

		return dictionary;
	}

	/**
	 * Decodes the Bencoded data argument as whatever it represents.
	 * 
	 * @param bData The Bencoded data.
	 * @return {@link BData} containing the Bencoded data.
	 * @throws InvalidFormatException Thrown if the Bencoded data argument is an invalid format.
	 */
	private static BData decode(final String bData) throws InvalidFormatException {
		if (Character.isDigit(bData.charAt(0))) {
			return new BData(decodeBString(bData));
		} else if (bData.startsWith("i")) {
			return new BData(decodeBNumber(bData));
		} else if (bData.startsWith("l")) {
			return new BData(decodeList(bData));
		}

		throw new InvalidFormatException("Data does contain a valid Bencoded value: \"" + bData + "\"");
	}

	/**
	 * Extracts the next Bencoded data string from the Bencoded data argument.
	 * 
	 * @param bData The Bencoded data to retrieve the next value from.
	 * @return A string containing the Bencoded data, or null if one could not be ofund.
	 */
	private static String extractNextBDataString(final String bData) {
		if (StringUtils.isNotEmpty(bData)) {
			if (Character.isDigit(bData.charAt(0))) {
				// If the value starts with a number, it's a string.
				// Use a matcher to extract the length.
				Matcher leadingNumberMatcher = LEADING_NUMBER_PATTERN.matcher(bData);
				leadingNumberMatcher.find();
				String stringLength = leadingNumberMatcher.group(0);
				// And then use that length to determine how long the string is in the Bencoded data.
				int totalLength = stringLength.length() + 1 + Integer.valueOf(stringLength);
				if (bData.length() >= totalLength) {
					return bData.substring(0, stringLength.length() + 1 + Integer.valueOf(stringLength));
				} else {
					return null;
				}
			} else if (bData.startsWith("i")) {
				// If the value starts with i, it's a number.
				return bData.substring(0, bData.indexOf('e') + 1);
			} else if (bData.startsWith("l")) {
				// If the value starts with l, it's a list.
				// Recursively extract every sub-value in the list
				int index = 1;
				String extract;
				while ((extract = extractNextBDataString(bData.substring(index))) != null) {
					index = bData.indexOf(extract) + extract.length();
				}
				return bData.substring(0, index + 1);
			}
		}

		return null;
	}

	/**
	 * Returns the Bencoded data argument string with the leading trim content removed.
	 * 
	 * @param trimContent The content to remove from the start of the Bencoded data.
	 * @param bData The Bencoded data to trim.
	 * @return The Bencoded data, less the trim content.
	 */
	private static String trimBData(String trimContent, String bData) {
		return bData.substring(bData.indexOf(trimContent) + trimContent.length());
	}

}
