package org.callas.absyn.sensors;

import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.types.CallasType;

/**
 * Always typechecks and positions are always in range.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Sep 29, 2009
 * 
 */
public class DummyPositionOperator implements IPositionOperator {

	public boolean inRange(CallasValue p1, CallasValue p2) {
		return true;
	}

	public void typecheck(CallasValue value, CallasType type) {
		// OK
	}

}
