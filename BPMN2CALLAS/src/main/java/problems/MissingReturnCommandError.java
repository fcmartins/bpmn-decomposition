package problems;

import bpmn2.TBaseElement;

public class MissingReturnCommandError extends BPMNError {

	private static final long serialVersionUID = -5593583666289155935L;

	public MissingReturnCommandError(String s, TBaseElement e) {
		super(s, e);
	}

}
