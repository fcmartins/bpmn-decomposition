package org.tyco.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class mapper holds a map and associates a key (a class) with an object 
 * (the target). When you call the method <code>get</code> it will return
 * the object associated with the type of that object
 * (using <code>getMRO</code>).
 *  
 * @author Tiago Cogumbreiro
 * @version $Id: ClassMapper.java,v 1.3 2009/01/07 15:58:56 tyco Exp $
 * @param <T> the type of the returned objects.
 */
public class ClassMapper<T> {
	
	protected Map<Class<?>, T> mappers = new HashMap<Class<?>, T>();
	
	/**
	 * Returns a list of the method resolution order of a certain class. It
	 * includes the given class in the method resolution order.
	 * 
	 * @param cls
	 * The target of the method resolution order.
	 * 
	 * @return
	 * A list of class (and interfaces) according to the method resolution
	 * order.
	 */
	public static List<Class<?>> getMRO(Class<?> cls) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		while (cls != Object.class) {
			result.add(cls);
			for (Class<?> iface: cls.getInterfaces()) {
				result.add(iface);
			}
			cls = cls.getSuperclass();
		}
		return result;
	}
	
	/**
	 * Returns the object associated with the type of the <code>key</code>.
	 * @param key 
	 * @return The object associated with the key, or none if it is not found.
	 */
	public T get(Object key) {
		T result;
		
		// spider through super classes to find an appropriate printer.
		for (Class<?> cls: getMRO(key.getClass())) {
			result = mappers.get(cls);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	/**
	 * Registers the association of a type to a <code>target</code> object.
	 * @param key
	 * The type of the key.
	 * @param target
	 * The object associated with the type.
	 */
	public void register(Class<?> key, T target) {
		mappers.put(key, target);
	}
	
	/**
	 * Gets the value associated with a given class.
	 * @param cls
	 * @return
	 */
	public T getRegistred(Class<?> cls) {
		return mappers.get(cls);
	}
}
