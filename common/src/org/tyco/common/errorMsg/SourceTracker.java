package org.tyco.common.errorMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Tracks the column number and the line number associated with various files.
 * 
 * @author Tiago Cogumbreiro
 *
 */
public class SourceTracker {
	/**
	 * A list of sources, usefull for searching the appropriate source
	 * description regarding an absolute offset.
	 */
	private List<SourceDescription> sources = new ArrayList<SourceDescription>();
	
	/**
	 * Stack that keeps track of the current source description, as well as
	 * a stack of previously opened sources.
	 */
	private Stack<SourceDescription> stack = new Stack<SourceDescription>();
	
	/**
	 * Invoke when a new file is read.
	 * @param filename the new filename
	 * @param pos the absolute position
	 */
	public void pushFile(String filename, int pos) {
		if (stack.size() > 0) {
			assert stack.peek().start <= pos: String.format("start: %d, end: %d", Integer.valueOf(stack.peek().start), Integer.valueOf(pos));
			stack.peek().end = pos;
		}
		SourceDescription next = new SourceDescription(filename, pos);
		sources.add(next);
		stack.push(next);
	}
	
	/**
	 * Invoke when the file is done reading.
	 * @param end the current absolute position of this file (marks the end)
	 */
	public void popFile(int end) {
		SourceDescription source = stack.pop();
		source.end = end;
		
		if (stack.size() > 0) {
			SourceDescription prev = stack.pop();
			int offset[] = prev.getLineCol(prev.end);
			SourceDescription next = new SourceDescription(prev.filename, end, offset[1], offset[0] - 1);
			sources.add(next);
			stack.push(next);
		}
		assert source.start <= end: String.format("start: %d, end: %d", Integer.valueOf(source.start), Integer.valueOf(source.end));
	}
	
	/**
	 * Marks a new line.
	 * @param pos
	 */
	public void newLine(int pos) {
		stack.peek().newLine(pos);
	}
	
	/**
	 * Returns the line number and the column number, relative to the absolute
	 * position.
	 * @param pos the absolute position.
	 * @return an array with the line number followed by the column number
	 */
	public SourceLocation getLocation(int pos) {
		for (SourceDescription src: sources) {
			if (src.contains(pos)) {
				return new SourceLocation(src.filename, src.getLineCol(pos));
			}
		}
		assert false;
		throw new Error(String.format("Call pushFile() first. Pos: %d\n%s", Integer.valueOf(pos), sources));
	}
}

	