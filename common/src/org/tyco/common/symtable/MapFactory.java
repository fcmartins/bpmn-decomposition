package org.tyco.common.symtable;

import java.util.Map;

/**
 * Factory of maps.
 * @author Tiago Cogumbreiro
 * @version $Id: MapFactory.java,v 1.1 2008/01/16 16:29:40 tyco Exp $
 * @param <K>
 * @param <V>
 */
public interface MapFactory<K, V> {
	/**
	 * Creates a new {@link Map}.
	 * @return
	 */
	Map<K, V> createMap();
}
