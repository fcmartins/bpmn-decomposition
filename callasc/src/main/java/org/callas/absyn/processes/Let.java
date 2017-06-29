package org.callas.absyn.processes;


/**
 * A Let process.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 *
 */
public class Let extends CallasProcess {
	/**
	 * Name of the variable defined by let
	 */
	public final Variable variable;
	/**
	 * Process whose evaluation result is to be assign to <i>varname</i>
	 */
	public final CallasProcess value;
	/**
	 * Process that makes the body of the Let expression
	 */
	public final CallasProcess inProcess;

	/**
	 * The extended constructor.
	 * @param variable
	 * @param value
	 * @param inProcess
	 */
	public Let(Variable variable, CallasProcess value, CallasProcess inProcess) {
		super(variable.getSourceLocation());
		this.variable = variable;
		this.value = value;
		this.inProcess = inProcess;
	}

	/**
	 * Creates a let instruction using strings instead of values.
	 * 
	 * @param variable
	 * @param value
	 * @param target
	 */
	public Let(String variable, CallasProcess value, CallasProcess target) {
		this(new Variable(variable), value, target);
	}
	
	@Override
	public String toString() {
		return "let " + variable + " = " + value + " in " + inProcess;
	}
}
