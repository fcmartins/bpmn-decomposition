package intermediate;

import java.util.Set;

import bpmn2.TBaseElement;

public class GetAccelX extends Command {

	public GetAccelX(TBaseElement elem,  Set<Variable> in,  Set<Variable> out) {
		super(elem, in, out);
	}
	
	@Override
	public void accept(Visit v) {
		v.visitGetAccelX(this);
	}
}
