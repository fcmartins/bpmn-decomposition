package org.callas.vm.ast;


/**
 * Syntax Tree representation of a function call
 */

public class CVMReturn extends CVMStmt {
	private CVMReturn() {
		// OK
	}
	@Override
	public String toString() {
		return "return";
	}
	public static final CVMReturn RETURN = new CVMReturn();
}
