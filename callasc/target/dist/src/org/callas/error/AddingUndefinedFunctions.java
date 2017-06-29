package org.callas.error;

import java.util.TreeSet;

import org.callas.absyn.Absyn;
import org.callas.absyn.types.CodeType;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.IPrinter;
import org.tyco.common.symbol.Symbol;

/**
 * Tried installing a function in a sensor that was not defined.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class AddingUndefinedFunctions extends ErrorMessage {

	private CodeType sensorCodeType;
	private CodeType moduleType;
	private TreeSet<Symbol> undefinedFunctions;
	private TreeSet<Symbol> redeclaredFunctions;

	public AddingUndefinedFunctions(Absyn where, CodeType sensorCodeType, CodeType moduleType) {
		super(where.getSourceLocation(), null);
		this.sensorCodeType = sensorCodeType;
		this.moduleType = moduleType;
		this.undefinedFunctions = new TreeSet<Symbol>(sensorCodeType.functions.keySet());
		this.undefinedFunctions.removeAll(moduleType.functions.keySet());
		this.redeclaredFunctions = new TreeSet<Symbol>(sensorCodeType.functions.keySet());
		this.redeclaredFunctions.retainAll(moduleType.functions.keySet());
		
	}

	@Override
	public void printDetails(IPrinter<Object> out) {
		for (Symbol label : redeclaredFunctions) {
			out.print("Function type of sensor:\n");
			out.print(sensorCodeType.functions.get(label));
			out.print("\n");
			out.print("Function type of the installed code:\n");
			out.print(moduleType.functions.get(label));
			out.print("\n");
		}
	}

	@Override
	public String toString() {
		String msg = "";
		if (undefinedFunctions.size() > 0) {
			msg = String.format("Installing functions %s in the sensor " +
					"that are not defined in the sensor interface.", undefinedFunctions);
		}
		if (redeclaredFunctions.size() > 0) {
			msg = String.format("Installing functions %s in the sensor " +
					"with a different type signature.", redeclaredFunctions);
		}
		return msg;
	}

}
