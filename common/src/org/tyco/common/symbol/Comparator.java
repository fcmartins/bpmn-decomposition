package org.tyco.common.symbol;
/**
 * Comparing Symbols
 *
 * @author Vasco T. Vasconcelos
 * @version $Id$
 */
public final class Comparator implements java.util.Comparator<Object> {
    /**
     * Compares its two arguments for order
     * @param o1 One of the reference objects
     * @param o2 The other the reference objects
     * @return A negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.  
     * @pre o1 instanceof org.tyco.tycoc.symbol && o2 instanceof org.tyco.tycoc.symbol
     */
    public int compare(Object o1, Object o2) {
		return ((Symbol) o1).toString().compareTo(((Symbol) o2).toString());
    }
}
