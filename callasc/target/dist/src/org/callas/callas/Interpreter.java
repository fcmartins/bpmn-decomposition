package org.callas.callas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.callas.absyn.FileSection;
import org.callas.absyn.NetworkFile;
import org.callas.absyn.SourceValue;
import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.sensors.CallasNetwork;
import org.callas.absyn.sensors.Sensor;
import org.callas.util.SensorIterator;
import org.tyco.common.api.IParser;
import org.tyco.common.api.IRunner;
import org.tyco.common.api.ITypechecker;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.ErrorMessagesException;
import org.tyco.common.errorMsg.IPrinter;
import org.tyco.common.errorMsg.msgs.SyntacticError;
import org.tyco.common.symtable.KeyNotFoundException;

/**
 * The command line application for the interpreter.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 31, 2008
 * 
 */
public class Interpreter {
	private final CallasAPI api;

	public Interpreter() {
		this.api = new CallasAPI();
		api.useSystemExterns();
	}

	private CallasNetwork loadNetwork(NetworkFile netFile)
			throws ErrorMessagesException {
		CallasNetwork network;
		List<ErrorMessage> errors = new LinkedList<ErrorMessage>();
		// construct a map from filenames to processes running in each sensor
		List<Sensor> nodes = new LinkedList<Sensor>();
		for (FileSection sensor : netFile.getSensorSections()) {
			parseSensor(sensor, nodes, errors);
		}
		if (errors.size() > 0) {
			throw new ErrorMessagesException(errors);
		}
		network = SensorIterator.constructNetwork(nodes);
		return network;
	}

	private void parseSensor(FileSection sensor, List<Sensor> nodes,
			List<ErrorMessage> errors) throws ErrorMessagesException {
		int instances = 1;
		SourceValue<String> instancesVal;
		try {
			instancesVal = sensor.get("instances");
			try {
				instances = Integer.parseInt(instancesVal.getValue());
				if (instances < 0) {
					instances = 0;
					throw new NumberFormatException(
							"The number of instances must be non-negative.");
				}
			} catch (NumberFormatException e) {
				errors.add(new SyntacticError(instancesVal.getSourceLocation(),
						e.toString()));
			}
		} catch (KeyNotFoundException e) {
			// use default value: 1
		}

		try {
			CallasProcess proc = sensor.parseFieldWith("code", api
					.createProcessParser());
			for (int i = 0; i < instances; i++) {
				nodes.add(new Sensor(sensor.getSourceLocation(), proc));
			}
		} catch (KeyNotFoundException e) {
			errors.add(new SyntacticError(sensor.getSourceLocation(),
					"Expecting a field named 'code'."));
		}
	}

	public void run(String filename, InputStream in, boolean typecheckOnly,
			boolean printStep) throws ErrorMessagesException {
		CallasNetwork network;
		IParser<NetworkFile> parser = api.createNetworkFileParser();

		NetworkFile netFile = parser.parse(filename, in);
		ITypechecker<NetworkFile, Void> typechecker = api.createFullTypechecker();
		typechecker.typecheck(netFile);

		network = loadNetwork(netFile);

		if (typecheckOnly) {
			return;
		}

		IRunner interpreter = api.createInterpreter(network);
		IPrinter<Object> printer = api.createPrinter(System.out);
		Iterable<Sensor> sensors = SensorIterator.flattenNetwork(network);
		while (interpreter.isRunning()) {
			interpreter.runStep();
			if (printStep) {
				System.out
						.println("--------------------- print-step --------------------\n");
				for (Sensor node : sensors) {
					printer.print(node);
					printer.print("\n\n");
				}
				System.out
						.println("\n---------------------------------------------------\n");
			}
		}
	}

	private void printErrors(PrintStream out, Collection<ErrorMessage> errors) {
		api.printErrors(out, errors);
	}

	// Parse parameters and setup

	/**
	 * If there is one parameter it is the filename, otherwise the source code
	 * is read through the standard input stream.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String args[]) throws FileNotFoundException {
		String filename = "<STDIN>";

		if (args.length == 0) {
			usage();
			System.exit(-1);
			return;
		}

		boolean printStep = false;
		List<String> argsList = new ArrayList<String>(Arrays.asList(args));
		// remove the last element
		filename = argsList.remove(args.length - 1);

		if (argsList.contains("-print-step")) {
			printStep = true;
			argsList.remove("-print-step");
		}
		boolean typecheckOnly = false;
		if (argsList.contains("-typecheck-only")) {
			argsList.remove("-typecheck-only");
			typecheckOnly = true;
		}
		if (argsList.size() > 0) {
			usage();
			System.exit(-1);
			return;
		}

		if (filename.equals("-")) {
			filename = null;
		}

		final InputStream in = filename == null ? System.in
				: new FileInputStream(filename);
		try {
			Interpreter interpreter = new Interpreter();
			try {
				interpreter.run(filename, in, typecheckOnly, printStep);
			} catch (ErrorMessagesException e) {
				interpreter.printErrors(System.err, e.getErrors());
				System.exit(-1);
				return;
			}
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void usage() {
		System.err
				.println("Usage: callasi [-print-step] [-typecheck-only] <networkfile.calnet>");
	}

}
