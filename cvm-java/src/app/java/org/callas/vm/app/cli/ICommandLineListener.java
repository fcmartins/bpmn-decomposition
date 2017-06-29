package org.callas.vm.app.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

/**
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @since Feb 2, 2011
 */
public interface ICommandLineListener {
	/**
	 * Invoked upon starting up to allow listeners to operate the command line
	 * parser plugin.
	 * 
	 * @param ctx
	 */
	void handleCommandLineContext(ICommandLineContext ctx);

	/**
	 * Called after parsing the commands line arguments.
	 * 
	 * @param result
	 *            The parsed command line.
	 * @throws ParseException
	 *             Thrown when the listener finds a problem in the parsed
	 *             command line.
	 */
	void onParsedCommandLine(CommandLine result) throws ParseException;
}
