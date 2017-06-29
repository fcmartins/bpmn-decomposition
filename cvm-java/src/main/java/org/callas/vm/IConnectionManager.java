package org.callas.vm;

import java.io.IOException;

/**
 * 
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @data Apr 10, 2012
 *
 */

public interface IConnectionManager {
	boolean open(String str);
	Object lookupInput(String str);
	Object lookupOutput(String str);
	//Call popCall(String channel);
	void close(String channel) throws IOException;
}