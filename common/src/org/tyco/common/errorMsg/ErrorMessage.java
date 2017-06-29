package org.tyco.common.errorMsg;

import org.tyco.common.util.SourceElement;


/**
 * An error message, very similar to an exception.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: ErrorMessage.java,v 1.5 2008/10/07 00:42:36 tyco Exp $
 */
public abstract class ErrorMessage {
	/**
	 * The source code location of the error message.
	 */
	public final SourceLocation location;
	
	/**
	 * Where this error has occurred.
	 */
	public final int pos;
	
	/**
	 * The AST element associated with this error (optional).
	 */
	public final Object what;
	
	/**
	 * Create an error message and initialize its location.
	 * 
	 * @param pos
	 * The location of the error.
	 * 
	 * @param what
	 * The AST element associated to this error message.
	 */
	public ErrorMessage(int pos, Object what) {
		this.location = null;
		this.pos = pos;
		this.what = what;
	}
	
	/**
	 * Create an error message and initialize its location.
	 * 
	 * @param loc
	 * The location of the error.
	 * 
	 * @param what
	 * The AST element associated to this error message.
	 */
	public ErrorMessage(SourceLocation loc, Object what) {
		this.location = loc;
		this.pos = -1;
		this.what = what;
	}
	
	/**
	 * Create an error message and associate it to the location of the node.
	 * In this case the position of the error is located in the position of the
	 * AST element.
	 * 
	 * @param what
	 * An AST element which the error is related to.
	 */
	public ErrorMessage(SourceElement element) {
		this(element.getLocation(), element);
	}
	
	/**
	 * Called to print the context (or more detailed information) of the error.
	 * 
	 * @param out
	 * The object use to output the information.
	 */
	public abstract void printDetails(IPrinter<Object> out);
	
	/**
	 * Override this method and implement the message associated with this
	 * error.
	 */
	@Override
	public abstract String toString();
}
