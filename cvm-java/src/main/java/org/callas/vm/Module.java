package org.callas.vm;

import java.util.*;

/**
 * This class is the run-time representation of a Callas module
 * 
 * @author Pedro Gomes (prg@dcc.fc.up.pt)
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @since May 20, 2009
 * 
 */
public class Module {
	/**
	 * The table of functions.
	 */
	private final Hashtable functions;

	/**
	 * Create a module from an array of functions.
	 * 
	 */
	public Module(Function[] functions) {
		//this.index = index;
		this.functions = new Hashtable();
		for (int i = 0; i < functions.length; i++) {
			Function func = functions[i];
			this.functions.put(func.declaration.name, func);
		}
	}

	/**
	 * Create a new module.
	 * 
	 */
	private Module(Hashtable functions) {
		this.functions = functions;
	}

	/**
	 * Utility constructor for creating an empty module.
	 */
	public Module() {
		this(new Function[0]);
	}

	/**
	 * Define the module's functions.
	 * 
	 * @param functionDeclarations
	 */
	private static void putCopies(Hashtable target, Enumeration funcs) {
		// populate the map
		while (funcs.hasMoreElements()) {
			Function func = (Function) funcs.nextElement();
			target.put(func.declaration.name, new Function(func));
		}
	}

	/**
	 * Implements the update operator (+)
	 * 
	 * @param other
	 * @return
	 */
	public Module update(Module other) {
		Hashtable funcs = new Hashtable();
		putCopies(funcs, this.functions.elements());
		putCopies(funcs, other.functions.elements());
		return new Module(/*this.index, */funcs);
	}

	/**
	 * Lookup a function in the module.
	 * 
	 * @param functionName
	 * @return function
	 */
	public Function lookup(String functionName) {
		Object func = functions.get(functionName);
		if (func == null) {
			throw new IllegalArgumentException(functionName);
		}
		return (Function) func;
	}

	/**
	 * Creates an array holding the function names available in this module
	 * declaration.
	 * 
	 * @return A type safe copy of the function names.
	 */
	public String[] getNames() {
		String[] names = new String[functions.size()];
		int index = 0;
		for (Enumeration en = functions.keys(); en.hasMoreElements(); index++) {
			names[index] = (String) en.nextElement();
		}
		return names;
	}
}
