package org.callas.util;

import java.util.Set;
import java.util.TreeSet;

import org.callas.absyn.processes.*;
import org.tyco.common.symbol.Symbol;

/**
 * Declares the bound names function.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 18, 2009
 * 
 */
public class BoundNames extends AbstractProcessVisitor<Set<Symbol>> {

	/**
	 * Computes the bound names for a given process.
	 * 
	 * @param proc
	 * @return
	 */
	public Set<Symbol> boundNames(CallasProcess proc) {
		return visit(proc);
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
		return new TreeSet<Symbol>();
	}
	
	@Override
	public Set<Symbol> caseClose(Close close) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseCode(Code code) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseExtern(Extern external) {
		return new TreeSet<Symbol>();
	}
	
	@Override
	public Set<Symbol> caseKill(Kill kill) {
		return new TreeSet<Symbol>();
	}
	
	@Override
	public Set<Symbol> caseOpen(Open open) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseUpdate(Update install) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseLet(Let let) {
		Set<Symbol> p1Syms = boundNames(let.inProcess);
		p1Syms.addAll(boundNames(let.value));
		p1Syms.add(let.variable.name);
		return p1Syms;
	}

	@Override
	public Set<Symbol> caseReceive(Receive recv) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseSend(Send send) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseTimer(Timer timer) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseVariable(Variable var) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseBranch(Branch branch) {
		Set<Symbol> boundNames = boundNames(branch.thenProc);
		boundNames.addAll(boundNames(branch.elseProc));
		return boundNames;
	}

	public Set<Symbol> boundNames(ProcessAbstraction value) {
		TreeSet<Symbol> names = new TreeSet<Symbol>();
		for (Variable var : value.parameters) {
			names.add(var.name);
		}
		names.addAll(boundNames(value.process));
		return names;
	}

	@Override
	public Set<Symbol> caseLoadSensorCode(LoadSensorCode load) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseStoreSensorCode(StoreSensorCode store) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseBinaryOperation(BinaryOperation binop) {
		return new TreeSet<Symbol>();
	}

	@Override
	public Set<Symbol> caseUnaryOperation(UnaryOperation uop) {
		return new TreeSet<Symbol>();
	}
}
