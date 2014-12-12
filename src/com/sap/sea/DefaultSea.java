package com.sap.sea;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DefaultSea implements Sea{
	Map<String, Island> map = new LinkedHashMap<String, Island>();
	List<String> list = new LinkedList<String>();
	
	
	public Island getIslandOrCreate(String ip) {
		addToList(ip);
		if (map.containsKey(ip)) {
			return map.get(ip);
		}else {
			Island island = new DefaultIsland(ip);
			map.put(ip, island);
			return island;
		}
	}


	private void addToList(String ip) {
		if (!list.contains(ip)) {
			list.add(ip);
		}
	}
}
