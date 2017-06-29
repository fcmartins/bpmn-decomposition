package intermediate;

import java.util.Set;

import bpmn2.TBaseElement;

public class GuardedCommand extends Command {
	

	public final Command cmd;
	public final Expression guard;

	public GuardedCommand(Expression e, Command c, TBaseElement elem,  
			Set<Variable> in,  Set<Variable> out) {
		super(elem, in, out);
		this.cmd = c;
		this.guard = e;
	}
	
	@Override
	public void accept(Visit v) {
		v.visitGuardedCommand(this);
	}
}
