package org.callas.absyn.types;

import org.callas.absyn.Absyn;
import org.tyco.common.errorMsg.SourceLocation;

/**
 * The root class of all types.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 2, 2008
 *
 */
public abstract class CallasType extends Absyn {

	/**
	 * Creates a type given the location of the source code.
	 * 
	 * @param location
	 *            The location on the source code this node was parsed.
	 */
	public CallasType(SourceLocation location) {
		super(location);
	}

	/**
	 * Discards the location of the source code.
	 */
	public CallasType() {
		super(null);
	}
}
