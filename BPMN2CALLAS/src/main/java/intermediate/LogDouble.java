package intermediate;

import java.util.Set;

import bpmn2.TBaseElement;

public class LogDouble extends Command {
	
	public LogDouble(TBaseElement elem,  Set<Variable> in,  Set<Variable> out) {
		super(elem, in, out);
	}
	
	@Override
	public void accept(Visit v) {
		v.visitLogDouble(this);
	}
}
