package org.callas.translate;

import static org.callas.translate.Functions.concat;
import static org.callas.translate.Functions.multiConcat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.callas.absyn.processes.*;
import org.callas.util.AbstractProcessVisitor;

public class Modules extends AbstractProcessVisitor<List<Code>> {

	@Override
	public List<Code> caseBBool(BBool bbool) {
		return new ArrayList<Code>();
	}

	@Override
	public List<Code> caseBDouble(BDouble bfloat) {
		return new ArrayList<Code>();
	}

	@Override
	public List<Code> caseBLong(BLong bint) {
		return new ArrayList<Code>();
	}

	@Override
	public List<Code> caseBString(BString bstring) {
		return new ArrayList<Code>();
	}

	@Override
	public List<Code> caseBinaryOperation(BinaryOperation binop) {
		return modulesValues(Arrays.asList(binop.left, binop.right));
	}

	@Override
	public List<Code> caseBranch(Branch branch) {
		List<List<Code>> result = new LinkedList<List<Code>>();
		
		result.add(modulesProc(branch.condition));
		result.add(modulesProc(branch.thenProc));
		result.add(modulesProc(branch.elseProc));
		
		return multiConcat(result);
	}

	@Override
	public List<Code> caseCall(Call call) {
		ArrayList<CallasValue> args = new ArrayList<CallasValue>();
		
		args.add(call.module);
		args.addAll(call.arguments);
		
		return modulesValues(args);
	}
	
	@Override
	public List<Code> caseClose(Close close) {
		return modulesProc(close.channel);
	}

	@Override
	public List<Code> caseCode(Code code) {
		List<List<Code>> result = new LinkedList<List<Code>>();
		
		result.add(Arrays.asList(code));
		
		for (ProcessAbstraction abs : code.processes.values()) {
			result.add(modulesProc(abs.process));
		}
		
		return multiConcat(result);
	}

	@Override
	public List<Code> caseExtern(Extern external) {
		return modulesValues(external.arguments);
	}
	
	@Override
	public List<Code> caseKill(Kill kill) {
		return modulesProc(kill.timerID);
	}

	@Override
	public List<Code> caseLet(Let let) {
		return concat(modulesProc(let.value), modulesProc(let.inProcess));
	}

	@Override
	public List<Code> caseLoadSensorCode(LoadSensorCode load) {
		return new LinkedList<Code>();
	}
	
	@Override
	public List<Code> caseOpen(Open open) {
		return modulesProc(open.channel);
	}

	@Override
	public List<Code> caseReceive(Receive recv) {
		return modulesProc(recv.channel);
	}

	@Override
	public List<Code> caseSend(Send send) {
		List<List<Code>> result = new LinkedList<List<Code>>();
		
		result.add(modulesProc(send.channel));
		result.add(modulesValues(send.arguments));
		
		return multiConcat(result);
	}

	@Override
	public List<Code> caseStoreSensorCode(StoreSensorCode store) {
		return modulesProc(store.code);
	}

	@Override
	public List<Code> caseTimer(Timer timer) {
		List<List<Code>> result = new LinkedList<List<Code>>();
		
		result.add(modulesValues(timer.arguments));
		result.add(modulesProc(timer.every));
		
		return multiConcat(result);
	}

	@Override
	public List<Code> caseUnaryOperation(UnaryOperation uop) {
		return modulesProc(uop.operand);
	}

	@Override
	public List<Code> caseUpdate(Update update) {
		return modulesValues(Arrays.asList(update.left, update.right));
	}

	@Override
	public List<Code> caseVariable(Variable var) {
		return new LinkedList<Code>();
	}

	// finish
	
	public List<Code> modules(Code code) {
		return visit(code);
	}

	public List<Code> modulesProc(CallasProcess proc) {
		return visit(proc);
	}

	private List<Code> modulesValues(List<CallasValue> list) {
		List<List<Code>> result = new LinkedList<List<Code>>();

		for (CallasValue val : list) {
			result.add(modulesProc(val));
		}
		
		return multiConcat(result);
	}

}
