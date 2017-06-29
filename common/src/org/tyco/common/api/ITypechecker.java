package org.tyco.common.api;


import org.tyco.common.errorMsg.ErrorMessagesException;

/**
 * The interface that embodies typechecking.
 * 
 * An instance of this class is a type system that typechecks a given value and
 * returns its type.
 * 
 * We assume that if there are typing environment are given in the constructor,
 * not on the method signature.
 * 
 * @param <V>
 *            the class of the value being typechecked
 * @param <V>
 *            the class that represents the result of typechecking: the value's
 *            type
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Feb 12, 2010
 * 
 */
public interface ITypechecker<V, T> {
	/**
	 * Typechecks the given Callas process.
	 * 
	 * @param proc
	 *            The process being typechecked.
	 * @return The type of the process.
	 * @throws ErrorMessagesException
	 *             Thrown if the process is not well-typed.
	 */
	T typecheck(V value) throws ErrorMessagesException;
}
