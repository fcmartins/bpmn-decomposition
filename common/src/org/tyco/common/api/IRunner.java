package org.tyco.common.api;

/**
 * Represents an object that executes by steps.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public interface IRunner {
	/**
	 * Checks if the interpreter is still running.
	 * 
	 * @return
	 */
	boolean isRunning();

	/**
	 * Does one iteration on the interpeter.
	 */
	void runStep();
}
