package org.callas.absyn.sensors;

import java.util.Comparator;

import org.callas.absyn.sensors.Sensor.Event;

/**
 * Utility class to compare {@link Event}s.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class TimeComparator implements Comparator<Event> {

	private final ITimeOperator timeOps;

	/**
	 * We need a time operator to compare events.
	 * @param timeOps
	 */
	public TimeComparator(ITimeOperator timeOps) {
		super();
		this.timeOps = timeOps;
	}

	public int compare(Event o1, Event o2) {
		if (timeOps.areEqual(o1.getTimeToExpire(), o2.getTimeToExpire())) {
			return 0;
		}
		return timeOps
				.lessEqualThan(o1.getTimeToExpire(), o2.getTimeToExpire()) ? -1
				: 1;
	}

}
