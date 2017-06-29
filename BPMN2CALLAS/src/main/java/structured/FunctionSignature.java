package structured;

import java.util.List;

public class FunctionSignature {

	public final String name;
	public final List<VarDecl> arguments;
	
	public FunctionSignature(String name, List<VarDecl> arguments) {
		this.name = name;
		this.arguments = arguments;
	}
	
	public String toString() {
		return "(" + name + ")";
	}
	
}
