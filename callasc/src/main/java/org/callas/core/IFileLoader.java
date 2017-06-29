package org.callas.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An interface for loading files from strings that should be the filesystem
 * location. This interface allows for loading files from virtual places and
 * also helps testings without having the need for real files.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 11, 2009
 * 
 */
public interface IFileLoader {
	/**
	 * From a given string format load an inputstream.
	 * 
	 * @param format
	 *            The filename.
	 * @return The input stream representing the file-
	 * @throws IOException
	 *             Loading files might lead to IO exceptions.
	 */
	InputStream loadFile(String format) throws IOException;

	/**
	 * This is the standard loader that creates a file input stream from a
	 * filename.
	 */
	IFileLoader STANDARD_IO = new IFileLoader() {
		public InputStream loadFile(String format) throws IOException {
			return new FileInputStream(new File(format));
		}
	};
}
