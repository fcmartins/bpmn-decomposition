package org.tyco.common.symtable;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory of {@link HashMap}s.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: HashMapFactory.java,v 1.1 2008/01/16 16:29:40 tyco Exp $
 * @param <K>
 * @param <V>
 */
public class HashMapFactory<K, V> implements MapFactory<K, V> {

	public Map<K, V> createMap() {
		return new HashMap<K, V>();
	}

}
