package org.callas.vm.ast;

import java.util.List;


/**
 * A process abstraction contains a process and the free names of the abstracted
 * process. Processes abstractions are used in {@link CVMModule}s.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 26, 2008
 */
public class CVMFunction {
	
	/**
	 * The size of the frame (number of locals to be created).
	 */
	public final byte localsCount;

	/**
	 * The statements that constitute the function.
	 */
	public final List<CVMStmt> stmts;
	
	/**
	 * The data segment.
	 */
	public final List<CVMValue> constants;

	public final byte parametersCount;

	public final byte freeVarsCount;
	
	/**
	 * Creates a process abstraction from a list of parameters and a concrete
	 * process.
	 * 
	 * @param parameters the parameters
	 * @param stmts the concrete process
	 */
	public CVMFunction(byte parametersCount, byte freeVarsCount, byte localsCount, List<CVMStmt> stmts, List<CVMValue> constants) {
		this.parametersCount = parametersCount;
		this.freeVarsCount = freeVarsCount;
		this.localsCount = localsCount;
		this.stmts = stmts;
		this.constants = constants;
	}

	@Override
	public String toString() {
		String result = String.format("PARAMETERS %d FREEVARS %d LOCALS %d CODE\n", parametersCount, freeVarsCount, localsCount);
		for (CVMStmt stmt : stmts) {
			result += stmt + "\n";
		}
		result += "SYMBOLS\n";
		for (CVMValue val : constants) {
			result += val + "\n";
		}
		return result;
	}
}
