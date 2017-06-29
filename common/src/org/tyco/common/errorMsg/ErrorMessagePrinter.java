package org.tyco.common.errorMsg;

public class ErrorMessagePrinter implements IPrinter<ErrorMessage> {

	private IPrinter<Object> printer;
	
	/**
	 * @param printer will print the objects bound to the error messages and
	 */
	public ErrorMessagePrinter(IPrinter<Object> printer) {
		this.printer = printer;
	}

	public void print(ErrorMessage err) {
		SourceLocation loc = err.location;
		if (loc != null) {
			String fname = loc.filename == null ? "" : loc.filename + ":";
			printer.print(fname + loc.line + "." + loc.col + ": ");
		}
		printer.print(err.toString() + "\n");
		// print the details
		err.printDetails(printer);
		// print the object associated to the error
		if (err.what != null) {
			printer.print(err.what);
			printer.print("\n");
		}
	}

}
