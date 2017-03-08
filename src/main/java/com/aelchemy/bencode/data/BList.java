package com.aelchemy.bencode.data;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link BList} represents a Bencoded list.
 * 
 * @author Aelexe
 *
 */
public class BList extends BData {

	private final List<BData> data = new ArrayList<BData>();

	public List<BData> getData() {
		return data;
	}

	public void addData(final BData data) {
		this.data.add(data);
	}

	@Override
	public String encode() {
		return null;
	}

}
