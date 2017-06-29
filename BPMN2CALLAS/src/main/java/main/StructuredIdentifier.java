package main;

import structured.BlockCommand;
import structured.Command;
import structured.IfStructured;

public class StructuredIdentifier {

	public static Command getCommand(String id, BlockCommand block) {
		for (Command c: block.commands) 
			if (c instanceof BlockCommand)
				return getCommand(id, (BlockCommand)c);
			else if (c instanceof IfStructured) {
				IfStructured i = (IfStructured)c;
				Command temp = getCommand(id, (BlockCommand)i.thenBranches);
				if(temp!=null)
					temp = getCommand(id, (BlockCommand)i.elseBranch);
				return temp;
			} else if(id.equals(c.id+""))
				return c;
		return null;
	}
}
