package org.tyco.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Dispatches a method call by the type of one of the parameters.
 * 
 * @author Tiago Cogumbreiro
 * 
 * @param <T>
 *            the type of the target class (where the methods will be invoked)
 * @param <R>
 *            the type of the return value of the method.
 */
public class ParamTypeDispatcher<T, R> {

	private Class<T> targetCls;

	private Map<Class<?>, Method> methods = new HashMap<Class<?>, Method>();

	private Class<?>[] paramTypesTemplate;

	private int paramIndex;

	private boolean useDynamicDispatch = true;

	/**
	 * Creates a new dispatcher that works on a target class and the signature
	 * of the method being dispatched has a target signature (with a place
	 * holder being specified by null).
	 * 
	 * @param targetCls
	 *            the type of the object where the methods will be invoked.
	 * @param paramTypes
	 *            one of the paramTypes must be null and represents a
	 *            placeholder for the parametrized type.
	 */
	public ParamTypeDispatcher(Class<T> targetCls, Class<?>... paramTypes) {
		this.targetCls = targetCls;
		this.paramTypesTemplate = paramTypes;
		List<Integer> indexes = new ArrayList<Integer>(paramTypes.length);
		int index = 0;
		for (Class<?> param : paramTypes) {
			if (param == null) {
				indexes.add(index);
			}
			index++;
		}
		// if the signature is not specified it is considered that there is only
		// one parameter which is the one being dispatched.
		if (paramTypes.length == 0) {
			indexes.add(0);
			paramTypesTemplate = new Class<?>[] { null };
		}
		if (indexes.size() != 1) {
			throw new IllegalArgumentException(
					"Can only specify one null argument. Specified indexes: "
							+ indexes);
		}
		this.paramIndex = indexes.get(0).intValue();
	}

	/**
	 * <p>
	 * Registers a class to a certain method name. Notice that the
	 * <code>methodName</code> is a formatted string, when using the
	 * <code>%s</code> will be substituted by the class' simple name.
	 * </p>
	 * 
	 * <p>
	 * Consider we are dispatching a value of type {@link String}. If we want
	 * the method call to be dispatched to method
	 * <code>caseString(String)</code>, then the method name may be
	 * <code>case%s</code>, instead of <code>caseString</code>.
	 * </p>
	 * 
	 * @param cls
	 *            the class that represents the concrete parameter type.
	 * @param methodName
	 *            the name of the method to be called.
	 * @return This object.
	 * @throws SecurityException
	 *             raised by <code>Class.getMethod</code>
	 * @throws NoSuchMethodException
	 *             raised by <code>Class.getMethod</code>
	 * @see Class
	 * 
	 * @return <code>this</code>
	 */
	public ParamTypeDispatcher<T, R> register(Class<?> cls, String methodName)
			throws SecurityException, NoSuchMethodException {
		Class<?>[] paramTypes = paramTypesTemplate.clone();
		paramTypes[paramIndex] = cls;
		methods.put(cls, targetCls.getMethod(String.format(methodName, cls
				.getSimpleName()), paramTypes));
		return this;
	}

