package org.tyco.common.errorMsg;

/**
 * A source code location.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: SourceLocation.java,v 1.2 2009/10/15 16:33:08 tyco Exp $
 * 
 */
public class SourceLocation {
	/**
	 * the line number (base 1).
	 */
	public final int line;

	/**
	 * The column number (base 0).
	 */
	public final int col;

	/**
	 * The name of the file.
	 */
	public String filename;

	/**
	 * Creates a new source location.
	 * 
	 * @param filename
	 * @param line
	 * @param col
	 */
	public SourceLocation(String filename, int line, int col) {
		this.filename = filename;
		this.line = line;
		this.col = col;
	}

	/**
	 * Creates a new source location from an array containing the number of the
	 * line (position 0) and the number of the column (position 1).
	 * 
	 * @param filename
	 * @param linecol
	 */
	public SourceLocation(String filename, int[] linecol) {
		this(filename, linecol != null ? linecol[0] : -1,
				linecol != null ? linecol[1] : -1);
	}

	/**
	 * A source location that discards the source code location, but specifies
	 * the filename.
	 * 
	 * @param filename The name of the file holding the source code.
	 */
	public SourceLocation(String filename) {
		this(filename, null);
	}
}
