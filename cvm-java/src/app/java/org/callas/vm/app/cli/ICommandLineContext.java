package org.callas.vm.app.cli;

import org.apache.commons.cli.Option;

/**
 * A command-line context holds methods to register command-line options and
 * alternate commands.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * 
 */
public interface ICommandLineContext {
	/**
	 * Add a command line option to the application.
	 * 
	 * @param option
	 */
	void registerOption(Option option);

	/**
	 * Add an alternative control flow that is executed when the user selects
	 * the given option.
	 * 
	 * @param key
	 *            The command line argument that represents the alternate
	 *            control flow.
	 * @param command
	 *            When the command line argument option is selected the
	 *            application executes this {@link Runnable} object.
	 */
	void registerAlternativeCommand(Option key, Runnable command);
}
