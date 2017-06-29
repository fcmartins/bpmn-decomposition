package org.tyco.common.symtable;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for populating the scope structure in a stacked based environment, e.g.,
 * when used within a visitor.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: ScopeBuilder.java,v 1.14 2009/07/16 21:10:04 tyco Exp $
 * @param <K>
 * @param <V>
 */
public class ScopeBuilder<K, V> {

	/**
	 * The current scope.
	 */
	private Scope<K, V> currScope;

	/**
	 * The bindings to scope.
	 */
	private Map<Object, Scope<K, V>> bindings = new HashMap<Object, Scope<K, V>>();

	/**
	 * Builds scopes using one scope as its base.
	 * 
	 * @param root
	 */
	public ScopeBuilder(Scope<K, V> root) {
		if (root == null) {
			throw new IllegalArgumentException("Scope cannot be null.");
		}
		currScope = root;
	}

	/**
	 * Builds a scope with a new scope.
	 */
	public ScopeBuilder() {
		this(new LinkedScope<K, V>());
	}

	/**
	 * Picks the current scope and creates a new child. Replaces the current
	 * scope with the newly created child scope.
	 */
	public void beginScope() {
		currScope = currScope.createChild();
	}

	/**
	 * Creates a new scope. Binds the new scope to a certain object, which be
	 * used to retrieve it afterwards.
	 * 
	 * @param obj
	 *            the key object which the new scope will be bound to.
	 */
	public void beginScopeWith(Object obj) {
		beginScope();
		bindings.put(obj, currScope);
	}

	/**
	 * Retrieves a scope associated to a certain object.
	 * 
	 * @param obj
	 * @return
	 */
	public Scope<K, V> getScopeFor(Object obj) {
		return bindings.get(obj);
	}

	/**
	 * Sets the current scope to the one associated to target object.
	 * 
	 * @param obj
	 *            must be present in the bindings
	 * @throws KeyNotFoundException
	 */
	public void loadScopeFrom(Object obj) throws KeyNotFoundException {
		Scope<K, V> scope = bindings.get(obj);
		if (scope == null) {
			throw new KeyNotFoundException();
		}
		currScope = scope;
	}

	/**
	 * Gets all bindings that exist in this scope builder.
	 * 
	 * @return
	 */
	public Map<Object, Scope<K, V>> getScopeBindings() {
		return bindings;
	}

	/**
	 * Replaces the current scope with its parent.
	 */
	public void endScope() {
		if (currScope.getParent() == null) {
			throw new IllegalStateException("Current scope has no parent.");
		}
		currScope = currScope.getParent();
	}

	/**
	 * Obtains the current scope.
	 * 
	 * @return
	 */
	public Scope<K, V> getCurrent() {
		return currScope;
	}

	/**
	 * Delegates the put to the current scope.
	 * 
	 * @param key
	 * @param value
	 * @throws AlreadyInScopeException
	 */
	public void put(K key, V value) throws AlreadyInScopeException {
		currScope.put(key, value);
	}

	/**
	 * Gets the value from the current scope.
	 * 
	 * @param key
	 * @return
	 * @throws KeyNotFoundException
	 */
	public V get(K key) throws KeyNotFoundException {
		return currScope.get(key);
	}

	/**
	 * Gets the value from the current scope.
	 * 
	 * @param key
	 * @return
	 * @throws KeyNotFoundException
	 */
	public V getUnsafe(K key) {
		return currScope.getUnsafe(key);
	}

	/**
	 * Delegates the replace to the current scope.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public V replace(K key, V value) {
		return currScope.replace(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getCurrent() == null ? "{}" : getCurrent().toString();
	}

	/**
	 * Gets the root scope, i.e. the ancestor that has no ancestors.
	 * 
	 * @return The root scope.
	 */
	public Scope<K, V> getRoot() {
		Scope<K, V> root = currScope;
		while (root.getParent() != null) {
			root = root.getParent();
		}
		return root;
	}

	/**
	 * Creates a new navigator based on the root of the scope tree.
	 * 
	 * @return
	 */
	public ScopeNavigator<K, V> navigateRoot() {
		return new ScopeNavigator<K, V>(getRoot());
	}

	/**
	 * Creates a scope navigator for the current scope.
	 * 
	 * @return
	 */
	public ScopeNavigator<K, V> navigateCurrent() {
		return new ScopeNavigator<K, V>(currScope);
	}
	
	/**
	 * Delegates the contains key to the current scope.
	 * @param key
	 * @return
	 */
	public boolean containsKey(K key) {
		return getCurrent().containsKey(key);
	}
}
