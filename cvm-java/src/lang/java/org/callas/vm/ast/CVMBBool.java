package org.callas.vm.ast;


/**
 * A boolean value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 *
 */
public class CVMBBool extends CVMBuiltinValue {
	/**
	 * The Java representation of this value.
	 */
	public final boolean value;

	/**
	 * Create a value discarding its source code location.
	 * @param value
	 */
	public CVMBBool(boolean value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "BOOL " + (value ? "1" : "0");
	}
}
