package org.tyco.common.errorMsg;


/** 
 * This class provides means to report error messages containing 
 * filename, line, and column information. 
 * It is a modification of the errorMsg class as found in the 
 * "Tiger" book (see the package description).
 * Modified by Joao Martins to handle multiple files.
 *
 * @author Andrew Appel, Joao Martins, Vasco T. Vasconcelos, Andre Goncalves
 * @version $Id$
 */
public class ErrorMsg {
	
    /**
     * The number of errors reported (via method <code>error</code>).
     */
    private int noOfErrors;
    
	/**
	 * The object that will print the error messages.
	 */
	private final IPrinter printer;
	
	/**
	 * The source code tracker (for lines and columns).
	 */
	private SourceTracker tracker = new SourceTracker();

    /**
     * Construct given the initial source file and stream.
     * @param filename The initial source file.
     * @param outStream The stream where we print the error messages.
     */
    public ErrorMsg(String filename, java.io.PrintStream out) {
    	this(filename, new StreamPrinter(out));	
    }	
    
    /**
     * Construct given the initial source file and writer.
     * @param filename The initial source file.
     * @param printer The printer where we print the error messages.
     */
    public ErrorMsg(String filename, IPrinter printer) {
    	tracker.pushFile(filename, -1);
    	this.printer = printer;
    }

	/**
	 *  The number of errors reported so far.
	 */
	public int noOfErrors() {
		return noOfErrors;
	}

    /**
     * Inform that a new file was included.
     * @param f The name of the new file.
     * @param pos The position where the file was included.
     */
    public void newfile(int pos, String f) {
		tracker.pushFile(f, pos);
    }

    /**
     * Inform that the current file has ended, and all 
     * subsequent positions belong to the previous file.
     * @param pos The position where the file ended.
     */
    public void endfile(int pos) {
        tracker.popFile(pos);
    }

    /**
     * Inform that a line change occurred in the source file.
     * @param pos The position where the line change occurred.
     */
    public void newline(int pos) {
        tracker.newLine(pos);
    }

	/**
	 * Print an error message.
	 * @param pos The position in the source-file.
	 * @param msg The message to print.
	 */
	public void error(int pos, String msg) {
		noOfErrors++;
		say(pos, msg);
	}

    /**
     * Print a message on a given <code>PrintStream</code>.
     * Calculates the filename, line, and column number to
     * prefix the message.
     * @param pos The position in the source-file. If it is -1, then it is
     * assumed that the message is not bound to a file.
     * @param msg The message to print.
     * @param out The stream where to print.
     */
    public void say(int pos, String msg) {
    	if (pos == -1) {
    		printer.print(msg + "\n");
    	} else {
    		SourceLocation loc = tracker.getLocation(pos);
    		printer.print(loc.filename + ":" + loc.line + "." + loc.col + ": " + msg + "\n");
    	}
    }
    
    /**
     * Returns the line number
     * @param pos
     * @return
     */
	public int getLine(int pos) {
		return tracker.getLocation(pos).line;
	}

	/**
	 * Returns the column number.
	 * @param pos
	 * @return
	 */
	public int getCol(int pos) {
		return tracker.getLocation(pos).col;
	}

}
