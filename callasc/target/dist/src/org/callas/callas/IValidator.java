package org.callas.callas;

import java.io.FileNotFoundException;

/**
 * The interface for the validator, the object that is manipulated by editors.
 * 
 * @author Tiago Cogumbreiro
 * 
 */
public interface IValidator {
	/**
	 * Validates a certain filename with the default options.
	 * 
	 * @param filename
	 *            A file-system dependent file name, containing the source code.
	 * @return The result of the syntactic and semantic validation.
	 */
	public Result validateDefault(String filename) throws FileNotFoundException;
}
