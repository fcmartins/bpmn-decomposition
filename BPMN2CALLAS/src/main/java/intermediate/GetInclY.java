package intermediate;

import java.util.Set;

import bpmn2.TBaseElement;

public class GetInclY extends Command {
	public GetInclY(TBaseElement elem,  Set<Variable> in,  Set<Variable> out) {
		super(elem, in, out);
	}
	
	@Override
	public void accept(Visit v) {
		v.visitGetInclY(this);
	}
}
