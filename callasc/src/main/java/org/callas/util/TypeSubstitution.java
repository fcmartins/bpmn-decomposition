package org.callas.util;

import java.util.*;
import java.util.Map.Entry;

import org.callas.absyn.types.*;
import org.tyco.common.symbol.Symbol;

/**
 * Performs a substitution in a copy of the given type.
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 30, 2009
 *
 */
public class TypeSubstitution extends AbstractTypeVisitor<CallasType> {

	private final TypeVariable variable;
	private final CallasType replacementType;
	private final Set<TypeVariable> occurences;
	
	public TypeSubstitution(TypeVariable variable, CallasType replacementType) {
		this.variable = variable;
		this.replacementType = replacementType;
		this.occurences = new FreeTypeVariables().freeTypeVariables(replacementType);
	}

	/**
	 * Apply the substitution in the given type.
	 * @param type Where substitution will occur.
	 * @return the (new) type affected by the substitution 
	 */
	public CallasType replace(CallasType type) {
		return visit(type);
	}

	@Override
	public CallasType caseBBoolType(BBoolType bbool) {
		return bbool;
	}

	@Override
	public CallasType caseBDoubleType(BDoubleType bfloat) {
		return bfloat;
	}

	@Override
	public CallasType caseBLongType(BLongType bint) {
		return bint;
	}

	@Override
	public CallasType caseBStringType(BStringType bstring) {
		return bstring;
	}

	@Override
	public CallasType caseFunctionType(FunctionType func) {
		List<CallasType> params = new ArrayList<CallasType>(func.parameters.size());
		for (CallasType type : func.parameters) {
			params.add(replace(type));
		}
		return new FunctionType(func.getSourceLocation(), params, replace(func.result));
	}

	private TypeVariable freshTypeVariable(String base) {
		TypeVariable var;
		int counter = 1;
		do {
			var = new TypeVariable(base + counter);
			counter++;
		} while (occurences.contains(var));
		return var;
	}
	
	@Override
	public CallasType caseRecursiveType(RecursiveType rec) {
		if (rec.variable.equals(variable)) {
			return rec;
		}
		if (occurences.contains(rec.variable)) {
			TypeVariable newVar = freshTypeVariable(rec.variable.toString());
			CallasType newChild = new TypeSubstitution(rec.variable, newVar).replace(rec.type);
			rec = new RecursiveType(newVar, newChild);
		}
		return new RecursiveType(rec.getSourceLocation(), rec.variable, replace(rec.type));
	}

	@Override
	public CallasType caseCodeType(CodeType sens) {
		Map<Symbol, FunctionType> funcs = new HashMap<Symbol, FunctionType>();
		for (Entry<Symbol, FunctionType> entry : sens.functions.entrySet()) {
			// this replacement is safe
			FunctionType replaced = (FunctionType) replace(entry.getValue());
			funcs.put(entry.getKey(), replaced);
		}
		return new CodeType(sens.getSourceLocation(), funcs);
	}

	@Override
	public CallasType caseTypeVariable(TypeVariable tyVar) {
		if (tyVar.name.equals(variable.name)) {
			return replacementType;
		}
		return tyVar;
	}
}
