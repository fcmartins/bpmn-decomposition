package org.tyco.common.symtable;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * This implementation uses a back reference to the parent scope.
 * 
 * The children are not kept in reference. 
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: BaseScope.java,v 1.10 2010/06/01 17:20:02 cogumbreiro Exp $
 * @param <K>
 * @param <V>
 */
public class BaseScope<K, V> implements Scope<K, V> {

	private Scope<K, V> parent;
	protected MapFactory<K, V> mapFactory;
	private int depth;
	private Map<K, V> map;

	/**
	 * Constructs a new linked scope.
	 * @param mapFactory
	 * @param parent
	 */
	public BaseScope(MapFactory<K, V> mapFactory, Scope<K, V> parent) {
		assert mapFactory != null;
		this.mapFactory = mapFactory;
		setParent(parent);
		this.map = mapFactory.createMap();
		assert map != null;
	}
	
	/**
	 * Creates a scope child of another.
	 * @param parent if null, then it has none.
	 */
	public BaseScope(Scope<K, V> parent) {
		this(new LinkedHashMapFactory<K, V>(), parent);
	}
	
	/**
	 * Creates a root-less scope.
	 */
	public BaseScope() {
		this(null);
	}

	public Map<K, V> getPartialView() {
		return map;
	}
		
	public V get(K key) throws KeyNotFoundException {
		V value = getPartialView().get(key);
		if (value == null) {
			return getNotFound(key);
		}
		return value;
	}

	public V getUnsafe(K key) {
		try {
			return get(key);
		} catch (KeyNotFoundException e) {
			return null;
		}
	}

	public void put(K key, V value) throws AlreadyInScopeException {
		if (getPartialView().containsKey(key)) {
			throw new AlreadyInScopeException();
		}
		getPartialView().put(key, value);
	}
	
	public Map<K, V> createView() {
		LinkedList<Map<K, V>> views = new LinkedList<Map<K,V>>();
		views.add(getPartialView());
		 
		Scope<K, V> scope = this.getParent();
		while (scope != null) {
			views.add(scope.getPartialView());
			scope = scope.getParent();
		}
		Collections.reverse(views);
		
		Map<K, V> view = mapFactory.createMap();
		for (Map<K, V> partialView : views) {
			view.putAll(partialView);
		}
		return view;
	}
	
	public Scope<K, V> createChild() {
		return new BaseScope<K, V>(mapFactory, this);
	}
	
	public Scope<K, V> getParent() {
		return parent;
	}

	public void setParent(Scope<K, V> parent) {
		this.parent = parent;
		if (parent == null) {
			depth = 0;
		} else {
			depth = parent.getDepth() + 1;
		}
	}

	public V replace(K key, V value) {
		return getPartialView().put(key, value);
	}

	/**
	 * This method is called when the value is not present in the associated map.
	 * @param key
	 * @return
	 * @throws KeyNotFoundException
	 */
	protected V getNotFound(K key) throws KeyNotFoundException {
		try {
			return getParent().get(key);
		} catch (NullPointerException e) {
			throw new KeyNotFoundException();
		}
	}

	public List<Scope<K, V>> getChildren()
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public int size() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	public int getDepth() {
		return depth;
	}

	public boolean containsKey(K key) {
		try {
			get(key);
			return true;
		} catch (KeyNotFoundException e) {
			return false;
		}
	}

	/**
	 * Creates a root scope from the given map.
	 * @param contents Cannot be null.
	 * @throws IllegalArgumentException when contents are null.
	 */
	public static <K, V> Scope<K, V> fromMap(Map<? extends K, ? extends V> contents) {
		if (contents == null) {
			throw new IllegalArgumentException("Map cannot be null.");
		}
		BaseScope<K, V> scope = new BaseScope<K, V>(null);
		scope.map.putAll(contents);
		return scope;
	}
	
}
