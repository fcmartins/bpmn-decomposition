package org.callas.absyn.sensors;

import org.callas.absyn.processes.BLong;
import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.types.BLongType;
import org.callas.absyn.types.CallasType;
import org.callas.error.TypeMismatch;
import org.callas.util.DifferentTypesException;
import org.tyco.common.errorMsg.ErrorMessagesException;

/**
 * Represents time as an integer.
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 *
 */
public class TimeAsInt implements ITimeOperator {

	public CallasValue plus(CallasValue left, CallasValue right) {
		return new BLong(((BLong) left).value + ((BLong) right).value);
	}

	public void typecheck(CallasValue value, CallasType type) throws ErrorMessagesException {
		if (!(type instanceof BLongType)) {
			throw new ErrorMessagesException(new TypeMismatch(value, new DifferentTypesException("Not a long type.", BLongType.LONG_TYPE, type)));
		}
	}

	public boolean lessEqualThan(CallasValue left, CallasValue right) {
		return ((BLong) left).value <= ((BLong) right).value;
	}

	public CallasValue increment(CallasValue value) {
		return plus(value, new BLong(1));
	}

	public boolean areEqual(CallasValue left, CallasValue right) {
		return ((BLong) left).value == ((BLong) right).value;
	}

}
