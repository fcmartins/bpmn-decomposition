package org.callas.absyn.processes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.tyco.common.errorMsg.ISourceLocationHolder;
import org.tyco.common.errorMsg.SourceLocation;

/**
 * A process abstraction contains a process and the free names of the abstracted
 * process. Processes abstractions are used in {@link Code}s.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 26, 2008
 */
public class ProcessAbstraction implements ISourceLocationHolder {

	/**
	 * The source location
	 */
	private SourceLocation where;
	
	/**
	 * The free names of the process.
	 */
	public final List<Variable> parameters;

	/**
	 * The abstracted process.
	 */
	public final CallasProcess process;
	
	/**
	 * Creates a process abstraction from a list of parameters and a concrete
	 * process.
	 * 
	 * @param parameters the parameters
	 * @param process the concrete process
	 */
	public ProcessAbstraction(SourceLocation where, List<Variable> parameters, CallasProcess process) {
		this.where = where;
		this.parameters = Collections.unmodifiableList(new ArrayList<Variable>(parameters));
		this.process = process;
	}

	/**
	 * Utility constructor.
	 * @param process the concrete process
	 */
	public ProcessAbstraction(CallasProcess process, String...params) {
		this(null, toVariables(params), process);
	}

	public ProcessAbstraction(List<Variable> parameters, CallasProcess proc) {
		this(null, parameters, proc);
	}

	private static final List<Variable> toVariables(String[] names) {
		Variable[] vars = new Variable[names.length];
		for (int i = 0; i < names.length; i++) {
			vars[i] = new Variable(names[i]);
		}
		return Arrays.asList(vars);
	}
	
	@Override
	public String toString() {
		return parameters.toString() + process.toString();
	}

	public SourceLocation getSourceLocation() {
		return where;
	}
	
	/**
	 * Sets the source location.
	 * @param loc
	 */
	public void setSourceLocation(SourceLocation loc) {
		this.where = loc;
	}

}
