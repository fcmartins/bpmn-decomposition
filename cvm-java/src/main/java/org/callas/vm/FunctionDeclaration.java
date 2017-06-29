package org.callas.vm;

/**
 * The declaration that is used to create the run-time representation of a
 * {@link Function}.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @author Pedro Gomes (prg@dcc.fc.up.pt)
 * @date Mar 18, 2009
 */
public class FunctionDeclaration {

	public final String name;
	public final byte[] byteCode;
	public final Object[] symbols;
	public final byte parametersCount;
	public final byte localsCount;

	public FunctionDeclaration(String name, byte parametersCount,
			byte localsCount, byte[] byteCode, Object[] symbols) {
		if (parametersCount > localsCount) {
			throw new IllegalArgumentException("Function " + name
					+ ": number of parameters (" + parametersCount
					+ ") is greater than the number of locals (" + localsCount
					+ ").");
		}
		if (parametersCount < 1) {
			throw new IllegalArgumentException("Function " + name
					+ ": minimum one parameter, " + parametersCount + " given.");
		}
		this.name = name;
		this.byteCode = byteCode;
		this.symbols = symbols;
		this.parametersCount = parametersCount;
		this.localsCount = localsCount;
	}
}
