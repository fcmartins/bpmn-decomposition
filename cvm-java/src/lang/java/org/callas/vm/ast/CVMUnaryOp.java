package org.callas.vm.ast;


public class CVMUnaryOp extends CVMStmt {
	/**
	 * The operator.
	 */
	public final UnaryOperator operator;

	/**
	 * @param operator
	 */
	public CVMUnaryOp(UnaryOperator operator) {
		this.operator = operator;
	}
	
	@Override
	public String toString() {
		return operator.toString();
	}
}
