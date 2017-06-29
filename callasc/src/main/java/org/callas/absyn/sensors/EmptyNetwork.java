package org.callas.absyn.sensors;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * The empty network.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 5, 2008
 *
 */
public class EmptyNetwork extends CallasNetwork {
    public EmptyNetwork(SourceLocation location) {
		super(location);
	}
    
    private EmptyNetwork() {
    	super();
    }

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof EmptyNetwork;
	}

	/**
	 * The singleton empty network.
	 */
    public static final EmptyNetwork EMPTY_NETWORK = new EmptyNetwork();
}
