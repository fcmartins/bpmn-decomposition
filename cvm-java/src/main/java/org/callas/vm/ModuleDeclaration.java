package org.callas.vm;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * The declaration from which modules are created.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date May 20, 2009
 * 
 */
public class ModuleDeclaration {
	/**
	 * Holds the declaration of each function.
	 */
	private final Hashtable functionDeclarations;
	/**
	 * Holds the free variables found in this module.
	 */
	private final Hashtable freeVars;

	/**
	 * Creates a module declaration from an array of function declarations.
	 * 
	 * The array of declarations and of sizes must be ordered in a way that the
	 * i-th function declaration in <code>decs</code> has the number of free
	 * vars available in the i-th position of array <code>freeVars</code>.
	 * 
	 * @param decs
	 *            The function declarations that constitute this module.
	 * @param freeVars
	 *            The size of the free variables of each function declaration.
	 */
	public ModuleDeclaration(FunctionDeclaration[] decs, byte[] freeVars) {
		this.functionDeclarations = new Hashtable();
		this.freeVars = new Hashtable();
		for (int i = 0; i < decs.length; i++) {
			FunctionDeclaration func = decs[i];
			this.functionDeclarations.put(func.name, func);
			this.freeVars.put(func.name, new Byte(freeVars[i]));
		}
	}

	/**
	 * Return the number of functions in the module.
	 * 
	 * @return the number of functions
	 */
	public byte size() {
		return (byte) functionDeclarations.size();
	}

	/**
	 * Define the module's functions.
	 * 
	 * @param functionDeclarations
	 */
	private static void putAll(Hashtable target, Enumeration decls) {
		// populate the map
		while (decls.hasMoreElements()) {
			FunctionDeclaration func = (FunctionDeclaration) decls
					.nextElement();
			target.put(func.name, func);
		}
	}

	/**
	 * Lookup a function in the module. Used in LOADM.
	 * 
	 * @param functionName
	 * @return function
	 */
	public FunctionDeclaration lookup(String functionName) {
		Object func = functionDeclarations.get(functionName);
		if (func == null) {
			throw new IllegalArgumentException(functionName);
		}
		return (FunctionDeclaration) func;
	}

	/**
	 * Creates an array holding the function names available in this module
	 * declaration.
	 * 
	 * @return A type safe copy of the function names.
	 */
	public String[] getNames() {
		String[] names = new String[functionDeclarations.size()];
		int index = 0;
		for (Enumeration en = functionDeclarations.keys(); en.hasMoreElements(); index++) {
			names[index] = (String) en.nextElement();
		}
		return names;
	}

	/**
	 * Gets the environment size.
	 * 
	 * @param functionName
	 * @return
	 */
	public byte getFreeVarsCount(String functionName) {
		return ((Byte) freeVars.get(functionName)).byteValue();
	}

	/**
	 * Creates a module instance from a module declaration (with no free
	 * variables).
	 * 
	 * @return
	 */
	public Module create() {
		Function[] funcs = new Function[functionDeclarations.size()];
		Enumeration decls = functionDeclarations.elements();
		for (int i = 0; decls.hasMoreElements(); i++) {
			FunctionDeclaration dec = (FunctionDeclaration) decls.nextElement();
			funcs[i] = new Function(dec, new Object[0]);
		}
		return new Module(funcs);
	}
}
