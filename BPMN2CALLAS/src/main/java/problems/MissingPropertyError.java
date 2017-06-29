package problems;

import bpmn2.TBaseElement;

public class MissingPropertyError extends BPMNError {

	private static final long serialVersionUID = 2614067465055530200L;

	public MissingPropertyError(String s, TBaseElement e) {
		super(s, e);
	}

}
