package org.callas.vm.ast;


/**
 * Loads a value from the local environment.
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 18, 2009
 *
 */
public class CVMLoad extends CVMStmt {
	/**
	 * Load the value from this index.
	 */
	public final byte index;

	/**
	 * @param index
	 */
	public CVMLoad(byte index) {
		this.index = index;
	}
	
	@Override
	public String toString() {
		return "load " + index;
	}	
}
