package org.callas.semant;

import java.util.*;
import java.util.Map.Entry;

import org.callas.absyn.Absyn;
import org.callas.absyn.processes.Timer;
import org.callas.absyn.processes.*;
import org.callas.absyn.types.*;
import org.callas.error.*;
import org.callas.util.DifferentTypesException;
import org.callas.util.TypeEquality;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.symtable.KeyNotFoundException;
import org.tyco.common.symtable.Scope;
import org.tyco.common.util.Functional;
import org.tyco.common.util.Pair;

/**
 * The implementation of typechecking rules for processes.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @author Rui Mendes        (rui.mendes@dcc.fc.up.pt)
 * @date Oct 9, 2009
 * 
 */
class ProcessTypingRulesImpl extends AbstractProcessTypingRules {

	private final TypeEquality typeMatch = new TypeEquality();
	private final CodeType sensorCodeType;

	public ProcessTypingRulesImpl(Scope<Symbol, CallasType> baseScope,
			List<ErrorMessage> errors, CodeType sensorCodeType) {
		super(baseScope, errors);
		this.sensorCodeType = sensorCodeType;
	}

	private void report(ErrorMessage err) {
		getErrors().add(err);
	}

	private void ensureEqualTypes(Absyn where, CallasType expected,
			CallasType provided) {
		try {
			typeMatch.areEqual(expected, provided);
		} catch (DifferentTypesException e) {
			report(new TypeMismatch(where, e));
		}
	}

	public BBoolType tBBool(Scope<Symbol, CallasType> moduleScope, BBool bbool) {
		return BBoolType.BOOL_TYPE;
	}

	public BDoubleType tBDouble(Scope<Symbol, CallasType> moduleScope,
			BDouble bfloat) {
		return BDoubleType.DOUBLE_TYPE;
	}

	public BLongType tBLong(Scope<Symbol, CallasType> moduleScope, BLong bint) {
		return BLongType.LONG_TYPE;
	}

	public BStringType tBString(Scope<Symbol, CallasType> moduleScope,
			BString bstring) {
		return BStringType.STRING_TYPE;
	}

	public CodeType tCode(Scope<Symbol, CallasType> moduleScope, Code module) {
		if (!module.processes.keySet().equals(module.type.functions.keySet())) {
			report(new LabelsDiffer(module));
			return module.type;
		}
		for (Entry<Symbol, ProcessAbstraction> entry : module.processes
				.entrySet()) {
			ProcessAbstraction proc = entry.getValue();
			FunctionType funcType = module.type.functions.get(entry.getKey());
			if (funcType.parameters.size() != proc.parameters.size()) {
				report(new ArgumentCountMismatch(proc.getSourceLocation(),
						entry.getKey(), funcType.parameters.size(),
						proc.parameters.size()));
				return module.type;
			}
			Scope<Symbol, CallasType> funcScope = moduleScope.createChild();
			for (Pair<Variable, CallasType> pair : Functional.join(
					proc.parameters, funcType.parameters)) {
				funcScope.replace(pair.getLeft().name, pair.getRight());
			}
			CallasType resultType = typecheck(funcScope, proc.process);
			ensureEqualTypes(proc.process, funcType.result, resultType);
		}
		return module.type;
	}

	public CallasType tVariable(Scope<Symbol, CallasType> moduleScope,
			Variable var) {
		try {
			return moduleScope.get(var.name);
		} catch (KeyNotFoundException e) {
			report(UndefinedSymbol.undefinedVariable(var));
			return CodeType.NIL_TYPE;
		}
	}

	public CallasType tCall(Scope<Symbol, CallasType> moduleScope, Call call) {
		CallasType type = unfold(typecheck(moduleScope, call.module));
		if (!(type instanceof CodeType)) {
			report(new ClassOfTypeMismatch(call.module, CodeType.class, type));
			return CodeType.NIL_TYPE;
		}
		CodeType codeType = (CodeType) type;

		Scope<Symbol, CallasType> lblScope = moduleScope.createChild();
		FunctionType functionType = codeType.functions.get(call.functionName);
		if (functionType != null) {
			lblScope.replace(call.functionName, functionType);
		}
		ArrayList<CallasValue> args = new ArrayList<CallasValue>(call.arguments
				.size() + 1);
		args.add(call.module);
		args.addAll(call.arguments);

		return typecheckCallLike(moduleScope, lblScope, call, call.functionName,
				args, false);
	}
	
