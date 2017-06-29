package org.callas.absyn.sensors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

import org.callas.absyn.processes.BLong;
import org.callas.absyn.processes.Call;
import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.processes.CallasValue;
import org.callas.absyn.processes.Code;
import org.callas.absyn.processes.Send;
import org.tyco.common.errorMsg.SourceLocation;

/**
 * A sensor node is an abstraction for a sensor device.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 5, 2008
 * 
 */
public class Sensor extends CallasNetwork {
	/**
	 * A timer event.
	 * 
	 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
	 * @date Sep 29, 2009
	 * 
	 */
	public final static class Event {
		/**
		 * The function call to issue.
		 */
		private final Call call;
		/**
		 * The time unit to be incremented to the next trigger.
		 */
		private final CallasValue increment;
		/**
		 * After this time the event expires.
		 */
		private final CallasValue timeToExpire;
		/**
		 * On this time unit the function call is issued.
		 */
		private CallasValue nextTrigger;

		/**
		 * The default constructor.
		 * 
		 * @param call
		 *            The function call to issue.
		 * @param increment
		 *            The time unit to be incremented to the next trigger.
		 * @param timeToExpire
		 *            After this time the event expires.
		 * @param nextTrigger
		 *            On this time unit the function call is issued.
		 */
		public Event(Call call, CallasValue increment,
				CallasValue timeToExpire, CallasValue nextTrigger) {
			this.call = call;
			this.increment = increment;
			this.timeToExpire = timeToExpire;
			this.nextTrigger = nextTrigger;
		}

		/**
		 * @return the nextTrigger
		 */
		public CallasValue getNextTrigger() {
			return nextTrigger;
		}

		/**
		 * @param nextTrigger
		 *            the nextTrigger to set
		 */
		public void setNextTrigger(CallasValue nextTrigger) {
			this.nextTrigger = nextTrigger;
		}

		/**
		 * @return the call
		 */
		public Call getCall() {
			return call;
		}

		/**
		 * @return the increment
		 */
		public CallasValue getIncrement() {
			return increment;
		}

		/**
		 * @return the timeToExpire
		 */
		public CallasValue getTimeToExpire() {
			return timeToExpire;
		}

		@Override
		public String toString() {
			return "Event [call=" + call + ", increment=" + increment
					+ ", timeToExpire=" + timeToExpire + ", nextTrigger="
					+ nextTrigger + "]";
		}

		@Override
		public boolean equals(Object obj) {
			return obj != null && obj instanceof Event
					&& (obj.toString().equals(this.toString()));
		}
	}

	/**
	 * The running process.
	 */
	private CallasProcess running;

	/**
	 * A double-ended queue of processes scheduled for execution (R).
	 */
	private final List<CallasProcess> runQueue;

	/**
	 * The installed code for the application (M).
	 */
	private Code installedCode;

	/**
	 * A table of timers for function calls (T).
	 */
	private final SortedSet<Event> events;

	/**
	 * This queue buffers messages received from the network (I).
	 */
	private final Queue<Send> input;

	/**
	 * This queue buffers messages sent to the network (O).
	 */
	private final Queue<Send> output;

	/**
	 * The sensor's current time.
	 */
	private CallasValue currentTime;

	/**
	 * The sensor's position.
	 */
	private CallasValue position;

	/**
	 * @param location
	 * @param callStack
	 * @param runQueue
	 * @param installedCode
	 * @param timers
	 * @param input
	 * @param output
	 */
	public Sensor(SourceLocation location, CallasProcess running,
			List<CallasProcess> runQueue, Code installedCode,
			SortedSet<Event> timers, Queue<Send> input, Queue<Send> output) {
		super(location);
		this.running = running;
		this.runQueue = runQueue;
		this.installedCode = installedCode;
		this.events = timers;
		this.input = input;
		this.output = output;
	}

	/**
	 * Utility constructor.
	 * 
	 * @param location
	 * @param running
	 */
	public Sensor(SourceLocation location, CallasProcess running) {
		this(location, running,
				new ArrayList<CallasProcess>(), Code.NIL,
				new TreeSet<Event>(new TimeComparator(new TimeAsInt())),
				new LinkedList<Send>(), new LinkedList<Send>());
		this.currentTime = new BLong(0);
	}
	/**
	 * Utility constructor that discards the source code location.
	 * @param running
	 */
	public Sensor(CallasProcess running) {
		this(null, running);
	}

	public Sensor() {
		this(Code.NIL);
	}

	/**
	 * @return the running process.
	 */
	public CallasProcess getRunning() {
		return running;
	}

	/**
	 * @return the running process.
	 */
	public void setRunning(CallasProcess running) {
		this.running = running;
	}

	/**
	 * @return the runQueue
	 */
	public List<CallasProcess> getRunQueue() {
		return runQueue;
	}

	/**
	 * @return the installedCode
	 */
	public Code getInstalledCode() {
		return installedCode;
	}

	/**
	 * @return the timers
	 */
	public SortedSet<Event> getEvents() {
		return events;
	}

	/**
	 * @return the input
	 */
	public Queue<Send> getInput() {
		return input;
	}

	/**
	 * @return the output
	 */
	public Queue<Send> getOutput() {
		return output;
	}

	/**
	 * @param installedCode
	 *            the installedCode to set
	 */
	public void setInstalledCode(Code installedCode) {
		this.installedCode = installedCode;
	}

	/**
	 * @return the currentTime
	 */
	public CallasValue getCurrentTime() {
		return currentTime;
	}

	/**
	 * @param currentTime
	 *            the currentTime to set
	 */
	public void setCurrentTime(CallasValue currentTime) {
		this.currentTime = currentTime;
	}

	/**
	 * @return the position
	 */
	public CallasValue getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(CallasValue position) {
		this.position = position;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SensorNode [currentTime=" + currentTime + ", events=" + events
				+ ", input=" + input + ", installedCode=" + installedCode
				+ ", output=" + output + ", position=" + position
				+ ", runQueue=" + runQueue + ", running=" + running + "]";
	}
}