package com.sap.sea;

import java.util.Iterator;

public class SimpleSea extends AbstarctSea {
	private static Sea sea = new SimpleSea();
	private SimpleSea() {
		
	}
	
	public static Sea instance(){
		return sea;
	}
	
	@Override
	public Island pick() {
		for (Iterator<Island> iterator = this.iterator(); iterator.hasNext();) {
			Island island = iterator.next();
			if (island.available()) {
				return island;
			}
		}
		return null;
	}

}
