package org.callas.absyn.types;

import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.symbol.Symbol;

/**
 * A type variable.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 30, 2009
 * 
 */
public class TypeVariable extends CallasType implements Comparable<TypeVariable> {
	/**
	 * The name of the type variable.
	 */
	public final Symbol name;

	public TypeVariable(SourceLocation location, Symbol name) {
		super(location);
		this.name = name;
	}

	public TypeVariable(String name) {
		this(Symbol.symbol(name));
	}

	public TypeVariable(Symbol name) {
		super();
		this.name = name;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	public int compareTo(TypeVariable o) {
		return name.compareTo(o.name);
	}
}
