package org.callas.absyn;

import org.callas.absyn.processes.*;
import org.callas.absyn.sensors.CallasNetwork;
import org.callas.absyn.sensors.EmptyNetwork;
import org.callas.absyn.sensors.NetworkComposition;
import org.callas.absyn.sensors.Sensor;
import org.callas.absyn.types.*;
import org.tyco.common.util.ParamTypeDispatcher;

/**
 * Acts serves as the schema of this package, listing and grouping the included
 * classes. It is useful for classes like the {@link ParamTypeDispatcher}.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 27, 2008
 *
 */
public class AbsynPackageSchema {

	/**
	 * The concrete subclasses of {@link CallasValue}.
	 */
	public static final Class<?>[] VALUES = {
		BBool.class,
		BDouble.class,
		BLong.class,
		BString.class,
		Variable.class,
		Code.class,
		UnaryOperation.class,
		BinaryOperation.class,
	};
	
	/**
	 * The concrete subclasses of {@link CallasProcess}.
	 */
	public static final Class<?>[] PROCESSES = {
		// The values
		BBool.class,
		BDouble.class,
		BLong.class,
		BString.class,
		Variable.class,
		Code.class,
		UnaryOperation.class,
		BinaryOperation.class,
		//
		Call.class,
		Extern.class,
		Timer.class,
		Kill.class,
		Open.class,
		Close.class,
		Send.class,
		Receive.class,
		Update.class,
		Let.class,
		Branch.class,
		StoreSensorCode.class,
		LoadSensorCode.class
	};
	
	/**
	 * The concrete subclasses of {@link CallasNetwork}.
	 */
	public static final Class<?>[] NETWORKS = {
		Sensor.class,
		NetworkComposition.class,
		EmptyNetwork.class		
	};
	
	/**
	 * The concrete subclasses of {@link CallasType}.
	 */
	public static final Class<?>[] TYPES = {
		CodeType.class,
		BBoolType.class,
		BDoubleType.class,
		BLongType.class,
		BStringType.class,
		FunctionType.class,
		RecursiveType.class,
		TypeVariable.class
	};

}