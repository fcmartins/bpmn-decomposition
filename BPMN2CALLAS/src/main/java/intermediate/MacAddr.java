package intermediate;

import java.util.Set;

import bpmn2.TBaseElement;

public class MacAddr extends Command {

	public MacAddr(TBaseElement elem,  Set<Variable> in,  Set<Variable> out) {
		super(elem, in, out);
	}
	
	@Override
	public void accept(Visit v) {
		v.visitMacAddr(this);
	}
}
