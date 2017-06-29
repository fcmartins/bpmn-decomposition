package problems;

import bpmn2.TBaseElement;

public abstract class TypeMismatchError extends BPMNError {

	private static final long serialVersionUID = 63515255127023296L;

	public TypeMismatchError(String s, TBaseElement e) {
		super(s, e);
	}

}
