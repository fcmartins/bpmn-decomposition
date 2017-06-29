package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Represents a binary operation.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 26, 2009
 * 
 */
public class BinaryOperation extends CallasValue {
	/**
	 * @param sourceLocation
	 * @param left
	 * @param operator
	 * @param right
	 */
	public BinaryOperation(SourceLocation sourceLocation, CallasValue left,
			BinaryOperator operator, CallasValue right) {
		super(sourceLocation);
		this.left = left;
		this.operator = operator;
		this.right = right;
	}
	
	/**
	 * Constructor that discards the source code location.
	 * @param left
	 * @param operator
	 * @param right
	 */
	public BinaryOperation(CallasValue left, BinaryOperator operator,
			CallasValue right) {
		super();
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	public final CallasValue left;
	public final BinaryOperator operator;
	public final CallasValue right;

	@Override
	public String toString() {
		return String.format("( %s %s %s)", left, operator, right);
	}
}
