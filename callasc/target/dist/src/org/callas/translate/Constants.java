package org.callas.translate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.callas.absyn.processes.*;
import org.callas.util.AbstractProcessVisitor;
import org.tyco.common.symbol.Symbol;

class Constants extends AbstractProcessVisitor<List<CallasValue>> {

	public List<CallasValue> constants(CallasProcess process) {
		return visit(process);
	}

	private List<CallasValue> list(CallasValue val) {
		return Arrays.asList(val);
	}

	@Override
	public List<CallasValue> caseBBool(BBool bbool) {
		return list(bbool);
	}

	@Override
	public List<CallasValue> caseBDouble(BDouble bfloat) {
		return list(bfloat);
	}

	@Override
	public List<CallasValue> caseBLong(BLong bint) {
		return list(bint);
	}

	@Override
	public List<CallasValue> caseBString(BString bstring) {
		return list(bstring);
	}

	@Override
	public List<CallasValue> caseBinaryOperation(BinaryOperation binop) {
		List<List<CallasValue>> result = new LinkedList<List<CallasValue>>();
		result.add(constants(binop.left));
		result.add(constants(binop.right));
		return Functions.multiConcat(result);
	}

	@Override
	public List<CallasValue> caseBranch(Branch branch) {
		List<List<CallasValue>> result = new LinkedList<List<CallasValue>>();
		result.add(constants(branch.condition));
		result.add(constants(branch.thenProc));
		result.add(constants(branch.elseProc));
		return Functions.multiConcat(result);
	}

	@Override
	public List<CallasValue> caseCall(Call call) {
		List<List<CallasValue>> result = new LinkedList<List<CallasValue>>();
		
		result.add(constants(call.module));
		result.add(constants(call.functionName));
		result.add(constants(call.arguments));
		
		return Functions.multiConcat(result);
	}

	private List<CallasValue> constants(Symbol sym) {
		return list(new BString(sym.toString()));
	}

	private List<CallasValue> constants(List<CallasValue> arguments) {
		List<CallasValue> result = new LinkedList<CallasValue>();

		for (CallasValue value : arguments) {
			result.addAll(constants(value));
		}

		return result;
	}
	
	@Override
	public List<CallasValue> caseClose(Close close) {
		return constants(close.channel);
	}

	@Override
	public List<CallasValue> caseCode(Code code) {
		List<List<CallasValue>> result = new LinkedList<List<CallasValue>>();
		result.add(list(code));
		for (Symbol sym : code.processes.keySet()) {
			result.add(constants(sym));
		}
		for (ProcessAbstraction proc : code.processes.values()) {
			result.add(constants(proc.process));
		}
		
		return Functions.multiConcat(result);
	}

	@Override
	public List<CallasValue> caseExtern(Extern external) {
		List<List<CallasValue>> result = new LinkedList<List<CallasValue>>();

		result.add(constants(external.functionName));
		result.add(constants(external.arguments));

		return Functions.multiConcat(result);
	}
	
	@Override
	public List<CallasValue> caseKill(Kill kill) {
		return constants(kill.timerID);
	}

	@Override
	public List<CallasValue> caseLet(Let let) {
		List<List<CallasValue>> result = new LinkedList<List<CallasValue>>();

		result.add(constants(let.value));
		result.add(constants(let.inProcess));

		return Functions.multiConcat(result);
	}

	@Override
	public List<CallasValue> caseLoadSensorCode(LoadSensorCode load) {
		return new LinkedList<CallasValue>();
	}
	
	@Override
	public List<CallasValue> caseOpen(Open open) {
		return constants(open.channel);
	}

	@Override
	public List<CallasValue> caseReceive(Receive recv) {
		return constants(recv.channel);
	}

	@Override
	public List<CallasValue> caseSend(Send send) {
		List<List<CallasValue>> result = new LinkedList<List<CallasValue>>();

		result.add(constants(send.channel));
		result.add(constants(send.functionName));
		result.add(constants(send.arguments));

		return Functions.multiConcat(result);
	}

	@Override
	public List<CallasValue> caseStoreSensorCode(StoreSensorCode store) {
		return constants(store.code);
	}

	@Override
	public List<CallasValue> caseTimer(Timer timer) {
		List<List<CallasValue>> result = new LinkedList<List<CallasValue>>();

		result.add(constants(timer.functionName));
		result.add(constants(timer.arguments));
		result.add(constants(timer.every));

		return Functions.multiConcat(result);
	}

	@Override
	public List<CallasValue> caseUnaryOperation(UnaryOperation uop) {
		return constants(uop.operand);
	}

	@Override
	public List<CallasValue> caseUpdate(Update update) {
		List<List<CallasValue>> result = new LinkedList<List<CallasValue>>();

		result.add(constants(update.left));
		result.add(constants(update.right));

		return Functions.multiConcat(result);
	}

	@Override
	public List<CallasValue> caseVariable(Variable var) {
		return new LinkedList<CallasValue>();
	}

}
