package org.callas.vm.ast;


/**
 * Synchronously wait for a call from the network.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 5, 2009
 * 
 */
public class CVMReceive extends CVMStmt {
	private CVMReceive() {
		// OK
	}
	@Override
	public String toString() {
		return "receive";
	}
	public static final CVMReceive RECEIVE = new CVMReceive();
}
