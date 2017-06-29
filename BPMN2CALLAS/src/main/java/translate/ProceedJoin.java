package translate;

import intermediate.If;
import intermediate.Join;
import intermediate.VarDecl;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

import structured.Command;
import structured.IfStructured;

public class ProceedJoin extends ToStructured {
	public final Join toAnalyze;
	
	public ProceedJoin (Stack <Command> st, Join j, Map <If, Join> map, Map<If,IfStructured> ifs,
			Set <VarDecl> v) {
		super(st, map, ifs, v);
		toAnalyze = j;
	}	
	
	@Override
	public boolean visit (Join j) {
		return toAnalyze.equals(j);				
	}
	
	@Override
	public void visitJoin (Join j) {
		if (visit (j)) {
			j.getContinuation().accept(this);
			endVisit(j);
		}
	}
	
	@Override
	public void endVisit (Join j) {
		if (visit(j)) {
			for (Command c : block)
				translatedCommand.push(c);
		}
	}
		
	@Override
	public void visitIf (If i) {
		ToStructured toS = new ToStructured(translatedCommand, map, ifs, varsD);
		i.accept(toS);
	}
}
