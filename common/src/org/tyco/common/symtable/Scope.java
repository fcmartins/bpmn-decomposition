package org.tyco.common.symtable;

import java.util.List;
import java.util.Map;

/**
 * A scope data structure is the base of symbol tables.
 * 
 * A scope is a tree of maps. Each scope, a node of the tree, is associated to a
 * map, that can be retrieved by the method <code>getMap</code>. Scopes
 * aggregate a number of children scopes. A scope may have a parent scope.
 * Method <code>get</code> retrieves a value associated to a certain key in
 * the map of that scope, if that key has no associated value, the value is get
 * from the parent of that scope. Method <code>put</code> associates a key
 * with a value in the map of that scope.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: Scope.java,v 1.9 2009/07/16 21:10:04 tyco Exp $
 * @param <K>
 *            the type of the keys
 * @param <V>
 *            the type of the values
 * @param <S>
 *            the type of the parents
 */
public interface Scope<K, V> {
	/**
	 * Sets the parent scope.
	 * 
	 * @param parent
	 *            The parent scope. May be <code>null</code>, if it is a root
	 *            scope (orphan).
	 */
	void setParent(Scope<K, V> parent);

	/**
	 * Returns the parent scope.
	 * 
	 * @return The parent scope. May return <code>null</code>, if it is a root
	 *         scope (orphan).
	 */
	Scope<K, V> getParent();

	/**
	 * Creates a child, associating it to this scope and returns it.
	 * 
	 * @return The new child scope.
	 */
	Scope<K, V> createChild();

	/**
	 * The associations that exist in this scope (and this scope alone).
	 * 
	 * @return The map of this scope.
	 */
	Map<K, V> getPartialView();

	/**
	 * Looks up the value associated to a certain key in the associated map. If
	 * there is no association, then tries looks up in the parent scope (when
	 * there is one).
	 * 
	 * @param key
	 *            The key that is associated to the value we want to retrieve.
	 * @return The value associated with the key.
	 * @throws KeyNotFoundException Thrown when the key is not present.
	 */
	V get(K key) throws KeyNotFoundException;

	/**
	 * Looks up the value associated to a certain key in the associated map. If
	 * there is no association, then tries looks up in the parent scope (when
	 * there is one).
	 * 
	 * @param key
	 *            The key that is associated to the value we want to retrieve.
	 * @return The value associated with the key, or null (if none there is no
	 *         binding for this key).
	 */
	V getUnsafe(K key);

	/**
	 * Associates a key to a value in the map of this scope. It can only
	 * associate if it was not defined in this scope already (may exist in
	 * parent scopes already).
	 * 
	 * @param key
	 *            The key.
	 * @param value
	 *            The value.
	 * @throws AlreadyInScopeException
	 *             Cannot put a value if it was already defined in this scope.
	 */
	void put(K key, V value) throws AlreadyInScopeException;

	/**
	 * Associates a key to a value in the map of this scope.
	 * 
	 * @param key
	 * @param value
	 * @return the old value, null in case of none.
	 */
	V replace(K key, V value);
	
	/**
	 * Creates a view of the scope.
	 * @return
	 */
	Map<K, V> createView();
	
	/**
	 * Checks if a given key exists.
	 * @param key the key present in the scope.
	 * @return if the key exists in the scope.
	 */
	boolean containsKey(K key);
	
	/**
	 * This is an optional operation. Returns the number of children available
	 * in this map.
	 * 
	 * @return
	 * @throws UnsupportedOperationException if not implemented.
	 */
	public List<Scope<K, V>> getChildren() throws UnsupportedOperationException;
	/**
	 * This is an optional operation.
	 * Counts the number of children of this scope.
	 * 
	 * @return The number of children of this scope.
	 * @throws UnsupportedOperationException if not implemented.
	 */
	int size() throws UnsupportedOperationException;
	/**
	 * Get the depth of this scope.
	 * @return the depth of this scope.
	 */
	int getDepth();
}
