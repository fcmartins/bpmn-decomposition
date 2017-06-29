package org.tyco.common.util;


/**
 * A set.
 *
 * @author  Vasco T. Vasconcelos
 * @version $Id$
 */
public class Set<V> extends java.util.TreeSet<V> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2403586304772226562L;

	/**
     * Constructs a new, empty set, sorted according to the
     * given comparator.
     */
    public Set(java.util.Comparator<V> c) {
        super(c);
    }
    
    /**
     * Add all the elements in the specified set to this set.
     * @param other Set whose elements are to be added.
     */
    public void add(Set<V> other) {
        addAll(other);
    }

    /**
     * Add all the elements in the specified array to this set.
     * @param elements Array whose elements are to be added
     */
    public void add(V[] elements) {
        for (int i = 0; i < elements.length; i++) {
            add(elements[i]);
        }
    }
        
    /**
     * Remove all the elements in the specified set from this set.
     * @param other Set whose elements are to be removed.
     */
    public void remove(Set<V> other) {
        removeAll(other);
    }

    /**
     * Remove all the elements in the specified array from this set.
     * @param elements Array whose elements are to be removed.
     */
    public void remove(V[] elements) {
        for (int i = 0; i < elements.length; i++) {
            remove(elements[i]);
        }
    }
}
