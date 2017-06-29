package problems;

import bpmn2.TBaseElement;

public abstract class BPMNWarning {
	
	/**
	 * problems needed:
	 *    MissingInputVarError
	 *    MissingOutputVarError
	 *    MissingReturnCommandError
	 *    MissingDefaultCommandError
	 *    FunctionHasVarsError
	 *    ReturnCommandError
	 *    CycleFoundError
	 *    GatewayNotSupportedError
	 *    
	 */

	public final TBaseElement element;
	public final String warning;


	public BPMNWarning(String s, TBaseElement e){
		warning = s;
		element = e;
	}
	
}


