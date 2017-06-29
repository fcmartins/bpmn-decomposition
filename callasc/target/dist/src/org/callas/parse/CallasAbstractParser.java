package org.callas.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.callas.core.IFileLoader;
import org.tyco.common.util.AbstractParser;

public abstract class CallasAbstractParser<T> extends AbstractParser<T> {
	protected final IFileLoader fileLoader;

	public CallasAbstractParser(IFileLoader fileLoader) {
		this.fileLoader = fileLoader;
	}

	@Override
	protected InputStream toInputStream(File file) throws FileNotFoundException {
		try {
			return fileLoader.loadFile(file.toString());
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			// break it all
			throw new IllegalArgumentException(e);
		}
	}
}
