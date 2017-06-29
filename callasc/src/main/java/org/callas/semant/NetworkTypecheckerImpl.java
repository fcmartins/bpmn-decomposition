package org.callas.semant;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.callas.absyn.SourceValue;
import org.callas.absyn.sensors.CallasNetwork;
import org.callas.absyn.types.CallasType;
import org.callas.absyn.types.CodeType;
import org.callas.absyn.types.FunctionType;
import org.callas.absyn.types.TypeVariable;
import org.callas.error.UndefinedSymbol;
import org.callas.external.Externs;
import org.callas.util.FreeTypeVariables;
import org.tyco.common.api.ITypechecker;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.ErrorMessagesException;
import org.tyco.common.errorMsg.SourceLocation;
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
public class NetworkTypecheckerImpl implements ITypechecker<CallasNetwork, Void> {

	private final SourceValue<Externs> registry;
	private final SourceValue<CodeType> iface;

	/**
	 * Creates a typechecker bound to a certain registry.
	 * @param registry
	 */
	public NetworkTypecheckerImpl(SourceValue<CodeType> sensorCodeType,
			SourceValue<Externs> registry) {
		this.registry = registry;
		this.iface = sensorCodeType;
	}

	public Void typecheck(CallasNetwork network) throws ErrorMessagesException {
		List<ErrorMessage> errors = new LinkedList<ErrorMessage>();
		CodeType ifaceType = iface.getValue();
		checkClosedType(iface.getSourceLocation(), errors, iface.getValue(), "in the network interface");
		Scope<Symbol, CallasType> rootScope = new BaseScope<Symbol, CallasType>();
		for (Entry<Symbol, FunctionType> entry : ifaceType.functions.entrySet()) {
			rootScope.replace(entry.getKey(), entry.getValue());
		}
		Scope<Symbol, CallasType> externsScope = registry.getValue().createScope();
		for (CallasType type : externsScope.createView().values()) {
			checkClosedType(registry.getSourceLocation(), errors, type, "in the external operations interface");
		}
		SensorTypingRules typingRules = new SensorTypingRules(ifaceType,
				externsScope, rootScope, errors);
		typingRules.typecheck(network);
		if (errors.size() > 0) {
			throw new ErrorMessagesException(errors);
		}
		return null;
	}

	private static void checkClosedType(SourceLocation loc,
			List<ErrorMessage> errors, CallasType type, String extraMessage) {
		FreeTypeVariables ftv = new FreeTypeVariables();
		Set<TypeVariable> freeTypeVariables = ftv.freeTypeVariables(type);
		if (freeTypeVariables.size() > 0) {
			for (TypeVariable tv : freeTypeVariables) {
				errors.add(UndefinedSymbol.undefinedType(loc, tv.name, extraMessage));
			}
		}
	}
}
