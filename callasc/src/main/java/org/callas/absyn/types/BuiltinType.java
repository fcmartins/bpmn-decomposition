package org.callas.absyn.types;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * The base class of all built-in types.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 2, 2008
 * 
 */
public abstract class BuiltinType extends CallasType {
	/**
	 * Creates a type given the location of the source code.
	 * 
	 * @param loc
	 *            The location on the source code this node was parsed.
	 */
	public BuiltinType(SourceLocation loc) {
		setSourceLocation(loc);
	}

	/**
	 * Discards the location of the source code.
	 */
	public BuiltinType() {
		super();
	}

	@Override
	public boolean equals(Object that) {
		return that != null && that.getClass().equals(this.getClass());
	}

}
