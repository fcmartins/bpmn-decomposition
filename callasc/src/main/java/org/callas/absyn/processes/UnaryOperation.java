package org.callas.absyn.processes;

import org.tyco.common.errorMsg.SourceLocation;

/**
 * Represents an unary operation.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 26, 2009
 * 
 */
public class UnaryOperation extends CallasValue {
	/**
	 * The operator.
	 */
	public final UnaryOperator operator;
	/**
	 * The target of the operation.
	 */
	public final CallasValue operand;

	/**
	 * The default constructor.
	 * @param sourceLocation
	 * @param operator
	 * @param operand
	 */
	public UnaryOperation(SourceLocation sourceLocation, UnaryOperator operator,
			CallasValue operand) {
		super(sourceLocation);
		this.operator = operator;
		this.operand = operand;
	}
	
	/**
	 * This constructor discards the source code location.
	 * @param operator
	 * @param operand
	 */
	public UnaryOperation(UnaryOperator operator, CallasValue operand) {
		super();
		this.operator = operator;
		this.operand = operand;
	}

	@Override
	public String toString() {
		return String.format("(%s %s)", operator, operand);
	}
}
