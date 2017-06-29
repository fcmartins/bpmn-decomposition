package org.callas.vm.ast;

/**
 * 
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Apr 10, 2012
 * 
 */
public class CVMKill extends CVMStmt {
	private CVMKill() {
		// OK
	}
	@Override
	public String toString() {
		return "kill";
	}
	public static final CVMKill KILL = new CVMKill();
}