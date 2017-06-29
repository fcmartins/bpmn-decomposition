package translate;

import intermediate.If;
import intermediate.Join;
import intermediate.Process;
import intermediate.VarDecl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import structured.Command;
import structured.IfStructured;

public class TranslateResponsible {

	
	private Stack <Command> stack;
	public final Map <If, Join> map;
	private Map <If, IfStructured> ifs;
	public final Set <VarDecl> varD;
	
	public TranslateResponsible (Map <If, Join> m, Set <VarDecl> v) {
		stack = new Stack<Command>();
		map = m;
		ifs = new HashMap <If, IfStructured>();
		varD = v;
	}
	
	public List<Command> translate (List<Process> processes) {
		for (Process p : processes) {
			p.accept(new ToStructured(stack, map, ifs, varD));
		}
		
//		for (If i : map.keySet()) {
//			Stack <Command> s = new Stack<Command>();	
//			map.get(i).accept(new ProceedJoin(s, map.get(i), map, ifs, varD));
//			addContinuationBlock (i, s);			
//		}
		
		return stack;
	}
	
	private void addContinuationBlock (If i, Stack <Command> st) {
		stack.peek().accept(new FinishStructure(st, ifs.get(i)));
	}
}
