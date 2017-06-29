package semant;

import java.util.List;

import problems.BPMNError;
import problems.MissingReturnCommandError;

import intermediate.ReturnCommand;
import intermediate.VisitDepthFirst;
import intermediate.Process;

public class ValidateExistingReturnCommandVisitor extends VisitDepthFirst {
	
	private List<BPMNError> errors;
	private boolean hasReturnCommand;
	
	public ValidateExistingReturnCommandVisitor (List<BPMNError> l) {
		errors = l;
		hasReturnCommand = false;
	}
	
	@Override
	public void endVisit (ReturnCommand rc) {
		hasReturnCommand = true;
	}
	
	@Override
	public void endVisit (Process p) {
		if (!hasReturnCommand)
			errors.add(new MissingReturnCommandError("Process does not have " +
					"any EndEvent", p.element));
	}


}
