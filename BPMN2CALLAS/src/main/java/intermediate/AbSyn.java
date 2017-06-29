package intermediate;

import java.util.HashSet;
import java.util.Set;

import bpmn2.TBaseElement;

public abstract class AbSyn implements Visitable {
	public final Set<Variable> inputVar, outputVar;
	public final TBaseElement element;
	
	
	public AbSyn(TBaseElement elem) {
		this(elem, new HashSet<Variable>(),new HashSet<Variable>());
	}
	
	
	public AbSyn(TBaseElement elem,  Set<Variable> in,  Set<Variable> out) {
		this.inputVar = in;
		this.outputVar = out;
		this.element = elem;
	}

	@Override
	public void accept(Visit v) {
		v.visit(this);
	}

}
