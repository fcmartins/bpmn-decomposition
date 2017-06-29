package main;

import intermediate.If;
import intermediate.Join;
import intermediate.Process;
import intermediate.VisitDepthFirst;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import problems.BPMNError;
import problems.BPMNWarning;
import semant.FindIfJoinPair;
import semant.ValidateConnectionIfJoin;
import semant.ValidateCyclesVisitor;
import semant.ValidateDefaultCommandVisitor;
import semant.ValidateExistingReturnCommandVisitor;
import semant.ValidateFunctionParameters;

public class Validations {
	private List<BPMNError> errors;
	private List<BPMNWarning> warnings;
	private Map<If,Join> map;
	
	public Validations(List <BPMNError> l) {
		errors = l;
		warnings = new LinkedList<BPMNWarning>();
		map = new HashMap<If, Join>();
	}
	
	public void validate(List<Process> processes) {
		for (Process p : processes) {
			validate(p);
		}
	}
	
	public void validate(Process p) {
		VisitDepthFirst[] visitors = {
				new ValidateDefaultCommandVisitor(errors),
				new ValidateFunctionParameters(errors, p.getGlobVars().values()),
				new ValidateCyclesVisitor(errors),
				new ValidateExistingReturnCommandVisitor(errors),
					
				//obtain If Join pairs
				new FindIfJoinPair(errors, map),
				new ValidateConnectionIfJoin(map, errors)
		};
			
		int i = 0;
		while (i < visitors.length && errors.isEmpty()) {
			p.accept(visitors[i]);
			i++;
		}
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public void printErrors() {
		for (BPMNError err : errors)
			err.printStackTrace();
	}
	
	public Map <If, Join> getMap() {
		return map;
	}

}
