package org.callas.vm.ast;


/**
 * A double value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 *
 */
public class CVMBDouble extends CVMBuiltinValue {
	/**
	 * The Java representation of the value.
	 */
	public final double value;

	/**
	 * Creates a double representation at a certain source code location.
	 * @param value
	 */
	public CVMBDouble(double value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DOUBLE " + value;
	}
}
