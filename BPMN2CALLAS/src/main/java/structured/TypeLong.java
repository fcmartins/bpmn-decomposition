package structured;

public class TypeLong extends Type {
	@Override
	public String toString() {
		return "long";
	}
	@Override
	public void accept (Visitor v) {
		v.visitTypeLong(this);
	}
}
