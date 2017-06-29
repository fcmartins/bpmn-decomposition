package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * A double value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 *
 */
public class BDouble extends BuiltinValue {
	/**
	 * The Java representation of the value.
	 */
	public final double value;

	/**
	 * Creates a double representation at a certain source code location.
	 * @param location
	 * @param value
	 */
	public BDouble(SourceLocation location, double value) {
		super(location);
		this.value = value;
	}

	/**
	 * Creates a double value discarding its source code location.
	 * @param value
	 */
	public BDouble(double value) {
		super();
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
