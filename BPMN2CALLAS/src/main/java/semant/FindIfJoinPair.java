package semant;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import problems.BPMNError;
import problems.IfNotConverging;
import intermediate.If;
import intermediate.Join;
import intermediate.Process;
import intermediate.VisitDepthFirst;

public class FindIfJoinPair extends VisitDepthFirst {
	
	private List<BPMNError> errors;
	private Stack<If> stack;
	private Map<If,Join> pairs;

	public FindIfJoinPair(List<BPMNError> list, Map <If,Join> m) {
		errors = list;
		stack = new Stack<If>();
		pairs = m;
	}
	
	
	@Override
	public boolean visit (If i) {
		stack.push(i);
		return true;
	}
	
//	@Override
//	public void endVisit (If i) {
//		if (stack.contains(i))
//			stack.removeElement(i);
//	}
	
	@Override
	public boolean visit (Join j) {
		if (!stack.isEmpty() && !pairs.values().contains(j))
			pairs.put(stack.pop(), j);
		return true;
	}
	
	@Override
	public void endVisit(Process p) {
		if (!stack.isEmpty()){
			for (If i : stack)
				errors.add(new IfNotConverging("If does not end.", i.element));
		}
	}

}
