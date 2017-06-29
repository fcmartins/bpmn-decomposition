package org.callas.vm.ast;

public class CVMUpdate extends CVMStmt {
	private CVMUpdate() {
		// OK
	}
	@Override
	public String toString() {
		return "update";
	}
	public static final CVMUpdate UPDATE = new CVMUpdate();
}
