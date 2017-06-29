package org.callas.absyn.processes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * The base class for all values.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public abstract class CallasValue extends CallasProcess {
	/**
	 * Creates a node by providing its source location.
	 * 
	 * @param sourceLocation
	 */
	public CallasValue(SourceLocation sourceLocation) {
		super(sourceLocation);
	}
	
	/**
	 * Utility constructor that accepts a null location.
	 */
	public CallasValue() {
		super();
	}

	/**
	 * Creates a mutable list of values.
	 * 
	 * @param values
	 * @return
	 */
	public static List<CallasValue> asList(CallasValue... values) {
		ArrayList<CallasValue> procs = new ArrayList<CallasValue>();
		Collections.addAll(procs, values);
		return procs;
	}
}
