package org.callas.translate;

import java.util.*;
import java.util.Map.Entry;

import org.callas.absyn.processes.*;
import org.callas.absyn.processes.BinaryOperator;
import org.callas.absyn.processes.Timer;
import org.callas.absyn.processes.UnaryOperator;
import org.callas.util.AbstractProcessVisitor;
import org.callas.util.FreeNames;
import org.callas.vm.ast.*;
import org.tyco.common.api.ICompiler;
import org.tyco.common.symbol.Symbol;

public class ProcessTranslator extends
		AbstractProcessVisitor<List<CVMStmt>> implements
		ICompiler<CallasProcess, List<CVMStmt>> {

	private final List<Symbol> variables;
	private final List<CallasValue> symbols;
	private final FreeNames fn = new FreeNames();
	private final SizeOf sizeOf = new SizeOf();

	/**
	 * @param variables
	 * @param modules
	 * @param symbols
	 */
	public ProcessTranslator(List<Symbol> variables, List<CallasValue> symbols) {
		this.variables = variables;
		this.symbols = symbols;
	}

	public List<CVMStmt> compile(CallasProcess from) {
		return visit(from);
	}

	private List<CVMStmt> list(CVMStmt... stmts) {
		List<CVMStmt> result = new LinkedList<CVMStmt>();
		result.addAll(Arrays.asList(stmts));
		return result;
	}

	@Override
	public List<CVMStmt> caseBBool(BBool bbool) {
		return list(new CVMLoadConstant(indexOf(symbols, bbool)));
	}

	@Override
	public List<CVMStmt> caseBDouble(BDouble bfloat) {
		return list(new CVMLoadConstant(indexOf(symbols, bfloat)));
	}

	@Override
	public List<CVMStmt> caseBLong(BLong bint) {
		return list(new CVMLoadConstant(indexOf(symbols, bint)));
	}

	@Override
	public List<CVMStmt> caseBString(BString bstring) {
		return list(new CVMLoadConstant(indexOf(symbols, bstring)));
	}

	@Override
	public List<CVMStmt> caseBinaryOperation(BinaryOperation binop) {
		org.callas.vm.ast.BinaryOperator targetOp;
		switch (binop.operator) {
		case BOOL_AND: {
			targetOp = org.callas.vm.ast.BinaryOperator.BOOL_AND;
			break;
		}
		case LONG_AND: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_AND;
			break;
		}
		case LONG_OR: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_OR;
			break;
		}
		case LONG_DIFFERENT: {
			BinaryOperation eq = new BinaryOperation(binop.left,
					org.callas.absyn.processes.BinaryOperator.LONG_EQUALS,
					binop.right);
			UnaryOperation neq = new UnaryOperation(UnaryOperator.BOOL_NOT, eq);
			return compile(neq);
		}
		case DOUBLE_DIVISION: {
			targetOp = org.callas.vm.ast.BinaryOperator.DOUBLE_DIVISION;
			break;
		}
		case LONG_EQUALS: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_EQUALS;
			break;
		}
		case LONG_GREATER_THAN: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_GREATER_THAN;
			break;
		}
		case DOUBLE_GREATER_THAN: {
			targetOp = org.callas.vm.ast.BinaryOperator.DOUBLE_GREATER_THAN;
			break;
		}
		case LONG_GREATER_THAN_EQUALS: {
			BinaryOperation x_lt_y = new BinaryOperation(binop.left,
					BinaryOperator.LONG_LESS_THAN, binop.right);
			return compile(new UnaryOperation(UnaryOperator.BOOL_NOT, x_lt_y));
		}
		case LONG_DIVISION: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_DIVISION;
			break;
		}
		case LONG_LESS_THAN: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_LESS_THAN;
			break;
		}
		case DOUBLE_LESS_THAN: {
			targetOp = org.callas.vm.ast.BinaryOperator.DOUBLE_LESS_THAN;
			break;
		}
		case LONG_LESS_THAN_EQUALS: {
			BinaryOperation x_lt_y = new BinaryOperation(binop.left,
					BinaryOperator.LONG_GREATER_THAN, binop.right);
			return compile(new UnaryOperation(UnaryOperator.BOOL_NOT, x_lt_y));
		}
		case LONG_SUBTRACTION: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_SUBTRACTION;
			break;
		}
		case DOUBLE_SUBTRACTION: {
			targetOp = org.callas.vm.ast.BinaryOperator.DOUBLE_SUBTRACTION;
			break;
		}
		case LONG_MODULUS: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_REMAINDER;
			break;
		}
		case BOOL_OR: {
			targetOp = org.callas.vm.ast.BinaryOperator.BOOL_OR;
			break;
		}
		case LONG_ADDITION: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_ADDITION;
			break;
		}
		case DOUBLE_ADDITION: {
			targetOp = org.callas.vm.ast.BinaryOperator.DOUBLE_ADDITION;
			break;
		}
		case LONG_SHIFT_LEFT: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_SHIFT_LEFT;
			break;
		}
		case LONG_SHIFT_RIGHT: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_SHIFT_RIGHT;
			break;
		}
		case LONG_MULTIPLICATION: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_MULTIPLICATION;
			break;
		}
		case DOUBLE_MULTIPLICATION: {
			targetOp = org.callas.vm.ast.BinaryOperator.DOUBLE_MULTIPLICATION;
			break;
		}
		case BOOL_EXCLUSIVE_OR: {
			targetOp = org.callas.vm.ast.BinaryOperator.BOOL_EXCLUSIVE_OR;
			break;
		}
		case LONG_EXCLUSIVE_OR: {
			targetOp = org.callas.vm.ast.BinaryOperator.LONG_EXCLUSIVE_OR;
			break;
		}
		default: throw new IllegalArgumentException("Unsupported binary operation: " + binop.operator);
		}
		
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();
		
		stmts.addAll(compile(binop.right)); // v2
		stmts.addAll(compile(binop.left)); // v1
		stmts.add(new CVMBinaryOp(targetOp));
		
		return stmts;
	}

	@Override
	public List<CVMStmt> caseBranch(Branch branch) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();
		List<CVMStmt> thenTrans = compile(branch.thenProc);
		List<CVMStmt> elseTrans = compile(branch.elseProc);
		
		CVMGoto cvmGoto = new CVMGoto(sizeOf.sizeOf(thenTrans));

		stmts.addAll(compile(branch.condition));
		stmts.add(new CVMIfTrue(sizeOf.sizeOf(elseTrans) + sizeOf.sizeOf(cvmGoto)));
		stmts.addAll(elseTrans);
		stmts.add(cvmGoto);
		stmts.addAll(thenTrans);

		return stmts;
	}

	@Override
	public List<CVMStmt> caseCall(Call call) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		List<CallasValue> args = new ArrayList<CallasValue>(call.arguments
				.size() + 1);
		args.add(call.module);
		args.addAll(call.arguments);
		stmts.addAll(compileArgs(call.functionName, args));
		stmts.add(CVMCall.CALL);

		return stmts;
	}

	private Collection<CVMStmt> compileArgs(Symbol functionName,
			List<CallasValue> args) {
		List<CallasValue> reversedArgs = new ArrayList<CallasValue>(args);
		Collections.reverse(reversedArgs);

		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		for (CallasValue arg : reversedArgs) {
			stmts.addAll(compile(arg));
		}

		stmts.addAll(compile(new BString(functionName.toString())));

		return stmts;
	}

	private List<Symbol> order(Set<Symbol> set) {
		return new ArrayList<Symbol>(new TreeSet<Symbol>(set));
	}
	
	@Override
	public List<CVMStmt> caseClose(Close close) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		stmts.addAll(compile(close.channel));
		stmts.add(CVMClose.CLOSE);
		stmts.addAll(compile(Code.NIL));

		return stmts;
	}

	@Override
	public List<CVMStmt> caseCode(Code code) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		for (Entry<Symbol, ProcessAbstraction> entry : code.processes
				.entrySet()) {
			List<Symbol> names = order(fn.freeNames(entry.getValue()));
			List<CallasValue> args = new ArrayList<CallasValue>(names.size());
			for (Symbol name : names) {
				args.add(new Variable(name));
			}
			stmts.addAll(compileArgs(entry.getKey(), args));
		}
		stmts.add(new CVMLoadConstant(indexOf(symbols, code)));
		stmts.add(CVMLoadModule.LOADM);

		return stmts;
	}

	@Override
	public List<CVMStmt> caseExtern(Extern external) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		stmts.addAll(compileArgs(external.functionName, external.arguments));
		stmts.add(CVMExtern.EXTERN);
		return stmts;
	}
	
	@Override
	public List<CVMStmt> caseKill(Kill kill) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		stmts.addAll(compile(kill.timerID));
		stmts.add(CVMKill.KILL);
		stmts.addAll(compile(Code.NIL));

		return stmts;
	}

	@Override
	public List<CVMStmt> caseUpdate(Update update) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		stmts.addAll(compile(update.right)); // v2
		stmts.addAll(compile(update.left)); // v1
		stmts.add(CVMUpdate.UPDATE);

		return stmts;
	}

	@Override
	public List<CVMStmt> caseLet(Let let) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		List<Symbol> newVars = add(variables, let.variable);

		stmts.addAll(compile(let.value));
		stmts.add(new CVMStore(indexOf(newVars, let.variable.name)));
		ProcessTranslator newTransFunc = new ProcessTranslator(newVars, symbols);
		stmts.addAll(newTransFunc.compile(let.inProcess));

		return stmts;
	}

	private static List<Symbol> add(List<Symbol> vars, Variable variable) {
		List<Symbol> result = new ArrayList<Symbol>(vars);
		if (!vars.contains(variable.name)) {
			result.add(variable.name);
		}
		return result;
	}

	@Override
	public List<CVMStmt> caseLoadSensorCode(LoadSensorCode load) {
		return list(CVMLoadSensorCode.LOADB);
	}
	
	@Override
	public List<CVMStmt> caseOpen(Open open) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		stmts.addAll(compile(open.channel));
		stmts.add(CVMOpen.OPEN);
		stmts.addAll(compile(Code.NIL));

		return stmts;
	}

	@Override
	public List<CVMStmt> caseReceive(Receive recv) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();
		
		stmts.addAll(compile(recv.channel));
		stmts.add(CVMReceive.RECEIVE);
		stmts.addAll(compile(Code.NIL));

		return stmts;
	}

	@Override
	public List<CVMStmt> caseSend(Send send) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		stmts.addAll(compileArgs(send.functionName, send.arguments));
		stmts.addAll(compile(send.channel));
		stmts.add(CVMSend.SEND);
		stmts.addAll(compile(Code.NIL));

		return stmts;
	}

	@Override
	public List<CVMStmt> caseStoreSensorCode(StoreSensorCode store) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		stmts.addAll(compile(store.code));
		stmts.add(CVMStoreSensorCode.STOREB);
		stmts.addAll(compile(Code.NIL));

		return stmts;
	}

	@Override
	public List<CVMStmt> caseTimer(Timer timer) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		stmts.addAll(compile(timer.every));
		List<CallasValue> args = new ArrayList<CallasValue>(timer.arguments
				.size() + 1);
		args.add(timer.module);
		args.addAll(timer.arguments);
		stmts.addAll(compileArgs(timer.functionName, args));
		stmts.add(CVMTimer.TIMER);
		//stmts.addAll(compile(Code.NIL));
		return stmts;
	}

	@Override
	public List<CVMStmt> caseUnaryOperation(UnaryOperation uop) {
		org.callas.vm.ast.UnaryOperator targetOp;
		switch (uop.operator) {
		case BOOL_NOT: {
			targetOp = org.callas.vm.ast.UnaryOperator.BOOL_NOT;
			break;
		}
		case LONG_NOT: {
			targetOp = org.callas.vm.ast.UnaryOperator.LONG_NOT;
			break;
		}
		case LONG_NEGATION: {
			targetOp = org.callas.vm.ast.UnaryOperator.LONG_NEGATION;
			break;
		}
		case DOUBLE_NEGATION: {
			targetOp = org.callas.vm.ast.UnaryOperator.DOUBLE_NEGATION;
			break;
		}
		default: throw new IllegalArgumentException("Unsupported binary operation: " + uop.operator);
		}
		
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		stmts.addAll(compile(uop.operand));
		stmts.add(new CVMUnaryOp(targetOp));

		return stmts;
	}

	@Override
	public List<CVMStmt> caseVariable(Variable var) {
		List<CVMStmt> stmts = new LinkedList<CVMStmt>();

		stmts.add(new CVMLoad(indexOf(variables, var.name)));

		return stmts;
	}

	private static <T> byte indexOf(List<T> lst, T obj) {
		int indexOf = lst.indexOf(obj);
		if (indexOf < 0) {
			throw new IllegalArgumentException(String.format("'%s' not in %s", obj, lst));
		}
		return (byte) indexOf;
	}

}
