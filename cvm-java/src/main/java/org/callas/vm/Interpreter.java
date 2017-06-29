package org.callas.vm;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import java.util.Hashtable;

import org.callas.vm.BoundedTimedTask;
import org.callas.vm.Call;
import org.callas.vm.INetworkOutputInterface;

/**
 * Represents the CVM, interprets the bytecode by visiting the program in
 * execution.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @author Pedro Gomes (prg@dcc.fc.up.pt)
 * @author Rui Mendes (rui.mendes@dcc.fc.up.pt)
 * @date Apr 06, 2012
 * 
 */
public class Interpreter implements IByteCodeVisitor {
	/**
	 * The program to execute.
	 */
	private final ModuleDeclaration program;
	/**
	 * The native operations of the interpreter.
	 */
	private final INativeOperations nativeOps;
	/**
	 * The interface to manage the communication channels
	 */
	public final IConnectionManager connManager;
	/**
	 * Where {@link Call}s are read from when calling a <code>receive</code>.
	 */
	private final Vector inQueue = new Vector();  //TO REMOVE (NOT USED NOW)
	/**
	 * The run-queue of the interpreter (fragments that will be executed).
	 */
	private final Vector runQueue = new Vector();
	/**
	 * The call stack of the current {@link Runnable} being executed (the head
	 * of the run-queue).
	 */
	private final Stack callStack = new Stack();
	/**
	 * The memory of the interpreter.
	 */
	private Module installed = null;
	/**
	 * Where the active timers are stored
	 */
	public final Hashtable timers = new Hashtable();
	/**
	 * Clock to obtain the current time of the system
	 */
	public final IClock clock = IClock.SYSTEM;

	/**
	 * The interpreter will execute the <code>program</code>. The machine is
	 * also parameterized by the native operations and by the scheduler of timed
	 * calls.
	 * 
	 * @param program
	 *            The program to execute.
	 * @param nativeOps
	 *            The native operations accessed by the program.
	 * @param timer
	 *            The scheduler of timed calls.
	 */
	public Interpreter(ModuleDeclaration program, INativeOperations nativeOps,
			IConnectionManager connManager) {
		this.program = program;
		this.nativeOps = nativeOps;
		this.connManager = connManager;
		//connManager.setDataQueue(inQueue);
		// hardware
		nativeOps.init();
		// get the main function
		Module firstModule = program.create();
		installed = firstModule;
		Function firstFunction = firstModule.lookup("init");
		setExecuting(new Running(firstFunction, new Object[] { firstModule }));
	}
	
	/**
	 * Returns a view of the current input messages to be processed.
	 * 
	 * @return A view of the current input messages to be processed
	 */
	public Call[] getInputMessages() {
		return toCallArray(inQueue.elements(), inQueue.size());
	}
	
	/**
	 * Places a message in the set of input messages.
	 * 
	 * @param message
	 *            The incoming message to be placed in the run-queue.
	 */
	public void addInputMessage(Call message) {
		inQueue.addElement(message);
	}
	
	/**
	 * Returns a view of the current timers.
	 * 
	 * @return An view of the current timers.
	 */
	public BoundedTimedTask[] getTimedCalls() {
		Enumeration elems = timers.elements();
		BoundedTimedTask[] btt = new BoundedTimedTask[timers.size()];
		for (int i = 0; elems.hasMoreElements(); i++) {
			btt[i] = (BoundedTimedTask) elems.nextElement();
		}
		return btt;
	}

	/**
	 * Check if the virtual machine is running.
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return isExecuting() || runQueue.size() > 0 || !timers.isEmpty();
	}

	/**
	 * Return the current frame being executed.
	 * 
	 * @return
	 */
	public Running getExecuting() {
		return (Running) callStack.peek();
	}

	/**
	 * Clears the call stack and places a new object to be executed.
	 * 
	 * @param run
	 *            The new object to execute.
	 */
	public void setExecuting(Running run) {
		callStack.removeAllElements();
		callStack.push(run);
	}

	/**
	 * Empties the call-stack, thereby clearing the object being executed.
	 */
	public void clearExecuting() {
		callStack.removeAllElements();
	}

	/**
	 * Pushes a new object to be executed into the call-stack.
	 * 
	 * @param run
	 *            The new object to be executed.
	 */
	public void pushExecuting(Running run) {
		callStack.push(run);
	}

	/**
	 * Pops the current object being executed.
	 */
	public void popExecuting() {
		callStack.pop();
	}

