package intermediate;

import java.util.Set;

import bpmn2.TBaseElement;

public abstract class Command  extends AbSyn {
	
	private Command continuation;

	public Command(TBaseElement elem,  Set<Variable> in,  Set<Variable> out) {
		super(elem, in, out);
		
	}
	
	public void setContinuation(Command c) {
		continuation = c;
	}
	
	public Command getContinuation() {
		return continuation;
	}
}
 	