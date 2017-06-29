package problems;

import bpmn2.TBaseElement;

public class IfNotConverging extends BPMNError {

	private static final long serialVersionUID = -2630907113088977862L;

	public IfNotConverging(TBaseElement e) {
		super(e);
	}

	public IfNotConverging(String arg0, TBaseElement e) {
		super(arg0, e);
	}

	public IfNotConverging(Throwable arg0, TBaseElement e) {
		super(arg0, e);
	}

	public IfNotConverging(String arg0, Throwable arg1, TBaseElement e) {
		super(arg0, arg1, e);
	}

}
