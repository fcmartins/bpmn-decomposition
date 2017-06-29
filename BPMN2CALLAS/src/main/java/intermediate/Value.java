package intermediate;

import bpmn2.TBaseElement;

public class Value extends Expression implements Visitable {
	
	public Value(TBaseElement elem, String v) {
		super(elem, v);
	}
	
	@Override
	public void accept(Visit v) {
		v.visitValue(this);
	}
}
 		