package org.callas.absyn.types;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.symbol.Symbol;

/**
 * Represents the type of an anonymous module.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 2, 2008
 *
 */
public class CodeType extends CallasType {
	/**
	 * Represents the empty module type with no associated location.
	 */
	@SuppressWarnings("unchecked")
	public static final CodeType NIL_TYPE = new CodeType(Collections.EMPTY_MAP);
	
	public final Map<Symbol, FunctionType> functions;

	@SuppressWarnings("unchecked")
	public CodeType(SourceLocation loc) {
		this(loc, Collections.unmodifiableMap(Collections.EMPTY_MAP));
	}
	
	public CodeType(SourceLocation loc, Map<Symbol, FunctionType> types) {
		super(loc);
		this.functions = Collections.unmodifiableMap(new TreeMap<Symbol, FunctionType>(types));
	}

	public CodeType(Map<Symbol, FunctionType> types) {
		super();
		this.functions = Collections.unmodifiableMap(new TreeMap<Symbol, FunctionType>(types));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return functions.toString();
	}
}
