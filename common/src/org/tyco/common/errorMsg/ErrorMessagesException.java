/**
 * 
 */
package org.tyco.common.errorMsg;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.tyco.common.errorMsg.ErrorMessage;

/**
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 *
 */
public class ErrorMessagesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4649366448127917485L;
	
	/**
	 * The errors found.
	 */
	private final Collection<ErrorMessage> errors;

	/**
	 * @param errors
	 */
	public ErrorMessagesException(Collection<ErrorMessage> errors) {
		this.errors = errors;
	}
	
	/**
	 * Utility constructor.
	 * @param errors
	 */
	public ErrorMessagesException(ErrorMessage... errors) {
		this(Arrays.asList(errors));
	}

	/**
	 * Gets the errors.
	 * @return The error messages.
	 */
	public Collection<ErrorMessage> getErrors() {
		return errors;
	}
}
