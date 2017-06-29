package org.tyco.common.symtable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A factory of {@link LinkedHashMap}s.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: LinkedHashMapFactory.java,v 1.1 2009/01/19 22:16:23 tyco Exp $
 * @param <K>
 * @param <V>
 */
public class LinkedHashMapFactory<K, V> implements MapFactory<K, V> {

	public Map<K, V> createMap() {
		return new LinkedHashMap<K, V>();
	}

}
