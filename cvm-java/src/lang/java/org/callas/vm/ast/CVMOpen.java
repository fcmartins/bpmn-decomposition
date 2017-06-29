package org.callas.vm.ast;


/**
 * 
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Apr 12, 2012
 * 
 */
public class CVMOpen extends CVMStmt {
	private CVMOpen() {
		// OK
	}
	@Override
	public String toString() {
		return "open";
	}
	public static final CVMOpen OPEN = new CVMOpen();
}
