package structured;

import intermediate.AbSyn;

public class IfStructured extends Command {
	public final Expression guard;
	public final Command thenBranches;
	public final Command elseBranch;
	
	public IfStructured(AbSyn elem, Expression guard, Command thenBranch,
			Command elseBranch) {
		super(elem, ElementIds.nextId());
		this.guard = guard;
		this.thenBranches = thenBranch;
		this.elseBranch = elseBranch;
	}

	@Override
	public void accept (Visitor v) {
		v.visitIfStructured(this);
	}
}
