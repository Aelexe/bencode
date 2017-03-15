package com.aelchemy.bencode.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.aelchemy.bencode.Bencode;

/**
 * {@link BDictionary} represents a Bencoded dictionary.
 * 
 * @author Aelexe
 * 
 */
public class BDictionary extends BType {

	private Map<String, BData> data = new LinkedHashMap<String, BData>();

	public boolean contains(String key) {
		return data.containsKey(key);
	}

	public BData get(String key) {
		return data.get(key);
	}

	public Set<String> keySet() {
		return data.keySet();
	}

	public void put(String key, final BData data) {
		this.data.put(key, data);
	}

	public void put(String key, String string) {
		put(key, new BData(new BString(string)));
	}

	public void put(String key, long number) {
		put(key, new BData(new BNumber(number)));
	}

	public void put(String key, BList list) {
		put(key, new BData(list));
	}

	public int size() {
		return data.size();
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public String encode() {
		return Bencode.encodeDictionary(this);
	}

}
