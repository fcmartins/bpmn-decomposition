package org.callas.vm;

import java.util.Enumeration;
import java.util.Stack;

/**
 * This class represents the structure of a running process.
 * 
 * @author Pedro Gomes (prg@dcc.fc.up.pt)
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date May 30, 2009
 * 
 */
public class Running extends ByteCodeCursor {
	private final static byte[] CALL_CODE = new byte[] {Opcodes.LDC, 0, Opcodes.CALL, 
		Opcodes.RET };
	
	private final Stack operandsStack;
	public final Object[] environment;
	public final Object[] symbols;
	/**
	 * Only useful for debugging.
	 * 
	 * @param programCounter
	 * @param operandsStack
	 *            The order of the array has the top of the queue on the 0-th
	 *            index and the first element on the last element to the right.
	 * @param environment
	 * @param byteCode
	 * @param symbols
	 */
	Running(int programCounter, Object[] operandsStack, Object[] environment,
			byte[] byteCode, Object[] symbols) {
		super(programCounter, byteCode);
		this.operandsStack = new Stack();
		for (int i = operandsStack.length - 1; i >= 0; i--) {
			this.operandsStack.push(operandsStack[i]);
		}
		this.environment = environment;
		this.symbols = symbols;
	}

	/**
	 * Creates a running process by applying the arguments in a function.
	 * 
	 */
	public Running(Function function, Object[] arguments) {
		super(0, function.declaration.byteCode);
		this.operandsStack = new Stack();
		this.symbols = function.declaration.symbols;
		this.environment = new Object[function.declaration.localsCount];
		System.arraycopy(arguments, 0, environment, 0, arguments.length);
		System.arraycopy(function.freeVariables, 0, environment,
				function.declaration.parametersCount,
				function.freeVariables.length);
	}

	/**
	 * 
	 * Creates a running process from a passivated call.
	 * 
	 * @param programCounter
	 * @param operandStack
	 * @param environment
	 * @param byteCode
	 * @param symbols
	 */
	public Running(Call call) {
		super(0, CALL_CODE);
		this.operandsStack = new Stack();
		for (int i = call.arguments.length - 1; i >= 0; i--) {
			operandsStack.push(call.arguments[i]);
		}
		this.environment = new Object[0];
		this.symbols = new Object[] { call.functionName };
	}

	/**
	 * Pushes an operand into the operands stack.
	 * 
	 * @param value
	 *            The value to be placed on top of the stack.
	 */
	public void pushOperand(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("Cannot push null objects.");
		}
		operandsStack.push(value);
	}

	/**
	 * Removes the top element of the stack and returns it.
	 * 
	 * @return The top element of the stack that is removed.
	 */
	public Object popOperand() {
		return operandsStack.pop();
	}

	/**
	 * Returns the top element of the stack.
	 * 
	 * @return The top element of the stack.
	 */
	public Object peekOperand() {
		return operandsStack.peek();
	}

	/**
	 * Gets a view of the operands stack. The order of the array has the top of
	 * the queue on the 0-th index and the first element on the last element to
	 * the right.
	 * 
	 * @return A view of the operands-stack.
	 */
	public Object[] getOperandsStack() {
		Object[] result = new Object[operandsStack.size()];
		Enumeration elems = operandsStack.elements();
		for (int i = result.length - 1; elems.hasMoreElements(); i--) {
			result[i] = elems.nextElement();
		}
		return result;
	}	
}
