package org.callas.absyn.sensors;

import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.types.CallasType;
import org.tyco.common.errorMsg.ErrorMessagesException;

/**
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public interface IPositionOperator {
	/**
	 * Typechecks a position value.
	 * 
	 * @param value
	 * @param type
	 * @throws ErrorMessagesException
	 */
	void typecheck(CallasValue value, CallasType type)
			throws ErrorMessagesException;

	/**
	 * Checks if two positions are in range.
	 * 
	 * @param p1
	 *            The object of the function.
	 * @param p2
	 *            The target of the function.
	 * @return If p1 is in range of p2.
	 */
	boolean inRange(CallasValue p1, CallasValue p2);
}
