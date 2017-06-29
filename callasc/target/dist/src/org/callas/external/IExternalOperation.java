package org.callas.external;

import java.util.List;
import java.util.Map;

import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.sensors.Sensor;
import org.callas.absyn.types.FunctionType;
import org.tyco.common.symbol.Symbol;

/**
 * An external operation provides type-checking and operational semantics.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 1, 2008
 * 
 */
public interface IExternalOperation {
	/**
	 * Returns the type of the operation.
	 * 
	 * @return The type of the operation.
	 */
	FunctionType getSignatureType();

	/**
	 * Executes this operation.
	 * 
	 * @param sensor
	 *            the sensor where this operation is being executed.
	 * 
	 * @param closure
	 *            the state of local variables available to this operation.
	 * 
	 * @param parameters
	 *            the run-time values being passed to the operation.
	 * @return the result of this external operation.
	 */
	CallasProcess execute(Sensor sensor, Map<Symbol, CallasValue> closure,
			List<CallasValue> parameters);
}
