package org.callas.parse;

import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.processes.Variable;

/**
 * Private class used for generating a let.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 26, 2009
 * 
 */
class Assignment extends CallasProcess {

	public final Variable variable;
	public final CallasProcess value;

	public Assignment(Variable variable, CallasProcess value) {
		super(variable.getSourceLocation());
		this.variable = variable;
		this.value = value;
	}

	@Override
	public String toString() {
		return variable + " := " + value;
	}
}
