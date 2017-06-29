package org.callas.semant;

import java.util.List;

import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.sensors.CallasNetwork;
import org.callas.absyn.sensors.EmptyNetwork;
import org.callas.absyn.sensors.NetworkComposition;
import org.callas.absyn.sensors.Sensor;
import org.callas.absyn.sensors.Sensor.Event;
import org.callas.absyn.types.CallasType;
import org.callas.absyn.types.CodeType;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.symtable.Scope;

/**
 * The implementation of the sensor typing rules.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
class SensorTypingRules implements ISensorTypingRules {

	private final Scope<Symbol, CallasType> sensorScope;
	private final Scope<Symbol, CallasType> moduleScope;
	private final List<ErrorMessage> errors;
	private final IProcessTypingRules procTypechecker;

	public SensorTypingRules(CodeType sensorCodeType,
			Scope<Symbol, CallasType> sensorScope,
			Scope<Symbol, CallasType> moduleScope, List<ErrorMessage> errors) {
		this.sensorScope = sensorScope;
		this.moduleScope = moduleScope;
		this.errors = errors;
		this.procTypechecker = new ProcessTypingRulesImpl(sensorScope,
				errors, sensorCodeType);
	}

	public void tOff(EmptyNetwork s) {
		// OK
	}

	public void tPar(NetworkComposition s) {
		typecheck(s.left);
		typecheck(s.right);
	}

	private void typecheck(Iterable<? extends CallasProcess> procs) {
		for (CallasProcess proc : procs) {
			typecheck(proc);
		}
	}

	private void typecheck(CallasProcess proc) {
		procTypechecker.typecheck(getModuleScope(), proc);
	}

	public void tSensor(Sensor s) {
		typecheck(s.getRunning());
		typecheck(s.getInput());
		procTypechecker.typecheck(moduleScope, s.getInstalledCode());
		typecheck(s.getOutput());
		typecheck(s.getRunQueue());
		for (Event event : s.getEvents()) {
			typecheck(event);
		}
	}

	private void typecheck(Event event) {
		typecheck(event.getCall());
		typecheck(event.getIncrement());
		typecheck(event.getNextTrigger());
		typecheck(event.getTimeToExpire());
	}

	public void typecheck(CallasNetwork s) {
		if (s instanceof EmptyNetwork) {
			tOff((EmptyNetwork) s);
		} else if (s instanceof NetworkComposition) {
			tPar((NetworkComposition) s);
		} else if (s instanceof Sensor) {
			tSensor((Sensor) s);
		} else {
			throw new IllegalArgumentException("Unsupported class: "
					+ s.getClass());
		}
	}

	/**
	 * @return the sensorScope
	 */
	public Scope<Symbol, CallasType> getSensorScope() {
		return sensorScope;
	}

	/**
	 * @return the moduleScope
	 */
	public Scope<Symbol, CallasType> getModuleScope() {
		return moduleScope;
	}

	/**
	 * @return the errors
	 */
	public List<ErrorMessage> getErrors() {
		return errors;
	}
}
