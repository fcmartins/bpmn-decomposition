package org.callas.absyn.sensors;

import java.util.Queue;

import org.callas.absyn.processes.Send;

/**
 * Places the message in the input queue of another sensor.
 * 
 * It is function 'networkRoute' of the calculus.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Sep 29, 2009
 * 
 */
public class BasicNetworkRoute implements INetworkRoute {

	public void apply(Send message, Queue<Send> input, Queue<Send> output) {
		input.add(message);
	}

}
