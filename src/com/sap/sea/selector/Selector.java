package com.sap.sea.selector;

import java.util.TreeMap;

import com.sap.sea.Island;

public abstract class Selector {
	protected TreeMap<String, Island> islands;
	public Selector(TreeMap<String, Island> islands) {
		this.islands = islands;
	}
	
}
