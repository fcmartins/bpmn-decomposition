package org.callas.semant;

import java.util.List;

import org.callas.absyn.processes.*;
import org.callas.absyn.types.*;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.symtable.Scope;

/**
 * An interface for the process typing rules.
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 *
 */
public interface IProcessTypingRules {
	CallasType tBinaryOperation(Scope<Symbol, CallasType> moduleScope, BinaryOperation binop);
	CallasType tUnaryOperation(Scope<Symbol, CallasType> moduleScope, UnaryOperation uop);
	BBoolType tBBool(Scope<Symbol, CallasType> moduleScope, BBool bbool);
	BDoubleType tBDouble(Scope<Symbol, CallasType> moduleScope, BDouble bdouble);
	BLongType tBLong(Scope<Symbol, CallasType> moduleScope, BLong blong);
	BStringType tBString(Scope<Symbol, CallasType> moduleScope, BString bstring);
	CodeType tCode(Scope<Symbol, CallasType> moduleScope, Code module);
	CallasType tVariable(Scope<Symbol, CallasType> moduleScope, Variable var);
	List<CallasType> tSeq(Scope<Symbol, CallasType> moduleScope, List<CallasValue> vals);
	
	CallasType tLoadSensorCode(Scope<Symbol, CallasType> moduleScope, LoadSensorCode load);
	CallasType tStoreSensorCode(Scope<Symbol, CallasType> moduleScope, StoreSensorCode store);
	CallasType tExtern(Scope<Symbol, CallasType> moduleScope, Extern extern);
	CallasType tUpdate(Scope<Symbol, CallasType> moduleScope, Update install);
	CodeType tSend(Scope<Symbol, CallasType> moduleScope, Send send);
	CodeType tReceive(Scope<Symbol, CallasType> moduleScope, Receive receive);
	BStringType tTimer(Scope<Symbol, CallasType> moduleScope, Timer timedCall);
	CallasType tCall(Scope<Symbol, CallasType> moduleScope, Call call);
	CallasType tBranch(Scope<Symbol, CallasType> moduleScope, Branch branch);
	CallasType tLet(Scope<Symbol, CallasType> moduleScope, Let let);
	CallasType tKill(Scope<Symbol, CallasType> moduleScope, Kill kill);
	CallasType tOpen(Scope<Symbol, CallasType> moduleScope, Open open);
	CallasType tClose(Scope<Symbol, CallasType> moduleScope, Close close);
	/**
	 * The entry point to typecheck.
	 * @param moduleScope
	 * @param proc
	 * @return
	 */
	CallasType typecheck(Scope<Symbol, CallasType> moduleScope, CallasProcess proc);
	
	Scope<Symbol, CallasType> getBaseScope();
	List<ErrorMessage> getErrors();
}
