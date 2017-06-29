package org.tyco.common.util;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;

import org.tyco.common.api.IParser;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.ErrorMessagesException;

/**
 * An abstract parser based on JavaCUP.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Sep 14, 2009
 * 
 * @param <T>
 */
public abstract class AbstractParser<T> implements IParser<T> {

	public T parse(String data) throws ErrorMessagesException {
		InputStream stream = new ByteArrayInputStream(data.getBytes());
		return doParse(null, stream);
	}

	public T parse(String filename, InputStream stream)
			throws ErrorMessagesException {
		return doParse(filename, stream);
	}

	protected InputStream toInputStream(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}
	
	/**
	 * Note that the input stream may not be null.
	 * 
	 * @param errors
	 *            The list where errors should be reported to.
	 * @param filename
	 *            The filename of the source code. This is optional and
	 *            <code>null</code> represents that the filename was not
	 *            provided.
	 * @param inputStream
	 *            The input stream that consists of the source code.
	 * @return The
	 */
	abstract protected lr_parser createGrm(List<ErrorMessage> errors,
			String filename, InputStream inputStream);

	/**
	 * Converts the parsed object into an object of type <code>T</code>.
	 * 
	 * @param errors
	 *            The list where we should report errors to.
	 * @param parsed
	 *            The object that was parsed by the <code>lr_parser</code>
	 *            object.
	 * @return The result of the parsing, an object of type <code>T</code>.
	 */
	@SuppressWarnings("unchecked")
	protected T adaptParsed(List<ErrorMessage> errors, Object parsed) {
		return (T) parsed;
	}

	private T doParse(String filename, InputStream stream)
			throws ErrorMessagesException {
		if (filename == null && stream == null) {
			throw new IllegalArgumentException();
		}
		List<ErrorMessage> errors = new LinkedList<ErrorMessage>();
		lr_parser grm = createGrm(errors, filename, stream);
		Symbol parsed;
		try {
			parsed = grm.parse();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		T val = adaptParsed(errors, parsed.value);
		if (val == null) {
			throw new NullPointerException("Parser returned null");
		}
		if (errors.size() > 0) {
			throw new ErrorMessagesException(errors);
		}
		return val;
	}

}
