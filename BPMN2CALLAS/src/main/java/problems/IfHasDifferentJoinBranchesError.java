package problems;

import bpmn2.TBaseElement;

public class IfHasDifferentJoinBranchesError extends BPMNError {

	private static final long serialVersionUID = -7091035964399817562L;

	public IfHasDifferentJoinBranchesError(TBaseElement e) {
		super(e);
	}

	public IfHasDifferentJoinBranchesError(String arg0, TBaseElement e) {
		super(arg0, e);
	}

	public IfHasDifferentJoinBranchesError(Throwable arg0, TBaseElement e) {
		super(arg0, e);
	}

	public IfHasDifferentJoinBranchesError(String arg0, Throwable arg1,
			TBaseElement e) {
		super(arg0, arg1, e);
	}

}
