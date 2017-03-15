package com.aelchemy.bencode.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aelchemy.bencode.Bencode;

/**
 * {@link BList} represents a Bencoded list.
 * 
 * @author Aelexe
 * 
 */
public class BList extends BType implements Iterable<BData> {

	private final List<BData> data = new ArrayList<BData>();

	public BData get(int index) {
		return data.get(index);
	}

	public List<BData> getData() {
		return data;
	}

	public void add(final BData data) {
		this.data.add(data);
	}

	public void add(String string) {
		this.data.add(new BData(new BString(string)));
	}

	public void add(long number) {
		this.data.add(new BData(new BNumber(number)));
	}

	public void add(BList list) {
		this.data.add(new BData(list));
	}

	public int size() {
		return data.size();
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public String encode() {
		return Bencode.encodeList(this);
	}

	@Override
	public Iterator<BData> iterator() {
		return data.iterator();
	}

}
