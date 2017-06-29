package structured;

import intermediate.AbSyn;

import java.util.List;

public class ExternalGetFunctionCall extends Command {

	public final String functionName;
	public final List<String> arguments;
	
	public ExternalGetFunctionCall(AbSyn elem, String functionName,
			List<String> arguments) {
		super(elem, ElementIds.nextId());
		this.functionName = functionName;
		this.arguments = arguments;
	}	
	
	@Override
	public void accept (Visitor v) {
		v.visitExternalGetFunctionCall(this);
	}
}
