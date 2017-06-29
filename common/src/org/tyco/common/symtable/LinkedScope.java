package org.tyco.common.symtable;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This implementation keeps a list of children scopes with a linked list.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: LinkedScope.java,v 1.9 2008/10/21 23:12:29 tyco Exp $
 * @param <K>
 * @param <V>
 */
public class LinkedScope<K, V> extends BaseScope<K, V> {

	private List<Scope<K, V>> scopes = new LinkedList<Scope<K, V>>();

	/**
	 * Constructs a new linked scope.
	 * 
	 * @param mapFactory
	 * @param parent
	 */
	public LinkedScope(MapFactory<K, V> mapFactory, Scope<K, V> parent) {
		super(mapFactory, parent);
	}

	/**
	 * Create a scope from a given parent. Uses the hash map factory by default.
	 * 
	 * @param parent
	 */
	public LinkedScope(LinkedScope<K, V> parent) {
		super(parent);
	}

	/**
	 * Create a root linked scope. Uses the hash map factory by default.
	 */
	public LinkedScope() {
		super();
	}

	public LinkedScope<K, V> createChild() {
		LinkedScope<K, V> scope = new LinkedScope<K, V>(mapFactory, this);
		scopes.add(scope);
		return scope;
	}

	@Override
	public int size() {
		return scopes.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getParent() == null ? getPartialView().toString() : (getParent()
				.toString() + getPartialView().toString());
	}

	@Override
	public List<Scope<K, V>> getChildren() {
		return scopes;
	}

}
