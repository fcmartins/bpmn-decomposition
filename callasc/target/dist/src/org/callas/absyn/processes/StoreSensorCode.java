package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Stores the code in the sensor.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 16, 2009
 * 
 */
public class StoreSensorCode extends CallasProcess {
	/**
	 * The code being that gets stored in the sensor.
	 */
	public final CallasValue code;

	/**
	 * An utility constructor that discards the source code location and expects
	 * the code to be stored.
	 * 
	 * @param code
	 *            The code being that gets stored in the sensor.
	 */
	public StoreSensorCode(CallasValue code) {
		super();
		this.code = code;
	}

	/**
	 * The most general constructor that expects the source code location and
	 * the code to be stored.
	 * 
	 * @param sourceLocation
	 *            The location of this node in the source code.
	 * @param code
	 *            The code being that gets stored in the sensor.
	 */
	public StoreSensorCode(SourceLocation sourceLocation, CallasValue code) {
		super(sourceLocation);
		this.code = code;
	}

	@Override
	public String toString() {
		return "store " + code;
	}
}
