package org.tyco.common.errorMsg;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Used internally to compute the relative line and column (of a filename)
 * from an absolute position.
 *  
 * @author Tiago Cogumbreiro
 * @version $Id: SourceDescription.java,v 1.4 2009/01/02 17:57:18 tyco Exp $
 *
 */
class SourceDescription {
	/**
	 * The initial position of this source.
	 */
	public int start;
	/**
	 * The last position of this source.
	 */
	public int end = Integer.MAX_VALUE;
	/**
	 * The filename of this source.
	 */
	public String filename;
	/**
	 * A set of reverse ordered lines.
	 */
	public SortedSet<Integer> lines = new TreeSet<Integer>(Collections.reverseOrder());
	/**
	 * The column offset.
	 */
	private int colOffset;
	/**
	 * The line offset.
	 */
	private int lineOffset;
	
	/**
	 * Creates a Source descriptor, containing the filename and the associated
	 * line numbers (through offsets).
	 * @param filename
	 * @param head
	 * @param offset the relative offset.
	 */
	public SourceDescription(String filename, int head, int colOffset, int lineOffset) {
		this.filename = filename;
		this.start = head;
		this.colOffset = colOffset;
		this.lineOffset = lineOffset;
	}
	
	/**
	 * Creates a Source descriptor, containing the filename and the associated
	 * line numbers (through offsets).
	 * @param filename
	 * @param head
	 */
	public SourceDescription(String filename, int head) {
		this(filename, head, 0, 0);
	}
	
	/**
	 * Marks a new line.
	 * @param pos
	 */
	public void newLine(int pos) {
		lines.add(Integer.valueOf(pos));
	}
	
	/**
	 * Returns the line number and the column number, relative to the absolute
	 * position.
	 * @param pos the absolute position.
	 * @return an array with the line number followed by the column number
	 */
	public int[] getLineCol(int pos) {
		assert contains(pos) : String.format("%d <= %d <= %d", start, pos, end);
		int line = lines.size();
		int currHead = start;
		int currLine = 0;
		for (int linePos: lines) {
			if (linePos < pos) {
				currHead = linePos;
				currLine = line;
				break;
			}
			line--;
		}
		return new int[]{currLine + 1 + lineOffset, pos - currHead + colOffset};
	}
	
	/**
	 * Checks if the absolute position is contained within this
	 * SourceDescription.
	 * @param pos
	 * @return
	 */
	public boolean contains(int pos) {
		return start <= pos && pos <= end;
	}
	
	@Override
	public String toString() {
		return String.format("{start:%d, end: %d, filename: '%s'}", Integer.valueOf(start), Integer.valueOf(end), filename);
	}
}
