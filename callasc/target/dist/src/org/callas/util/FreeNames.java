package org.callas.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.callas.absyn.processes.*;
import org.tyco.common.symbol.Symbol;

/**
 * Represents function <code>fn</code> for getting the free names of a process.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @author Rui Mendes        (rui.mendes@dcc.fc.up.pt)
 * @date Aug 17, 2009
 * 
 */
public class FreeNames extends AbstractProcessVisitor<Set<Symbol>> {

	/**
	 * Gets the free names of a given process.
	 * @param proc The target of the function. 
	 * @return The free names of the process.
	 */
	public Set<Symbol> freeNames(CallasProcess proc) {
		return visit(proc);
	}

	/**
	 * Gets the free names of a given process abstraction.
	 * @param proc The target of the function. 
	 * @return The free names of the process.
	 */
	public Set<Symbol> freeNames(ProcessAbstraction proc) {
		Set<Symbol> fn = freeNames(proc.process);
		fn.removeAll(toSymbols(proc.parameters));
		return fn;
	}

	private <S extends CallasProcess> Set<Symbol> freeNames(Iterable<S> procs,
			S... moreProcs) {
		Set<Symbol> syms = new TreeSet<Symbol>();
		for (S val : procs) {
			syms.addAll(freeNames(val));
		}
		for (S val : moreProcs) {
			syms.addAll(freeNames(val));
		}
		return syms;
	}

	/**
	 * Convert variables to symbols.
	 * 
	 * @param variables
	 * @return
	 */
	private Collection<Symbol> toSymbols(List<Variable> variables) {
		ArrayList<Symbol> syms = new ArrayList<Symbol>(variables.size());
		for (Variable var : variables) {
			syms.add(var.name);
		}
		return syms;
	}

	@Override
	public Set<Symbol> caseBBool(BBool bbool) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseBDouble(BDouble bfloat) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseBLong(BLong bint) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseBString(BString bstring) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseCall(Call call) {
		return freeNames(call.arguments, call.module);
	}
	
	@Override
	public Set<Symbol> caseClose(Close close) {
		return freeNames(close.channel);
	}

	@Override
	public Set<Symbol> caseCode(Code code) {
		Set<Symbol> syms = new TreeSet<Symbol>();
		for (ProcessAbstraction abs : code.processes.values()) {
			syms.addAll(freeNames(abs));
		}
		return syms;
	}

	@Override
	public Set<Symbol> caseExtern(Extern external) {
		return freeNames(external.arguments);
	}
	
	@Override
	public Set<Symbol> caseKill(Kill kill) {
		return freeNames(kill.timerID);
	}
	
	@Override
	public Set<Symbol> caseOpen(Open open) {
		return freeNames(open.channel);
	}

	@Override
	public Set<Symbol> caseUpdate(Update install) {
		Set<Symbol> syms = freeNames(install.left);
		syms.addAll(freeNames(install.right));
		return syms;
	}

	@Override
	public Set<Symbol> caseLet(Let let) {
		Set<Symbol> syms = freeNames(let.inProcess);
		syms.remove(let.variable.name);
		syms.addAll(freeNames(let.value));
		return syms;
	}

	@Override
	public Set<Symbol> caseReceive(Receive recv) {
		return freeNames(recv.channel);
	}

	@Override
	public Set<Symbol> caseSend(Send send) {
		return freeNames(send.arguments,send.channel);
	}

	@Override
	public Set<Symbol> caseTimer(Timer timer) {
		return freeNames(timer.arguments, timer.module, timer.every);
	}

	@Override
	public Set<Symbol> caseVariable(Variable var) {
		TreeSet<Symbol> syms = new TreeSet<Symbol>();
		syms.add(var.name);
		return syms;
	}

	@Override
	public Set<Symbol> caseBranch(Branch branch) {
		Set<Symbol> syms = freeNames(branch.condition);
		syms.addAll(freeNames(branch.thenProc));
		syms.addAll(freeNames(branch.elseProc));
		return syms;
	}

	@Override
	public Set<Symbol> caseLoadSensorCode(LoadSensorCode load) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseStoreSensorCode(StoreSensorCode store) {
		TreeSet<Symbol> syms = new TreeSet<Symbol>();
		syms.addAll(freeNames(store.code));
		return syms;
	}

	@Override
	public Set<Symbol> caseBinaryOperation(BinaryOperation binop) {
		TreeSet<Symbol> syms = new TreeSet<Symbol>();
		syms.addAll(freeNames(binop.left));
		syms.addAll(freeNames(binop.right));
		return syms;
	}

	@Override
	public Set<Symbol> caseUnaryOperation(UnaryOperation uop) {
		return freeNames(uop.operand);
	}
}
