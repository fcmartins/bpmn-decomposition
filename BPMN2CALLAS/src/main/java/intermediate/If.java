package intermediate;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import bpmn2.TBaseElement;

public class If extends Command {
	

	private List<GuardedCommand> guardedCommands;
	
	public If(TBaseElement elem,  Set<Variable> in,  Set<Variable> out) {
		super(elem, in, out);
		guardedCommands = new LinkedList<GuardedCommand>();
	}
	
	public void addGuardedCommand(GuardedCommand cl) {
		this.guardedCommands.add(cl);
	}
	
	public List<GuardedCommand> getGuardedCommands() {
		return guardedCommands;
	}

	
	@Override
	public void accept(Visit v) {
		v.visitIf(this);
	}
}
