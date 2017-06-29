package org.callas.absyn.processes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.tyco.common.symbol.Symbol;

/**
 * Syntax Tree representation of a function call
 */
public class Call extends CallasProcess {
	/**
	 * The name of the function.
	 */
	public final Symbol functionName;

	/**
	 * The module.
	 */
	public final CallasValue module;

	/**
	 * List of parameters.
	 */
	public final List<CallasValue> arguments;
	
	/**
	 * Creates a call process discarding the source code location.
	 * 
	 * @param module
	 * @param functionName
	 * @param parameters
	 */
	public Call(CallasValue module, Symbol functionName,
			List<CallasValue> parameters) {
		super(module.getSourceLocation());
		this.module = module;
		this.functionName = functionName;
		this.arguments = Collections.unmodifiableList(new ArrayList<CallasValue>(parameters));
	}

	/**
	 * Creates a call process using the function name of type string and
	 * specifying the parameters as a var-array.
	 * 
	 * @param module
	 * @param functionName
	 * @param values
	 */
	public Call(CallasValue module, String functionName, CallasValue... values) {
		super(module.getSourceLocation());
		this.module = module;
		this.functionName = Symbol.symbol(functionName);
		this.arguments = Collections.unmodifiableList(Arrays.asList(values));
	}
	
	@Override
	public String toString() {
		return module + "." + functionName + arguments;
	}
}
