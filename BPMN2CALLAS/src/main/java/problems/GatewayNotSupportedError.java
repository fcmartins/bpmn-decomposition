package problems;

import bpmn2.TBaseElement;

public class GatewayNotSupportedError extends BPMNError {

	private static final long serialVersionUID = 2681642174832015829L;

	public GatewayNotSupportedError(String s, TBaseElement e) {
		super(s, e);
	}

}
