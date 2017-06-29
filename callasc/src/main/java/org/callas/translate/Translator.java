package org.callas.translate;

import java.util.*;
import java.util.Map.Entry;

import org.callas.absyn.processes.*;
import org.callas.absyn.types.CodeType;
import org.callas.translate.Functions;
import org.callas.util.BoundNames;
import org.callas.util.FreeNames;
import org.callas.vm.ast.*;
import org.tyco.common.api.ICompiler;
import org.tyco.common.symbol.Symbol;

public class Translator implements ICompiler<CallasProcess, CVMModule> {
	
	private BoundNames bn = new BoundNames();
	private FreeNames fn = new FreeNames();

	private CVMModule translate(Code code) {
		Map<Symbol, CVMFunction> funcs = new TreeMap<Symbol, CVMFunction>();
		for (Entry<Symbol, ProcessAbstraction> entry : code.processes.entrySet()) {
			funcs.put(entry.getKey(), translate(entry.getValue()));
		}
		return new CVMModule(funcs);
	}

	private CVMFunction translate(ProcessAbstraction value) {
		
		List<CallasValue> syms = Functions.concat(Arrays.asList((CallasValue) Code.NIL), Functions.constants(value.process));
		
		List<Symbol> freeNames = Functions.sort(fn.freeNames(value));
		
		List<Symbol> vars = Functions.concat(Functions.getNames(value.parameters), freeNames);
		
		ICompiler<CallasProcess, List<CVMStmt>> compiler = new ProcessTranslator(vars, syms);
		Set<Symbol> boundNames = bn.boundNames(value);
		List<CVMStmt> stmts = compiler.compile(value.process);
		stmts.add(CVMReturn.RETURN);
		return new CVMFunction((byte) value.parameters.size(), (byte) freeNames.size(), (byte) (boundNames.size() + freeNames.size()), stmts, translate(syms));
	}
	
	private List<CVMValue> translate(List<CallasValue> syms) {
		ArrayList<CVMValue> result = new ArrayList<CVMValue>(syms.size());
		for (CallasValue val : syms) {
			CVMValue sym = null;
			if (val instanceof BBool) {
				sym = new CVMBBool(((BBool) val).value);
			} else if (val instanceof BDouble) {
				sym = new CVMBDouble(((BDouble) val).value);
			} else if (val instanceof BLong) {
				sym = new CVMBLong(((BLong) val).value);
			} else if (val instanceof BString) {
				sym = new CVMBString(((BString) val).value);
			} else if (val instanceof Code) {
				sym = translate((Code) val);
			} else {
				throw new IllegalArgumentException(val.getClass().toString());
			}
			result.add(sym);
		}
		return result;
	}

	public CVMModule compile(CallasProcess from) {
		List<Code> modules = Functions.modulesProc(from);
		return translate(modules.get(0));
	}

}
