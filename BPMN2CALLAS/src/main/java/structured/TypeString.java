package structured;

public class TypeString extends Type {
	@Override
	public String toString() {
		return "string";
	}
	@Override
	public void accept (Visitor v) {
		v.visitTypeString(this);
	}
}
