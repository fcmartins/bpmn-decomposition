package org.callas.translate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.callas.absyn.processes.Code;
import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.processes.Variable;
import org.tyco.common.symbol.Symbol;

class Functions {

	public static List<Symbol> getNames(Iterable<Variable> variables) {
		LinkedList<Symbol> syms = new LinkedList<Symbol>();
		
		for (Variable var : variables) {
			syms.add(var.name);
		}
		
		return syms;
	}
	
	public static List<Symbol> sort(Set<Symbol> names) {
		return new ArrayList<Symbol>(new TreeSet<Symbol>(names));
	}
	
	public static <T> List<T> concat(List<T> left, List<T> right) {
		ArrayList<T> result = new ArrayList<T>(left.size() + right.size());
		result.addAll(left);
		for (T obj : right) {
			if (! result.contains(obj)) {
				result.add(obj);
			}
		}
		return result;
	}

	public static <T> List<T> multiConcat(List<List<T>> lists) {
		List<T> result = new LinkedList<T>();
		
		for (List<T> list : lists) {
			result = concat(result, list);
		}
		return result;
	}
	
	public static <T> List<T> concat(List<T> left, T... right) {
		return concat(left, Arrays.asList(right));
	}
	
	public static List<CallasValue> constants(CallasProcess process) {
		return new Constants().constants(process);
	}
	/*
	public static List<Code> modules(Code code) {
		return new Modules().modules(code);
	}*/
	
	public static List<Code> modulesProc(CallasProcess process) {
		return new Modules().modulesProc(process);
	}
}
