package org.callas.util;

/**
 * Tests wether two objects are equal.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Sep 30, 2009
 * 
 * @param <T> The base class.
 */
public interface Matcher<T> {
	/**
	 * Checks if two objects are equal.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	void areEqual(T o1, T o2) throws DifferentTypesException;
}
