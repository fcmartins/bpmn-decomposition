package org.callas.absyn.processes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.callas.absyn.Absyn;
import org.tyco.common.errorMsg.SourceLocation;

/**
 * The base class for all processes.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public abstract class CallasProcess extends Absyn {
	/**
	 * Creates a node by providing its source location.
	 * 
	 * @param sourceLocation
	 *            the location in the source code.
	 */
	public CallasProcess(SourceLocation sourceLocation) {
		super(sourceLocation);
	}

	/**
	 * Utility constructor that accepts a null location.
	 */
	public CallasProcess() {
		super();
	}

	/**
	 * Creates a mutable list of processes.
	 * 
	 * @param processes
	 * @return
	 */
	public static List<CallasProcess> asList(CallasProcess... processes) {
		ArrayList<CallasProcess> procs = new ArrayList<CallasProcess>();
		Collections.addAll(procs, processes);
		return procs;
	}
}
