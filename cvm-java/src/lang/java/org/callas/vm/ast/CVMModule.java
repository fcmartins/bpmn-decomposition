package org.callas.vm.ast;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.tyco.common.symbol.Symbol;
import org.tyco.common.util.Strings;

/**
 * An anonymous module.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 * 
 */
public class CVMModule extends CVMValue {
	
	/**
	 * Where the processes are kept.
	 */
	public final Map<Symbol, CVMFunction> functions;

	/**
	 * The default constructor.
	 * 
	 * @param isOpen
	 * @param functions
	 */
	public CVMModule(Map<Symbol, CVMFunction> functions) {
		this.functions = Collections.unmodifiableMap(new TreeMap<Symbol, CVMFunction>(functions));
	}

	@Override
	public String toString() {
		String result = "MODULE\n";
		for (Entry<Symbol, CVMFunction> entry : functions.entrySet()) {
			result += "FUNCTION " + Strings.escape(entry.getKey().toString());
			result += " " + entry.getValue();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static final CVMModule NIL = new CVMModule(Collections.EMPTY_MAP);
}