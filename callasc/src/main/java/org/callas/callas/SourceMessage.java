package org.callas.callas;

import java.util.logging.Level;

/**
 * A message associated with a certain source code.
 * 
 * @author Tiago Cogumbreiro
 *
 */
public class SourceMessage {
	/**
	 * A one line description of the message.
	 */
	public String title;
	/**
	 * A multi-line description of the message. For example, showing the
	 * expected type of a certain variable.
	 * 
	 */
	public String summary;
	/**
	 * A file-system dependent file name, containing the source code.
	 */
	public String filename;
	/**
	 * The offset, from the start of a line, in terms of characters, where the
	 * error is found. The base of the index is 1.
	 */
	public int column;
	/**
	 * The offset, from the start of the file, in terms of lines, where the
	 * error is found. The base of the index is 1.
	 */
	public int line;
	/**
	 * The severity of the error. This specification of this datum is specified
	 * next.
	 */
	public Level level;

	/**
	 * @param title
	 * @param summary
	 * @param filename
	 * @param column
	 * @param line
	 * @param level
	 */
	public SourceMessage(String title, String summary, String filename,
			int column, int line, Level level) {
		this.title = title;
		this.summary = summary;
		this.filename = filename;
		this.column = column;
		this.line = line;
		this.level = level;
	}
}
