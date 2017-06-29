package org.tyco.common.errorMsg;

/**
 * Prints any object of type T.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: IPrinter.java,v 1.2 2009/05/22 10:48:17 tyco Exp $
 * @param <T> The type being printed.
 */
public interface IPrinter<T> {
	/**
	 * Prints the given object.
	 * @param obj The object to be printed.
	 */
	void print(T obj);
	
	/**
	 * A printer that prints nothing.
	 */
	IPrinter<Object> SILENT = new IPrinter<Object>() {
		public void print(Object obj) {
			// nothing to print
		}
	};
}
