package org.callas.absyn.processes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.symbol.Symbol;

/**
 * The external operation.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 31, 2008
 * 
 */
public class Extern extends CallasProcess {

	/**
	 * The operation name.
	 */
	public final Symbol functionName;

	/**
	 * The parameters of the operation.
	 */
	public final List<CallasValue> arguments;

	/**
	 * Creates an external operation from a certain name and the paramenters
	 * given.
	 * 
	 * @param operation
	 * @param parameters
	 */
	public Extern(String operation, CallasValue... parameters) {
		this(null, Symbol.symbol(operation), Arrays.asList(parameters));
	}

	/**
	 * Creates an external operation from a certain name and the paramenters
	 * given.
	 * 
	 * @param operation
	 * @param parameters
	 *            The parameters are copied to a read-only list.
	 */
	public Extern(SourceLocation loc, Symbol operation,
			List<CallasValue> parameters) {
		super(loc);
		this.functionName = operation;
		this.arguments = Collections
				.unmodifiableList(new ArrayList<CallasValue>(parameters));
	}

	@Override
	public String toString() {
		return "extern " + functionName + arguments;
	}
}
