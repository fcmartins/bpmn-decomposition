package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Merges to modules into a new one, giving preferences to functions on the left.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public class Update extends CallasProcess {
	/**
	 * The module on the left.
	 */
	public final CallasValue left;
	/**
	 * The module on the right.
	 */
	public final CallasValue right;

	/**
	 * Creates a process in a certain target location.
	 * 
	 * @param location
	 *            The source code location.
	 * @param left
	 *            Install the module in <code>target</code> module.
	 * @param right
	 *            The <code>module</code> to be installed.
	 */
	public Update(SourceLocation loc, CallasValue left, CallasValue right) {
		super(loc);
		this.left = left;
		this.right = right;
	}

	/**
	 * Creates an install process discarding the source code location.
	 * 
	 * @param target
	 *            Install the module in <code>target</code> module.
	 * @param module
	 *            The <code>module</code> to be installed.
	 */
	public Update(CallasValue target, CallasValue module) {
		super();
		this.left = target;
		this.right = module;
	}

	@Override
	public String toString() {
		return "(" + left + "||" + right + ")";
	}
}
