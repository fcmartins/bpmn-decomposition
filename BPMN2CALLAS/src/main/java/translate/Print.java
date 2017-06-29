package translate;

import intermediate.*;
import intermediate.Process;

import java.util.HashSet;
import java.util.Set;



public class Print extends VisitDepthFirst {
	
	private Set<AbSyn> visitedNodes;
	private final String space = "   ";
	private int depth;
	
	public Print(){
		visitedNodes = new HashSet<AbSyn>();
		depth = 0;
	}
	
	@Override
	public void visitGuardedCommand(GuardedCommand gc) {
		if(visit(gc)) { 
			visitedNodes.add(gc);
			System.out.print("if (");			
			gc.guard.accept(this);
			System.out.print(") then\n");
			depth++;
			endVisit(gc);
			
			gc.cmd.accept(this);
			depth--;
		}

	}
	
	@Override
	public void visitIf(If i) {
		if (visit(i)) {
			visitedNodes.add(i);			
			for (GuardedCommand gc : i.getGuardedCommands()) {				
				gc.accept(this);
				System.out.print("else ");
			}
			i.getContinuation().accept(this);
			endVisit (i);
		}
	}
	
	
	@Override
	public void endVisit (ReturnCommand rc) {
		System.out.print("pass\n");
	}
	
	@Override
	public void endVisit(Process p) {
	}
	
	@Override
	public void visitSend (Send s) {
		if (visit (s)) {
			visitedNodes.add(s);
			printSpaces();
			System.out.print("send(");
			for (Variable v : s.inputVar)
				v.accept(this);
			System.out.print(")\n");
			endVisit(s);
		}
		s.getContinuation().accept(this);
	}
	
	@Override
	public void visitGetTemp(GetTemp g) {
		if (visit(g)) {
			visitedNodes.add(g);
			printSpaces();
			for(Variable v : g.outputVar)
				v.accept(this);
			System.out.print(" = extern getTemperature() \n");
		}
		endVisit (g);
		g.getContinuation().accept(this);
	}

	@Override
	public void endVisit (Value v) {
		System.out.print(v.name);
	}
	
	@Override
	public void endVisit(Variable v) {
		System.out.print(v.name);
	}
	
	@Override
	public void endVisit(VarDecl v) {
		System.out.print(v.type);
	}
	
	private void printSpaces() {
		for(int i = 0; i < depth; i++)
			System.out.print(space);
	}
}
