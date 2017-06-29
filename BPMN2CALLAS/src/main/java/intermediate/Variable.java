package intermediate;

import bpmn2.TBaseElement;

public class Variable extends Expression implements Visitable {
	
	public Variable(TBaseElement elem, String n) {
		super(elem, n); 
	}
	
	@Override
	public void accept(Visit v) {
		v.visitVariable(this);
	}
}
