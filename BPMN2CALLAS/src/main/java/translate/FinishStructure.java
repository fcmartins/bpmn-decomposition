package translate;

import structured.Command;

import java.util.Stack;

import structured.BlockCommand;
import structured.IfStructured;
import structured.VisitDepthFirstVisitor;

public class FinishStructure extends VisitDepthFirstVisitor {
	
	private Stack <BlockCommand> blockCommands;
	private Stack <Command> toAdd;
	public final IfStructured ifs;
	private boolean isToAdd, done;

	public FinishStructure (Stack <Command> s, IfStructured i) {
		blockCommands = new Stack<BlockCommand>();
		toAdd = s;
		ifs = i;
	}
	
	@Override
	public boolean visit (BlockCommand bc) {
		blockCommands.push(bc);
		return true;
	}
	
	@Override
	public void endVisit (BlockCommand bc) {
		if (isToAdd && !done) {
			for (Command c : toAdd)
				bc.addCommand(c);
			done = true;
		}
		blockCommands.pop();
	}
	
	@Override
	public boolean visit (IfStructured i) {
		if (ifs.equals(i))
			isToAdd = true;		
		return true;
	}

}
