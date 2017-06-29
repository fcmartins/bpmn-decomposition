package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Represents process 'load' that loads the code stored in the sensor.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 16, 2009
 * 
 */
public class LoadSensorCode extends CallasProcess {

	private LoadSensorCode() {
		super();
	}

	/**
	 * The constructor that expects the source code location.
	 * 
	 * @param sourceLocation
	 *            The location of this node in the source code.
	 */
	public LoadSensorCode(SourceLocation sourceLocation) {
		super(sourceLocation);
	}

	@Override
	public String toString() {
		return "load";
	}

	/**
	 * The singleton load object.
	 */
	public static final LoadSensorCode LOAD = new LoadSensorCode();
}
