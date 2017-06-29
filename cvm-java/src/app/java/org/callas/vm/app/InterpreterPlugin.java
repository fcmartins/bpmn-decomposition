package org.callas.vm.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.callas.vm.*;

/**
 * This plugin also publishes two I/O queues where messages arrive from and are
 * delivered to the network. Method <code>run</code> may be executed by another
 * plugin to run the interpreter. The network handling must be implemented by
 * another plugin.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @since Feb 2, 2011
 */
public class InterpreterPlugin implements IPluginListener, IInterpreter {
	/**
	 * The plugins that are listeners.
	 */
	private Collection<IInterpreterListener> listeners;
	/**
	 * The externs factory (a plugin).
	 */
	private INativeOperationsFactory externsFactory;
	/**
	 * Existing messages will be consumed by the interpreter.
	 */

	public void run(ModuleDeclaration program, IConnectionManager connManager) {
		for (IInterpreterListener handler : listeners) {
			handler.handleInterpreterContext(this);
		}

		INativeOperations nativeOps = externsFactory.createNativeOperations();
		Interpreter engine = new Interpreter(program, nativeOps, connManager);

		for (IInterpreterListener handler : listeners) {
			handler.onInterpreterStart();
		}
		while (engine.isRunning())
			engine.runBigStep();
		// warn our listeners we are done
		for (IInterpreterListener handler : listeners) {
			handler.onInterpreterShutdown();
		}
	}

	public void handlePluginContext(IPluginContext loader)
			throws PluginRegistryException {
		listeners = loader.getPluginsFor(IInterpreterListener.class);
		try {
			externsFactory = loader
					.getSingleton(INativeOperationsFactory.class);
		} catch (PluginRegistryException e) {
			String msg = getClass().getCanonicalName();
			msg += " could not load externals factory.";
			throw new PluginRegistryException(msg, e);
		}
	}
}
