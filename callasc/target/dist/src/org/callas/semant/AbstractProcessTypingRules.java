package org.callas.semant;

import java.util.ArrayList;
import java.util.List;

import org.callas.absyn.AbsynPackageSchema;
import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.types.CallasType;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.symtable.Scope;
import org.tyco.common.util.ParamTypeDispatcher;

/**
 * The abstract typing rules implement the two entry points: the
 * <code>typecheck</code> and <code>tSeq</code> methods.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 5, 2009
 * 
 */
abstract class AbstractProcessTypingRules implements IProcessTypingRules {

	private static final ParamTypeDispatcher<AbstractProcessTypingRules, CallasType> dispatcher = new ParamTypeDispatcher<AbstractProcessTypingRules, CallasType>(
			AbstractProcessTypingRules.class, Scope.class, null)
			.registerManySilently("t%s", AbsynPackageSchema.PROCESSES);

	/**
	 * The errors found.
	 */
	private final List<ErrorMessage> errors;

	/**
	 * The sensor scope.
	 */
	private final Scope<Symbol, CallasType> baseScope;

	/**
	 * The default constructor.
	 * 
	 * @param errors
	 *            A container for the errors that are found.
	 */
	public AbstractProcessTypingRules(Scope<Symbol, CallasType> sensorScope,
			List<ErrorMessage> errors) {
		this.baseScope = sensorScope;
		this.errors = errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.callas.semant.IValuesTypingRules#tSeq(org.tyco.common.symtable.Scope,
	 * org.tyco.common.symtable.Scope, java.util.List)
	 */
	public List<CallasType> tSeq(Scope<Symbol, CallasType> moduleScope,
			List<CallasValue> vals) {
		ArrayList<CallasType> types = new ArrayList<CallasType>(vals.size());
		for (CallasValue value : vals) {
			types.add(typecheck(moduleScope, value));
		}
		return types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.callas.semant.IValuesTypingRules#typecheck(org.tyco.common.symtable
	 * .Scope, org.tyco.common.symtable.Scope,
	 * org.callas.absyn.processes.CallasProcess)
	 */
	public CallasType typecheck(Scope<Symbol, CallasType> moduleScope,
			CallasProcess proc) {
		return dispatcher.unsafeInvoke(this, moduleScope, proc);
	}

	/**
	 * @return the errors
	 */
	public List<ErrorMessage> getErrors() {
		return errors;
	}

	/**
	 * @return the sensorScope
	 */
	public Scope<Symbol, CallasType> getBaseScope() {
		return baseScope;
	}
}
