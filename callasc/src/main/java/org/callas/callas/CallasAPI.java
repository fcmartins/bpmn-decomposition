package org.callas.callas;

import java.io.PrintStream;
import java.util.Collection;

import org.callas.absyn.NetworkFile;
import org.callas.absyn.SourceValue;
import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.sensors.CallasNetwork;
import org.callas.absyn.types.CodeType;
import org.callas.core.IFileLoader;
import org.callas.external.Externs;
import org.callas.external.IExternalOperation;
import org.callas.parse.CallasProcessParser;
import org.callas.parse.ExternsParser;
import org.callas.parse.NetworkFileParser;
import org.callas.parse.SensorTypeParser;
import org.callas.semant.NetworkTypecheckerImpl;
import org.callas.translate.Translator;
import org.callas.vm.ast.CVMModule;
import org.tyco.common.api.ICompiler;
import org.tyco.common.api.IParser;
import org.tyco.common.api.IRunner;
import org.tyco.common.api.ITypechecker;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.ErrorMessagePrinter;
import org.tyco.common.errorMsg.ErrorMessagesException;
import org.tyco.common.errorMsg.IPrinter;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.errorMsg.StreamPrinter;

/**
 * Serves as a unified point to manipulate the internals of the Callas API.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date May 29, 2009
 * 
 */
public class CallasAPI {

	private SourceValue<Externs> externs = new SourceValue<Externs>(new SourceLocation(null), new Externs());

	private IFileLoader fileLoader = IFileLoader.STANDARD_IO;

	/**
	 * This is an interpreter of the network.
	 * 
	 * @param network
	 *            The network being interpreted.
	 * @return The interpreter.
	 */
	public IRunner createInterpreter(CallasNetwork network) {
		// TODO: implement me
		// return new InterpreterImpl(externs, network);
		throw new UnsupportedOperationException("The interpreter is not implemented yet.");
	}

	/**
	 * Clears all operations in the current registry.
	 */
	public void clearRegistry() {
		externs = new SourceValue<Externs>(new SourceLocation(null), new Externs());
	}

	/**
	 * Registers a new operation. The new registered operation only affects new
	 * instances of the typechecker, the interpreter, etc.
	 * 
	 * @param name
	 * @param op
	 */
	public void registerExternalOperation(String name, IExternalOperation op) {
		externs.getValue().registerOperation(name, op);
	}

	/**
	 * Clears the current externs and uses the defaults ones.
	 */
	public void useSystemExterns() {
		externs = new SourceValue<Externs>(new SourceLocation(null), Externs.getInstance().clone());
	}

	/**
	 * Uses the given external ops registry. It only affects new instances of
	 * typecheckers, parsers, interpreters, etc.
	 * 
	 * @param externs
	 *            The new external ops registry.
	 */
	public void useRegistry(Externs externs) {
		useRegistry(new SourceValue<Externs>(new SourceLocation(null), externs));
	}
	
	/**
	 * Uses the given external ops registry. It only affects new instances of
	 * typecheckers, parsers, interpreters, etc.
	 * 
	 * @param externs
	 *            The new external ops registry.
	 */
	public void useRegistry(SourceValue<Externs> externs) {
		this.externs = externs;
	}

	/**
	 * Creates a parser of files that hold the network interface.
	 */
	public IParser<CodeType> createNetworkInterfaceParser() {
		return SensorTypeParser.createParser(fileLoader);
	}

	/**
	 * Creates a parser for the process language.
	 * 
	 * @return
	 */
	public IParser<CallasProcess> createProcessParser() {
		return new CallasProcessParser(fileLoader);
	}

	/**
	 * Creates a parser for the abstract network language.
	 * 
	 * @return
	 */
	public IParser<NetworkFile> createNetworkFileParser() {
		return new NetworkFileParser(fileLoader);
	}

	public IParser<Externs> createExternsParser() {
		return ExternsParser.createParser(fileLoader);
	}

	/**
	 * Typechecks the network, given the type of the network and the externs.
	 * 
	 * @param sensorType
	 *            The type that represents the network interface.
	 * @param externs
	 *            The available externs.
	 * @return A typechecker of sensor networks.
	 */
	public ITypechecker<CallasNetwork, Void> createTypechecker(
			SourceValue<CodeType> sensorType, SourceValue<Externs> localExterns) {
		return new NetworkTypecheckerImpl(sensorType, localExterns);
	}

