package org.callas.external;

import static org.callas.absyn.types.CodeType.NIL_TYPE;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.callas.absyn.processes.BString;
import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.processes.Code;
import org.callas.absyn.sensors.Sensor;
import org.callas.absyn.types.CallasType;
import org.callas.absyn.types.FunctionType;
import org.tyco.common.symbol.Symbol;

/**
 * This is a generic print operation that operates over a list of values (which
 * will be separated by a white space and delimited by a '\n').
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 1, 2008
 * 
 */
public class PrintOperation implements IExternalOperation {

	private final PrintStream stream;
	private final FunctionType functionType;

	/**
	 * The output stream where the values will be printed to.
	 * 
	 * @param stream
	 */
	public PrintOperation(CallasType argType, PrintStream stream) {
		this.functionType = new FunctionType(NIL_TYPE, argType);
		this.stream = stream;
	}

	public CallasValue execute(Sensor sensor, Map<Symbol, CallasValue> closure, List<CallasValue> parameters) {
		CallasValue callasValue = parameters.get(0);
		if (callasValue instanceof BString) {
			stream.print(((BString) callasValue).value);
		} else {
			stream.print(callasValue);
		}
		return Code.NIL;
	}

	public FunctionType getSignatureType() {
		return functionType;
	}
}
