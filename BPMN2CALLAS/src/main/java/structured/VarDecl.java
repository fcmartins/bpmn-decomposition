package structured;

public class VarDecl {
	public final Variable var;
	public final Type type;
	
	public VarDecl(Variable v, Type t) {
		this.var = v;
		this.type = t;
	}	
	
	public void accept (Visitor v) {
		v.visitVarDecl(this);
	}
}
