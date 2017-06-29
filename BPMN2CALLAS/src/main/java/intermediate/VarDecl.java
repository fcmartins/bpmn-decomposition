package intermediate;

import bpmn2.TDataObject;

public class VarDecl extends AbSyn {
	public final Variable var;
	public final Type type;
	
	public VarDecl(Variable v, Type t, TDataObject dt) {
		super(dt);
		this.var = v;
		this.type = t;
	}
	
	@Override
	public void accept(Visit v) {
		v.visitVarDecl(this);
	}
}
