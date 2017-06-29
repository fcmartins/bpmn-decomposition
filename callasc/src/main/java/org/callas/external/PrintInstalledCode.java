package org.callas.external;

import java.util.List;
import java.util.Map;

import org.callas.absyn.processes.BString;
import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.sensors.Sensor;
import org.callas.absyn.types.BStringType;
import org.callas.absyn.types.FunctionType;
import org.tyco.common.symbol.Symbol;

/**
 * Print the local state of the sensor.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class PrintInstalledCode implements IExternalOperation {

	private static final FunctionType TYPE = new FunctionType(
			BStringType.STRING_TYPE);

	public CallasValue execute(Sensor sensor, Map<Symbol, CallasValue> closure,
			List<CallasValue> parameters) {
		
		return new BString(sensor.toString());
	}

	public FunctionType getSignatureType() {
		return TYPE;
	}
}
