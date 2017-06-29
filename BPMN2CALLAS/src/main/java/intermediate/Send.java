package intermediate;

import java.util.Set;

import bpmn2.TBaseElement;

public class Send extends Command {

	public Send(TBaseElement elem,  Set<Variable> in,  Set<Variable> out) {
		super(elem, in, out);
	}
	
	@Override
	public void accept(Visit v) {
		v.visitSend(this);
	}
}
