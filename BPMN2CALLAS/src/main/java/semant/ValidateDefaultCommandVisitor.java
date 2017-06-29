package semant;

import java.util.List;

import problems.BPMNError;
import problems.MissingDefaultCommandError;

import intermediate.If;
import intermediate.VisitDepthFirst;

public class ValidateDefaultCommandVisitor extends VisitDepthFirst {
	
	private List<BPMNError> errors;
	
	public ValidateDefaultCommandVisitor(List<BPMNError> l) {
		errors = l;
	}

	@Override
	public boolean visit(If i) {		
		if(i.getContinuation() == null)
			errors.add(new MissingDefaultCommandError("Gateway does not have" +
					" default continuation", i.element));
		return true;
	}
}
