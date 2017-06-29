package problems;

import bpmn2.TBaseElement;

public class MissingDefaultCommandError extends BPMNError {

	private static final long serialVersionUID = -6349256344915069230L;

	public MissingDefaultCommandError(String s, TBaseElement e) {
		super(s, e);
	}

}
