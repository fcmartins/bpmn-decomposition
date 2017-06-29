package org.callas.vm.ast;


/**
 * Swaps the two toplevel values of the operands queue.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public class CVMSwap extends CVMStmt {

	private CVMSwap() {
		// OK
	}
	
	@Override
	public String toString() {
		return "swap";
	}
	
	public static CVMSwap SWAP = new CVMSwap(); 
}
