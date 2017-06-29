package org.callas.vm;

public interface INativeOperations {
	void init();
	Object execute(String systemCall, Object[] args);
	byte getParameterCount(String systemCall);
}
