package org.callas.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.callas.absyn.sensors.CallasNetwork;
import org.callas.absyn.sensors.EmptyNetwork;
import org.callas.absyn.sensors.NetworkComposition;
import org.callas.absyn.sensors.Sensor;

/**
 * Linearizes a sensor network turning any {@link CallasNetwork} into an
 * iterator of {@link Sensor}s.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jan 9, 2009
 * 
 */
public class SensorIterator implements Iterator<Sensor> {

	private static class SensorNodeIterable implements Iterable<Sensor> {
		private CallasNetwork sensor;

		public SensorNodeIterable(CallasNetwork sensor) {
			this.sensor = sensor;
		}

		public Iterator<Sensor> iterator() {
			return new SensorIterator(sensor);
		}
	}

	private boolean hasFinished = false;
	private CallasNetwork sensor;
	private Iterator<Sensor> left;
	private Iterator<Sensor> right;

	public SensorIterator(CallasNetwork sensor) {
		this.sensor = sensor;
		if (sensor instanceof EmptyNetwork) {
			hasFinished = true;
		}
		if (sensor instanceof NetworkComposition) {
			NetworkComposition par = (NetworkComposition) sensor;
			left = new SensorIterator(par.left);
			right = new SensorIterator(par.right);
		}
	}

	public boolean hasNext() {
		if (hasFinished) {
			return false;
		}
		if (sensor instanceof NetworkComposition) {
			return left.hasNext() || right.hasNext();
		}
		return true;
	}

	public Sensor next() {
		if (hasFinished) {
			throw new NoSuchElementException();
		}
		if (sensor instanceof Sensor) {
			hasFinished = true;
			return (Sensor) sensor;
		}
		if (left.hasNext()) {
			return left.next();
		}
		return right.next();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates an iterable. Very useful for using in foreach'es.
	 * 
	 * @param sensor
	 *            A callas sensor.
	 * @return An iterable.
	 */
	public static final Iterable<Sensor> flattenNetwork(CallasNetwork sensor) {
		return new SensorNodeIterable(sensor);
	}

	/**
	 * From an iterable of sensors constructs a network.
	 * 
	 * @param sensors
	 *            The sensors that will constitute the network.
	 * @return A network of sensors.
	 */
	public static final CallasNetwork constructNetwork(Iterable<Sensor> sensors) {
		CallasNetwork network = EmptyNetwork.EMPTY_NETWORK;
		for (Sensor node : sensors) {
			if (network == EmptyNetwork.EMPTY_NETWORK) {
				network = node;
			} else {
				network = new NetworkComposition(network, node);
			}
		}
		return network;
	}
}