	/**
	 * Converts an enumeration that only holds {@link Call}s into an array of a
	 * given size.
	 * 
	 * @param elems
	 *            The {@link Enumeration} of {@link Call}s.
	 * @param size
	 *            The number of elements to be copied from the
	 *            {@link Enumeration}.
	 * @return An array holding the {@link Call}s.
	 */
	private static Call[] toCallArray(Enumeration elems, int size) {
		Call[] calls = new Call[size];
		for (int i = 0; elems.hasMoreElements(); i++) {
			calls[i] = (Call) elems.nextElement();
		}
		return calls;
	}

	/**
	 * Returns a view of the run-queue, ordered by FIFO.
	 * 
	 * @return A view of the run-queue, ordered by FIFO.
	 */
	public Call[] getRunQueue() {
		return toCallArray(runQueue.elements(), runQueue.size());
	}

	/**
	 * Adds a pending object to the end of the run-queue.
	 * 
	 * @param pending
	 *            The pending object to be added.
	 */
	public void addRunQueue(Call pending) {
		runQueue.addElement(pending);
	}

	/**
	 * Returns a view of the stack of elements being executed. The fist object
	 * of the enumeration is the last one to be executed (i.e. we get a reverse
	 * view of the execution order).
	 * 
	 * @return A view of the call-stack.
	 */
	public Running[] getCallStack() {
		Running[] result = new Running[callStack.size()];
		Enumeration elems = callStack.elements();
		for (int i = 0; elems.hasMoreElements(); i++) {
			result[i] = (Running) elems.nextElement();
		}
		return result;
	}

	/**
	 * Returns the program being run by the emulator.
	 * 
	 * @return
	 */
	public ModuleDeclaration getProgram() {
		return program;
	}

	/**
	 * Tests if there is a function running in the VM.
	 * 
	 * @return If there is a function running in the VM.
	 */
	protected boolean isExecuting() {
		return callStack.size() > 0;
	}

	/**
	 * Processes the object being executed and then handles pending calls (time-
	 * and network-related).
	 * 
	 * @param currTime
	 *            The current time, useful for timed-calls.
	 */
	public void runBigStep() {
		while (isExecuting()) {
			processByteCode();
		}
		handlePendingCalls();
	}

	/**
	 * If there is a an object to be executed, then we the an operation step on
	 * it.
	 * 
	 * @param currTime
	 *            The current time, useful for timed-calls.
	 */
	public void runSmallStep() {
		if (isExecuting()) {
			processByteCode();
		}
		handlePendingCalls();
	}

