package org.callas.vm.ast;


/**
 * Store instruction.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public class CVMStore extends CVMStmt {
	/**
	 * The slot index where the value is going to be stored.
	 */
	public final byte index;

	/**
	 * 
	 * @param index
	 */
	public CVMStore(byte index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "store " + index;
	}
}
