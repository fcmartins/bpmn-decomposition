package org.callas.absyn.types;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Represents the type of a double value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 2, 2008
 *
 */
public class BDoubleType extends BuiltinType {

	/**
	 * Default float type.
	 */
	public static final BDoubleType DOUBLE_TYPE = new BDoubleType();
	
	public BDoubleType(SourceLocation loc) {
		super(loc);
	}

	private BDoubleType() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "double";
	}
}
