package org.callas.error;

import org.callas.absyn.processes.Variable;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.IPrinter;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.symbol.Symbol;

/**
 * Issued when a certain symbol is unknown.
 * 
 * @author Tiago Cogumbreiro
 *
 */
public class UndefinedSymbol extends ErrorMessage {

	/**
	 * The category of the undefined symbol, e.g. a type, a variable, etc.
	 */
	private final String object;
	
	/**
	 * The name of the symbol that is undefined.
	 */
	public final Symbol name;

	private final String extraMessage;

	private UndefinedSymbol(SourceLocation loc, Symbol name, String object, String extraMessage) {
		super(loc, null);
		this.name = name;
		this.object = object;
		this.extraMessage = extraMessage.length() > 0 ? " " + extraMessage : "";
	}
	
	@Override
	public void printDetails(IPrinter<Object> out) {
		// OK
	}

	@Override
	public String toString() {
		return "Undefined " + object + extraMessage + ": " + name;
	}
	
	public static UndefinedSymbol undefinedVariable(Variable var) {
		return undefinedVariable(var, "");
	}
	
	public static UndefinedSymbol undefinedVariable(Variable var, String extraMessage) {
		return new UndefinedSymbol(var.getSourceLocation(), var.name, "variable", extraMessage);
	}
	
	public static UndefinedSymbol undefinedType(SourceLocation loc, Symbol name) {
		return undefinedType(loc, name, "");
	}
	public static UndefinedSymbol undefinedType(SourceLocation loc, Symbol name, String extraMessage) {
		return new UndefinedSymbol(loc, name, "type", extraMessage);
	}

	public static UndefinedSymbol undefinedLabel(SourceLocation loc, Symbol name) {
		return undefinedLabel(loc, name, "");
	}
	
	public static UndefinedSymbol undefinedLabel(SourceLocation loc, Symbol name, String extraMessage) {
		return new UndefinedSymbol(loc, name, "label", extraMessage);
	}
}
