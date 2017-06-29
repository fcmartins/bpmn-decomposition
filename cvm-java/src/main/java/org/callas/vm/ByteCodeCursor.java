package org.callas.vm;

import org.callas.vm.Opcodes;

/**
 * A cursor for navigating the bytecode, encapsulating the program counter and
 * instruction fetching.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Jun 10, 2010
 * 
 */
class ByteCodeCursor {

	private int programCounter;
	public final byte[] byteCode;

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
	public ByteCodeCursor(int programCounter, byte[] byteCode) {
		this.programCounter = programCounter;
		this.byteCode = byteCode;
	}

	/**
	 * Reads a byte and advances the program counter.
	 * 
	 * @return The read byte.
	 */
	private byte readByte() {
		byte result = byteCode[programCounter];
		programCounter++;
		return result;
	}

	/**
	 * Reads four bytes of the byte-code and advances the program counter four
	 * positions.
	 * 
	 * @return The read integer.
	 */
	private int readInt() {
		int result = readByte() << 24;
		result |= readByte() << 16;
		result |= readByte() << 8;
		result |= readByte();
		return result;
	}

	/**
	 * Increments the program counter a certain amount of values.
	 * 
	 * @param amount
	 *            The amount to increase the program counter.
	 */
	public void skip(int amount) {
		programCounter += amount;
	}

	/**
	 * Sets the new program counter.
	 * 
	 * @param programCounter
	 *            The new value of the program counter.
	 */
	public void setProgramCounter(int programCounter) {
		if (programCounter < 0 || programCounter >= byteCode.length) {
			throw new IllegalArgumentException("Expecting a non-negative number smaller than " + byteCode.length + "Given program counter " + programCounter);
		}
		this.programCounter = programCounter;
	}

	/**
	 * Returns the program counter.
	 * 
	 * @return The program counter.
	 */
	public int getProgramCounter() {
		return programCounter;
	}

	/**
	 * Advances the cursor to the next operation, calling the respective method
	 * in the visitor.
	 * 
	 * @param visitor
	 *            The object whose method this object will call according to the
	 *            next op-code found.
	 */
	public void next(IByteCodeVisitor visitor) {
		// the switch/case that actually runs the byte-code
		byte opCode = readByte();
		switch (opCode) {

		case Opcodes.LADD: {
			visitor.caseLongAddition();
			break;
		}

		case Opcodes.DADD: {
			visitor.caseDoubleAddition();
			break;
		}

		case Opcodes.LSUB: {
			visitor.caseLongSubtraction();
			break;
		}

		case Opcodes.DSUB: {
			visitor.caseDoubleSubtraction();
			break;
		}

		case Opcodes.LMUL: {
			visitor.caseLongMultiplication();
			break;
		}

		case Opcodes.DMUL: {
			visitor.caseDoubleMultiplication();
			break;
		}

		case Opcodes.LDIV: {
			visitor.caseLongDivision();
			break;
		}

		case Opcodes.DDIV: {
			visitor.caseDoubleDivision();
			break;
		}

		case Opcodes.LREM: {
			visitor.caseLongRemainder();
			break;
		}

		case Opcodes.LSHL: {
			visitor.caseLongShiftLeft();
			break;
		}

		case Opcodes.LSHR: {
			visitor.caseLongShiftRight();
			break;
		}

		case Opcodes.LNEG: {
			visitor.caseLongNegation();
			break;
		}

		case Opcodes.DNEG: {
			visitor.caseDoubleNegation();
			break;
		}

		case Opcodes.BNOT: {
			visitor.caseBooleanNot();
			break;
		}

		case Opcodes.LNOT: {
			visitor.caseLongNot();
			break;
		}

		case Opcodes.LAND: {
			visitor.caseLongAnd();
			break;
		}

		case Opcodes.BAND: {
			visitor.caseBooleanAnd();
			break;
		}

		case Opcodes.LOR: {
			visitor.caseLongOr();
			break;
		}

		case Opcodes.BOR: {
			visitor.caseBooleanOr();
			break;
		}

		case Opcodes.LXOR: {
			visitor.caseLongXor();
			break;
		}

		case Opcodes.BXOR: {
			visitor.caseBooleanXor();
			break;
		}

		case Opcodes.POP: {
			visitor.casePop();
			break;
		}

		case Opcodes.DUP: {
			visitor.caseDuplicate();
			break;
		}

		case Opcodes.SWAP: {
			visitor.caseSwap();
			break;
		}

		case Opcodes.LDC: {
			visitor.caseLoadConstant(readByte());
			break;
		}

		case Opcodes.LD: {
			visitor.caseLoad(readByte());
			break;
		}

		case Opcodes.LDM: {
			visitor.caseLoadModule();
			break;
		}

		case Opcodes.ST: {
			visitor.caseStore(readByte());
			break;
		}

		case Opcodes.LE: {
			visitor.caseLongEquals();
			break;
		}

		case Opcodes.LLT: {
			visitor.caseLongLessThan();
			break;
		}

		case Opcodes.LGT: {
			visitor.caseLongGreaterThan();
			break;
		}

		case Opcodes.DE: {
			visitor.caseDoubleEquals();
			break;
		}

		case Opcodes.DLT: {
			visitor.caseDoubleLessThan();
			break;
		}

		case Opcodes.DGT: {
			visitor.caseDoubleGreaterThan();
			break;
		}

		case Opcodes.IFT: {
			visitor.caseIfTrue(readInt());
			break;
		}

		case Opcodes.GOTO: {
			visitor.caseGoTo(readInt());
			break;
		}

		case Opcodes.UPD: {
			visitor.caseUpdate();
			break;
		}

		case Opcodes.SND: {
			visitor.caseSend();
			break;
		}

		case Opcodes.RCV: {
			visitor.caseReceive();
			break;
		}

		case Opcodes.CALL: {
			visitor.caseCall();
			break;
		}

		case Opcodes.SYS: {
			visitor.caseSystemCall();
			break;
		}

		case Opcodes.TMR: {
			visitor.caseTimedCall();
			break;
		}

		case Opcodes.LDB: {
			visitor.caseLoadSensorCode();
			break;
		}

		case Opcodes.STB: {
			visitor.caseStoreSensorCode();
			break;
		}

		case Opcodes.RET: {
			visitor.caseReturn();
			break;
		}
		
		case Opcodes.OPEN: {
			visitor.caseOpen();
			break;
		}
		
		case Opcodes.CLS: {
			visitor.caseClose();
			break;
		}
		
		case Opcodes.KILL: {
			visitor.caseKill();
			break;
		}

		default:
			// nothing to do
			throw new IllegalArgumentException("Unknown bytecode: " + opCode);
		}
	}
	
	public boolean hasNext() {
		return programCounter < byteCode.length;
	}

}
