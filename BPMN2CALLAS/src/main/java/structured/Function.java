package structured;


public class Function extends Command {
	
	public final FunctionSignature signature;
	public final BlockCommand blockCommand;
	
	public Function(FunctionSignature signature, BlockCommand blockCommand) {
		super(ElementIds.nextId());
		this.signature = signature;
		this.blockCommand = blockCommand;
	}
	
	@Override
	public void accept (Visitor v) {
		v.visitFunction(this);
	}
	
}
