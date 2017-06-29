package org.callas.vm.ast;


/**
 * Loads a value from the local environment.
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 18, 2009
 *
 */
public class CVMLoadConstant extends CVMStmt {
	/**
	 * Load the value from this index.
	 */
	public final byte index;

	/**
	 * @param index
	 */
	public CVMLoadConstant(byte index) {
		this.index = index;
	}
	
	@Override
	public String toString() {
		return "loadc " + index;
	}	
}
