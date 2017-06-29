package org.callas.absyn.types;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * The recursive type.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 30, 2009
 * 
 */
public class RecursiveType extends CallasType {
	/**
	 * The type variable that represents the recursive type.
	 */
	public final TypeVariable variable;
	/**
	 * The actual type.
	 */
	public final CallasType type;

	/**
	 * The standard constructor.
	 * 
	 * @param location
	 * @param variable
	 * @param type
	 */
	public RecursiveType(SourceLocation location, TypeVariable variable,
			CallasType type) {
		super(location);
		this.variable = variable;
		this.type = type;
	}

	/**
	 * Utility constructor that discards the source code location and accepts a
	 * variable as a string.
	 * 
	 * @param variable
	 * @param type
	 */
	public RecursiveType(String variable, CallasType type) {
		this(new TypeVariable(variable), type);
	}

	/**
	 * Utility constructor that discards the source code location.
	 * @param variable
	 * @param type
	 */
	public RecursiveType(TypeVariable variable, CallasType type) {
		super();
		this.variable = variable;
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "rec " + variable + "." + type;
	}
}
