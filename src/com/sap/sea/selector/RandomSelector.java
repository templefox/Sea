package com.sap.sea.selector;

import java.util.TreeMap;

import javax.ws.rs.Path;

import com.sap.sea.Island;

public class RandomSelector extends Selector {
	
	public RandomSelector(TreeMap<String, Island> islands) {
		super(islands);
	}

	@Path("/")
	public Island select() {
		for (String key : islands.keySet()) {
			Island island = islands.get(key);
			island.enableShell(false);
			if (island.available()) {
				return island;
			}
		}
		return null;
	}

}
