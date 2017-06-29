package org.callas.absyn.sensors;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * The composition of sensors.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 5, 2008
 *
 */
public class NetworkComposition extends CallasNetwork {
	/**
	 * The element on the left.
	 */
	public CallasNetwork left;

	/**
	 * The element on the right.
	 */
	public CallasNetwork right;

	/**
	 * Create a sensor composition specifying the source code location.
	 * @param location
	 * @param left
	 * @param right
	 */
	public NetworkComposition(SourceLocation location, CallasNetwork left, CallasNetwork right) {
		super(location);
		this.left = left;
		this.right = right;
	}

	/**
	 * Create a sensor composition not specifying the source code location.
	 * @param left
	 * @param right
	 */
	public NetworkComposition(CallasNetwork left, CallasNetwork right) {
		this(left.getSourceLocation(), left, right);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NetworkComposition other = (NetworkComposition) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

}
