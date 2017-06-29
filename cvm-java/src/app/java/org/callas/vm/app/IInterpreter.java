package org.callas.vm.app;

import java.util.concurrent.BlockingQueue;

import org.callas.vm.Call;
import org.callas.vm.ModuleDeclaration;
import org.callas.vm.IConnectionManager;

/**
 * Holds the actions and properties related to the interpreter
 * 
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @since Feb 2, 2011
 */
public interface IInterpreter {
	/**
	 * Invoke this method to make the CVM execute the given program.
	 * @param program The program to be executed.
	 */
	void run(ModuleDeclaration program, IConnectionManager connManager);
}
