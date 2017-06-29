package org.callas.vm.ast;


/**
 * The base class for all processes.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public abstract class CVMStmt {
	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}
}
