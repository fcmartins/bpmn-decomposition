package problems;

import bpmn2.TBaseElement;

public abstract class BPMNError extends Error {

	private static final long serialVersionUID = 4367957196415077218L;
	public final TBaseElement element;

	public BPMNError(TBaseElement e) {
		element = e;
	}

	public BPMNError(String arg0, TBaseElement e) {
		super(arg0);
		element = e;
	}

	public BPMNError(Throwable arg0, TBaseElement e) {
		super(arg0);
		element = e;
	}

	public BPMNError(String arg0, Throwable arg1, TBaseElement e) {
		super(arg0, arg1);
		element = e;
	}

}
