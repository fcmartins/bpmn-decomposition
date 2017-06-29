package org.callas.util;

import org.callas.absyn.types.CallasType;

public class DifferentTypesException extends Exception {
	private static final long serialVersionUID = -5917584768800767689L;
	public final CallasType type1;
	public final CallasType type2;

	public DifferentTypesException(String message, CallasType type1, CallasType type2) {
		super(message);
		this.type1 = type1;
		this.type2 = type2;
	}
	
	public DifferentTypesException(String message, DifferentTypesException cause, CallasType type1, CallasType type2) {
		super(message, cause);
		this.type1 = type1;
		this.type2 = type2;
	}
}
