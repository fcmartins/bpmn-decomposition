package org.callas.parse;

import java.io.InputStream;

import org.tyco.common.api.IParser;
import org.tyco.common.errorMsg.ErrorMessagesException;

/**
 * Is viewed as a parser, but infact it adapts the outcome of one parser into
 * another type.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 * @param <S>
 *            The type of the objects being originally parsed.
 * @param <T>
 *            The type of the objects that this parser parses (adapts).
 */
class AdaptedParser<S, T> implements IParser<T> {

	private final IParser<S> delegated;

	private final IFileAdapter<S, T> adapter;

	/**
	 * Given a parser and an adapter we create a new parser that parses elements
	 * from the outcome of the adapter.
	 * 
	 * @param delegated
	 *            The decorated parser.
	 * @param adapter
	 *            The adapter that converts the outcome of the decorated parser.
	 */
	public AdaptedParser(IParser<S> delegated, IFileAdapter<S, T> adapter) {
		this.delegated = delegated;
		this.adapter = adapter;
	}

	public T parse(String data) throws ErrorMessagesException {
		return adapter.adapt(null, delegated.parse(data));
	}

	public T parse(String filename, InputStream stream)
			throws ErrorMessagesException {
		return adapter.adapt(filename, delegated.parse(filename, stream));
	}

}
