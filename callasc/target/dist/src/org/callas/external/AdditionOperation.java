package org.callas.external;

import java.util.List;
import java.util.Map;

import org.callas.absyn.processes.BLong;
import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.sensors.Sensor;
import org.callas.absyn.types.FunctionType;
import org.tyco.common.symbol.Symbol;

import static org.callas.absyn.types.BLongType.LONG_TYPE;

/**
 * Add two integer.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class AdditionOperation implements IExternalOperation {

	private static final FunctionType TYPE = new FunctionType(LONG_TYPE,
			LONG_TYPE, LONG_TYPE);

	public CallasProcess execute(Sensor sensor,
			Map<Symbol, CallasValue> closure, List<CallasValue> parameters) {
		BLong left = (BLong) parameters.get(0);
		BLong right = (BLong) parameters.get(1);
		return new BLong(left.value + right.value);
	}

	public FunctionType getSignatureType() {
		return TYPE;
	}

}
