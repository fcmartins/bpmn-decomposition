package org.tyco.common.errorMsg.msgs;

import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.IPrinter;
import org.tyco.common.errorMsg.SourceLocation;

/**
 * Syntactic error. The message is set by the parser or by the lexer.
 * @author Tiago Cogumbreiro
 * @version $Id: SyntacticError.java,v 1.5 2008/10/07 00:42:36 tyco Exp $
 */
public class SyntacticError extends ErrorMessage {
	/**
	 * The error message.
	 */
	private final String message;
	
	/**
	 * Creates a new message.
	 * @param pos
	 * The location of the error.
	 * @param message
	 * The error message.
	 */
	public SyntacticError(int pos, String message) {
		super(pos, null);
		this.message = message;
	}

	public SyntacticError(SourceLocation loc, String message) {
		super(loc, null);
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}

	@Override
	public void printDetails(IPrinter out) {
		// no details to print
	}
}
