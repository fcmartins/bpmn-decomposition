package org.callas.util;

import org.callas.absyn.types.*;

/**
 * Type visitor.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jan 9, 2009
 *
 */
public interface ICallasTypeVisitor<T> {
	/**
	 * Visit a boolean type.
	 * @param bbool The type to visit.
	 */
	T caseBBoolType(BBoolType bbool);
	
	/**
	 * Visit a double type.
	 * @param bdouble The type to visit.
	 */
	T caseBDoubleType(BDoubleType bdouble);
	
	/**
	 * Visit an long type.
	 * @param blong The type to visit.
	 */
	T caseBLongType(BLongType blong);
	
	/**
	 * Visit a string type.
	 * @param bstring The type to visit.
	 */
	T caseBStringType(BStringType bstring);
	
	/**
	 * Visit a sensor code type.
	 * @param codeType The type to visit.
	 */
	T caseCodeType(CodeType codeType);

	/**
	 * Visit a recursive type.
	 * @param recursiveType The type to visit.
	 */
	T caseRecursiveType(RecursiveType recursiveType);

	/**
	 * Visit a function type.
	 * @param functionType The type to visit.
	 */
	T caseFunctionType(FunctionType functionType);

	/**
	 * Visit a type variable.
	 * @param typeVariable The type to visit.
	 */
	T caseTypeVariable(TypeVariable typeVariable);
}
