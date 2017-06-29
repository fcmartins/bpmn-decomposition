package org.tyco.common.util;

/**
 * Raised when the found type is not the expected type.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: CheckedClassCastException.java,v 1.1 2008/10/07 00:42:12 tyco Exp $
 */
public class CheckedClassCastException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5002302517133244258L;

	/**
	 * The type found.
	 */
	private Object found;

	/**
	 * The class of the type we were expecting.
	 */
	private Class<?> expected;

	/**
	 * @param found
	 */
	public CheckedClassCastException(Class<?> expected, Object found) {
		this.expected = expected;
		this.found = found;
	}

	/**
	 * The type found.
	 * 
	 * @return the type found.
	 */
	public Object getFound() {
		return found;
	}

	/**
	 * Sets the found type.
	 * 
	 * @param found
	 *            the found type.
	 */
	public void setFound(Object found) {
		this.found = found;
	}

	/**
	 * The class of the expected type.
	 * 
	 * @return the class of the expected type.
	 */
	public Class<?> getExpected() {
		return expected;
	}

	/**
	 * Sets the class of the expected type.
	 * 
	 * @param expected
	 */
	public void setExpected(Class<?> expected) {
		this.expected = expected;
	}
}
