package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * A boolean value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 *
 */
public class BBool extends BuiltinValue {
	/**
	 * The Java representation of this value.
	 */
	public final boolean value;

	/**
	 * Create a value at a certain location.
	 * @param location
	 * @param value
	 */
	public BBool(SourceLocation location, boolean value) {
		super(location);
		this.value = value;
	}

	/**
	 * Create a value discarding its source code location.
	 * @param value
	 */
	public BBool(boolean value) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 *
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BBool other = (BBool) obj;
		if (value != other.value)
			return false;
		return true;
	}*/
}