	public CodeType tClose(Scope<Symbol, CallasType> moduleScope,
			Close close) {
		CallasType t = typecheck(moduleScope, close.channel);
		CallasType ut = unfold(t);
		if (!(ut instanceof BStringType)) {
			report(new ClassOfTypeMismatch(close.channel, BStringType.class,
					ut));
			return CodeType.NIL_TYPE;
		}
		return CodeType.NIL_TYPE;
	}

	private CallasType typecheckCallLike(Scope<Symbol, CallasType> moduleScope,
			Scope<Symbol, CallasType> labelScope, Absyn where, Symbol label,
			List<CallasValue> arguments, boolean discardFirstParameter) {
		final CallasType type;
		try {
			type = labelScope.get(label);
		} catch (KeyNotFoundException e) {
			report(UndefinedSymbol.undefinedLabel(where.getSourceLocation(),
					label));
			return CodeType.NIL_TYPE;
		}
		if (!(type instanceof FunctionType)) {
			report(new ClassOfTypeMismatch(where, FunctionType.class, type));
			return CodeType.NIL_TYPE;
		}
		FunctionType typeSignature = (FunctionType) type;
		final List<CallasType> params;
		if (discardFirstParameter) {
			// an external call or a timed call
			params = new ArrayList<CallasType>(typeSignature.parameters);
			params.remove(0);
		} else {
			params = typeSignature.parameters;
		}
		int expectedSize = params.size();
		if (arguments.size() != expectedSize) {
			report(new ArgumentCountMismatch(where, expectedSize, arguments
					.size()));
			return CodeType.NIL_TYPE;
		}

		// type-check \vec{v}
		List<CallasType> vs = tSeq(moduleScope, arguments);

		// verify if each the types match
		Iterable<Pair<CallasType, CallasType>> pairs = Functional.join(params,
				vs);
		Iterator<CallasValue> valsIter = arguments.iterator();
		for (Pair<CallasType, CallasType> pair : pairs) {
			ensureEqualTypes(valsIter.next(), pair.getLeft(), pair.getRight());
		}

		// return the resulting type of 'l', t_j
		return typeSignature.result;
	}

	public CallasType tExtern(Scope<Symbol, CallasType> moduleScope,
			Extern extern) {
		return typecheckCallLike(moduleScope, getBaseScope(), extern,
				extern.functionName, extern.arguments, false);
	}
	
	public CodeType tKill(Scope<Symbol, CallasType> moduleScope,
			Kill kill) {
		CallasType t = typecheck(moduleScope, kill.timerID);
		CallasType ut = unfold(t);
		if (!(ut instanceof BStringType)) {
			report(new ClassOfTypeMismatch(kill.timerID, BStringType.class,
					ut));
			return CodeType.NIL_TYPE;
		}
		return CodeType.NIL_TYPE;
	}
	
	public CallasType tOpen(Scope<Symbol, CallasType> moduleScope,
			Open open) {
		CallasType t = typecheck(moduleScope, open.channel);
		CallasType ut = unfold(t);
		if (!(ut instanceof BStringType)) {
			report(new ClassOfTypeMismatch(open.channel, BStringType.class,
					ut));
			return CodeType.NIL_TYPE;
		}
		return CodeType.NIL_TYPE;
	}

	public CallasType tUpdate(Scope<Symbol, CallasType> moduleScope,
			Update update) {
		CallasType t1 = typecheck(moduleScope, update.left);
		CallasType t1Unfold = unfold(t1);
		if (!(t1Unfold instanceof CodeType)) {
			report(new ClassOfTypeMismatch(update.left, CodeType.class,
					t1Unfold));
			return CodeType.NIL_TYPE;
		}
		CodeType code1 = (CodeType) t1Unfold;

		CallasType t2 = typecheck(moduleScope, update.right);
		CallasType t2Unfold = unfold(t2);
		if (!(t2Unfold instanceof CodeType)) {
			report(new ClassOfTypeMismatch(update.right, CodeType.class,
					t2Unfold));
			return CodeType.NIL_TYPE;
		}
		CodeType code2 = (CodeType) t2Unfold;

		ensuresIntersectionIsEqual(update, code1, code2);
		return union(code1, code2);
	}

	/**
	 * Make sure the given code types are compatible.
	 * 
	 * @param where
	 * @param t1
	 * @param t2
	 */
	private void ensuresIntersectionIsEqual(Absyn where, CodeType t1,
			CodeType t2) {
		// get the intersection of labels from a1 and a2
		Set<Symbol> labelIntersection = new TreeSet<Symbol>(t1.functions
				.keySet());
		labelIntersection.retainAll(t2.functions.keySet());

		// make sure each function type in the intersection is equal
		for (Symbol label : labelIntersection) {
			FunctionType f1 = eraseFirstParameter(t1.functions.get(label));
			FunctionType f2 = eraseFirstParameter(t2.functions.get(label));
			ensureEqualTypes(where, f1, f2);
		}
	}

