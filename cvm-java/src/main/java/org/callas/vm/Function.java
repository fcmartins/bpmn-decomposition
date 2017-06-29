package org.callas.vm;

/**
 * This is the run-time representation of a Function.
 * 
 * @author Pedro Gomes (prg@dcc.fc.up.pt)
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Mar 18, 2009
 */
public class Function {

	public final Object[] freeVariables;
	public final FunctionDeclaration declaration;

	/**
	 * Constructs a function from its function declaration and from 
	 * @param declaration
	 * @param freeVariables
	 */
	public Function(FunctionDeclaration declaration, Object[] freeVariables) {
		this.freeVariables = freeVariables;
		this.declaration = declaration;
	}

	/**
	 * Utility constructor for functions with no free variables.
	 * @param declaration
	 */
	public Function(FunctionDeclaration declaration) {
		this(declaration, new Object[0]);
	}

	/**
	 * Creates copy of the given function.
	 * 
	 * @param other The function to copy.
	 */
	public Function(Function other) {
		freeVariables = new Object[other.freeVariables.length];
		declaration = other.declaration;
		System.arraycopy(other.freeVariables, 0, freeVariables, 0,
				other.freeVariables.length);
	}
}
