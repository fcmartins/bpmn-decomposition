package problems;

import bpmn2.TBaseElement;

public class HasInputVariableError extends BPMNError {

	private static final long serialVersionUID = 3451688245915045156L;

	public HasInputVariableError(TBaseElement e) {
		super(e);
	}

	public HasInputVariableError(String arg0, TBaseElement e) {
		super(arg0, e);
	}

	public HasInputVariableError(Throwable arg0, TBaseElement e) {
		super(arg0, e);
	}

	public HasInputVariableError(String arg0, Throwable arg1, TBaseElement e) {
		super(arg0, arg1, e);
	}

}
