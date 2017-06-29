package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Synchronously wait for a call from the network that is placed in the run
 * queue.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Aug 5, 2009
 * 
 */
public class Receive extends CallasProcess {
	/**
	 * The channel for the connection.
	 */
	public final CallasValue channel;
	/**
	 * An utility constructor.
	 */
	private Receive(CallasValue channel) {
		super();
		this.channel=channel;
	}

	/**
	 * The standard constructor.
	 * 
	 * @param sourceLocation
	 *            The source location.
	 */
	public Receive(SourceLocation sourceLocation, CallasValue channel) {
		super(sourceLocation);
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "select " + channel + " receive";
	}

	public static final Receive RECEIVE = new Receive(null); // ?
}