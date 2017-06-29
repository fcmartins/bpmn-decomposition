package org.callas.vm.ast;


/**
 * Copies a value on the operands queue.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public class CVMDup extends CVMStmt {
	private CVMDup() {
		// OK
	}
	@Override
	public String toString() {
		return "dup";
	}
	public static final CVMDup DUP = new CVMDup();
}
