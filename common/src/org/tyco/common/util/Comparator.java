package org.tyco.common.util;
/**
 * Comparing Objects.
 *
 * @author Vasco T. Vasconcelos
 * @version $Id$
 */
public final class Comparator<T> implements java.util.Comparator<T> {
	
    /**
     * Compares its two arguments for order.
     * Note from the Java Platform 1.2: As much as is reasonably
     * practical, the hashCode method defined by class Object does
     * return distinct integers for distinct objects.
     * @param o1 One object
     * @param o2 The other object
     * @return A negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     */
    public int compare(T o1, T o2) {
        int hash1 = o1.hashCode();
        int hash2 = o2.hashCode();
        return hash1 < hash2 ? -1 : (hash1 == hash2 ? 0 : 1);
    }
}
