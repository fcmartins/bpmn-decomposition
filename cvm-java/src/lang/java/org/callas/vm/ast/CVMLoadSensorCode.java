package org.callas.vm.ast;


/**
 * Represents the value <code>sensor</code>.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 4, 2009
 * 
 */
public class CVMLoadSensorCode extends CVMStmt {
	private CVMLoadSensorCode() {
		// OK
	}

	@Override
	public String toString() {
		return "loadb";
	}
	
	public static CVMLoadSensorCode LOADB = new CVMLoadSensorCode();
}
