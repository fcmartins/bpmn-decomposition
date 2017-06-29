package org.callas.callas;

import java.util.LinkedList;
import java.util.List;

import org.callas.absyn.FileSection;
import org.callas.absyn.NetworkFile;
import org.callas.absyn.SourceValue;
import org.callas.absyn.processes.CallasProcess;
import org.callas.absyn.sensors.CallasNetwork;
import org.callas.absyn.sensors.Sensor;
import org.callas.absyn.types.CodeType;
import org.callas.util.SensorIterator;
import org.tyco.common.api.IParser;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.ErrorMessagesException;
import org.tyco.common.errorMsg.SourceLocation;
import org.tyco.common.errorMsg.msgs.SyntacticError;
import org.tyco.common.symtable.KeyNotFoundException;

/**
 * This is a utility class. Should not be used directly.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Feb 17, 2010
 * 
 */
class NetworkFileExtractor {
	private final NetworkFile value;

	/**
	 * @param value
	 */
	public NetworkFileExtractor(NetworkFile value) {
		this.value = value;
	}

	public SourceValue<CodeType> getNetworkInterface(IParser<CodeType> ifaceParser)
			throws ErrorMessagesException {
		try {
			CodeType iface = value.parseFieldWith("interface", ifaceParser);
			SourceLocation loc = value.get("interface").getSourceLocation();
			return new SourceValue<CodeType>(loc, iface);
		} catch (KeyNotFoundException e) {
			// ok
		}
		// default
		return new SourceValue<CodeType>(value.getSourceLocation(), CodeType.NIL_TYPE);
	}

	public CallasNetwork getNetwork(IParser<CallasProcess> procParser)
			throws ErrorMessagesException {
		List<ErrorMessage> errors = new LinkedList<ErrorMessage>();
		List<Sensor> sensors = new LinkedList<Sensor>();
		for (FileSection sect : value.getSensorSections()) {
			try {
				CallasProcess proc = sect.parseFieldWith("code", procParser);
				Sensor sensor = new Sensor(sect.getSourceLocation(), proc);
				sensors.add(sensor);
			} catch (KeyNotFoundException e) {
				errors.add(new SyntacticError(sect.getSourceLocation(),
						"All sensors must declare a 'code' field."));
			}
		}
		if (errors.size() > 0) {
			throw new ErrorMessagesException(errors);
		}
		CallasNetwork network = SensorIterator.constructNetwork(sensors);
		return network;
	}

}
