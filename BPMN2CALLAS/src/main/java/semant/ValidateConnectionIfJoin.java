package semant;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import problems.BPMNError;
import problems.IfHasDifferentJoinBranchesError;

import intermediate.If;
import intermediate.Join;
import intermediate.VisitDepthFirst;

public class ValidateConnectionIfJoin extends VisitDepthFirst {
	
	private Map <If, Join> map;
	private Stack <If> stack;
	private List <BPMNError> errors;
	private Stack <If> closedIfs;

	public ValidateConnectionIfJoin(Map <If, Join> m, List<BPMNError> l) {
		map = m;
		stack = new Stack<If>();
		errors = l;
		closedIfs = new Stack<If>();
	}
	
	@Override
	public boolean visit (If i) {
		stack.push(i);
		return true;
	}
	

	@Override
	public void endVisit (If i) {
		stack.pop();
	}
	
	
	@Override
	public boolean visit (Join j) {
		If actualIf = stack.peek();
		if (map.get(actualIf).equals(j)){
			stack.pop();
			
			// we are doing a DFS so in this branch we don't need this if anymore, but other branches need it
			closedIfs.push(actualIf);
			
			return true;
		}
		errors.add(new IfHasDifferentJoinBranchesError("Branches converge " +
				"into different joins", actualIf.element));
		return false;	
	}
	
	@Override
	public void endVisit(Join j) {
		// recover last popped if since we are possibly going a different branch
		stack.push(closedIfs.pop());
	}
	
}
