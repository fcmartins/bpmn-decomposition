package problems;

import bpmn2.TBaseElement;

public class HasOutputVariableError extends BPMNError {

	private static final long serialVersionUID = 7963842031766521874L;

	public HasOutputVariableError(TBaseElement e) {
		super(e);
	}

	public HasOutputVariableError(String arg0, TBaseElement e) {
		super(arg0, e);
	}

	public HasOutputVariableError(Throwable arg0, TBaseElement e) {
		super(arg0, e);
	}

	public HasOutputVariableError(String arg0, Throwable arg1, TBaseElement e) {
		super(arg0, arg1, e);
	}

}
