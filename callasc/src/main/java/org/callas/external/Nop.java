package org.callas.external;

import java.util.List;
import java.util.Map;

import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.sensors.Sensor;
import org.callas.absyn.types.FunctionType;
import org.tyco.common.symbol.Symbol;

/**
 * This is an operation that should only be used in typechecking. Running this
 * operation makes the interpreter crash.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 13, 2009
 * 
 */
public class Nop implements IExternalOperation {

	private final FunctionType type;

	public Nop(FunctionType type) {
		this.type = type;
	}

	public CallasProcess execute(Sensor sensor,
			Map<Symbol, CallasValue> closure, List<CallasValue> parameters) {
		throw new UnsupportedOperationException();
	}

	public FunctionType getSignatureType() {
		return type;
	}

}