	/**
	 * Dispatches the method call to the appropriate specialized method.
	 * 
	 * @param target
	 *            The object that holds the method we are about to invoke.
	 * @param params
	 *            The parameters that are passed to the method call.
	 * @return The result of the invoked method.
	 * @throws IllegalArgumentException
	 *             The argument types or a different number of them.
	 * @throws IllegalAccessException
	 *             The method must be public in a public class.
	 * @throws InvocationTargetException
	 *             The method we are invoking raised an exception.
	 */
	@SuppressWarnings("unchecked")
	public R invoke(T target, Object... params)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		assert params.length == paramTypesTemplate.length;
		Object argument = params[paramIndex];
		if (argument == null) {
			throw new IllegalArgumentException("Argument cannot be null.");
		}
		Method method = methods.get(argument.getClass());
		if (method == null && useDynamicDispatch) {
			for (Class<?> cls : ClassMapper.getMRO(argument.getClass())) {
				method = methods.get(cls);
				if (method != null) {
					break;
				}
			}
		}
		if (method == null) {
			throw new IllegalArgumentException("No method for class "
					+ argument.getClass().getName());
		}
		return (R) method.invoke(target, params);
	}

	/**
	 * See invoke().
	 * 
	 * @param target
	 *            The object that holds the method we are about to invoke.
	 * @param params
	 *            The parameters that are passed to the method call.
	 * @return The result of the invoked method.
	 * @throws RuntimeException
	 *             wraps the checked exception into an unchecked one.
	 */
	public R unsafeInvoke(T target, Object... params) {
		try {
			return invoke(target, params);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException invExcp) {
			Throwable e = invExcp.getTargetException();
			if (e instanceof Error) {
				throw (Error) e;
			} else if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new IllegalArgumentException(e);
			}
		}
	}

	/**
	 * Registers every pair it can. Aggregating the errors in the end.
	 * 
	 * @param pairs
	 *            pairs to be registered
	 * @throws AggregateException
	 *             throws if any exception is throw while registring a pair
	 */
	public void registerMany(Pair<Class<?>, String>... pairs)
			throws AggregateException {
		List<Exception> exceptions = new LinkedList<Exception>();
		for (Pair<Class<?>, String> pair : pairs) {
			try {
				register(pair.getLeft(), pair.getRight());
			} catch (Exception e) {
				exceptions.add(e);
			}
		}
		if (exceptions.size() > 0) {
			throw new AggregateException(exceptions);
		}
	}

	/**
	 * Registers every pair it can. Errors are printed to the output.
	 * 
	 * @param pairs
	 *            pairs to be registered
	 * @return returns itself
	 */
	public ParamTypeDispatcher<T, R> registerManySilently(
			Pair<Class<?>, String>... pairs) {
		try {
			registerMany(pairs);
		} catch (AggregateException e) {
			List<? extends Exception> exceptions = e.getExceptions();
			for (Exception exception : exceptions) {
				exception.printStackTrace();
			}
		}
		return this;
	}

	/**
	 * Utility function for registering a number of classes to the same method
	 * name.
	 * 
	 * @param methodName
	 * @param classes
	 * @throws AggregateException
	 */
	@SuppressWarnings("unchecked")
	public void registerMany(String methodName, Class<?>... classes)
			throws AggregateException {
		Pair<Class<?>, String>[] pairs = new Pair[classes.length];
		int i = 0;
		for (Class<?> cls : classes) {
			pairs[i] = new Pair<Class<?>, String>(cls, methodName);
			i++;
		}
		registerMany(pairs);
	}

	/**
	 * Utility function for registering a number of classes to the same method
	 * name.
	 * 
	 * @param methodName
	 * @param classes
	 * @return itself
	 */
	public ParamTypeDispatcher<T, R> registerManySilently(String methodName,
			Class<?>... classes) {
		try {
			registerMany(methodName, classes);
		} catch (AggregateException e) {
			List<? extends Exception> exceptions = e.getExceptions();
			for (Exception exception : exceptions) {
				exception.printStackTrace();
			}
		}
		return this;
	}

	/**
	 * Creates the parameter dispatcher and registers many classes silently.
	 * 
	 * @param <T>
	 *            the type of the dispatching class.
	 * @param <R>
	 *            the type of the value returned by the dispatched method.
	 * @param targetCls
	 *            the dispatching class
	 * @param returnCls
	 *            the type of the returned value.
	 * @param methodName
	 *            the method name, which may contain a <code>%s</code> wildcard,
	 *            see <code>ParamTypeDispatcher.register</code>.
	 * @param classes
	 *            the classes to be registered, see
	 *            <code>ParamTypeDispatcher.registerMany</code>.
	 * @return the parameter type dispatcher.
	 */
	public static <T, R> ParamTypeDispatcher<T, R> createSimpleDispatcher(
			Class<T> targetCls, Class<R> returnCls, String methodName,
			Class<?>... classes) {
		return new ParamTypeDispatcher<T, R>(targetCls).registerManySilently(
				methodName, classes);
	}

	/**
	 * Creates the parameter dispatcher and registers many classes silently.
	 * 
	 * @param <T>
	 *            the type of the dispatching class.
	 * @param <R>
	 *            the type of the value returned by the dispatched method.
	 * @param targetCls
	 *            the dispatching class
	 * @param methodName
	 *            the method name, which may contain a <code>%s</code> wildcard,
	 *            see <code>ParamTypeDispatcher.register</code>.
	 * @param classes
	 *            the classes to be registered, see
	 *            <code>ParamTypeDispatcher.registerMany</code>.
	 * @return the parameter type dispatcher.
	 */
	public static <T> ParamTypeDispatcher<T, Object> createSimpleDispatcher(
			Class<T> targetCls, String methodName, Class<?>... classes) {
		return new ParamTypeDispatcher<T, Object>(targetCls)
				.registerManySilently(methodName, classes);
	}
}
