package org.callas.util;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.callas.absyn.types.*;

public class FreeTypeVariables extends AbstractTypeVisitor<Set<TypeVariable>> {

	public Set<TypeVariable> freeTypeVariables(CallasType type) {
		return visit(type);
	}
	
	@Override
	public Set<TypeVariable> caseBBoolType(BBoolType bbool) {
		return Collections.emptySet();
	}

	@Override
	public Set<TypeVariable> caseBDoubleType(BDoubleType bfloat) {
		return Collections.emptySet();
	}

	@Override
	public Set<TypeVariable> caseBLongType(BLongType bint) {
		return Collections.emptySet();
	}

	@Override
	public Set<TypeVariable> caseBStringType(BStringType bstring) {
		return Collections.emptySet();
	}

	@Override
	public Set<TypeVariable> caseCodeType(CodeType sensorCodeType) {
		TreeSet<TypeVariable> res = new TreeSet<TypeVariable>();
		for (FunctionType ty : sensorCodeType.functions.values()) {
			res.addAll(freeTypeVariables(ty));
		}
		return res;
	}

	@Override
	public Set<TypeVariable> caseFunctionType(FunctionType functionType) {
		TreeSet<TypeVariable> res = new TreeSet<TypeVariable>();
		for (CallasType ty : functionType.parameters) {
			res.addAll(freeTypeVariables(ty));
		}
		res.addAll(freeTypeVariables(functionType.result));
		return res;
	}

	@Override
	public Set<TypeVariable> caseRecursiveType(RecursiveType recursiveType) {
		TreeSet<TypeVariable> res = new TreeSet<TypeVariable>();
		res.addAll(freeTypeVariables(recursiveType.type));
		res.remove(recursiveType.variable);
		return res;
	}

	@Override
	public Set<TypeVariable> caseTypeVariable(TypeVariable typeVariable) {
		TreeSet<TypeVariable> res = new TreeSet<TypeVariable>();
		res.add(typeVariable);
		return res;
	}

}
