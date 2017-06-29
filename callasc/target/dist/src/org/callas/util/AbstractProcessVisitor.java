package org.callas.util;

import org.callas.absyn.AbsynPackageSchema;
import org.callas.absyn.processes.*;
import org.tyco.common.util.ParamTypeDispatcher;

/**
 * Implements the dispatching part of the visitor. You just have to call the
 * protected method <code>visit</code> and then the appropriate visitor method
 * is invoked.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Aug 18, 2009
 * 
 * @param <T>
 *            The return type of the visitor.
 */
public abstract class AbstractProcessVisitor<T> implements
		ICallasProcessVisitor<T> {
	@SuppressWarnings("unchecked")
	private static final ParamTypeDispatcher<AbstractProcessVisitor, Object> dispatcher = ParamTypeDispatcher
			.createSimpleDispatcher(AbstractProcessVisitor.class,
					"case%s", AbsynPackageSchema.PROCESSES);

	/**
	 * Calling this method invokes the respective visitor method. For example
	 * 
	 * <pre>
	 * AbstractCallasProcessVisitor&lt;String&gt; visitor = ...;
	 * String result = visitor.visit(new BInt(0));
	 * </pre>
	 * 
	 * invokes method <code>caseBInt</code> with object <code>new BInt(0)</code>
	 * as argument. The result of method <code>caseBInt</code> is the result of
	 * method <code>visit</code>.
	 * 
	 * @param proc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T visit(CallasProcess proc) {
		return (T) dispatcher.unsafeInvoke(this, proc);
	}

	public abstract T caseBBool(BBool bbool);

	public abstract T caseBDouble(BDouble bdouble);

	public abstract T caseBLong(BLong blong);

	public abstract T caseBString(BString bstring);

	public abstract T caseBinaryOperation(BinaryOperation binop);

	public abstract T caseBranch(Branch branch);

	public abstract T caseCall(Call call);

	public abstract T caseCode(Code code);

	public abstract T caseClose(Close close);

	public abstract T caseExtern(Extern external);
	
	public abstract T caseKill(Kill kill);

	public abstract T caseLet(Let let);

	public abstract T caseLoadSensorCode(LoadSensorCode load);
	
	public abstract T caseOpen(Open open);

	public abstract T caseReceive(Receive recv);

	public abstract T caseSend(Send send);

	public abstract T caseStoreSensorCode(StoreSensorCode store);

	public abstract T caseTimer(Timer timer);

	public abstract T caseUnaryOperation(UnaryOperation uop);

	public abstract T caseUpdate(Update update);

	public abstract T caseVariable(Variable var);
}