	/**
	 * Creates a typechecker given a network interface and the current externs
	 * present in the CallasAPI.
	 * 
	 * @param sensorCodeType
	 *            The network interface.
	 * @return The typechecker of sensor networks.
	 */
	public ITypechecker<CallasNetwork, Void> createNetworkTypechecker(
			SourceValue<CodeType> sensorType) {
		return createTypechecker(sensorType, externs);
	}
	/**
	 * Creates a typechecker given a network interface and the current externs
	 * present in the CallasAPI.
	 * 
	 * @param sensorCodeType
	 *            The network interface.
	 * @return The typechecker of sensor networks.
	 */
	public ITypechecker<CallasNetwork, Void> createNetworkTypechecker(
			CodeType sensorType) {
		return createNetworkTypechecker(new SourceValue<CodeType>(new SourceLocation(null), sensorType));
	}

	/**
	 * Checks if the network file has a field 'code' in every sensor. Then
	 * extracts every CallasProcess in that code and builds up a network. Gets
	 * the type of the network from field 'interface'. Gets the type of the
	 * external ops via field 'externs'. Finally, typechecks the network, using
	 * the type of the network and type of the external ops.
	 * 
	 * @param networkFile
	 *            The network file containing the network definition.
	 * @throws ErrorMessagesException
	 *             The error messages found.
	 * @return The typechecker of network files (that will typecheck sensor
	 *         networks).
	 */
	public ITypechecker<NetworkFile, Void> createFullTypechecker() {
		final SourceValue<Externs> usedExterns = new SourceValue<Externs>(externs.getSourceLocation(), externs.getValue().clone());
		return new ITypechecker<NetworkFile, Void>() {

			public Void typecheck(NetworkFile value)
					throws ErrorMessagesException {
				// we use the netfile extractor to get the various components
				NetworkFileExtractor extractor = new NetworkFileExtractor(value);

				// get the network
				IParser<CallasProcess> procParser = createProcessParser();
				CallasNetwork network = extractor.getNetwork(procParser);

				// get the sensor type
				IParser<CodeType> ifaceType = createNetworkInterfaceParser();
				SourceValue<CodeType> iface = extractor.getNetworkInterface(ifaceType);

				// get the externs
				SourceValue<Externs> newReg = usedExterns;

				// typecheck the network
				ITypechecker<CallasNetwork, Void> typechecker = createTypechecker(
						iface, newReg);
				typechecker.typecheck(network);
				return null;
			}
		};
	}

	/**
	 * Should be used to print elements elements of the callas API.
	 * 
	 * @param output The stream where the errors will be printed.
	 * @return The printer.
	 */
	public IPrinter<Object> createPrinter(PrintStream output) {
		return new StreamPrinter<Object>(output);
	}

	/**
	 * Print the elements of the Callas API into the given printer.
	 * 
	 * @param printer
	 *            Where the outcome will be printed.
	 * @return The new printer that decorates the given one.
	 */
	public IPrinter<Object> createPrinter(final IPrinter<String> printer) {
		return new IPrinter<Object>() {

			public void print(Object obj) {
				printer.print(obj.toString());
			}
		};
	}

	/**
	 * Creates an instance of the compiler.
	 * 
	 * @return
	 */
	public ICompiler<CallasProcess, CVMModule> createCompiler() {
		return new Translator();
	}

	/**
	 * Prints a collection of errors to a print stream.
	 * 
	 * @param out
	 * @param errors
	 */
	public void printErrors(PrintStream out, Collection<ErrorMessage> errors) {
		ErrorMessagePrinter printer = new ErrorMessagePrinter(
				createPrinter(out));
		for (ErrorMessage msg : errors) {
			printer.print(msg);
		}
		out.printf("Found %d errors.\n", errors.size());
	}

	/**
	 * Sets the file loader to use.
	 * @param fileLoader the fileLoader to set
	 */
	public void setFileLoader(IFileLoader fileLoader) {
		this.fileLoader = fileLoader;
	}
}
