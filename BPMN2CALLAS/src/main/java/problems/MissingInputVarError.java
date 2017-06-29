package problems;

import bpmn2.TBaseElement;

public class MissingInputVarError extends BPMNError {

	private static final long serialVersionUID = 2614067465077530200L;

	public MissingInputVarError(String s, TBaseElement e) {
		super(s, e);
	}

}
