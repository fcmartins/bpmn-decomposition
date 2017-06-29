package structured;

public abstract class Type {
	
	public void accept (Visitor v) {
		v.visitType (this); 
	}
}