	/**
	 * Creates a union of the two given code types.
	 * 
	 * @param t1
	 * @param t2
	 * @return A new recursive type over code type.
	 */
	private static CallasType union(CodeType t1, CodeType t2) {
		Map<Symbol, FunctionType> functions = new HashMap<Symbol, FunctionType>();
		TypeVariable var = new TypeVariable("X");
		functions.putAll(replaceFirstParameterBy(t1.functions, var));
		functions.putAll(replaceFirstParameterBy(t2.functions, var));
		return new RecursiveType(var, new CodeType(functions));
	}

	/**
	 * Creates a map whose components are equal to the ones in
	 * <code>functions</code> with the first parameter of each function replaced
	 * by <code>var</code>.
	 * 
	 * @param functions
	 *            The functions to use.
	 * @param var
	 *            The new first parameter of functions.
	 * @return A new map.
	 */
	private static Map<Symbol, FunctionType> replaceFirstParameterBy(
			Map<Symbol, FunctionType> functions, TypeVariable var) {
		Map<Symbol, FunctionType> result = new TreeMap<Symbol, FunctionType>();
		for (Entry<Symbol, FunctionType> entry : functions.entrySet()) {
			FunctionType func = entry.getValue();
			ArrayList<CallasType> params = new ArrayList<CallasType>(func.parameters);
			if (params.isEmpty()) {
				throw new IllegalArgumentException(func.parameters.toString());
			}
			params.set(0, var);
			SourceLocation loc = func.getSourceLocation();
			CallasType res = func.result;
			result.put(entry.getKey(), new FunctionType(loc, params, res));
		}
		return result;
	}

	/**
	 * Unfolds the given type.
	 * 
	 * @param type
	 * @return A new unfolded type (or the same if already unfolded).
	 */
	private CallasType unfold(CallasType type) {
		return typeMatch.unfold(type);
	}

	/**
	 * Erases the first parameter of a function type.
	 * 
	 * @param funcType
	 * @return
	 */
	private static FunctionType eraseFirstParameter(FunctionType funcType) {
		assert funcType.parameters.size() > 0;
		List<CallasType> params = funcType.parameters.subList(1,
				funcType.parameters.size());
		funcType = new FunctionType(funcType.getSourceLocation(), params,
				funcType.result);
		return funcType;
	}

	public CodeType tReceive(Scope<Symbol, CallasType> moduleScope,
			Receive receive) {
		CallasType t = typecheck(moduleScope, receive.channel);
		CallasType ut = unfold(t);
		if (!(ut instanceof BStringType)) {
			report(new ClassOfTypeMismatch(receive.channel, BStringType.class,
					ut));
			return CodeType.NIL_TYPE;
		}
		return CodeType.NIL_TYPE;
	}

	public CodeType tSend(Scope<Symbol, CallasType> moduleScope, Send send) {
		CallasType t = typecheck(moduleScope, send.channel);
		CallasType ut = unfold(t);
		if (!(ut instanceof BStringType)) {
			report(new ClassOfTypeMismatch(send.channel, BStringType.class,
					ut));
			return CodeType.NIL_TYPE;
		}
		CallasType resultType = typecheckCallLike(moduleScope, moduleScope,
				send, send.functionName, send.arguments, true);
		ensureEqualTypes(send, CodeType.NIL_TYPE, resultType);
		return CodeType.NIL_TYPE;
	}

	public BStringType tTimer(Scope<Symbol, CallasType> moduleScope, Timer timer) {
		CallasType type = unfold(typecheck(moduleScope, timer.module));
		if (!(type instanceof CodeType)) {
			report(new ClassOfTypeMismatch(timer.module, CodeType.class, type));
			return BStringType.STRING_TYPE;
		}
		CodeType codeType = (CodeType) type;

		Scope<Symbol, CallasType> lblScope = moduleScope.createChild();
		FunctionType functionType = codeType.functions.get(timer.functionName);
		if (functionType != null) {
			lblScope.replace(timer.functionName, functionType);
		}
		ArrayList<CallasValue> args = new ArrayList<CallasValue>(timer.arguments.size() + 1);
		args.add(timer.module);
		args.addAll(timer.arguments);
		
		CallasType resultType = typecheckCallLike(moduleScope, lblScope,
				timer, timer.functionName, args, false);
		ensureEqualTypes(timer, CodeType.NIL_TYPE, resultType);
		return BStringType.STRING_TYPE;
	}

