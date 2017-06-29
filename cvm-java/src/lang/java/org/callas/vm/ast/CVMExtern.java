package org.callas.vm.ast;


/**
 * This operation is not part of the calculus.
 * 
 * It is useful for interacting with external aspects of the sensor.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 31, 2008
 * 
 */
public class CVMExtern extends CVMStmt {
	private CVMExtern() {
		// OK
	}
	@Override
	public String toString() {
		return "extern";
	}
	public static final CVMExtern EXTERN = new CVMExtern();
}
