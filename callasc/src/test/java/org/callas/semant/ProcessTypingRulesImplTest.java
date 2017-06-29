package org.callas.semant;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.callas.absyn.processes.*;
import org.callas.absyn.processes.Timer;
import org.callas.absyn.types.*;
import org.callas.error.*;
import org.callas.util.DifferentTypesException;
import org.callas.util.TypeEquality;
import org.junit.Before;
import org.junit.Test;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.symtable.BaseScope;

/**
 * Test the process typing rules.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class ProcessTypingRulesImplTest {
	ProcessTypingRulesImpl rules;
	BaseScope<Symbol, CallasType> sensorScope;
	BaseScope<Symbol, CallasType> moduleScope;
	TypeEquality matcher = new TypeEquality();

	@Before
	public void setUp() {
		sensorScope = new BaseScope<Symbol, CallasType>();
		moduleScope = new BaseScope<Symbol, CallasType>();
		rules = new ProcessTypingRulesImpl(sensorScope, new LinkedList<ErrorMessage>(),
				CodeType.NIL_TYPE);
	}

	private void setSensorCodeType(CodeType sensorCodeType) {
		rules = new ProcessTypingRulesImpl(sensorScope, new LinkedList<ErrorMessage>(),
				sensorCodeType);
	}

	private void registerInSensorScope(String name, CallasType type) {
		sensorScope.replace(Symbol.symbol(name), type);
	}

	private void registerInModuleScope(String name, CallasType type) {
		moduleScope.replace(Symbol.symbol(name), type);
	}

	private CallasType typecheck(CallasProcess proc) {
		return rules.typecheck(moduleScope, proc);
	}

	private void assertTypechecks(CallasProcess proc, CallasType expected) {
		assertTypechecks(proc, expected, false);
	}

	private void assertTypechecks(CallasProcess proc, CallasType expected,
			boolean output) {
		CallasType provided = assertTypechecks(proc, output);
		try {
			matcher.areEqual(expected, provided);
		} catch (DifferentTypesException e) {
			assertEquals(expected.toString(), provided.toString());
		}
	}

	private CallasType assertTypechecks(CallasProcess proc, Class<?>... errors) {
		return assertTypechecks(proc, false, errors);
	}

	private CallasType assertTypechecks(CallasProcess proc, boolean output,
			Class<?>... errors) {
		CallasType type = typecheck(proc);
		List<Class<?>> providedErrors = new LinkedList<Class<?>>();
		for (ErrorMessage err : rules.getErrors()) {
			providedErrors.add(err.getClass());
		}
		if (output) {
			for (ErrorMessage err : rules.getErrors()) {
				System.err.println(err);
			}
		}
		assertEquals(Arrays.asList(errors), providedErrors);
		return type;
	}

	@Test
	public void tBBool() {
		assertTypechecks(new BBool(true), BBoolType.BOOL_TYPE);
		assertTypechecks(new BBool(false), BBoolType.BOOL_TYPE);
	}

	@Test
	public void tBFloat() {
		assertTypechecks(new BDouble(1.0f), BDoubleType.DOUBLE_TYPE);
	}

	@Test
	public void tBInt() {
		assertTypechecks(new BLong(1), BLongType.LONG_TYPE);
	}

	@Test
	public void tBString() {
		assertTypechecks(new BString("asd"), BStringType.STRING_TYPE);
	}

	@Test
	public void tCode() {
		assertTypechecks(Code.NIL, CodeType.NIL_TYPE);
	}

	@Test
	public void tCodeLabelsMismatch1() {
		// The module has more labels than the type
		Map<Symbol, ProcessAbstraction> funcs = new HashMap<Symbol, ProcessAbstraction>();
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0)));
		assertTypechecks(new Code(funcs, CodeType.NIL_TYPE), LabelsDiffer.class);
	}

	@Test
	public void tCodeLabelsMismatch2() {
		// The type has more labels than the value
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE));
		assertTypechecks(new Code(funcs, new CodeType(funcTypes)),
				LabelsDiffer.class);
	}

	@Test
	public void tCodeLabelsReorderedLabels() {
		// The labels are the same, but they are given in a strange order
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE));
		funcTypes.put(Symbol.symbol("bar"), new FunctionType(
				BLongType.LONG_TYPE));
		funcs.put(Symbol.symbol("bar"), new ProcessAbstraction(new BLong(0)));
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0)));
		assertTypechecks(new Code(funcs, new CodeType(funcTypes)));
	}

	@Test
	public void tCodeArgumentCountDiffer1() {
		// the first function has more arguments than its type
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE));
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0),
				"x"));
		assertTypechecks(new Code(funcs, new CodeType(funcTypes)),
				ArgumentCountMismatch.class);
	}

	@Test
	public void tCodeArgumentCountDiffer2() {
		// the first function has less arguments than its type
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE, BLongType.LONG_TYPE));
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0)));
		assertTypechecks(new Code(funcs, new CodeType(funcTypes)),
				ArgumentCountMismatch.class);
	}

	@Test
	public void tCodeArgumentResultMismatch() {
		// the first function has less arguments than its type
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BDoubleType.DOUBLE_TYPE));
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0)));
		assertTypechecks(new Code(funcs, new CodeType(funcTypes)),
				TypeMismatch.class);
	}

	@Test
	public void tVariable() {
		registerInModuleScope("foo", BLongType.LONG_TYPE);
		assertTypechecks(new Variable("foo"), BLongType.LONG_TYPE);
	}

	@Test
	public void tVariableUndef() {
		registerInSensorScope("foo", BLongType.LONG_TYPE);
		assertTypechecks(new Variable("foo"), UndefinedSymbol.class);
	}

	@Test
	public void tVariableUndef2() {
		assertTypechecks(new Variable("foo"), UndefinedSymbol.class);
	}

	@Test
	public void tCallUndef() {
		assertTypechecks(new Call(Code.NIL, "foo"), UndefinedSymbol.class);
	}

	@Test
	public void tCallLabelTypeMismatch() {
		registerInModuleScope("foo", BLongType.LONG_TYPE);
		assertTypechecks(new Call(Code.NIL, "foo"), ClassOfTypeMismatch.class);
	}

	@Test
	public void tCallArgumentCountMismatch() {
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE));
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0)));
		Code code = new Code(funcs, new CodeType(funcTypes));
		assertTypechecks(new Call(code, "foo", new BLong(0)),
				ArgumentCountMismatch.class);
	}

	@Test
	public void tCallArgumentTypeMismatch() {
		TypeVariable tvar = new TypeVariable("X");

		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE, tvar, BStringType.STRING_TYPE));

		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0),
				"self", "a"));

		CodeType type = new CodeType(funcTypes);
		RecursiveType rec = new RecursiveType("X", type);

		TypeEquality typeEq = new TypeEquality();
		Code code = new Code(funcs, (CodeType) typeEq.unfold(rec));

		assertTypechecks(new Call(code, "foo", new BLong(0)),
				TypeMismatch.class);
	}

	/*
	 * We are calling something like: foo.foo()
	 */
	@Test
	public void tCallSameName() {
		TypeVariable tvar = new TypeVariable("X");
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE, tvar));
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0),
				"self"));
		CodeType type = new CodeType(funcTypes);
		RecursiveType rec = new RecursiveType("X", type);
		TypeEquality typeEq = new TypeEquality();
		CodeType codeType = (CodeType) typeEq.unfold(rec);
		moduleScope.replace(Symbol.symbol("foo"), codeType);

		assertTypechecks(new Call(new Variable("foo"), "foo"), BLongType.LONG_TYPE);
	}

	@Test
	public void tCall() {
		TypeVariable tvar = new TypeVariable("X");
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE, tvar));
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0),
				"self"));
		CodeType type = new CodeType(funcTypes);
		RecursiveType rec = new RecursiveType("X", type);
		TypeEquality typeEq = new TypeEquality();
		
		Code code = new Code(funcs, (CodeType) typeEq.unfold(rec));

		assertTypechecks(new Call(code, "foo"), BLongType.LONG_TYPE);
	}

	@Test
	public void tCallNotModule() {
		assertTypechecks(new Call(new BLong(1), "foo"),
				ClassOfTypeMismatch.class);
	}

	@Test
	public void tExtern() {
		registerInSensorScope("foo", new FunctionType(BLongType.LONG_TYPE));
		assertTypechecks(new Extern("foo"), BLongType.LONG_TYPE);
	}

	@Test
	public void tExternUndef1() {
		assertTypechecks(new Extern("foo"), UndefinedSymbol.class);
	}

	@Test
	public void tExternUndef2() {
		registerInModuleScope("foo", new FunctionType(BLongType.LONG_TYPE));
		assertTypechecks(new Extern("foo"), UndefinedSymbol.class);
	}

	@Test
	public void tExternClassMismtach() {
		registerInSensorScope("foo", BLongType.LONG_TYPE);
		assertTypechecks(new Extern("foo"), ClassOfTypeMismatch.class);
	}

	@Test
	public void tExternArgumentCountMismatch() {
		registerInSensorScope("foo", new FunctionType(BLongType.LONG_TYPE));
		assertTypechecks(new Extern("foo", new BLong(0)),
				ArgumentCountMismatch.class);
	}

	@Test
	public void tExternArgumentTypeMismatch() {
		registerInSensorScope("foo", new FunctionType(BLongType.LONG_TYPE,
				BDoubleType.DOUBLE_TYPE));
		assertTypechecks(new Extern("foo", new BLong(0)), TypeMismatch.class);
	}

	@Test
	public void tUpdate() {
		assertTypechecks(new Update(Code.NIL, Code.NIL), CodeType.NIL_TYPE);
	}

	@Test
	public void tUpdateRHSTypeMismtach() {
		assertTypechecks(new Update(new BLong(0), Code.NIL),
				ClassOfTypeMismatch.class);
	}

	@Test
	public void tUpdateLHSTypeMismtach() {
		assertTypechecks(new Update(Code.NIL, new BLong(0)),
				ClassOfTypeMismatch.class);
	}

	@Test
	public void tUpdateZeroPlusOne() {
		TypeVariable var = new TypeVariable("X");
		Map<Symbol, FunctionType> sigs = new LinkedHashMap<Symbol, FunctionType>();
		sigs.put(Symbol.symbol("foo"), new FunctionType(BLongType.LONG_TYPE, var));
		CallasType type = new RecursiveType(var, new CodeType(sigs));

		sigs = new LinkedHashMap<Symbol, FunctionType>();
		sigs.put(Symbol.symbol("foo"), new FunctionType(BLongType.LONG_TYPE, type));
		CodeType unfoldedType = new CodeType(sigs);
		
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0), "self"));
		Code code = new Code(funcs, unfoldedType);


		assertTypechecks(new Update(Code.NIL, code), type);
	}

	/**
	 * There is a mismatch in the signature of function foo.
	 */
	@Test
	public void tUpdateFuncFooMismatch() {
		TypeVariable var = new TypeVariable("X");
		Map<Symbol, ProcessAbstraction> funcs;
		Map<Symbol, FunctionType> funcTypes;
		
		funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE, var));
		RecursiveType t1 = new RecursiveType(var, new CodeType(funcTypes));
		funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0), "self"));
		Code code = new Code(funcs, unfold(t1));

		funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(CodeType.NIL_TYPE, var));
		CallasType t2 = new RecursiveType(var, new CodeType(funcTypes));
		funcs = new LinkedHashMap<Symbol, ProcessAbstraction>(); 
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(Code.NIL, "self"));
		Code target = new Code(funcs, unfold(t2));
		
		assertTypechecks(new Update(target, code), TypeMismatch.class);
	}

	private CodeType unfold(CallasType type) {
		return (CodeType) matcher.unfold(type);
	}

	/**
	 * There is a mismatch in the signature of function foo.
	 */
	@Test
	public void tUpdateJoinTwoFuncs() {
		TypeVariable var = new TypeVariable("X");
		
		Map<Symbol, ProcessAbstraction> funcs1 = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes1 = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes1.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE, var));
		funcs1.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0), "self"));
		RecursiveType t1 = new RecursiveType(var, new CodeType(funcTypes1));
		Code code1 = new Code(funcs1, unfold(t1));

		Map<Symbol, ProcessAbstraction> funcs2 = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes2 = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes2.put(Symbol.symbol("bar"), new FunctionType(
				BLongType.LONG_TYPE, var));
		funcs2.put(Symbol.symbol("bar"), new ProcessAbstraction(new BLong(0), "self"));
		RecursiveType t2 = new RecursiveType(var, new CodeType(funcTypes2));
		Code code2 = new Code(funcs2, unfold(t2));

		Map<Symbol, FunctionType> funcTypes3 = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes3.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE, var));
		funcTypes3.put(Symbol.symbol("bar"), new FunctionType(
				BLongType.LONG_TYPE, var));
		RecursiveType t3 = new RecursiveType(var, new CodeType(funcTypes3));
		assertTypechecks(new Update(code1, code2), t3);
	}

	/**
	 * Update a module with a function foo with another module that also only
	 * implements foo.
	 */
	@Test
	public void tUpdateCodeTypeEquals() {
		final TypeVariable var = new TypeVariable("X");
		
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE, var));
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0), "self"));
		CallasType t1 = new RecursiveType(var, new CodeType(funcTypes));
		Code code = new Code(funcs, unfold(t1));

		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE, var));
		CallasType t2 = new RecursiveType(var, new CodeType(funcTypes));

		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0), "self"));
		Code target = new Code(funcs, unfold(t2));
		assertTypechecks(new Update(target, code), t2);
	}

	@Test
	public void tReceive() {
		assertTypechecks(Receive.RECEIVE, CodeType.NIL_TYPE);
	}

	@Test
	public void tTimer() {
		registerInModuleScope("foo", new FunctionType(CodeType.NIL_TYPE, CodeType.NIL_TYPE));
		assertTypechecks(new Timer(new Variable("foo"), "foo", new BLong(0)),
				CodeType.NIL_TYPE);
	}

	@Test
	public void tTimerResultTypeMismatch() {
		registerInModuleScope("foo", new FunctionType(BLongType.LONG_TYPE, CodeType.NIL_TYPE));
		assertTypechecks(new Timer(new Variable("foo"), "foo", new BLong(0)),
				TypeMismatch.class);
	}

	@Test
	public void tTimerUndef1() {
		assertTypechecks(new Timer(new Variable("foo"), "foo", new BLong(0)),
				UndefinedSymbol.class);
	}

	@Test
	public void tTimerUndef2() {
		registerInSensorScope("foo", new FunctionType(BLongType.LONG_TYPE));
		assertTypechecks(new Timer(new Variable("foo"), "foo", new BLong(0)),
				UndefinedSymbol.class);
	}

	@Test
	public void tTimerClassMismtach() {
		registerInModuleScope("foo", BLongType.LONG_TYPE);
		assertTypechecks(new Timer(new Variable("foo"), "foo", new BLong(0)),
				ClassOfTypeMismatch.class);
	}

	@Test
	public void tTimerArgumentCountMismatch() {
		registerInModuleScope("foo", new FunctionType(BLongType.LONG_TYPE, CodeType.NIL_TYPE));
		assertTypechecks(new Timer(new Variable("foo"), "foo", new BLong(0), 
				new BLong(0)), ArgumentCountMismatch.class);
	}

	@Test
	public void tTimerArgumentTypeMismatch() {
		registerInModuleScope("foo", new FunctionType(CodeType.NIL_TYPE,
				CodeType.NIL_TYPE, BDoubleType.DOUBLE_TYPE));
		assertTypechecks(new Timer(new Variable("foo"), "foo", new BLong(0),
				new BLong(0)), TypeMismatch.class);
	}

	@Test
	public void tSend() {
		registerInModuleScope("foo", new FunctionType(CodeType.NIL_TYPE, CodeType.NIL_TYPE));
		assertTypechecks(new Send(new Variable("ch"), "foo"), CodeType.NIL_TYPE);
	}

	@Test
	public void tSendResultTypeMismatch() {
		registerInModuleScope("foo", new FunctionType(BLongType.LONG_TYPE, CodeType.NIL_TYPE));
		assertTypechecks(new Send(new Variable("ch"), "foo"), TypeMismatch.class);
	}

	@Test
	public void tSendUndef1() {
		assertTypechecks(new Send(new Variable("ch"), "foo"), UndefinedSymbol.class);
	}

	@Test
	public void tSendUndef2() {
		registerInSensorScope("foo", new FunctionType(BLongType.LONG_TYPE));
		assertTypechecks(new Send(new Variable("ch"), "foo"), UndefinedSymbol.class);
	}

	@Test
	public void tSendClassMismtach() {
		registerInModuleScope("foo", BLongType.LONG_TYPE);
		assertTypechecks(new Send(new Variable("ch"), "foo"), ClassOfTypeMismatch.class);
	}

	@Test
	public void tSendArgumentCountMismatch() {
		registerInModuleScope("foo", new FunctionType(BLongType.LONG_TYPE,
				BLongType.LONG_TYPE));
		assertTypechecks(new Send(new Variable("ch"), "foo", new BLong(0)),
				ArgumentCountMismatch.class);
	}

	@Test
	public void tSendArgumentTypeMismatch() {
		registerInModuleScope("foo", new FunctionType(CodeType.NIL_TYPE,
				CodeType.NIL_TYPE, BDoubleType.DOUBLE_TYPE));
		assertTypechecks(new Send(new Variable("ch"), "foo", new BLong(0)), TypeMismatch.class);
	}

	@Test
	public void tBranch() {
		assertTypechecks(
				new Branch(new BBool(true), new BLong(0), new BLong(0)),
				BLongType.LONG_TYPE);
	}

	@Test
	public void tBranchConditionTypeMismatch() {
		assertTypechecks(new Branch(new BLong(0), new BLong(0), new BLong(0)),
				TypeMismatch.class);
	}

	@Test
	public void tBranchResultTypeMismatch() {
		assertTypechecks(new Branch(new BBool(true), new BDouble(0), new BLong(
				0)), TypeMismatch.class);
	}

	@Test
	public void tLet() {
		assertTypechecks(new Let("x", new BLong(0), new Variable("x")),
				BLongType.LONG_TYPE);
	}

	@Test
	public void tLetOverrideModuleScope() {
		registerInModuleScope("x", BDoubleType.DOUBLE_TYPE);
		assertTypechecks(new Let("x", new BLong(0), new Variable("x")),
				BLongType.LONG_TYPE);
	}

	@Test
	public void tLetOverrideSensorScope() {
		registerInSensorScope("x", BDoubleType.DOUBLE_TYPE);
		assertTypechecks(new Let("x", new BLong(0), new Variable("x")),
				BLongType.LONG_TYPE);
	}

	@Test
	public void tStoreSensorCode() {
		assertTypechecks(new StoreSensorCode(Code.NIL), CodeType.NIL_TYPE);
	}

	@Test
	public void tStoreSensorCode2() {
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE));
		Map<Symbol, ProcessAbstraction> funcs = new LinkedHashMap<Symbol, ProcessAbstraction>();
		funcs.put(Symbol.symbol("foo"), new ProcessAbstraction(new BLong(0)));
		Code code = new Code(funcs, new CodeType(funcTypes));
		setSensorCodeType(new CodeType(funcTypes));
		
		assertTypechecks(new StoreSensorCode(code), CodeType.NIL_TYPE);
	}
	
	@Test
	public void tStoreSensorCodeMismatching() {
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE));
		setSensorCodeType(new CodeType(funcTypes));

		assertTypechecks(new StoreSensorCode(Code.NIL), TypeMismatch.class);
	}
	
	@Test
	public void tLoadSensorCode() {
		Map<Symbol, FunctionType> funcTypes = new LinkedHashMap<Symbol, FunctionType>();
		funcTypes.put(Symbol.symbol("foo"), new FunctionType(
				BLongType.LONG_TYPE));
		setSensorCodeType(new CodeType(funcTypes));
		assertTypechecks(LoadSensorCode.LOAD, new CodeType(funcTypes));
	}
}
