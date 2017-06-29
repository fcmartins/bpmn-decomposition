package org.tyco.common.symtable;

import java.util.Iterator;

/**
 * 
 * Does a depth first navigation on scopes, iterating over all scopes reachable
 * from a root scope, including itself.
 * 
 * @author Tiago Cogumbreiro
 *
 * @param <K> The type of the keys.
 * @param <V> The type of the values.
 */
public class ScopeIterator<K,V> implements Iterator<Scope<K, V>> {

	/**
	 * Uses the scope navigator to implement the iteration of scopes.
	 */
	private ScopeNavigator<K, V> nav;
	
	/**
	 * The next scope to visit.
	 */
	private Scope<K, V> next;
	
	public ScopeIterator(Scope<K, V> root) {
		nav = new ScopeNavigator<K, V>(root);
		next = nav.getCurrent(); // root is the next element
	}
	
	public boolean hasNext() {
		return next != null;
	}

	public Scope<K, V> next() {
		Scope<K, V> curr = next;
		
		while (curr != null && next == curr) {
			try {
				// try to go down on the scope
				nav.downScope();
				// if successful, replace the next scope
				next = nav.getCurrent();
			} catch (IllegalStateException e) {
				// otherwise, we can't go down anymore, try to go up
				if (nav.getDepth() > 0) {
					nav.upScope();
				} else {
					// no more scopes left
					next = null;
				}
			}
		}
		// we are always one step ahead. Return previous next.
		return curr;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
