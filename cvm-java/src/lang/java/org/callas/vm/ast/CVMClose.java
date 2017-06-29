package org.callas.vm.ast;


/**
 * 
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Apr 12, 2012
 * 
 */
public class CVMClose extends CVMStmt {
	private CVMClose() {
		// OK
	}
	@Override
	public String toString() {
		return "close";
	}
	public static final CVMClose CLOSE = new CVMClose();
}
