package org.callas.absyn.sensors;

import java.util.Queue;

import org.callas.absyn.processes.Send;

/**
 * Function 'networkRoute' decides where the message in the output queue of the
 * broadcasting sensor should be copied into the new sensor. The function can be
 * thought off as implementing the routing protocol for the sensor network.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public interface INetworkRoute {
	/**
	 * Applies the function the the terms.
	 * 
	 * @param message
	 *            The message being broadcasted.
	 * @param input
	 *            The input queue of the other sensor.
	 * @param output
	 *            The output queue of the broadcasting sensor.
	 */
	void apply(Send message, Queue<Send> input, Queue<Send> output);
}
