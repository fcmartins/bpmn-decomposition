package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.util.Strings;

/**
 * A String value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 *
 */
public class BString extends BuiltinValue {
	/**
	 * The Java representation of the value.
	 */
	public final String value;

	/**
	 * Creates a value at a certain source code location.
	 * @param location
	 * @param value
	 */
	public BString(SourceLocation location, String value) {
		super(location);
		this.value = value;
	}

	/**
	 * Creates a value discarding the source code location.
	 * @param value
	 */
	public BString(String value) {
		super();
		this.value = value;
	}

	@Override
	public String toString() {
		return Strings.escape(value);
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
		BString other = (BString) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}*/
}
