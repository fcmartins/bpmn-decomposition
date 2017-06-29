package structured;

import intermediate.AbSyn;

public class Variable extends Expression {

	public Variable(AbSyn elem, String n){
		super (elem, n);
	}
	
	@Override
	public void accept (Visitor v) {
		v.visitVariable(this);
	}
}
