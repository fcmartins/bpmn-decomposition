package org.callas.absyn;

import java.util.Collection;
import java.util.LinkedList;

import org.tyco.common.errorMsg.ISourceLocationHolder;
import org.tyco.common.errorMsg.SourceLocation;

public class SourceValue<T> implements ISourceLocationHolder {

	private final SourceLocation location;
	private final T value;

	public SourceValue(SourceLocation location, T value) {
		this.location = location;
		this.value = value;
	}

	public SourceLocation getSourceLocation() {
		return location;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Utility functions to dispose of the source location of a collection of
	 * values.
	 * 
	 * @param <T>
	 * @param sources
	 * @return
	 */
	public static <T> Collection<T> filterSourceLocations(Iterable<SourceValue<T>> sources) {
		Collection<T> result = new LinkedList<T>();
		for (SourceValue<T> sourceValue : sources) {
			result.add(sourceValue.getValue());
		}
		return result;
	}
}
