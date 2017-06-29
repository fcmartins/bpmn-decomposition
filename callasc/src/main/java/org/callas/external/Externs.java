package org.callas.external;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.callas.absyn.types.*;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.symtable.BaseScope;
import org.tyco.common.symtable.KeyNotFoundException;
import org.tyco.common.symtable.Scope;

/**
 * This is a singleton for registering external operations. It is both used
 * while type-checking and while running the interpreter.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 1, 2008
 *
 */
public class Externs implements Cloneable {

	/**
	 * Where the operations are kept.
	 */
	private Map<Symbol, IExternalOperation> registry = new HashMap<Symbol, IExternalOperation>();
	
	/**
	 * Gets the operation bound to this name.
	 * @param name
	 * @return
	 */
	public IExternalOperation getOperation(Symbol name) throws KeyNotFoundException {
		IExternalOperation op = registry.get(name);
		if (op == null) {
			throw new KeyNotFoundException(name.toString());
		}
		return op;
	}
	
	/**
	 * Registers the operation.
	 * @param name
	 * @param op
	 * @return the old registered operation. When none, returns null.
	 */
	public IExternalOperation registerOperation(String name, IExternalOperation op) {
		return registry.put(Symbol.symbol(name), op);
	}
	
	/**
	 * Creates a typing environment from this registry.
	 * @return
	 */
	public Scope<Symbol, CallasType> createScope() {
		Scope<Symbol, CallasType> scope = new BaseScope<Symbol, CallasType>();
		for (Entry<Symbol, IExternalOperation> entry : registry.entrySet()) {
			scope.replace(entry.getKey(), entry.getValue().getSignatureType());
		}
		return scope;
	}
	
	@Override
	public Externs clone() {
		Externs registryInstance = new Externs();
		registryInstance.registry.putAll(registry);
		return registryInstance;
	}
	
	//--------------------------------------------------------------------------
	
	private static final Externs DEFAULT = new Externs();
	
	static {
		DEFAULT.registerOperation("printInt", new PrintOperation(BLongType.LONG_TYPE, System.out));
		DEFAULT.registerOperation("printFloat", new PrintOperation(BDoubleType.DOUBLE_TYPE, System.out));
		DEFAULT.registerOperation("printBool", new PrintOperation(BBoolType.BOOL_TYPE, System.out));
		DEFAULT.registerOperation("printString", new PrintOperation(BStringType.STRING_TYPE, System.out));
		DEFAULT.registerOperation("getSensorRepr", new PrintInstalledCode());
		DEFAULT.registerOperation("closureRepr", new ClosureReprOperation());
		DEFAULT.registerOperation("getMacAddress", new GetMacAdressOperation());
		DEFAULT.registerOperation("getTime", new GetTimeOperation());
		DEFAULT.registerOperation("add", new AdditionOperation());
		DEFAULT.registerOperation("sub", new SubtractionOperation());
		DEFAULT.registerOperation("ieq", new IntEqualOperation());
	}
	
	/**
	 * Gets the default singleton.
	 * @return
	 */
	public static Externs getInstance() {
		return DEFAULT;
	}
}
