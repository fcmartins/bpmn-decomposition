package org.callas.vm.ast;


/**
 * Represents the value <code>sensor</code>.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 4, 2009
 * 
 */
public class CVMStoreSensorCode extends CVMStmt {
	private CVMStoreSensorCode() {
		// ok
	}
	@Override
	public String toString() {
		return "storeb";
	}
	public static final CVMStoreSensorCode STOREB = new CVMStoreSensorCode();
}
