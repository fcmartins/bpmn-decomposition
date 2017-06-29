package org.tyco.common.errorMsg;

import java.io.PrintStream;

/**
 * Implements a printer using a standard {@link PrintStream}.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @param <T>
 *            The type of the object to print.
 * @date Aug 3, 2008
 */
public class StreamPrinter<T> implements IPrinter<T> {

	/**
	 * Where the data will be printed into.
	 */
	private PrintStream stream;

	/**
	 * Create a printer from a stream.
	 * 
	 * @param stream
	 */
	public StreamPrinter(PrintStream stream) {
		this.stream = stream;
	}

	public void print(T obj) {
		stream.print(obj);
	}

}
