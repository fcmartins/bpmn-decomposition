package org.tyco.common.util;
/**
 * A map.
 *
 * @author  Vasco T. Vasconcelos
 * @version $Id$
 */
public class Map extends java.util.TreeMap {
	
    /**
     * Constructs a new, empty map, sorted according to
     * the given comparator.
     */
    public Map (java.util.Comparator c) { super (c); }
    
    /**
     * Constructs a new, empty map, sorted according to the keys'
     * natural order.
     */
    private Map () {}
    
    /**
     * An iterator over the keys in this map.
     */
    public java.util.Iterator iterator () { return keySet().iterator (); }
    
    /**
     * An iterator over the values in this map.
     */
    public java.util.Iterator valueIterator () { return values().iterator (); }
}
