package org.callas.vm.ast;


/**
 * Syntax Tree representation of a function call
 */

public class CVMTimer extends CVMStmt {
	private CVMTimer() {
	}
	
	@Override
	public String toString() {
		return "timer";
	}

	public static final CVMTimer TIMER = new CVMTimer();
}
