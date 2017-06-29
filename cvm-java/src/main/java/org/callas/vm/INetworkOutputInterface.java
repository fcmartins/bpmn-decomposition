package org.callas.vm;

import java.io.IOException;

/**
 * 
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 *
 */

public interface INetworkOutputInterface {
	void send(Call msg) throws IOException;
}