	/**
	 * Handles timed calls and the run queue
	 */
	private void handlePendingCalls() {
		if (!callStack.isEmpty()) {
			return;
		}

		// try the run-queue
		if (runQueue.size() == 0){ 
			if (!timers.isEmpty()) {
				synchronized(this){
					while (runQueue.size() == 0){
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}else
				return;
		}			
		Call pending = (Call) runQueue.elementAt(0);
		runQueue.removeElementAt(0);
		setExecuting(new Running(pending));
	}

	/**
	 * A small-step execution of a currently executing object.
	 * 
	 * @param currTime
	 *            The current time, useful for timed-calls.
	 */
	private void processByteCode() {
		getExecuting().next(this);
	}

	/**
	 * Pops the parameters of a function-call object.
	 * 
	 * @param functionName
	 * @return
	 */
	private Object[] popInstalledFunctionParameters(String functionName) {
		return popArray(installed.lookup(functionName).declaration.parametersCount - 1);
	}

	/**
	 * Pops an object from the operand stack.
	 * 
	 * @return The poped value.
	 */
	private Object popObject() {
		return getExecuting().popOperand();
	}

	/**
	 * Pops a module from the operand stack.
	 * 
	 * @return The popped value.
	 */
	private Module popModule() {
		return (Module) popObject();
	}

	/**
	 * Pops a module declaration from the operand stack.
	 * 
	 * @return The popped value.
	 */
	private ModuleDeclaration popModuleDeclaration() {
		return (ModuleDeclaration) popObject();
	}

	/**
	 * Peeks the module on top of the operand stack.
	 * 
	 * @return The poped value.
	 */
	private Module peekModule() {
		return (Module) getExecuting().peekOperand();
	}

	/**
	 * Pops a string from the operand stack.
	 * 
	 * @return The poped value.
	 */
	private String popString() {
		return (String) popObject();
	}

	/**
	 * Pops a boolean from the operand stack.
	 * 
	 * @return The poped value.
	 */
	private boolean popBoolean() {
		return ((Boolean) popObject()).booleanValue();
	}

	/**
	 * Pops a double from the operand stack.
	 * 
	 * @return The poped value.
	 */
	private double popDouble() {
		return ((Double) popObject()).doubleValue();
	}

	/**
	 * Pops a long from the operand stack.
	 * 
	 * @return The poped value.
	 */
	private long popLong() {
		return ((Long) popObject()).longValue();
	}

	/**
	 * Pops values from the stack and places them into an array.
	 * 
	 * @param size
	 * @return
	 */
	private Object[] popArray(int size) {
		Object[] data = new Object[size];
		for (int i = 0; i < size; i++) {
			data[i] = popObject();
		}
		return data;
	}

	/**
	 * Returns the installed module.
	 * 
	 * @return The installed module.
	 */
	public Module getInstalled() {
		return installed;
	}

	/**
	 * Sets the module installed in the VM.
	 * 
	 * @param installed
	 *            The module to be installed (replacing the previous one).
	 */
	public void setInstalled(Module installed) {
		this.installed = installed;
	}

	public void caseCall() {
		String functionName = popString();
		Module module = peekModule();
		Function function = module.lookup(functionName);
		Object[] params = popArray(function.declaration.parametersCount);
		Running newRunning = new Running(function, params);
		pushExecuting(newRunning);
	}
	
	public void caseClose() {  //need adjustments for inQueue
		String channel = popString();
		try{
			connManager.close(channel);
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public void caseDoubleEquals() {
		double value1 = popDouble();
		double value2 = popDouble();
		getExecuting().pushOperand(new Boolean(value1 == value2));
	}

	public void caseDoubleGreaterThan() {
		double value1 = popDouble();
		double value2 = popDouble();
		getExecuting().pushOperand(new Boolean(value1 > value2));
	}

	public void caseDoubleLessThan() {
		double value1 = popDouble();
		double value2 = popDouble();
		getExecuting().pushOperand(new Boolean(value1 < value2));
	}

	public void caseDuplicate() {
		Object operand = popObject();
		getExecuting().pushOperand(operand);
		getExecuting().pushOperand(operand);
	}

	public void caseGoTo(int offset) {
		getExecuting().skip(offset);
	}

	public void caseIfTrue(int offset) {
		boolean value = popBoolean();
		if (value) {
			getExecuting().skip(offset);
		}
	}

	public void caseLoad(byte index) {
		Object value = getExecuting().environment[index];
		if (value == null) {
			throw new IllegalArgumentException(
					"Loading an unitialized value at index " + index + ". ");
		}
		getExecuting().pushOperand(value);
	}

	public void caseLoadConstant(byte index) {
		getExecuting().pushOperand(getExecuting().symbols[index]);
	}

	public void caseLoadModule() {
		ModuleDeclaration modDec = popModuleDeclaration();

		Function[] funcs = new Function[modDec.size()];
		for (int i = 0; i < modDec.size(); i++) {
			String funcName = popString();
			FunctionDeclaration declr = modDec.lookup(funcName);
			byte freeVariablesCount = modDec.getFreeVarsCount(funcName);
			Object[] freeVars = new Object[freeVariablesCount];

			for (int varIndex = 0; varIndex < freeVariablesCount; varIndex++) {
				freeVars[varIndex] = popObject();
			}
			funcs[i] = new Function(declr, freeVars);
		}
		// update the new function
		getExecuting().pushOperand(new Module(funcs));
	}

	public void caseLoadSensorCode() {
		getExecuting().pushOperand(installed);
	}

	public void caseLongEquals() {
		long value1 = popLong();
		long value2 = popLong();
		getExecuting().pushOperand(new Boolean(value1 == value2));
	}

	public void caseLongGreaterThan() {
		long value1 = popLong();
		long value2 = popLong();
		getExecuting().pushOperand(new Boolean(value1 > value2));
	}

	public void caseLongLessThan() {
		long value1 = popLong();
		long value2 = popLong();
		getExecuting().pushOperand(new Boolean(value1 < value2));
	}
	
	public void caseKill() {
		String timerID = popString();
		BoundedTimedTask btt = (BoundedTimedTask) timers.get(timerID);
		btt.cancel();
		timers.remove(timerID);
	}
	
	public void caseOpen() { 
		String channel = popString();
		connManager.open(channel);
	}

	public void caseReceive() {
		String channel = popString();
		INetworkInputInterface inFace =(INetworkInputInterface) connManager.lookupInput(channel);
		Call call = inFace.popCall();
		if (call != null){
			call.addModule(installed);
			addRunQueue(call);
		}
	}

	public void caseReturn() {
		// get return value
		Object returnValue = popObject();
		popExecuting();
		if (isExecuting()) {
			getExecuting().pushOperand(returnValue);
		}
	}

	public void caseSend(){
		String channel = popString();
		INetworkOutputInterface outFace =(INetworkOutputInterface) connManager.lookupOutput(channel);
		String functionName = popString();
		Object[] args = popInstalledFunctionParameters(functionName);
		try {
			outFace.send(new Call(functionName, args));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void caseStore(byte index) {
		getExecuting().environment[index] = popObject();
	}

	public void caseStoreSensorCode() {
		installed = popModule();
	}

	public void caseSwap() {
		Object operand1 = popObject();
		Object operand2 = popObject();
		getExecuting().pushOperand(operand1);
		getExecuting().pushOperand(operand2);
	}

	public void caseSystemCall() {
		String systemCall = popString();
		byte paramsCount = nativeOps.getParameterCount(systemCall);
		Object[] params = popArray(paramsCount);
		Object value = nativeOps.execute(systemCall, params);
		getExecuting().pushOperand(value);
	}

	public void caseTimedCall() {
		String functionName = popString();
		Module module = peekModule();
		Function function = module.lookup(functionName);
		Object[] args = popArray(function.declaration.parametersCount);
		long period = popLong();
		BoundedTimedTask btt = new BoundedTimedTask(new Call(functionName,args), period, this);
		String value = functionName + clock.currentTimeMillis();
		timers.put(value, btt);
		btt.schedule();
		getExecuting().pushOperand(value);
	}

	public void caseUpdate() {
		Module mod1 = popModule();
		Module mod2 = popModule();

		getExecuting().pushOperand(mod1.update(mod2));
	}

	public void caseBooleanAnd() {
		boolean operand1 = popBoolean();
		boolean operand2 = popBoolean();
		getExecuting().pushOperand(new Boolean(operand1 && operand2));
	}

	public void caseBooleanNot() {
		boolean operand = popBoolean();
		getExecuting().pushOperand(new Boolean(!operand));
	}

	public void caseBooleanOr() {
		boolean operand1 = popBoolean();
		boolean operand2 = popBoolean();
		getExecuting().pushOperand(new Boolean(operand1 || operand2));
	}

	public void caseBooleanXor() {
		boolean operand1 = popBoolean();
		boolean operand2 = popBoolean();
		getExecuting().pushOperand(new Boolean(operand1 ^ operand2));
	}

	public void caseDoubleAddition() {
		double operand1 = popDouble();
		double operand2 = popDouble();
		getExecuting().pushOperand(new Double(operand1 + operand2));
	}

	public void caseDoubleDivision() {
		double operand1 = popDouble();
		double operand2 = popDouble();
		getExecuting().pushOperand(new Double(operand1 / operand2));
	}

	public void caseDoubleMultiplication() {
		double operand1 = popDouble();
		double operand2 = popDouble();
		getExecuting().pushOperand(new Double(operand1 * operand2));
	}

	public void caseDoubleNegation() {
		double operand = popDouble();
		getExecuting().pushOperand(new Double(-operand));
	}

	public void caseDoubleSubtraction() {
		double operand1 = popDouble();
		double operand2 = popDouble();
		getExecuting().pushOperand(new Double(operand1 - operand2));
	}

	public void caseLongAddition() {
		long operand1 = popLong();
		long operand2 = popLong();
		getExecuting().pushOperand(new Long(operand1 + operand2));
	}

	public void caseLongAnd() {
		long operand1 = popLong();
		long operand2 = popLong();
		getExecuting().pushOperand(new Long(operand1 & operand2));
	}

	public void caseLongDivision() {
		long operand1 = popLong();
		long operand2 = popLong();
		getExecuting().pushOperand(new Long(operand1 / operand2));
	}

	public void caseLongMultiplication() {
		long operand1 = popLong();
		long operand2 = popLong();
		getExecuting().pushOperand(new Long(operand1 * operand2));
	}

	public void caseLongNegation() {
		long operand = popLong();
		getExecuting().pushOperand(new Long(-operand));
	}

	public void caseLongNot() {
		long operand = popLong();
		getExecuting().pushOperand(new Long(~operand));
	}

	public void caseLongOr() {
		long operand1 = popLong();
		long operand2 = popLong();
		getExecuting().pushOperand(new Long(operand1 | operand2));
	}

	public void caseLongRemainder() {
		long operand1 = popLong();
		long operand2 = popLong();
		getExecuting().pushOperand(new Long(operand1 % operand2));
	}

	public void caseLongShiftLeft() {
		long operand1 = popLong();
		long operand2 = popLong();
		getExecuting().pushOperand(new Long(operand1 << operand2));
	}

	public void caseLongShiftRight() {
		long operand1 = popLong();
		long operand2 = popLong();
		getExecuting().pushOperand(new Long(operand1 >> operand2));
	}

	public void caseLongSubtraction() {
		long operand1 = popLong();
		long operand2 = popLong();
		getExecuting().pushOperand(new Long(operand1 - operand2));
	}

	public void caseLongXor() {
		long operand1 = popLong();
		long operand2 = popLong();
		getExecuting().pushOperand(new Long(operand1 ^ operand2));
	}

	public void casePop() {
		popObject();
	}
}
