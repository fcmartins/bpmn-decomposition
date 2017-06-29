package org.callas.vm;

public interface IByteCodeVisitor {

	void caseReturn();

	void caseStoreSensorCode();

	void caseLoadSensorCode();

	void caseTimedCall();

	void caseSystemCall();

	void caseCall();
	
	void caseOpen();
	
	void caseClose();
	
	void caseKill();

	void caseReceive();

	void caseSend();

	void caseUpdate();

	void caseGoTo(int offset);

	void caseIfTrue(int offset);

	void caseDoubleGreaterThan();

	void caseDoubleLessThan();

	void caseDoubleEquals();

	void caseLongGreaterThan();

	void caseLongLessThan();

	void caseLongEquals();

	void caseStore(byte index);

	void caseLoadModule();

	void caseLoad(byte index);

	void caseLoadConstant(byte index);

	void caseSwap();

	void caseDuplicate();

	void casePop();

	void caseBooleanXor();

	void caseLongXor();

	void caseBooleanOr();

	void caseLongOr();

	void caseBooleanAnd();

	void caseLongAnd();

	void caseLongNot();

	void caseBooleanNot();

	void caseDoubleNegation();

	void caseLongNegation();

	void caseLongShiftRight();

	void caseLongShiftLeft();

	void caseLongRemainder();

	void caseDoubleDivision();

	void caseLongDivision();

	void caseDoubleMultiplication();

	void caseLongMultiplication();

	void caseDoubleSubtraction();

	void caseLongSubtraction();

	void caseDoubleAddition();

	void caseLongAddition();
}
