package org.callas.semant;

import java.util.List;

import org.callas.absyn.sensors.CallasNetwork;
import org.callas.absyn.sensors.EmptyNetwork;
import org.callas.absyn.sensors.NetworkComposition;
import org.callas.absyn.sensors.Sensor;
import org.callas.absyn.types.CallasType;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.symtable.Scope;

/**
 * Typing rules for sensor networks.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 6, 2009
 * 
 */
public interface ISensorTypingRules {
	/**
	 * Where we report errors.
	 * 
	 * @return
	 */
	List<ErrorMessage> getErrors();

	/**
	 * Rule T-sensor.
	 * 
	 * @param scope
	 * @param s
	 */
	void tSensor(Sensor s);

	/**
	 * Rule T-off.
	 * 
	 * @param scope
	 * @param s
	 */
	void tOff(EmptyNetwork s);

	/**
	 * Rule T-par.
	 * 
	 * @param scope
	 * @param s
	 */
	void tPar(NetworkComposition s);

	/**
	 * Dispatches to the appropriate typing rule for sensors.
	 * 
	 * @param s
	 */
	void typecheck(CallasNetwork s);

	/**
	 * The sensor type environment.
	 * 
	 * @return
	 */
	Scope<Symbol, CallasType> getSensorScope();

	/**
	 * The module type environment.
	 * 
	 * @return
	 */
	Scope<Symbol, CallasType> getModuleScope();
}
