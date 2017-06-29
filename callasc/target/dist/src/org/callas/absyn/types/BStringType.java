package org.callas.absyn.types;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Represents the type of a string value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 2, 2008
 *
 */
public class BStringType extends BuiltinType {

	/**
	 * The default string type.
	 */
	public static final BStringType STRING_TYPE = new BStringType();

	public BStringType(SourceLocation loc) {
		super(loc);
	}

	private BStringType() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "string";
	}
}
