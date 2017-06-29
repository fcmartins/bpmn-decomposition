package org.callas.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.callas.absyn.AbsynPackageSchema;
import org.callas.absyn.types.*;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.symtable.KeyNotFoundException;
import org.tyco.common.symtable.ScopeBuilder;
import org.tyco.common.util.Functional;
import org.tyco.common.util.Pair;
import org.tyco.common.util.ParamTypeDispatcher;

/**
 * Checks if two types are equal.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class TypeEquality implements Matcher<CallasType> {

	private ScopeBuilder<Symbol, CallasType> unfoldings = new ScopeBuilder<Symbol, CallasType>();
	
	/**
	 * The dispatcher.
	 */
	private static final ParamTypeDispatcher<TypeEquality, Void> dispatcher = new ParamTypeDispatcher<TypeEquality, Void>(
			TypeEquality.class, null, CallasType.class).registerManySilently(
			"case%s", AbsynPackageSchema.TYPES);

	public void areEqual(CallasType o1, CallasType o2) throws DifferentTypesException {
		if (o1.getClass() != o2.getClass()) {
			if (o1 instanceof RecursiveType || o2 instanceof RecursiveType) {
				areEqual(unfold(o1), unfold(o2));
				return;
			}
			throw new DifferentTypesException("The kind of types differ.", o1, o2);
		}
		dispatchSameClass(o1, o2);
	}

	/**
	 * Dispatch to a method case$Type where Type is the name of the class of o1
	 * and o2.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 */
	protected void dispatchSameClass(CallasType o1, CallasType o2) throws DifferentTypesException {
		try {
			dispatcher.invoke(this, o1, o2);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof DifferentTypesException) {
				throw (DifferentTypesException) e.getTargetException();
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Unfolds a recursive type.
	 * 
	 * @param type
	 * @return
	 */
	public CallasType unfold(CallasType type) {
		if (type instanceof TypeVariable) {
			try {
				type = unfoldings.get(((TypeVariable) type).name);
			} catch (KeyNotFoundException e) {
				// not a known variable
			} 
		}
		while (type instanceof RecursiveType) {
			RecursiveType rec = (RecursiveType) type;
			TypeSubstitution subst = new TypeSubstitution(rec.variable, rec);
			type = subst.replace(rec.type);
		}
		return type;
	}

	public void caseBBoolType(BBoolType o1, CallasType o2) {
	}

	public void caseBDoubleType(BDoubleType o1, CallasType o2) {
	}

	public void caseBLongType(BLongType o1, CallasType o2) {
	}

	public void caseBStringType(BStringType o1, CallasType o2) {
	}

	public void caseFunctionType(FunctionType o1, CallasType o2) throws DifferentTypesException {
		FunctionType other = (FunctionType) o2;
		CallasType result1 = o1.result;
		CallasType result2 = other.result;
		List<CallasType> param1 = o1.parameters;
		List<CallasType> param2 = other.parameters;

		try {
			areEqual(result1, result2);
		} catch (DifferentTypesException e) {
			throw new DifferentTypesException("The result type of both function types is different.", e, o1, o2);
		}
		
		if (param1.size() != param2.size()) {
			throw new DifferentTypesException("The number of parameters of both function types is different. Lhs has " + param1.size() + " parameters and rhs has " + param2.size() + " parameters.", o1, o2);
		}
		int index = 1;
		for (Pair<CallasType, CallasType> pair : Functional
				.join(param1, param2)) {
			try {
				areEqual(pair.getLeft(), pair.getRight());
			} catch (DifferentTypesException e) {
				throw new DifferentTypesException("Parameter #" + index + " of both function types is different.", e, o1, o2);
			}
		}
	}

	public void caseTypeVariable(TypeVariable o1, CallasType o2) throws DifferentTypesException {
		TypeVariable other = (TypeVariable) o2;
		if (! o1.name.equals(other.name)) {
			throw new DifferentTypesException("The type variables differ.", o1, o2);
		}
	}

	public void caseRecursiveType(RecursiveType o1, CallasType o2) throws DifferentTypesException {
		RecursiveType other = (RecursiveType) o2;
		final CallasType right;
		if (o1.variable.equals(other.variable)) {
			// they are equal, just compare the type inside
			right = other.type;
		} else {
			TypeSubstitution subst = new TypeSubstitution(other.variable,
					o1.variable);
			// apply the substitution to the other
			right = subst.replace(other.type);
		}
		unfoldings.beginScope();
		unfoldings.replace(o1.variable.name, o1);
		areEqual(o1.type, right);
		unfoldings.endScope();
	}

	public void caseCodeType(CodeType o1, CallasType o2) throws DifferentTypesException {
		Map<Symbol, FunctionType> funcs1 = o1.functions;
		Map<Symbol, FunctionType> funcs2 = ((CodeType) o2).functions;

		Set<Symbol> missing = new TreeSet<Symbol>(funcs1.keySet());
		missing.removeAll(funcs2.keySet());
		Set<Symbol> extra = new TreeSet<Symbol>(funcs2.keySet());
		extra.removeAll(funcs1.keySet());
		
		
		if (missing.size() > 0 || extra.size() > 0) {
			throw new DifferentTypesException("Code types declare a different number of functions types. Lhs is declaring functions " + missing + " and rhs declares " + extra + ".", o1, o2);
		}
		for (Symbol key : funcs1.keySet()) {
			CallasType t1 = funcs1.get(key);
			CallasType t2 = funcs2.get(key);
			try {
				areEqual(t1, t2);
			} catch (DifferentTypesException e) {
				throw new DifferentTypesException("Function '" + key + "' of code type has different signatures.", e, o1, o2);
			}
		}
	}
}
