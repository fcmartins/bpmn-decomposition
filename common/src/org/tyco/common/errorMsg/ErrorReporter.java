package org.tyco.common.errorMsg;

import java.util.ArrayList;
import java.util.List;

import org.tyco.common.util.SourceElement;


/**
 * This class reports semantic errors, by printing a description of the error,
 * together with the type expected and the type found. We try to be somewhat
 * friendly.
 * 
 * @author Vasco T. Vasconcelos, Andre Goncalves, Tiago Cogumbreiro
 * @version $Id: ErrorReporter.java,v 1.7 2008/10/07 00:42:36 tyco Exp $
 */
public class ErrorReporter extends ErrorMsg {

	/**
	 * The error messages reported.
	 */
	public List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
	/**
	 * Where messages will be printed to.
	 */
	private final IPrinter printer;
	
	/**
	 * Construct given the initial source file.
	 * 
	 * @param filename
	 *            The initial source file.
	 */
	public ErrorReporter(String filename, IPrinter printer) {
		super(filename, printer);
		this.printer = printer;
	}
	
	@Override
	public int noOfErrors() {
		return errors.size();
	}

	/**
	 * Report that an error has occurred.
	 * @param err
	 * The error.
	 */
	@SuppressWarnings("unchecked")
	public void report(ErrorMessage err) {
		// print the line number and the error title
		super.error(err.pos, err.toString());
		// print the details
		err.printDetails(printer);
		// print the object associated to the error
		if (err.what != null) {
			printer.print(err.what);
		}
		printer.print("\n");
		// now add the error to the list
		errors.add(err);
	}
}
