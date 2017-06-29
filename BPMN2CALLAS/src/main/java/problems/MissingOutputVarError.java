package problems;

import bpmn2.TBaseElement;

public class MissingOutputVarError extends BPMNError {

	private static final long serialVersionUID = 7616355316374413163L;

	public MissingOutputVarError(String s, TBaseElement e) {
		super(s, e);
	}

}
