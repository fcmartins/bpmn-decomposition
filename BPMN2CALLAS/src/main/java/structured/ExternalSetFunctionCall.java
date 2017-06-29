package structured;

import intermediate.AbSyn;

import java.util.List;

public class ExternalSetFunctionCall extends Command {
	public final String functionName;
	public final List<String> arguments;
	
	public ExternalSetFunctionCall(AbSyn elem, String functionName,
			List<String> arguments) {
		super(elem, ElementIds.nextId());
		this.functionName = functionName;
		this.arguments = arguments;
	}	
	
	@Override
	public void accept (Visitor v) {
		v.visitExternalSetFunctionCall(this);
	}
}
