package intermediate;

import java.util.Set;

import bpmn2.TBaseElement;

public class GetAccelZ extends Command {
	public GetAccelZ(TBaseElement elem,  Set<Variable> in,  Set<Variable> out) {
		super(elem, in, out);
	}
	
	@Override
	public void accept(Visit v) {
		v.visitGetAccelZ(this);
	}
}
