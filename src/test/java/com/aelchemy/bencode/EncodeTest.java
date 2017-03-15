package com.aelchemy.bencode;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.aelchemy.bencode.data.BDictionary;
import com.aelchemy.bencode.data.BList;

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
	@Test
	public void testEncodeNumber() {
		assertEquals("i1e", Bencode.encodeNumber(1));
		assertEquals("i-12e", Bencode.encodeNumber(-12));
		assertEquals("i42e", Bencode.encodeNumber(42));
		assertEquals("i1596e", Bencode.encodeNumber(1596));
		assertEquals("i9223372036854775807e", Bencode.encodeNumber(9223372036854775807l));
		assertEquals("i-9223372036854775808e", Bencode.encodeNumber(-9223372036854775808l));
		assertEquals("i0e", Bencode.encodeNumber(0));
		assertEquals("i0e", Bencode.encodeNumber(-0));
	}

	/**
	 * Tests {@link Bencode#encodeList} returns the expected Bencoded lists for the provided lists.
	 */
	@Test
	public void testEncodeList() {
		BList brownFoxList = new BList();
		brownFoxList.add("The");
		brownFoxList.add("quick");
		brownFoxList.add("brown");
		brownFoxList.add("fox");
		brownFoxList.add("jumps");
		brownFoxList.add("over");
		brownFoxList.add("the");
		brownFoxList.add("lazy");
		brownFoxList.add("dog");
		brownFoxList.add(".");

		assertEquals("l3:The5:quick5:brown3:fox5:jumps4:over3:the4:lazy3:dog1:.e", Bencode.encodeList(brownFoxList));

		BList fibonacciList = new BList();
		fibonacciList.add(1);
		fibonacciList.add(1);
		fibonacciList.add(2);
		fibonacciList.add(5);
		fibonacciList.add(8);
		fibonacciList.add(13);
		fibonacciList.add(21);
		fibonacciList.add(34);
		fibonacciList.add(55);
		fibonacciList.add(89);
		fibonacciList.add(144);

		assertEquals("li1ei1ei2ei5ei8ei13ei21ei34ei55ei89ei144ee", Bencode.encodeList(fibonacciList));

		BList mixedList = new BList();
		mixedList.add("1 + 1");
		mixedList.add(1 + 1);
		mixedList.add("2 + 2");
		mixedList.add(2 + 2);
		mixedList.add("4 + 4");
		mixedList.add(4 + 4);
		mixedList.add("8 + 8");
		mixedList.add(8 + 8);
		mixedList.add("16 + 16");
		mixedList.add(16 + 16);

		assertEquals("l5:1 + 1i2e5:2 + 2i4e5:4 + 4i8e5:8 + 8i16e7:16 + 16i32ee", Bencode.encodeList(mixedList));
	}

	/**
	 * Tests {@link Bencode#encodeList} returns the expected Bencoded list for the provided nested list.
	 */
	@Test
	public void testEncodeList_NestedLists() {
		BList nestedList = new BList();
		BList currentList = nestedList;

		for (int depth = 0; depth < 5; depth++) {
			for (int i = 0; i < 5; i++) {
				BList childList = new BList();
				if (i != 0) {
					for (int j = i; j <= 5; j++) {
						childList.add(j);
					}
				}
				currentList.add(childList);
			}
			currentList = currentList.get(0).asList();
		}

		assertEquals(
				"lllllleli1ei2ei3ei4ei5eeli2ei3ei4ei5eeli3ei4ei5eeli4ei5eeeli1ei2ei3ei4ei5eeli2ei3ei4ei5eeli3ei4ei5eeli4ei5eeeli1ei2ei3ei4ei5eeli2ei3ei4ei5eeli3ei4ei5eeli4ei5eeeli1ei2ei3ei4ei5eeli2ei3ei4ei5eeli3ei4ei5eeli4ei5eeeli1ei2ei3ei4ei5eeli2ei3ei4ei5eeli3ei4ei5eeli4ei5eee",
				Bencode.encodeList(nestedList));
	}

	/**
	 * Tests {@link Bencode#encodeDictionary} returns the expected Bencoded dictionaries for the provided dictionaries.
	 */
	@Test
	public void testEncodeDictionary() {
		BDictionary classColors = new BDictionary();
		classColors.put("Warrior", "Brown");
		classColors.put("Paladin", "Pink");
		classColors.put("Hunter", "Green");
		classColors.put("Rogue", "Yellow");
		classColors.put("Priest", "White");
		classColors.put("Death Knight", "Red");
		classColors.put("Shaman", "Blue");
		classColors.put("Mage", "Light Blue");
		classColors.put("Warlock", "Purple");
		classColors.put("Monk", "Mint");
		classColors.put("Druid", "Orange");
		classColors.put("Demon Hunter", "Light Purple");

		assertEquals(
				"d7:Warrior5:Brown7:Paladin4:Pink6:Hunter5:Green5:Rogue6:Yellow6:Priest5:White12:Death Knight3:Red6:Shaman4:Blue4:Mage10:Light Blue7:Warlock6:Purple4:Monk4:Mint5:Druid6:Orange12:Demon Hunter12:Light Purplee",
				Bencode.encodeDictionary(classColors));

		BDictionary numbers = new BDictionary();
		numbers.put("One", 1);
		numbers.put("Eleven", 11);
		numbers.put("One Hundred and One", 101);
		numbers.put("Four Oh Four", 404);
		numbers.put("Nineteen", 19);

		assertEquals("d3:Onei1e6:Eleveni11e19:One Hundred and Onei101e12:Four Oh Fouri404e8:Nineteeni19ee", Bencode.encodeDictionary(numbers));
	}

	/**
	 * Tests {@link Bencode#encodeDictionary} returns the expected Bencoded dictionary for the provided nested dictionary.
	 */
	@Test
	public void testEncodeDictionary_NestedDictionaries() {

	}
}
