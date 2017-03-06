package com.aelchemy.bencode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

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
	 * @throws InvalidFormatException If thrown the test fails.
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

		for (String bData : invalidStrings) {
			try {
				Bencode.decodeString(bData);
			} catch (InvalidFormatException e) {
				continue;
			} catch (Exception e) {
				fail("Expected InvalidFormatException for test data: \"" + bData + "\" but received " + e.getClass().getName() + ".");
			}
			fail("Expected InvalidFormatException for test data: \"" + bData + "\" but received nothing.");
		}
	}

	/**
	 * Tests {@link Bencode#decodeString} returns the expected strings for the provided valid Bencoded strings.
	 * 
	 * @throws InvalidFormatException If thrown the test fails.
	 */
	@Test
	public void testDecodeNumber() throws InvalidFormatException {
		assertEquals(1, Bencode.decodeNumber("i1e"));
		assertEquals(13, Bencode.decodeNumber("i13e"));
		assertEquals(-7, Bencode.decodeNumber("i-7e"));
		assertEquals(0, Bencode.decodeNumber("i0e"));
		assertEquals(Long.MAX_VALUE, Bencode.decodeNumber("i9223372036854775807e"));
		assertEquals(Long.MIN_VALUE, Bencode.decodeNumber("i-9223372036854775808e"));
	}

	/**
	 * Tests {@link Bencode#decodeString} throws {@link InvalidFormatException} for the provided invalid Bencoded numbers.
	 */
	@Test
	public void testDecodeNumber_Invalid() throws InvalidFormatException {
		String[] invalidNumbers = new String[] { null, // Null
				"", // Empty.
				"ie", // No number.
				"ionee", // Not a number.
				"i001e", // Leading zero.
				"i-0e", // Negative zero.
				"i0.1e", // Decimal
				"i9223372036854775808e", // Out of bounds positive.
				"i-9223372036854775809e" // Out of bounds negative
		};

		for (String bData : invalidNumbers) {
			try {
				Bencode.decodeNumber(bData);
			} catch (InvalidFormatException e) {
				continue;
			} catch (Exception e) {
				fail("Expected InvalidFormatException for test data: \"" + bData + "\" but received " + e.getClass().getName() + ".");
			}
			fail("Expected InvalidFormatException for test data: \"" + bData + "\" but received nothing.");
		}
	}

}
