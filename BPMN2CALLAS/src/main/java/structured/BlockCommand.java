package structured;

import java.util.LinkedList;
import java.util.List;

public class BlockCommand extends Command {

	public List<Command> commands;

	public BlockCommand(List<Command> commands) {
		super(ElementIds.nextId());
		this.commands = new LinkedList <Command>();
		for (Command c : commands)
			addCommand (c);
	}	
	
	public void addCommand (Command command) {
		commands.add(command);
	}
	
	@Override
	public void accept (Visitor v) {
		v.visitBlockCommand(this);
	}
}
