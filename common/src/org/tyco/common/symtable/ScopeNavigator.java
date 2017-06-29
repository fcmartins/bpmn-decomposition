package org.tyco.common.symtable;

import java.util.Iterator;
import java.util.Stack;

/**
 * Utility class to navigate scopes.
 * @author Tiago Cogumbreiro
 * @version $Id: ScopeNavigator.java,v 1.3 2008/10/21 21:18:17 tyco Exp $
 * @param <K> The type of the keys.
 * @param <V> The type of the values.
 */
public class ScopeNavigator<K, V> {

	private Stack<Scope<K, V>> scopes = new Stack<Scope<K, V>>();
	private Stack<Iterator<Scope<K, V>>> iters = new Stack<Iterator<Scope<K, V>>>();
	private int depth = 0;
	
	/**
	 * Navigates the target scope.
	 * @param scope
	 */
	public ScopeNavigator(Scope<K, V> scope) {
		scopes.push(scope);
		iters.push(scope.getChildren().iterator());
	}
	
	/**
	 * This method is analogous to the beginScope() of the {@link ScopeBuilder}.
	 * Navigates into the children of the node.
	 */
	public void downScope() throws IllegalArgumentException {
		Iterator<Scope<K, V>> currIter = iters.peek();
		if (currIter.hasNext()) {
			Scope<K, V> scope = currIter.next();
			scopes.push(scope);
			iters.push(scope.getChildren().iterator());
			depth++;
		} else {
			throw new IllegalStateException("Call upScope() first.");
		}
	}
	
	/**
	 * This method is analogous to the endScope() of the {@link ScopeBuilder}.
	 * Navigates back to the parent scope.
	 */
	public void upScope() throws IllegalStateException {
		if (depth == 0) {
			throw new IllegalStateException("Call downScope() first.");
		}
		scopes.pop();
		iters.pop();
		depth--;
	}
	
	/**
	 * Returns the node we are currently visiting.
	 * @return The node we are currently visiting.
	 */
	public Scope<K, V> getCurrent() {
		return scopes.peek();
	}
	
	/**
	 * Returns the depth of the navigation.
	 * @return The depth of the navigation.
	 */
	public int getDepth() {
		return depth;
	}
}
