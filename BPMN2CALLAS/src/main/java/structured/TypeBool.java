package structured;

public class TypeBool extends Type {
	
	@Override
	public String toString() {
		return "bool";
	}
	
	@Override
	public void accept (Visitor v) {
		v.visitTypeBool(this);
	}
}
