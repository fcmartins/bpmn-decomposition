package org.callas.vm.ast;

public class CVMBinaryOp extends CVMStmt {
	/**
	 * The operator.
	 */
	public final BinaryOperator operator;

	/**
	 * @param operator
	 */
	public CVMBinaryOp(BinaryOperator operator) {
		this.operator = operator;
	}
	
	@Override
	public String toString() {
		return operator.toString();
	}
}
