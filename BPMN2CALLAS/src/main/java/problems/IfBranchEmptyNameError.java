package problems;

import bpmn2.TBaseElement;

public class IfBranchEmptyNameError extends BPMNError {

	private static final long serialVersionUID = -4231873001113332298L;

	public IfBranchEmptyNameError(TBaseElement e) {
		super(e);
	}

	public IfBranchEmptyNameError(String arg0, TBaseElement e) {
		super(arg0, e);
	}

	public IfBranchEmptyNameError(Throwable arg0, TBaseElement e) {
		super(arg0, e);
	}

	public IfBranchEmptyNameError(String arg0, Throwable arg1, TBaseElement e) {
		super(arg0, arg1, e);
	}

}
