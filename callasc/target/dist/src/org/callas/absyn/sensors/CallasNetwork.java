package org.callas.absyn.sensors;
import org.callas.absyn.Absyn;
import org.tyco.common.errorMsg.SourceLocation;

/**
 * The base type for all sensors.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 5, 2008
 *
 */
public abstract class CallasNetwork extends Absyn {
	public CallasNetwork(SourceLocation location) {
		super(location);
	}

	public CallasNetwork() {
		super();
	}
}
