package structured;

import intermediate.AbSyn;

import java.util.List;

public class SendFunctionCall extends Command {
	
	public final String functionName;
//	public final List<Expression> arguments;
	public final List<VarDecl> arguments;

	public SendFunctionCall(AbSyn elem, String functionName,
			List<VarDecl> arguments) {
		super(elem, ElementIds.nextId());
		this.functionName = functionName;
		this.arguments = arguments;
	}
	
	@Override
	public void accept (Visitor v) {
		v.visitSendFunctionCall(this);
	}
}
