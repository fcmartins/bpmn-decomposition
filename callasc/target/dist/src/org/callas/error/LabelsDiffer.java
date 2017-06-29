package org.callas.error;

import java.util.Set;
import java.util.TreeSet;

import org.callas.absyn.processes.Code;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.IPrinter;
import org.tyco.common.symbol.Symbol;

/**
 * The labels of two modules differ.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class LabelsDiffer extends ErrorMessage {

	private Code module;

	/**
	 * @param loc
	 * @param what
	 */
	public LabelsDiffer(Code module) {
		super(module.getSourceLocation(), null);
		this.module = module;
	}

	@Override
	public void printDetails(IPrinter<Object> out) {
		// ok
	}

	@Override
	public String toString() {
		Set<Symbol> funcNames = new TreeSet<Symbol>(module.type.functions.keySet());
		String result = "";
		Set<Symbol> moreNames = new TreeSet<Symbol>(module.processes.keySet());
		moreNames.removeAll(funcNames);
		if (moreNames.size() > 0) {
			result += String.format("You defined extra functions %s, either redefine the module type or remove functions.", moreNames);
		}
		Set<Symbol> missingNames = new TreeSet<Symbol>(module.type.functions.keySet());
		missingNames.removeAll(module.processes.keySet());
		if (missingNames.size() > 0) {
			result += String.format("Must implement functions %s.", missingNames);
		}
		return result;
	}

}
