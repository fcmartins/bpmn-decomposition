package org.callas.vm.ast;


/**
 * A Let process.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public class CVMGoto extends CVMStmt {
	/**
	 * The offset where to jump.
	 */
	public final int offset;

	public CVMGoto(int offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "goto " + offset;
	}
}
