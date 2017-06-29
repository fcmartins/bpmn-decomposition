package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * A Let process.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public class Branch extends CallasProcess {
	/**
	 * The condition under test.
	 */
	public final CallasValue condition;
	/**
	 * The process executed upon success.
	 */
	public final CallasProcess thenProc;
	/**
	 * The process executed upon failure.
	 */
	public final CallasProcess elseProc;

	/**
	 * @param loc
	 *            The source code location of this node.
	 * @param condition
	 *            The condition under test.
	 * @param thenProc
	 *            The process executed upon success.
	 * @param elseProc
	 *            The process executed upon failure.
	 */
	public Branch(SourceLocation loc, CallasValue condition,
			CallasProcess thenProc, CallasProcess elseProc) {
		super(loc);
		this.condition = condition;
		this.thenProc = thenProc;
		this.elseProc = elseProc;
	}

	/**
	 * @param condition
	 *            The condition under test.
	 * @param thenProc
	 *            The process executed upon success.
	 * @param elseProc
	 *            The process executed upon failure.
	 */
	public Branch(CallasValue condition, CallasProcess thenProc,
			CallasProcess elseProc) {
		super(null);
		this.condition = condition;
		this.thenProc = thenProc;
		this.elseProc = elseProc;
	}

	@Override
	public String toString() {
		return "if (" + condition + ") {" + thenProc + "} else {" + elseProc
				+ "}";
	}
}
