package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Represents a long integer value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 26, 2008
 *
 */
public class BLong extends BuiltinValue {
	/**
	 * The Java value representing the value
	 * 
	 */
	public final long value;

	/**
	 * Creates a long integer value at a certain source location.
	 * 
	 * @param location
	 * @param value
	 */
	public BLong(SourceLocation location, long value) {
		super(location);
		this.value = value;
	}

	/**
	 * Creates a new long integer, discarding the source code location (-1).
	 * 
	 * @param value
	 */
	public BLong(long value) {
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
