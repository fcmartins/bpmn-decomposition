package intermediate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bpmn2.TBaseElement;

public class Process extends AbSyn {

	private String name;
	private List<VarDecl> arguments;
	private Map<Variable,VarDecl> varsGlob;
	private Command initialCommand;
	
	public Process(TBaseElement elem) {
		super(elem);
		arguments = new LinkedList<VarDecl>();
		varsGlob = new  HashMap<Variable,VarDecl>();
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<VarDecl> getArguments() {
		return arguments;
	}
	
	public void addArgument(VarDecl v) {
		arguments.add(v);
	}
	
	public void setInitialCommand(Command c) {
		this.initialCommand = c;
	}
	
	public Command getInitialCommand() {
		return initialCommand;
	}
	
	public void addVarDecl(VarDecl v) {
		this.varsGlob.put(v.var, v);
	}
	
	public Map<Variable,VarDecl> getGlobVars() {
		return varsGlob;
	}
	
	@Override
	public void accept (Visit v) {
		v.visitProcess (this);
	}
}
