package org.tyco.common.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.tyco.common.errorMsg.ErrorMessagesException;

/**
 * Parses a textual representation of a term of type T.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Sep 11, 2009
 * 
 * @param <T>
 *            the type of the object being parsed.
 */
public interface IParser<T> {
	/**
	 * Parses a string holding an object of type T.
	 * 
	 * @param data
	 *            The Callas program.
	 * @return The parsed AST.
	 * @throws ErrorMessagesException
	 */
	T parse(String data) throws ErrorMessagesException;

	/**
	 * Parses a stream holding an object of type T.
	 * 
	 * @param filename
	 *            The filename of the source code. Serves for error reporting
	 *            proposes only.
	 * @param stream
	 *            The stream holding the source code. Cannot be <code>null</code>.
	 * @return The parsed object.
	 * @throws ErrorMessagesException
	 *             Thrown if there are syntactic errors in the source code.
	 */
	T parse(String filename, InputStream stream) throws ErrorMessagesException;
}
