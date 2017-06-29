package intermediate;

import bpmn2.TBaseElement;

public class TimedProcess extends Process {

	private Expression periodicity;
	
	public TimedProcess(TBaseElement elem, Expression periodicity) {
		super(elem);
		this.periodicity = periodicity;
	}

	public Expression getPeriodicity() {
		return periodicity;
	}

}
