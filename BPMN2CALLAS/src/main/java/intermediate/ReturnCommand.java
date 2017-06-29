package intermediate;

import java.util.Set;

import bpmn2.TBaseElement;

public class ReturnCommand extends Command {

	public ReturnCommand(TBaseElement elem, Set<Variable> in, 
			Set<Variable> out) {
		super(elem, in, out);
	}
	
	@Override
	public void accept(Visit v) {
		v.visitReturnCommand(this);
	}
}
