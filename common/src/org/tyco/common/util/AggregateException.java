package org.tyco.common.util;

import java.util.List;

/**
 * An exception that is just a composition of various exception.
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 31, 2008
 *
 */
public class AggregateException extends Exception {

	private static final long serialVersionUID = 7590457134057086422L;
	
	private List<? extends Exception> exceptions;
	
	public AggregateException(List<? extends Exception> exceptions) {
		super();
		this.exceptions = exceptions;
	}

	public List<? extends Exception> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<? extends Exception> exceptions) {
		this.exceptions = exceptions;
	}
}
