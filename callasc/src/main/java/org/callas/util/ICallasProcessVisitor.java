package org.callas.util;

import org.callas.absyn.processes.*;

/**
 * Represents a process visitor.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jan 9, 2009
 * 
 */
public interface ICallasProcessVisitor<T> {
	/**
	 * Visits a boolean.
	 * 
	 * @param bbool
	 *            The process to visit.
	 * @return
	 */
	T caseBBool(BBool bbool);

	/**
	 * Visits a float.
	 * 
	 * @param bloat
	 *            The process to visit.
	 */
	T caseBDouble(BDouble bdouble);

	/**
	 * Visits an integer.
	 * 
	 * @param blong
	 *            The process to visit.
	 */
	T caseBLong(BLong blong);

	/**
	 * Visits a string.
	 * 
	 * @param bstring
	 *            The process to visit.
	 * @return
	 */
	T caseBString(BString bstring);

	/**
	 * Visits a binary operation.
	 * 
	 * @param binop
	 *            The process to visit.
	 * @return
	 */
	T caseBinaryOperation(BinaryOperation binop);

	/**
	 * Visits an unary operation.
	 * 
	 * @param uop
	 *            The process to visit.
	 * @return
	 */
	T caseUnaryOperation(UnaryOperation uop);

	/**
	 * Visits a variable.
	 * 
	 * @param var
	 *            The process to visit.
	 * @return
	 */
	T caseVariable(Variable var);

	/**
	 * Visits a module.
	 * 
	 * @param mod
	 *            The process to visit.
	 */
	T caseCode(Code code);

	/**
	 * Visits a process abstraction call.
	 * 
	 * @param call
	 *            The process to visit.
	 * @return
	 */
	T caseCall(Call call);

	/**
	 * Visits an external call.
	 * 
	 * @param external
	 *            The process to visit.
	 * @return
	 */
	T caseExtern(Extern external);

	/**
	 * Visits a kill element
	 * 
	 * @param kill
	 * @return
	 */
	T caseKill(Kill kill);
	
	/**
	 * Visits a timer.
	 * 
	 * @param timer
	 * @return
	 */
	T caseTimer(Timer timer);

	/**
	 * Visits a send element.
	 * 
	 * @param send
	 * @return
	 */
	T caseSend(Send send);

	/**
	 * Visits a receive element.
	 * 
	 * @param recv
	 * @return
	 */
	T caseReceive(Receive recv);

	/**
	 * Visits an install process.
	 * 
	 * @param install
	 *            The process to visit.
	 * @return
	 */
	T caseUpdate(Update update);

	/**
	 * Visits a let.
	 * 
	 * @param let
	 *            The process to visit.
	 * @return
	 */
	T caseLet(Let let);

	/**
	 * Visits a conditional branch.
	 * 
	 * @param branch
	 * @return
	 */
	T caseBranch(Branch branch);

	/**
	 * Visits a load process.
	 * 
	 * @param load
	 * @return
	 */
	T caseLoadSensorCode(LoadSensorCode load);

	/**
	 * Visits a store process.
	 * 
	 * @param store
	 * @return
	 */
	T caseStoreSensorCode(StoreSensorCode store);
	
	/**
	 * Visits a open process.
	 * 
	 * @param open
	 * @return
	 */
	
	T caseOpen(Open open);
	
	/**
	 * Visits a close process.
	 * 
	 * @param close 
	 * @return
	 */

	T caseClose(Close close);
}
