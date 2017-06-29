package structured;


public class TimedFunction extends Function {
	
	private Expression periodicity;
	
	public TimedFunction(FunctionSignature signature, BlockCommand blockCommand, Expression periodicity) {
		super(signature, blockCommand);
		this.periodicity = periodicity;
	}

	public Expression getPeriodicity() {
		return periodicity;
	}
	
}
