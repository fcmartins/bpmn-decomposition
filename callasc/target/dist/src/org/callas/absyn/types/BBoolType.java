package org.callas.absyn.types;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Represents the type of a boolean value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 2, 2008
 *
 */
public class BBoolType extends BuiltinType {

	public BBoolType(SourceLocation loc) {
		super(loc);
	}
	
	private BBoolType() {
		super();
	}
	
	public static final BBoolType BOOL_TYPE = new BBoolType();

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "bool";
	}
}
