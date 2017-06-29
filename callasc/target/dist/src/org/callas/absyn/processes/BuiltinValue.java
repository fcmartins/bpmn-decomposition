package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * A built-in value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public abstract class BuiltinValue extends CallasValue {

	/**
	 * Creates a node by providing its source location.
	 * 
	 * @param sourceLocation
	 */
	public BuiltinValue(SourceLocation sourceLocation) {
		super(sourceLocation);
	}

	/**
	 * Utility constructor that omits the source code location.
	 */
	public BuiltinValue() {
		super();
	}
}
