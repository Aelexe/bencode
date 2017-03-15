package com.aelchemy.bencode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;

import com.aelchemy.bencode.data.BData;
import com.aelchemy.bencode.data.BDictionary;
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

		assertEquals(5, list.size());
		assertBString("One", list.get(0));
		assertBNumber(2, list.get(1));
		assertBString("Three", list.get(2));
		assertBNumber(4, list.get(3));
		assertBString("Five", list.get(4));

		BList emptyList = Bencode.decodeList("le");
		assertTrue(emptyList.isEmpty());
	}

	/**
	 * Tests {@link Bencode#decodeList} returns the expected {@link BList} for the provided valid Bencoded list with nested lists.
	 * 
	 * @throws InvalidFormatException If thrown the test fails.
	 */
	@Test
	public void testDecodeList_NestedLists() throws InvalidFormatException {
		BList list = Bencode
				.decodeList("l25:The next value is a list.li1ei3ei5ee57:End of list, next value is the sum of this lists numbers.i9elli1eeli1ei2eeee");

		assertEquals(5, list.size());
		assertBString("The next value is a list.", list.get(0));
		assertTrue(list.get(1).isList());
		assertBString("End of list, next value is the sum of this lists numbers.", list.get(2));
		assertBNumber(9, list.get(3));
		assertTrue(list.get(4).isList());

		BList firstList = list.get(1).asList();
		assertEquals(3, firstList.size());
		assertBNumber(1, firstList.get(0));
		assertBNumber(3, firstList.get(1));
		assertBNumber(5, firstList.get(2));

		BList secondList = list.get(4).asList();
		assertEquals(2, secondList.size());
		assertTrue(secondList.get(0).isList());
		BList subListOne = secondList.get(0).asList();
		assertEquals(1, subListOne.size());
		assertBNumber(1, subListOne.get(0));
		assertTrue(secondList.get(1).isList());
		BList subListTwo = secondList.get(1).asList();
		assertEquals(2, subListTwo.size());
		assertBNumber(1, subListTwo.get(0));
		assertBNumber(2, subListTwo.get(1));
	}

	/**
	 * Tests {@link Bencode#decodeList} throws {@link InvalidFormatException} for the provided invalid Bencoded lists.
	 */
	@Test
	public void testDecodeList_Invalid() {
		String[] invalidLists = new String[] { null, // Null
				"", // Empty.
				"i1ei2ei3ee", // Missing start.
				"li1ei2ei3e", // Missing end.
				"5:String", // String.
				"i5e", // Number.
				"d3:Onei1e3:Twoi2e5:Threei3ee", // Dictionary.
				"l3:One", // Incomplete string.
				"liee", // Incomplete number.
				"l3:Onei2e5:Threei4e4:Fivehereliesthegarbagee" // Garbage data.
		};

		for (String bData : invalidLists) {
			try {
				Bencode.decodeList(bData);
			} catch (InvalidFormatException e) {
				continue;
			} catch (Exception e) {
				fail("Expected InvalidFormatException for test data: \"" + bData + "\" but received " + e.getClass().getName() + ".");
			}
			fail("Expected InvalidFormatException for test data: \"" + bData + "\" but received nothing.");
		}
	}

	/**
	 * Tests {@link Bencode#decodeDictionary} returns the expected {@link BDictionary} for the provided valid Bencoded dictionaries.
	 * 
	 * @throws InvalidFormatException If thrown the test fails.
	 */
	@Test
	public void testDecodeDictionary() throws InvalidFormatException {
		BDictionary numberDictionary = Bencode.decodeDictionary("d3:Onei1e3:Twoi2e5:Threei3e4:Fouri4e4:Fivei5ee");

		assertEquals(5, numberDictionary.size());
		assertBDictionaryKeyOrder(new String[] { "One", "Two", "Three", "Four", "Five" }, numberDictionary);
		assertBDictionaryContainsNumber("One", 1, numberDictionary);
		assertBDictionaryContainsNumber("Two", 2, numberDictionary);
		assertBDictionaryContainsNumber("Three", 3, numberDictionary);
		assertBDictionaryContainsNumber("Four", 4, numberDictionary);
		assertBDictionaryContainsNumber("Five", 5, numberDictionary);

		BDictionary stringDictionary = Bencode.decodeDictionary("d4:Zero4:Zero4:Ichi3:One2:Ni3:Two3:San5:Three3:Shi4:Four2:Go4:Five4:Roku3:Sixe");

		assertEquals(7, stringDictionary.size());
		assertBDictionaryKeyOrder(new String[] { "Zero", "Ichi", "Ni", "San", "Shi", "Go", "Roku" }, stringDictionary);
		assertBDictionaryContainsString("Zero", "Zero", stringDictionary);
		assertBDictionaryContainsString("Ichi", "One", stringDictionary);
		assertBDictionaryContainsString("Ni", "Two", stringDictionary);
		assertBDictionaryContainsString("San", "Three", stringDictionary);
		assertBDictionaryContainsString("Shi", "Four", stringDictionary);
		assertBDictionaryContainsString("Go", "Five", stringDictionary);
		assertBDictionaryContainsString("Roku", "Six", stringDictionary);

		BDictionary listDictionary = Bencode
				.decodeDictionary("d6:Animall3:Cat3:Dog4:Goat8:Elephante4:Foodl7:Noodles3:Ham8:Sandwich6:Cookie6:Cheesee5:Drinkl5:Water4:Coke3:Tea6:Coffeeee");

		assertEquals(3, listDictionary.size());
		assertBDictionaryKeyOrder(new String[] { "Animal", "Food", "Drink" }, listDictionary);
		// Animal
		assertTrue(listDictionary.get("Animal").isList());
		BList animals = listDictionary.get("Animal").asList();
		assertEquals(4, animals.size());
		assertBString("Cat", animals.get(0));
		assertBString("Dog", animals.get(1));
		assertBString("Goat", animals.get(2));
		assertBString("Elephant", animals.get(3));
		// Food
		assertTrue(listDictionary.get("Food").isList());
		BList food = listDictionary.get("Food").asList();
		assertEquals(5, food.size());
		assertBString("Noodles", food.get(0));
		assertBString("Ham", food.get(1));
		assertBString("Sandwich", food.get(2));
		assertBString("Cookie", food.get(3));
		assertBString("Cheese", food.get(4));
		// Drink
		assertTrue(listDictionary.get("Drink").isList());
		BList drink = listDictionary.get("Drink").asList();
		assertEquals(4, drink.size());
		assertBString("Water", drink.get(0));
		assertBString("Coke", drink.get(1));
		assertBString("Tea", drink.get(2));
		assertBString("Coffee", drink.get(3));

		BDictionary emptyDictionary = Bencode.decodeDictionary("de");
		assertTrue(emptyDictionary.isEmpty());
	}

	/**
	 * Tests {@link Bencode#decodeDictionary} throws {@link InvalidFormatException} for the provided invalid Bencoded dictionaries.
	 */
	@Test
	public void testDecodeDictionary_Invalid() {
		String[] invalidDictionaries = new String[] { null, // Null
				"", // Empty.
				"3:Onei1e3:Twoi2e5:Threei3ee", // Missing start.
				"d3:Onei1e3:Twoi2e5:Threei3e", // Missing end.
				"5:String", // String.
				"i5e", // Number.
				"l3:Onei2e5:Threei4e4:Fivee", // List.
				"di3e5:Threee", // Non-string key.
				"d5:Threee", // Missing value.
				"d4:Sixi6e", // Garbage key.
				"d5:Threei3e", // Garbage value.
				"d5:Threei3eGarbageheree" // Garbage data.
		};

		for (String bData : invalidDictionaries) {
			try {
				Bencode.decodeDictionary(bData);
			} catch (InvalidFormatException e) {
				continue;
			} catch (Exception e) {
				fail("Expected InvalidFormatException for test data: \"" + bData + "\" but received " + e.getClass().getName() + ".");
			}
			fail("Expected InvalidFormatException for test data: \"" + bData + "\" but received nothing.");
		}
	}

	private void assertBString(final String expectedValue, final BData bString) {
		assertTrue(bString.isString());
		assertEquals(expectedValue, bString.asString().getValue());
	}

	private void assertBNumber(final long expectedValue, final BData bNumber) {
		assertTrue(bNumber.isNumber());
		assertEquals(expectedValue, bNumber.asNumber().getValue());
	}

	private void assertBDictionaryKeyOrder(String[] expectedKeys, BDictionary dictionary) {
		Set<String> keys = dictionary.keySet();

		int i = 0;
		for (String key : keys) {
			assertEquals(expectedKeys[i], key);
			i++;
		}
	}

	private void assertBDictionaryContainsString(String key, String expectedString, BDictionary dictionary) {
		assertTrue(dictionary.contains(key));
		BData string = dictionary.get(key);
		assertTrue(string.isString());
		assertEquals(expectedString, string.asString().getValue());

	}

	private void assertBDictionaryContainsNumber(String key, long expectedNumber, BDictionary dictionary) {
		assertTrue(dictionary.contains(key));
		BData number = dictionary.get(key);
		assertTrue(number.isNumber());
		assertEquals(expectedNumber, number.asNumber().getValue());
	}

}
