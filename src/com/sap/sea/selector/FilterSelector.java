package com.sap.sea.selector;

import java.util.TreeMap;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.sap.sea.Island;

public class FilterSelector extends Selector {
	
	public FilterSelector(TreeMap<String, Island> islands) {
		super(islands);
	}

	@Path("/{mem}")
	public Island select(@PathParam("mem") Integer mem) {
		for (String key : islands.keySet()) {
			Island island = islands.get(key);
			island.enableShell(false);
			if (island.available()) {
				if (island.checkMem(mem)) {
					return island;
				}
			}
		}
		return null;
	}

}
