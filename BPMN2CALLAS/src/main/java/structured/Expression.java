package structured;

import intermediate.AbSyn;

public abstract class Expression extends Command {
	
	public final String name;
	
	public Expression (AbSyn elem, String n) {
		super(elem, ElementIds.nextId());
		name = n;
	}
	
	public void accept (Visitor v) {
		v.visitExpression (this); 
	}
}
