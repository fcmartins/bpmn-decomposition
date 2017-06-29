package org.callas.util;

import org.callas.absyn.AbsynPackageSchema;
import org.callas.absyn.types.*;
import org.tyco.common.util.ParamTypeDispatcher;

/**
 * Implements the dispatching algorithm for creating CallasType-based visitor.
 * @author Tiago Cogumbreiro
 *
 * @param <T> the type of the value returned by each visit.
 */
public abstract class AbstractTypeVisitor<T> implements ICallasTypeVisitor<T> {
	/**
	 * The dispatcher.
	 */
	@SuppressWarnings({"unchecked" })
	private static final ParamTypeDispatcher<AbstractTypeVisitor, Object> dispatcher = new ParamTypeDispatcher<AbstractTypeVisitor, Object>(
			AbstractTypeVisitor.class).registerManySilently(
			"case%s", AbsynPackageSchema.TYPES);


	@SuppressWarnings("unchecked")
	protected T visit(CallasType type) {
		return (T) dispatcher.unsafeInvoke(this, type);
	}
	
	public abstract T caseBBoolType(BBoolType bbool);

	public abstract T caseBDoubleType(BDoubleType bfloat);

	public abstract T caseBLongType(BLongType bint);

	public abstract T caseBStringType(BStringType bstring);

	public abstract T caseCodeType(CodeType sensorCodeType);

	public abstract T caseFunctionType(FunctionType functionType);

	public abstract T caseRecursiveType(RecursiveType recursiveType);

	public abstract T caseTypeVariable(TypeVariable typeVariable);
}
