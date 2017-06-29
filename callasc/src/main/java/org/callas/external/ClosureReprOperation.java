package org.callas.external;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

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
public class ClosureReprOperation implements IExternalOperation {

	private static final FunctionType TYPE = new FunctionType(
			BStringType.STRING_TYPE);

	public CallasValue execute(Sensor sensor, Map<Symbol, CallasValue> closure,
			List<CallasValue> parameters) {

		TreeMap<String, String> result = new TreeMap<String, String>();

		for (Entry<Symbol, CallasValue> entry : closure.entrySet()) {
			result.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return new BString(result.toString());
	}

	public FunctionType getSignatureType() {
		return TYPE;
	}
}
