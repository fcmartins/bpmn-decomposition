package org.callas.callas;

/**
 * This represents the result of an operation, consisting of a boolean
 * indication of the operation's success and of a list of messages associated
 * with certain files of source code.
 * 
 * @author Tiago Cogumbreiro
 * 
 */
public class Result {
	/**
	 * The success of the operation.
	 */
	public boolean isSuccessful;
	/**
	 * The messages associated with the operation.
	 */
	public SourceMessage[] messages;

	/**
	 * @param isSuccessful
	 * @param messages
	 */
	public Result(boolean isSuccessful, SourceMessage... messages) {
		this.isSuccessful = isSuccessful;
		this.messages = messages;
	}
}
