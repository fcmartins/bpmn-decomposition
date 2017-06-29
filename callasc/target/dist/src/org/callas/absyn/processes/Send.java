package org.callas.absyn.processes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.symbol.Symbol;

/**
 * Asynchronous remote call placed in the network.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Aug 5, 2009
 * 
 */
public class Send extends CallasProcess {
	/**
	 * The function name.
	 */
	public final Symbol functionName;
	/**
	 * The arguments of the function.
	 */
	public final List<CallasValue> arguments;
	/**
	 * The channel for the connection.
	 */
	public final CallasValue channel;

	/**
	 * The default constructor.
	 * 
	 * @param sourceLocation
	 *            The source code location.
	 * @param function
	 *            The function name.
	 * @param arguments
	 *            The arguments of the function.
	 */
	
	public Send(SourceLocation sourceLocation, CallasValue channel, Symbol function,
			List<CallasValue> arguments) {
		super(sourceLocation);
		this.functionName = function;
		this.arguments = arguments;
		this.channel = channel;
	}

	/**
	 * Utility constructor that discards the source code location.
	 * 
	 * @param function
	 *            The function name.
	 * @param arguments
	 *            The arguments of the function.
	 */

	public Send(CallasValue channel, Symbol function, List<CallasValue> arguments) {
		super();
		this.functionName = function;
		this.arguments = Collections
				.unmodifiableList(new ArrayList<CallasValue>(arguments));
		this.channel = channel;
	}

	/**
	 * Another utility constuctor that expects a string and var-args for the
	 * arguments.
	 * 
	 * @param function
	 *            The function name.
	 * @param arguments
	 *            The arguments of the function.
	 */
	public Send(CallasValue channel, String function, CallasValue... arguments) {
		this(channel, Symbol.symbol(function), Arrays.asList(arguments));
	}


	@Override
	public String toString() {
		return "select" + channel + "send " + functionName + arguments;
	}
}