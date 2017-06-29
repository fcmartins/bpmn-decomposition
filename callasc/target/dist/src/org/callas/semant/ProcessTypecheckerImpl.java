package org.callas.semant;

import java.util.LinkedList;
import java.util.List;

import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.types.CallasType;
import org.callas.absyn.types.CodeType;
import org.callas.external.Externs;
import org.tyco.common.api.ITypechecker;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.ErrorMessagesException;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.symtable.BaseScope;
import org.tyco.common.symtable.Scope;

/**
 * Implements the intercace {@link ICallasTypechecker}.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class ProcessTypecheckerImpl implements ITypechecker<CallasProcess, CallasType> {

	private final Externs registry;
	private final CodeType sensorCodeType;

	/**
	 * Creates a typechecker bound to a certain registry.
	 * 
	 * @param registry
	 */
	public ProcessTypecheckerImpl(Externs registry,
			CodeType sensorCodeType) {
		this.registry = registry;
		this.sensorCodeType = sensorCodeType;
	}

	public CallasType typecheck(CallasProcess proc)
			throws ErrorMessagesException {
		List<ErrorMessage> errors = new LinkedList<ErrorMessage>();
		Scope<Symbol, CallasType> rootScope = new BaseScope<Symbol, CallasType>();
		ProcessTypingRulesImpl rules = new ProcessTypingRulesImpl(
				registry.createScope(), errors, sensorCodeType);
		CallasType procType = rules.typecheck(rootScope, proc);
		if (errors.size() > 0) {
			throw new ErrorMessagesException(errors);
		}
		return procType;
	}
}
