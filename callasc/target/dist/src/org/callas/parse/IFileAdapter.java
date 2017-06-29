package org.callas.parse;

import org.tyco.common.errorMsg.ErrorMessagesException;

/**
 * Adapts the content of a file into another type.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 15, 2009
 * 
 * @param <S>
 *            The type of the source object.
 * @param <T>
 *            The type of the target object (the adapted).
 */
public interface IFileAdapter<S, T> {
	/**
	 * Adapts the source object into a given one.
	 * 
	 * @param filename
	 *            The filename that the error messages should be related to.
	 * @param source
	 *            The contents of the file.
	 * @return The adapted object.
	 * @throws ErrorMessagesException
	 *             Any problems occured while adapting.
	 */
	T adapt(String filename, S source) throws ErrorMessagesException;
}
