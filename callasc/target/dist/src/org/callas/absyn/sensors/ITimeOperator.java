package org.callas.absyn.sensors;

import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.types.CallasType;
import org.tyco.common.errorMsg.ErrorMessagesException;

/**
 * Captures the operations on time values.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public interface ITimeOperator {
	/**
	 * Typechecks a time value.
	 * 
	 * @param value
	 * @param type
	 * @throws ErrorMessagesException
	 */
	void typecheck(CallasValue value, CallasType type)
			throws ErrorMessagesException;

	/**
	 * Addition between two time values.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	CallasValue plus(CallasValue left, CallasValue right);

	/**
	 * Increments one unit the given time value.
	 * 
	 * @param value
	 * @return
	 */
	CallasValue increment(CallasValue value);

	/**
	 * Checks if one value is smaller or equal than the other value.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	boolean lessEqualThan(CallasValue left, CallasValue right);

	/**
	 * Checks if two time values are equal.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	boolean areEqual(CallasValue left, CallasValue right);
}
