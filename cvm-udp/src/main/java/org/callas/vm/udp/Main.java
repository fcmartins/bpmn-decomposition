package org.callas.vm.udp;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

import org.apache.commons.cli.*;
import org.callas.vm.app.IPluginListener;
import org.callas.vm.app.InterpreterPlugin;
import org.callas.vm.app.PluginLoader;
import org.callas.vm.app.PluginRegistryException;
import org.callas.vm.app.cli.CLIInterpreterPlugin;
import org.callas.vm.app.cli.ICommandLineContext;
import org.callas.vm.app.cli.ICommandLineListener;
import org.callas.vm.app.cli.ShowUsageException;


/**
 * The real CLI application. After loading the plugins it executes the ones that
 * implement interface {@link ICommandLineListener}. In case of a parse error
 * this program shows the usage.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @since Feb 2, 2011
 */
public class Main implements ICommandLineContext {
	private final Options opts = new Options();
	private final OptionGroup runners = new OptionGroup();

	private Map<String, Runnable> commands = new HashMap<String, Runnable>();
	private final String[] args;

	public Main(String args[]) {
		this.args = args;
	}

	/**
	 * Runs the virtual machine.
	 * 
	 * @throws ClassNotFoundException
	 */
	public void run() throws IOException, ClassNotFoundException {
		List<Object> plugins = new LinkedList<Object>();
		plugins.add(new InterpreterPlugin());
		ConnectionManager connManager = new ConnectionManager();
		plugins.add(new CLIInterpreterPlugin(connManager));
		plugins.add(new UDPPlugin());
		plugins.add(new PCOpsFactory());
		PluginLoader loader = new PluginLoader(plugins);
		try {
			for (IPluginListener handler : loader
					.getPluginsFor(IPluginListener.class)) {
				try {
					handler.handlePluginContext(loader);
				} catch (PluginRegistryException e) {
					throw new ShowUsageException(e);
				}
			}
			processCommandLine(loader).run();
		} catch (ShowUsageException e) {
			String msg = e.getMessage();
			if (e.getCause() instanceof ParseException) {
				msg = e.getCause().getMessage();
			}
			System.err.println(msg);
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("cvm", opts);
			if (e.getCause() != null
					&& !(e.getCause() instanceof ParseException)) {
				e.printStackTrace();
			}
			return;
		}
	}

	private Runnable processCommandLine(PluginLoader loader)
			throws ShowUsageException {
		runners.setRequired(true);

		Collection<ICommandLineListener> optHandlers = loader
				.getPluginsFor(ICommandLineListener.class);
		for (ICommandLineListener handler : optHandlers) {
			handler.handleCommandLineContext(this);
		}
		opts.addOptionGroup(runners);
		try {
			// parse arguments
			CommandLineParser parser = new GnuParser();
			CommandLine cli = parser.parse(opts, args);
			for (ICommandLineListener handler : optHandlers) {
				handler.onParsedCommandLine(cli);
			}
			return commands.get(runners.getSelected());
		} catch (ParseException e) {
			throw new ShowUsageException(e);
		}
	}

	public static void main(String[] args) throws UnknownHostException,
			SocketException {
		Main mach = new Main(args);
		try {
			mach.run();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public void registerAlternativeCommand(Option option, Runnable command) {
		commands.put(option.getOpt(), command);
		runners.addOption(option);
	}

	public void registerOption(Option option) {
		opts.addOption(option);
	}
}
