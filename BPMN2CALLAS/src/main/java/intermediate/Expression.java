package intermediate;

import bpmn2.TBaseElement;

public abstract class Expression extends AbSyn {
	
	public final String name;

	public Expression(TBaseElement elem, String n) {
		super(elem);
		name = n;
	}

}
