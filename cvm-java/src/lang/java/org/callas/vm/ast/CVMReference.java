package org.callas.vm.ast;


/**
 * Represents a long value.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 26, 2008
 *
 */
public class CVMReference extends CVMBuiltinValue {
	/**
	 * The Java value representing the value
	 * 
	 */
	public byte value;

	/**
	 * Creates an long value.
	 * 
	 * @param location
	 * @param value
	 */
	public CVMReference(byte value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "REF " + value;
	}
}
