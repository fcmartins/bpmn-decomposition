package org.callas.vm.ast;


/**
 * Loads a value from the local environment.
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 18, 2009
 *
 */
public class CVMLoadModule extends CVMStmt {

	/**
	 * @param index
	 */
	private CVMLoadModule() {
	}
	
	@Override
	public String toString() {
		return "loadm";
	}
	
	public static final CVMLoadModule LOADM = new CVMLoadModule();
}
