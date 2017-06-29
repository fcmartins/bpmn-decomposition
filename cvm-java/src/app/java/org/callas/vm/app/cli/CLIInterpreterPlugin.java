package org.callas.vm.app.cli;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.callas.vm.Deserializer;
import org.callas.vm.ModuleDeclaration;
import org.callas.vm.app.IInterpreter;
import org.callas.vm.app.IPluginContext;
import org.callas.vm.app.IPluginListener;
import org.callas.vm.app.PluginRegistryException;
import org.callas.vm.IConnectionManager;

/**
 * Takes care of registering itself as a command-line listener, then adds a new
 * required command to invoke the interpreter upon issuing flag "-r". It depends
 * on the IInterpreter singleton.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @since Feb 2, 2011
 */
public class CLIInterpreterPlugin implements Runnable, ICommandLineListener,
		IPluginListener {

	/**
	 * The flag used in the command line.
	 */
	private static final String CLI_FLAG = "r";
	/**
	 * The filename of the CVM program to execute.
	 */
	private String filename;

	/**
	 * The CVM interpreter.
	 */
	private IInterpreter cvm;
	
	/**
	 * The Connection Manager.
	 */
	private IConnectionManager connManager;
	
	public CLIInterpreterPlugin(IConnectionManager connManager) {
		this.connManager = connManager;
	}

	public void run() {
		ModuleDeclaration program;
		try {
			program = loadProgram();
		} catch (IOException e) {
			// The program could not be loaded, show it to the user
			System.err.println("Error opening executing the progragm: " + e);
			return;
		}
		cvm.run(program, connManager);
	}

	private ModuleDeclaration loadProgram() throws IOException {
		InputStream fileIn = new FileInputStream(filename);
		DataInputStream dataIn = new DataInputStream(fileIn);
		Deserializer deser = new Deserializer(dataIn);
		return deser.readModuleDeclaration();
	}

	public void handlePluginContext(IPluginContext loader)
			throws PluginRegistryException {
		try {
			cvm = loader.getSingleton(IInterpreter.class);
		} catch (PluginRegistryException e) {
			String msg = getClass().getCanonicalName();
			msg += " could not load externals factory.";
			throw new PluginRegistryException(msg, e);
		}
	}

	@SuppressWarnings("static-access")
	public void handleCommandLineContext(ICommandLineContext ctx) {
		Option opt = OptionBuilder
				.withDescription("the Callas bytecode to execute") //
				.withArgName("BYTECODE") //
				.withLongOpt("run") //
				.hasArg() //
				.create(CLI_FLAG);
		ctx.registerAlternativeCommand(opt, this);
	}

	public void onParsedCommandLine(CommandLine result) {
		this.filename = result.getOptionValue(CLI_FLAG);
	}

}
