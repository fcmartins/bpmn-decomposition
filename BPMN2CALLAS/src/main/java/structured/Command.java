package structured;

import intermediate.AbSyn;

public abstract class Command {
	
	public final AbSyn element;
	public final int id; 
	
	public Command(int i) {
		element = null;
		id = i;
	}
	
	public Command (AbSyn elem, int i) {
		element = elem;
		id = i;
	}
	
	public void accept (Visitor v) {
		v.visitCommand(this);
	}

}
