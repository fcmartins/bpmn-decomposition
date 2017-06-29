/**
 * 
 */
package org.callas.error;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.callas.absyn.Absyn;
import org.callas.absyn.AbsynPackageSchema;
import org.callas.absyn.types.*;
import org.tyco.common.errorMsg.ErrorMessage;
import org.tyco.common.errorMsg.IPrinter;
import org.tyco.common.util.CheckedClassCastException;

/**
 * The class of the type is not expected.
 *
 */
public class ClassOfTypeMismatch extends ErrorMessage {

	private static final Map<Class<? extends CallasType>, String> clsNames = new HashMap<Class<? extends CallasType>, String>();
	
	static {
		clsNames.put(CodeType.class, "a module");
		clsNames.put(BLongType.class, "an integer");
		clsNames.put(BDoubleType.class, "a float");
		clsNames.put(BStringType.class, "a string");
		clsNames.put(BBoolType.class, "a boolean");
		clsNames.put(RecursiveType.class, "a recursive value");
		clsNames.put(TypeVariable.class, "a type variable");
		clsNames.put(FunctionType.class, "a function");
		assert clsNames.keySet().equals(new HashSet<Class<?>>(Arrays.asList(AbsynPackageSchema.TYPES))) : "Some types do not have a name associated to " + ClassOfTypeMismatch.class;
	}
	
	/**
	 * The found type.
	 */
	public final CallasType found;
	
	/**
	 * The class of the expected type.
	 */
	public final Class<? extends CallasType> expected;

	/**
	 * @param pos
	 * @param what
	 *
	@SuppressWarnings("unchecked")
	public TypesMismatch(int pos, Object what, UnexpectedTypeException exception) {
		super(pos, what);
		this.found = (Type) exception.getFound();
		this.expected = (Class<? extends Type>) exception.getExpected();
	}*/

	@SuppressWarnings("unchecked")
	public ClassOfTypeMismatch(Absyn element, CheckedClassCastException exception) {
		this(element, (Class<? extends CallasType>) exception.getExpected(), (CallasType) exception.getFound());
	}

	public ClassOfTypeMismatch(Absyn element, Class<? extends CallasType> expected, CallasType found) {
		super(element.getSourceLocation(), element);
		this.expected = expected;
		this.found = found;
	}

	/* (non-Javadoc)
	 * @see org.tyco.common.errorMsg.ErrorMessage#printDetails(org.tyco.common.errorMsg.Printer)
	 */
	@Override
	public void printDetails(IPrinter<Object> out) {
		out.print("This type:\n");
		out.print(found);
		out.print("\nis not " + clsNames.get(expected) + " type.");
		out.print("\n");
	}

	/* (non-Javadoc)
	 * @see org.tyco.common.errorMsg.ErrorMessage#toString()
	 */
	@Override
	public String toString() {
		return "Type mismatch.";
	}

}
