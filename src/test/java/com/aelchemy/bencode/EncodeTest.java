package com.aelchemy.bencode;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Contains tests for {@link Bencode} methods that expose {@link Encode} functionality.
 * 
 * @author Aelexe
 *
 */
public class EncodeTest {

	/**
	 * Tests {@link Bencode#encodeString} returns the expected Bencoded strings for the provided strings.
	 */
	@Test
	public void testEncodeString() {
		assertEquals("12:Hello world!", Bencode.encodeString("Hello world!"));
		assertEquals("15:Praise the sun!", Bencode.encodeString("Praise the sun!"));
		assertEquals(
				"445:Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
				Bencode.encodeString(
						"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));
		assertEquals("0:", Bencode.encodeString(""));
		assertEquals("1: ", Bencode.encodeString(" "));
		assertEquals("0:", Bencode.encodeString(null));
	}

	/**
	 * Tests {@link Bencode#encodeNumber} returns the expected Bencoded numbers for the provided numbers.
	 */
	public void testEncodeNumber() {
		assertEquals("i1e", Bencode.encodeNumber(1));
		assertEquals("i-12e", Bencode.encodeNumber(-12));
		assertEquals("i42e", Bencode.encodeNumber(42));
		assertEquals("i1596e", Bencode.encodeNumber(1596));
		assertEquals("i9223372036854775807e", Bencode.encodeNumber(9223372036854775807l));
		assertEquals("i-9223372036854775808e", Bencode.encodeNumber(-9223372036854775808l));
		assertEquals("0", Bencode.encodeNumber(0));
		assertEquals("0", Bencode.encodeNumber(-0));
	}

}
