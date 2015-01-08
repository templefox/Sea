package com.sap.sea.selector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import javax.ws.rs.Path;

import com.sap.sea.Island;

public class RandomSelector extends Selector {
	
	public RandomSelector(TreeMap<String, Island> islands) {
		super(islands);
	}

	@Path("/")
	public Island select() {
		List<Island> islandss = new ArrayList<Island>();
		islandss.addAll(islands.values());
		Collections.shuffle(islandss);
		for (Island island : islandss) {
			island.enableShell(false);
			if (island.available()) {
				return island;
			}
		}
		return null;
	}

}
