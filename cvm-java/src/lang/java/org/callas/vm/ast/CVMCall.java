package org.callas.vm.ast;


/**
 * Syntax Tree representation of a function call
 */

public class CVMCall extends CVMStmt {
	private CVMCall() {
		// OK
	}
	@Override
	public String toString() {
		return "call";
	}
	public static final CVMCall CALL = new CVMCall();
}
