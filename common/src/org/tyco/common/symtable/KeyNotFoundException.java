/**
 * 
 */
package org.tyco.common.symtable;

/**
 * @author Tiago Cogumbreiro
 * @version $Id: KeyNotFoundException.java,v 1.1 2008/02/11 21:26:26 tyco Exp $
 */
public class KeyNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4025012311284580645L;

	/**
	 * 
	 */
	public KeyNotFoundException() {
		super();
	}

	/**
	 * @param message
	 */
	public KeyNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public KeyNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public KeyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
