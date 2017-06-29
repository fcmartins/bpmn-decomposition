package org.callas.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.TreeMap;

import org.callas.absyn.types.*;
import org.junit.Test;
import org.tyco.common.symbol.Symbol;

public class TypeSubstitutionTest {

	@Test
	public void testCaseBBoolTypeBBoolType() {
		TypeSubstitution subst = new TypeSubstitution(new TypeVariable("a"), BDoubleType.DOUBLE_TYPE);
		assertSame(subst.replace(BBoolType.BOOL_TYPE), BBoolType.BOOL_TYPE);
	}

	@Test
	public void testCaseBDoubleTypeBDoubleType() {
		TypeSubstitution subst = new TypeSubstitution(new TypeVariable("a"), BBoolType.BOOL_TYPE);
		assertSame(subst.replace(BDoubleType.DOUBLE_TYPE), BDoubleType.DOUBLE_TYPE);
	}

	@Test
	public void testCaseBLongTypeBLongType() {
		TypeSubstitution subst = new TypeSubstitution(new TypeVariable("a"), BDoubleType.DOUBLE_TYPE);
		assertSame(subst.replace(BLongType.LONG_TYPE), BLongType.LONG_TYPE);
	}

	@Test
	public void testCaseBStringTypeBStringType() {
		TypeSubstitution subst = new TypeSubstitution(new TypeVariable("a"), BBoolType.BOOL_TYPE);
		assertSame(subst.replace(BStringType.STRING_TYPE), BStringType.STRING_TYPE);
	}

	@Test
	public void testCaseCodeTypeCodeType() {
		// { f(a) -> X }
		TreeMap<Symbol, FunctionType> funcs = new TreeMap<Symbol,FunctionType>();
		funcs.put(Symbol.symbol("f"), new FunctionType(new TypeVariable("X"), new TypeVariable("a")));
		CodeType ct = new CodeType(funcs);

		// { f(X) -> X }
		TreeMap<Symbol, FunctionType> funcs2 = new TreeMap<Symbol,FunctionType>();
		funcs2.put(Symbol.symbol("f"), new FunctionType(new TypeVariable("X"), new TypeVariable("X")));
		CodeType ct2 = new CodeType(funcs2);
		
		// [X/a]
		TypeSubstitution subst = new TypeSubstitution(new TypeVariable("a"), new TypeVariable("X"));
		// { f(a) -> X } => { f(X) -> X }
		assertEquals(ct2, subst.replace(ct));
	}

	@Test
	public void testCaseFunctionTypeFunctionType() {
		// (a) -> X
		FunctionType func = new FunctionType(new TypeVariable("X"), new TypeVariable("a"));

		// (X) -> X
		FunctionType func2 = new FunctionType(new TypeVariable("X"), new TypeVariable("X"));
		
		// [X/a]
		TypeSubstitution subst = new TypeSubstitution(new TypeVariable("a"), new TypeVariable("X"));
		// (a) -> X => (X) -> X
		assertEquals(func2, subst.replace(func));
	}

	@Test
	public void testCaseRecursiveTypeRecursiveType() {
		// rec X. { f(a) -> X }
		TreeMap<Symbol, FunctionType> funcs = new TreeMap<Symbol,FunctionType>();
		funcs.put(Symbol.symbol("f"), new FunctionType(new TypeVariable("X"), new TypeVariable("a")));
		CodeType ct = new CodeType(funcs);
		RecursiveType rec = new RecursiveType("X", ct);

		// rec X1. { f(X) -> X1 }
		TreeMap<Symbol, FunctionType> funcs2 = new TreeMap<Symbol,FunctionType>();
		funcs2.put(Symbol.symbol("f"), new FunctionType(new TypeVariable("X1"), new TypeVariable("X")));
		CodeType ct2 = new CodeType(funcs2);
		RecursiveType rec2 = new RecursiveType("X1", ct2);
		
		// [X/a]
		TypeSubstitution subst = new TypeSubstitution(new TypeVariable("a"), new TypeVariable("X"));
		// rec X. { f(a) -> X }[X/a] => rec X1. { f(X) -> X1 }
		assertEquals(rec2, subst.replace(rec));
	}

	@Test
	public void testCaseRecursiveTypeRecursiveType2() {
		// rec X. { f(a) -> X }
		TreeMap<Symbol, FunctionType> funcs = new TreeMap<Symbol,FunctionType>();
		funcs.put(Symbol.symbol("f"), new FunctionType(new TypeVariable("X"), new TypeVariable("a")));
		CodeType ct = new CodeType(funcs);
		RecursiveType rec = new RecursiveType("X", ct);
		
		// [a/X]
		TypeSubstitution subst = new TypeSubstitution(new TypeVariable("X"), new TypeVariable("a"));
		// rec X. { f(a) -> X }[a/X] => rec X. { f(a) -> X }
		assertSame(rec, subst.replace(rec));
	}

	@Test
	public void testCaseTypeVariableTypeVariableEq() {
		TypeSubstitution subst = new TypeSubstitution(new TypeVariable("a"), BBoolType.BOOL_TYPE);
		assertSame(subst.replace(new TypeVariable("a")), BBoolType.BOOL_TYPE);
	}

	@Test
	public void testCaseTypeVariableTypeVariableNeq() {
		TypeSubstitution subst = new TypeSubstitution(new TypeVariable("a"), BBoolType.BOOL_TYPE);
		TypeVariable b = new TypeVariable("b");
		assertSame(subst.replace(b), b);
	}
}
