package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * 
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Apr 12, 2012
 * 
 */
public class Close extends CallasProcess {
	/**
	 * The channel for the connection.
	 */
	public final CallasValue channel;
	/**
	 * An utility constructor.
	 */
	public Close(CallasValue channel) {
		super();
		this.channel=channel;
	}

	/**
	 * The standard constructor.
	 * 
	 * @param sourceLocation
	 *            The source location.
	 */
	public Close(SourceLocation sourceLocation, CallasValue channel) {
		super(sourceLocation);
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "close " + channel;
	}
}
