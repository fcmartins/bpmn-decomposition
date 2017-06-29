package structured;

public class TypeDouble extends Type {
	@Override
	public String toString() {
		return "double";
	}
	@Override
	public void accept (Visitor v) {
		v.visitTypeDouble(this);
	}
}
