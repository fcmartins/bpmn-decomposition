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
 * A pseudo operation to get the mac address, which actually just prints the
 * hashcode of the given sensor.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class GetMacAdressOperation implements IExternalOperation {

	private static final FunctionType TYPE = new FunctionType(
			BStringType.STRING_TYPE);

	public CallasValue execute(Sensor sensor, Map<Symbol, CallasValue> closure,
			List<CallasValue> parameters) {
		return new BString(String.format("%x", sensor.hashCode()));
	}

	public FunctionType getSignatureType() {
		return TYPE;
	}

}
