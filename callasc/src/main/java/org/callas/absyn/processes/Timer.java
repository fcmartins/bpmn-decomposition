package org.callas.absyn.processes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.callas.absyn.processes.CallasValue;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.symbol.Symbol;

/**
 * A timed call.
 */
public class Timer extends CallasProcess {
	/**
	 * The module.
	 */
	public final CallasValue module;
	
	/**
	 * The name of the function.
	 */
	public final Symbol functionName;

	/**
	 * List of parameters.
	 */
	public final List<CallasValue> arguments;

	/**
	 * The every <code>every</code> time units this call is fired.
	 */
	public final CallasValue every;
	
	/**
	 * Creates a call process discarding the source code location.
	 * 
	 * @param module
	 * @param functionName
	 * @param parameters
	 */
	public Timer(SourceLocation loc, CallasValue module, Symbol functionName,
			List<CallasValue> parameters, CallasValue every) {
		super(loc);
		this.module = module;
		this.functionName = functionName;
		this.arguments = Collections.unmodifiableList(new ArrayList<CallasValue>(parameters));
		this.every = every;
	}

	/**
	 * Creates a call process using the function name of type string and
	 * specifying the parameters as a var-array.
	 * 
	 * @param module
	 * @param functionName
	 * @param values
	 */
	public Timer(CallasValue module, String functionName, CallasValue every, CallasValue... values) {
		super();
		this.module = module;
		this.functionName = Symbol.symbol(functionName);
		this.arguments = Collections.unmodifiableList(Arrays.asList(values));
		this.every = every;
	}
	
	@Override
	public String toString() {
		return "" + module + "." + functionName + arguments + " every " + every;
	}

}
