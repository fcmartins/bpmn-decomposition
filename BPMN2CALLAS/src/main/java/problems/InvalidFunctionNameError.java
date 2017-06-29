package problems;

import bpmn2.TBaseElement;

public class InvalidFunctionNameError extends BPMNError {

	private static final long serialVersionUID = -8798088799107405643L;

	public InvalidFunctionNameError(String s, TBaseElement e) {
		super(s, e);
	}
}
