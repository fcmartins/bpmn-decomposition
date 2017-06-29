package org.callas.external;

import static org.callas.absyn.types.BLongType.LONG_TYPE;

import java.util.List;
import java.util.Map;

import org.callas.absyn.processes.BLong;
import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.sensors.Sensor;
import org.callas.absyn.types.FunctionType;
import org.tyco.common.symbol.Symbol;

/**
 * Gets the current time.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class GetTimeOperation implements IExternalOperation {

	private final static FunctionType TYPE = new FunctionType(LONG_TYPE);

	public CallasValue execute(Sensor sensor, Map<Symbol, CallasValue> closure,
			List<CallasValue> parameters) {
		// TODO: convert this to a BLong
		return new BLong((int) System.currentTimeMillis());
	}

	public FunctionType getSignatureType() {
		return TYPE;
	}
}
