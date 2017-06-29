package org.callas.error;

import org.callas.absyn.Absyn;
import org.callas.util.DifferentTypesException;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.IPrinter;

/**
 * Expected one type, but was given another.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class TypeMismatch extends ErrorMessage {

	private DifferentTypesException exception;

	public TypeMismatch(Absyn element, DifferentTypesException e) {
		super(element.getSourceLocation(), null);
		this.exception = e;
	}

	@Override
	public void printDetails(IPrinter<Object> out) {
		DifferentTypesException e = exception;
		while (e != null) {
			out.print(e.getMessage());
			out.print("\n");
			out.print("Expecting type:\n");
			out.print(e.type1);
			out.print("\nBut found a different type:\n");
			out.print(e.type2);
			out.print("\n");

			e = (DifferentTypesException) e.getCause();
			if (e != null) {
				out.print("|\n+-Because of the following error:\n");
			}
		}
	}

	@Override
	public String toString() {
		return "Type mismatch.";
	}

}
