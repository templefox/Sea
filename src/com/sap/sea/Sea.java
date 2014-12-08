package com.sap.sea;

import java.util.Collection;
import java.util.Iterator;

/**
 * Collection of Island
 * @author I302583
 *
 */
public interface Sea {
	Island pick();
	Iterator<Island> iterator();
	boolean contains(Island island);
	boolean contains(String key);
	boolean add(Island island);
	Island remove(String key);
	Island get(String key);
	boolean add(String key, Island island);
	
}
