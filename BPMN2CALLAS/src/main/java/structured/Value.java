package structured;

import intermediate.AbSyn;

public class Value extends Expression {
		
	public Value(AbSyn elem, String v) {
		super (elem, v);
	}	
	
	@Override
	public void accept (Visitor v) {
		v.visitValue(this);
	}
}
 		