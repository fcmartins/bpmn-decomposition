package org.callas.vm.ast;


/**
 * Pop a value from the operands queue.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public class CVMPop extends CVMStmt {
	private CVMPop() {
		// OK
	}
	@Override
	public String toString() {
		return "pop";
	}
	public static final CVMPop POP = new CVMPop();
}
