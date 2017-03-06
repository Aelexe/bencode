package com.aelchemy.bencode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.aelchemy.bencode.Bencode;
import com.aelchemy.bencode.Decode;
import com.aelchemy.bencode.exception.InvalidFormatException;

/**
 * Contains tests for {@link Bencode} methods that expose {@link Decode} functionality.
 * 
 * @author Aelexe
 *
 */
public class DecodeTest {

	/**
	 * Tests {@link Bencode#decodeString} returns the expected strings for the provided valid Bencoded strings.
	 * 
	 * @throws InvalidFormatException If thrown the test is failed.
	 */
	@Test
	public void testDecodeString() throws InvalidFormatException {
		assertEquals("", Bencode.decodeString("0:"));
		assertEquals("V", Bencode.decodeString("1:V"));
		assertEquals("To", Bencode.decodeString("2:To"));
		assertEquals("Wow", Bencode.decodeString("3:Wow"));
		assertEquals("Four", Bencode.decodeString("4:Four"));
		assertEquals("Hello", Bencode.decodeString("5:Hello"));
		assertEquals("Hello world!", Bencode.decodeString("12:Hello world!"));
	}

	/**
	 * Tests {@link Bencode#decodeString} throws {@link InvalidFormatException} for the provided invalid Bencoded strings.
	 */
	@Test
	public void testDecodeString_Invalid() {
		String[] invalidStrings = new String[] { null, // Null
				"", // Empty.
				":", // Colon on its own.
				":Word", // No length.
				"Another", // No length of colon.
				"1:", // No text.
				"0", // No text or colon.
				"5:Four" // Length mismatch.
		};

		for (String bString : invalidStrings) {
			try {
				Bencode.decodeString(bString);
			} catch (InvalidFormatException e) {
				continue;
			} catch (Exception e) {
				fail("Expected InvalidFormatException for test string: \"" + bString + "\" but received " + e.getClass().getName() + ".");
			}
			fail("Expected InvalidFormatException for test string: \"" + bString + "\" but received nothing.");
		}
	}

}
