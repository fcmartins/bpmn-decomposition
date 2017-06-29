package org.callas.absyn.types;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Represents the type of a long integer value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 2, 2008
 *
 */
public class BLongType extends BuiltinType {

	/**
	 * Default integer type.
	 */
	public static final BLongType LONG_TYPE = new BLongType();
	
	public BLongType(SourceLocation loc) {
		super(loc);
	}

	private BLongType() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "long";
	}
}
