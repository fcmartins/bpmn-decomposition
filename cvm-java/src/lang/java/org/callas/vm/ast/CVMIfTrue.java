package org.callas.vm.ast;


/**
 * A Let process.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public class CVMIfTrue extends CVMStmt {
	/**
	 * The offset where to jump.
	 */
	public final int offset;

	public CVMIfTrue(int offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "iftrue " + offset;
	}
}
