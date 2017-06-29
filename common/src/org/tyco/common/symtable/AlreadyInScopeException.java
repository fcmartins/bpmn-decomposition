/**
 * 
 */
package org.tyco.common.symtable;

/**
 * @author Tiago Cogumbreiro
 * @version $Id: AlreadyInScopeException.java,v 1.1 2008/01/18 22:07:39 tyco Exp $
 */
public class AlreadyInScopeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6983314879556430718L;

	/**
	 * 
	 */
	public AlreadyInScopeException() {
		super();
	}

	/**
	 * @param message
	 */
	public AlreadyInScopeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AlreadyInScopeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AlreadyInScopeException(String message, Throwable cause) {
		super(message, cause);
	}

}
