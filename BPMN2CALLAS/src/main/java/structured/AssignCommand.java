package structured;

import intermediate.AbSyn;

public class AssignCommand extends Command {
	
	public final VarDecl lhs;  // variable
	public final ExternalGetFunctionCall rhs;  // expression
	
	public AssignCommand(AbSyn elem, VarDecl lhs, ExternalGetFunctionCall rhs) {
		super(elem, ElementIds.nextId());
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	@Override
	public void accept (Visitor v) {
		v.visitAssignCommand(this);
	}
}
