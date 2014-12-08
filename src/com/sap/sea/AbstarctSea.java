package com.sap.sea;

import java.util.Iterator;
import java.util.LinkedHashMap;

public abstract class AbstarctSea implements Sea {
	private LinkedHashMap<String, Island> islands = new LinkedHashMap<String, Island>();

	@Override
	public Iterator<Island> iterator() {
		return islands.values().iterator();
	}

	@Override
	public boolean contains(Island island) {
		return islands.containsValue(island);
	}

	@Override
	public boolean contains(String key) {
		return islands.containsKey(islands);
	}

	@Override
	public boolean add(Island island) {
		return add(island.toString(), island);
	}

	@Override
	public boolean add(String key, Island island) {
		if (islands.put(key, island) == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Island remove(String key) {
		return islands.remove(key);
	}

	@Override
	public Island get(String key) {
		return islands.get(key);
	}

}
