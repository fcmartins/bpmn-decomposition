package problems;

import bpmn2.TBaseElement;

public class CycleFoundError extends BPMNError {

	private static final long serialVersionUID = 6755587303459413053L;

	public CycleFoundError(String s, TBaseElement e) {
		super(s, e);
	}

}