	public CallasType tBranch(Scope<Symbol, CallasType> moduleScope,
			Branch branch) {
		CallasType condType = typecheck(moduleScope, branch.condition);
		ensureEqualTypes(branch.condition, BBoolType.BOOL_TYPE, condType);
		CallasType thenType = typecheck(moduleScope, branch.thenProc);
		CallasType elseType = typecheck(moduleScope, branch.elseProc);
		ensureEqualTypes(branch.elseProc, thenType, elseType);
		return thenType;
	}

	public CallasType tLet(Scope<Symbol, CallasType> moduleScope, Let let) {
		// Typify P1
		CallasType tau1 = typecheck(moduleScope, let.value);

		// create a new type environment
		Scope<Symbol, CallasType> inScope = moduleScope.createChild();
		inScope.replace(let.variable.name, tau1);

		return typecheck(inScope, let.inProcess);
	}

	public CallasType tLoadSensorCode(Scope<Symbol, CallasType> moduleScope,
			LoadSensorCode load) {
		return sensorCodeType;
	}

	public CallasType tStoreSensorCode(Scope<Symbol, CallasType> moduleScope,
			StoreSensorCode store) {
		CallasType t2 = typecheck(moduleScope, store.code);
		CallasType moduleType = unfold(t2);
		if (!(moduleType instanceof CodeType)) {
			report(new ClassOfTypeMismatch(store.code, CodeType.class,
					moduleType));
			return CodeType.NIL_TYPE;
		}
		ensureEqualTypes(store, sensorCodeType, moduleType);
		return CodeType.NIL_TYPE;
	}

	public CallasType tBinaryOperation(Scope<Symbol, CallasType> moduleScope,
			BinaryOperation binop) {
		final CallasType leftType = typecheck(moduleScope, binop.left);
		final CallasType rightType = typecheck(moduleScope, binop.right);
		final CallasType argsType;
		final CallasType resultType;
		switch (binop.operator) {
		case LONG_AND:
		case LONG_OR:
		case LONG_SHIFT_LEFT:
		case LONG_SHIFT_RIGHT:
		case LONG_MODULUS:
		case LONG_DIVISION:
		case LONG_ADDITION:
		case LONG_EXCLUSIVE_OR:
		case LONG_MULTIPLICATION:
		case LONG_SUBTRACTION:
			argsType = BLongType.LONG_TYPE;
			resultType = BLongType.LONG_TYPE;
			break;
		case DOUBLE_DIVISION:
		case DOUBLE_ADDITION:
		case DOUBLE_MULTIPLICATION:
		case DOUBLE_SUBTRACTION:
			argsType = BDoubleType.DOUBLE_TYPE;
			resultType = BDoubleType.DOUBLE_TYPE;
			break;
		case DOUBLE_LESS_THAN:
		case DOUBLE_GREATER_THAN:
			argsType = BDoubleType.DOUBLE_TYPE;
			resultType = BBoolType.BOOL_TYPE;
			break;
			
		case LONG_DIFFERENT:
		case LONG_EQUALS:
		case LONG_GREATER_THAN:
		case LONG_GREATER_THAN_EQUALS:
		case LONG_LESS_THAN:
		case LONG_LESS_THAN_EQUALS:
			argsType = BLongType.LONG_TYPE;
			resultType = BBoolType.BOOL_TYPE;
			break;
			
		case BOOL_AND:
		case BOOL_EXCLUSIVE_OR:
		case BOOL_OR:
			argsType = BBoolType.BOOL_TYPE;
			resultType = BBoolType.BOOL_TYPE;
			break;
		default:
			throw new IllegalArgumentException();
		}
		ensureEqualTypes(binop, argsType, rightType);
		ensureEqualTypes(binop, argsType, leftType);
		return resultType;
	}

	public CallasType tUnaryOperation(Scope<Symbol, CallasType> moduleScope,
			UnaryOperation uop) {
		CallasType valueType = typecheck(moduleScope, uop.operand);
		final CallasType resultType;
		switch (uop.operator) {
		case BOOL_NOT:
			resultType = BBoolType.BOOL_TYPE;
			break;
		case LONG_NOT:
		case LONG_NEGATION:
			resultType = BLongType.LONG_TYPE;
			break;
		default:
			throw new IllegalArgumentException();
		}

		ensureEqualTypes(uop, resultType, valueType);
		return resultType;
	}
}
