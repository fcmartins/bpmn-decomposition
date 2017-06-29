package org.tyco.common.util;


/**
 * Resolves and casts types.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: Classes.java,v 1.1 2008/10/07 00:42:12 tyco Exp $
 * @param <T>
 */
public final class Classes {
	/**
	 * The type is resolved and cast to the type you provide. If the there is
	 * a type mismatch UnexpectedTypeException is thrown.
	 * 
	 * @param obj the type you want to cast
	 * @param cls the class of the type you expect
	 * @return the same object casted to <code>target</code>
	 */
    public static <T, S extends T> S cast(T obj, Class<S> cls) throws CheckedClassCastException {
    	assert cls != null;
		if (cls.isInstance(obj)) {
			return cls.cast(obj);
    	}
		throw new CheckedClassCastException(cls, obj);
    }
}
