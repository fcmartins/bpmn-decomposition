package org.callas.error;

import org.callas.absyn.Absyn;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.IPrinter;
import org.tyco.common.errorMsg.SourceLocation;

/**
 * The number of arguments differs from the expected count.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 */
public class ArgumentCountMismatch extends ErrorMessage {

	/**
	 * The number of parameters expected.
	 */
	public final int expected;

	/**
	 * The number of parameters found.
	 */
	public final int provided;

	public ArgumentCountMismatch(Absyn absyn, int expected, int provided) {
		this(absyn.getSourceLocation(), absyn, expected, provided);
	}

	public ArgumentCountMismatch(SourceLocation loc, Object what, int expected,
			int provided) {
		super(loc, what);
		this.expected = expected;
		this.provided = provided;
	}

	@Override
	public void printDetails(IPrinter<Object> out) {
		// ok
	}

	@Override
	public String toString() {
		return "Arguments mismatch. Expecting " + expected + " but found "
				+ provided + ".";
	}

}
