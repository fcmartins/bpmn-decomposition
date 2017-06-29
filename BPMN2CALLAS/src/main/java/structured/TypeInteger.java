package structured;

public class TypeInteger extends Type {
	@Override
	public String toString() {
		return "int";
	}
	@Override
	public void accept (Visitor v) {
		v.visitTypeInteger(this);
	}
}
