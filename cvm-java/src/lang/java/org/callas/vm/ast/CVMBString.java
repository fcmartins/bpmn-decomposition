package org.callas.vm.ast;

import org.tyco.common.util.Strings;

/**
 * A String value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 *
 */
public class CVMBString extends CVMBuiltinValue {
	/**
	 * The Java representation of the value.
	 */
	public String value;

	/**
	 * Creates a value at a certain source code location.
	 * @param value
	 */
	public CVMBString(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "STRING " + Strings.escape(value);
	}
}
