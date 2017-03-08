package com.aelchemy.bencode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.aelchemy.bencode.data.BData;
import com.aelchemy.bencode.data.BList;
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

	/**
	 * Tests {@link Bencode#decodeList} returns the expected {@link BList}s for the provided valid Bencoded lists.
	 * 
	 * @throws InvalidFormatException If thrown the test fails.
	 */
	@Test
	public void testDecodeList() throws InvalidFormatException {
		BList list = Bencode.decodeList("l3:Onei2e5:Threei4e4:Fivee");

		assertEquals(5, list.getData().size());
		assertBString("One", list.getData().get(0));
		assertBNumber(2, list.getData().get(1));
		assertBString("Three", list.getData().get(2));
		assertBNumber(4, list.getData().get(3));
		assertBString("Five", list.getData().get(4));

		BList emptyList = Bencode.decodeList("le");
		assertTrue(emptyList.getData().isEmpty());
	}

	/**
	 * Tests {@link Bencode#decodeList} returns the expected {@link BList} for the provided valid Bencoded list with nested lists.
	 * 
	 * @throws InvalidFormatException If thrown the test fails.
	 */
	@Test
	public void testDecodeList_NestedLists() throws InvalidFormatException {
		BList list = Bencode.decodeList("l25:The next value is a list.li1ei3ei5ee57:End of list, next value is the sum of this lists numbers.i9elli1eeli1ei2eeee");

		assertEquals(5, list.getData().size());
		assertBString("The next value is a list.", list.getData().get(0));
		assertTrue(list.getData().get(1).isList());
		assertBString("End of list, next value is the sum of this lists numbers.", list.getData().get(2));
		assertBNumber(9, list.getData().get(3));
		assertTrue(list.getData().get(4).isList());

		BList firstList = list.getData().get(1).getAsList();
		assertEquals(3, firstList.getData().size());
		assertBNumber(1, firstList.getData().get(0));
		assertBNumber(3, firstList.getData().get(1));
		assertBNumber(5, firstList.getData().get(2));

		BList secondList = list.getData().get(4).getAsList();
		assertEquals(2, secondList.getData().size());
		assertTrue(secondList.getData().get(0).isList());
		BList subListOne = secondList.getData().get(0).getAsList();
		assertEquals(1, subListOne.getData().size());
		assertBNumber(1, subListOne.getData().get(0));
		assertTrue(secondList.getData().get(1).isList());
		BList subListTwo = secondList.getData().get(1).getAsList();
		assertEquals(2, subListTwo.getData().size());
		assertBNumber(1, subListTwo.getData().get(0));
		assertBNumber(2, subListTwo.getData().get(1));
	}

	private void assertBString(final String expectedValue, final BData bString) {
		assertTrue(bString.isString());
		assertEquals(expectedValue, bString.getAsString().getValue());
	}

	private void assertBNumber(final long expectedValue, final BData bNumber) {
		assertTrue(bNumber.isNumber());
		assertEquals(expectedValue, bNumber.getAsNumber().getValue());
	}

}
