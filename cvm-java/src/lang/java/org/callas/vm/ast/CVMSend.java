package org.callas.vm.ast;


/**
 * Asynchronous remote call to a function in a module.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 5, 2009
 * 
 */
public class CVMSend extends CVMStmt {
	private CVMSend() {
		// OK
	}
	@Override
	public String toString() {
		return "send";
	}
	public static final CVMSend SEND = new CVMSend();
}
