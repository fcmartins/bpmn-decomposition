package intermediate;

import java.util.Set;

import bpmn2.TBaseElement;

public class Receive extends Command {

	public Receive(TBaseElement elem, Set<Variable> in, Set<Variable> out) {
		super(elem, in, out);
	}
	
	@Override
	public void accept(Visit v) {
		v.visitReceive(this);
	}
}
