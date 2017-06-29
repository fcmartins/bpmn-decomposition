package org.callas.util;

import static org.callas.absyn.types.BBoolType.BOOL_TYPE;
import static org.callas.absyn.types.BDoubleType.DOUBLE_TYPE;
import static org.callas.absyn.types.BLongType.LONG_TYPE;
import static org.callas.absyn.types.BStringType.STRING_TYPE;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.callas.absyn.types.*;
import org.junit.Test;
import org.tyco.common.symbol.Symbol;

/**
 * Tests the equality relation (on types).
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class TypeEqualityTest {

	@Test
	public void testCaseAnonymousModuleTypePass1() throws DifferentTypesException {
		// same
		Map<Symbol, FunctionType> types1 = new HashMap<Symbol, FunctionType>();
		Map<Symbol, FunctionType> types2 = new HashMap<Symbol, FunctionType>();
		types1.put(Symbol.symbol("a"), new FunctionType(LONG_TYPE));
		types2.put(Symbol.symbol("a"), new FunctionType(LONG_TYPE));
		CallasType t1 = new CodeType(types1);
		CallasType t2 = new CodeType(types2);
		TypeEquality cmp = new TypeEquality();
		cmp.areEqual(t1, t2);
	}

	@Test
	public void testCaseAnonymousModuleTypePass2() throws DifferentTypesException {
		// same
		Map<Symbol, FunctionType> types1 = new HashMap<Symbol, FunctionType>();
		Map<Symbol, FunctionType> types2 = new HashMap<Symbol, FunctionType>();
		types1.put(Symbol.symbol("a"), new FunctionType(LONG_TYPE));
		types2.put(Symbol.symbol("a"), new FunctionType(LONG_TYPE));
		types1.put(Symbol.symbol("b"), new FunctionType(LONG_TYPE));
		types2.put(Symbol.symbol("b"), new FunctionType(LONG_TYPE));
		CallasType t1 = new CodeType(types1);
		CallasType t2 = new CodeType(types2);
		TypeEquality cmp = new TypeEquality();
		cmp.areEqual(t1, t2);
	}

	@Test
	public void testCaseAnonymousModuleTypeFail1() {
		// same
		Map<Symbol, FunctionType> types1 = new HashMap<Symbol, FunctionType>();
		Map<Symbol, FunctionType> types2 = new HashMap<Symbol, FunctionType>();
		types1.put(Symbol.symbol("a"), new FunctionType(LONG_TYPE));
		types2.put(Symbol.symbol("b"), new FunctionType(LONG_TYPE));
		CallasType t1 = new CodeType(types1);
		CallasType t2 = new CodeType(types2);
		TypeEquality cmp = new TypeEquality();
		try {
			cmp.areEqual(t1, t2);
			fail();
		} catch (DifferentTypesException e) {
		}
	}

	@Test
	public void testCaseAnonymousModuleTypeFail2() {
		// same
		Map<Symbol, FunctionType> types1 = new HashMap<Symbol, FunctionType>();
		Map<Symbol, FunctionType> types2 = new HashMap<Symbol, FunctionType>();
		types1.put(Symbol.symbol("a"), new FunctionType(LONG_TYPE));
		types2.put(Symbol.symbol("a"), new FunctionType(LONG_TYPE));
		types2.put(Symbol.symbol("b"), new FunctionType(LONG_TYPE));
		CallasType t1 = new CodeType(types1);
		CallasType t2 = new CodeType(types2);
		TypeEquality cmp = new TypeEquality();
		try {
			cmp.areEqual(t1, t2);
			fail();
		} catch (DifferentTypesException e) {
		}
	}

	@Test
	public void testCaseBBoolTypeFail() {
		TypeEquality cmp = new TypeEquality();
		try {
			cmp.areEqual(BOOL_TYPE, LONG_TYPE);
			fail();
		} catch (DifferentTypesException e) {
		}
	}

	@Test
	public void testCaseBBoolTypePass() throws DifferentTypesException {
		TypeEquality cmp = new TypeEquality();
		cmp.areEqual(BOOL_TYPE, BOOL_TYPE);
	}

	@Test
	public void testCaseBFloatTypeFail() {
		TypeEquality cmp = new TypeEquality();
		try {
			cmp.areEqual(DOUBLE_TYPE, LONG_TYPE);
			fail();
		} catch (DifferentTypesException e) {
		}
	}

	@Test
	public void testCaseBFloatTypePass() throws DifferentTypesException {
		TypeEquality cmp = new TypeEquality();
		cmp.areEqual(DOUBLE_TYPE, DOUBLE_TYPE);
	}

	@Test
	public void testCaseBIntTypeFail() {
		TypeEquality cmp = new TypeEquality();
		try {
			cmp.areEqual(LONG_TYPE, BOOL_TYPE);
			fail();
		} catch (DifferentTypesException e) {
		}
	}

	@Test
	public void testCaseBIntTypePass() throws DifferentTypesException {
		TypeEquality cmp = new TypeEquality();
		cmp.areEqual(LONG_TYPE, LONG_TYPE);
	}

	@Test
	public void testCaseBStringTypeFail() {
		TypeEquality cmp = new TypeEquality();
		try {
			cmp.areEqual(STRING_TYPE, LONG_TYPE);
			fail();
		} catch (DifferentTypesException e) {
		}
	}

	@Test
	public void testCaseBStringTypePass() throws DifferentTypesException {
		TypeEquality cmp = new TypeEquality();
		cmp.areEqual(STRING_TYPE, STRING_TYPE);
	}

	@Test
	public void testCaseFunctionTypeFail1() {
		TypeEquality cmp = new TypeEquality();
		try {
			cmp.areEqual(new FunctionType(LONG_TYPE), LONG_TYPE);
			fail();
		} catch (DifferentTypesException e) {
		}
	}

	@Test
	public void testCaseFunctionTypeFail2() {
		TypeEquality cmp = new TypeEquality();
		try {
			cmp.areEqual(new FunctionType(LONG_TYPE), new FunctionType(
					LONG_TYPE, STRING_TYPE));
			fail();
		} catch (DifferentTypesException e) {
		}
	}

	@Test
	public void testCaseFunctionTypeFail3() {
		TypeEquality cmp = new TypeEquality();
		try {
			cmp.areEqual(new FunctionType(LONG_TYPE, STRING_TYPE),
					new FunctionType(LONG_TYPE));
			fail();
		} catch (DifferentTypesException e) {
		}
	}

	@Test
	public void testCaseFunctionTypePass() throws DifferentTypesException {
		TypeEquality cmp = new TypeEquality();
		cmp.areEqual(new FunctionType(LONG_TYPE), new FunctionType(
				LONG_TYPE));
	}

	@Test
	public void testCaseRecursiveTypePass1() throws DifferentTypesException {
		TypeEquality cmp = new TypeEquality();
		RecursiveType t1 = new RecursiveType("A", new FunctionType(
				new TypeVariable("A")));
		RecursiveType t2 = new RecursiveType("A", new FunctionType(
				new TypeVariable("A")));
		cmp.areEqual(t1, t2);
	}

	@Test
	public void testCaseRecursiveTypePass2() throws DifferentTypesException {
		TypeEquality cmp = new TypeEquality();

		FunctionType f1 = new FunctionType(new TypeVariable("A"));
		RecursiveType t1 = new RecursiveType("A", f1);

		FunctionType f2 = new FunctionType(new TypeVariable("B"));
		RecursiveType t2 = new RecursiveType("B", f2);

		cmp.areEqual(t1, t2);
	}

	@Test
	public void testCaseRecursiveTypePass3() throws DifferentTypesException {
		FunctionType f1 = new FunctionType(new TypeVariable("A"));
		CallasType t1 = new RecursiveType("A", f1);
		// the unfold of t1 with a different bound name
		FunctionType f2 = new FunctionType(new TypeVariable("B"));
		CallasType t2 = new FunctionType(new RecursiveType("B", f2));
		TypeEquality cmp = new TypeEquality();
		cmp.areEqual(t1, t2);
	}
	
	@Test
	public void testComplexExample() throws DifferentTypesException {
		Map<Symbol, FunctionType> echoFuncs = new TreeMap<Symbol, FunctionType>();
		FunctionType funcF = new FunctionType(BBoolType.BOOL_TYPE, new TypeVariable("Echo"));
		echoFuncs.put(Symbol.symbol("f"), funcF);
		CodeType echoMod = new CodeType(echoFuncs);
		RecursiveType echo = new RecursiveType("Echo", echoMod);
		
		Map<Symbol, FunctionType> xFuncs = new TreeMap<Symbol, FunctionType>();
		FunctionType fForX = new FunctionType(BBoolType.BOOL_TYPE, new TypeVariable("X"));
		xFuncs.put(Symbol.symbol("f"), fForX);
		CodeType xMod = new CodeType(xFuncs);
		RecursiveType xType = new RecursiveType("X", xMod);
		
		Map<Symbol, FunctionType> funcs = new TreeMap<Symbol, FunctionType>();
		funcs.put(Symbol.symbol("f"), new FunctionType(BBoolType.BOOL_TYPE, xType));
		CodeType code = new CodeType(funcs);
		
		TypeEquality cmp = new TypeEquality();
		cmp.areEqual(new RecursiveType("Echo", code), echo);
	}
	
	@Test
	public void complextExample2() throws DifferentTypesException {
		Map<Symbol, FunctionType> echoFuncs = new TreeMap<Symbol, FunctionType>();
		FunctionType funcF = new FunctionType(BBoolType.BOOL_TYPE, new TypeVariable("Echo"), new TypeVariable("Echo"));
		echoFuncs.put(Symbol.symbol("f"), funcF);
		CodeType echoMod = new CodeType(echoFuncs);
		RecursiveType echo = new RecursiveType("Echo", echoMod);
		
		Map<Symbol, FunctionType> xFuncs = new TreeMap<Symbol, FunctionType>();
		FunctionType fForX = new FunctionType(BBoolType.BOOL_TYPE, new TypeVariable("X"), new TypeVariable("X"));
		xFuncs.put(Symbol.symbol("f"), fForX);
		CodeType xMod = new CodeType(xFuncs);
		RecursiveType xType = new RecursiveType("X", xMod);
		
		Map<Symbol, FunctionType> funcs = new TreeMap<Symbol, FunctionType>();
		funcs.put(Symbol.symbol("f"), new FunctionType(BBoolType.BOOL_TYPE, xType, new TypeVariable("Echo")));
		CodeType code = new CodeType(funcs);
		
		TypeEquality cmp = new TypeEquality();
		cmp.areEqual(new RecursiveType("Echo", code), echo);
	}
}
