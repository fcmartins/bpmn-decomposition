package org.callas.vm;


public class Call {
	public final String functionName;
	public Object[] arguments;
	/**
	 * @param functionName
	 * @param arguments
	 */
	public Call(String functionName, Object[] arguments) {
		this.functionName = functionName;
		this.arguments = arguments;
	}
	
	public void addModule(Module module) {
		Object[] args = new Object[this.arguments.length + 1];
		args[0] = module;
		for (int i=1; i<=this.arguments.length; i++)
			args[i] = this.arguments[i-1];
		this.arguments = args;
	}

}
