package org.tyco.common.symbol;

import java.util.Iterator;
import java.util.Stack;

/**
 * A class that implements an iterator for a symbol table.
 * It performs a preorder tree transversing and continue at binders level. 

 * @author Francisco Martins
 * @version $Id$
 */

public class TableIterator<V> implements Iterator<Binder<V>> {

	/**
	 * The current scope in the symbol table tree.
	 */
	private Scope<V> currentScope;	
	
	/**
	 * The current binder in the binders list of the current scope.
	 */
	private Binder<V> currentBinder;

	/**
	 * The stack to keep track of visited nodes.
	 */
	private Stack<Scope<V>> stack;

	/**
	 * Construct a table iterator giving the root element of the symbol table.
	 * @param root	The root of the symbol table
	 */
	public TableIterator(Scope<V> root) {
		currentScope = root;
		stack = new Stack<Scope<V>>();
		
		// find the first binder 
		currentBinder = findNextBinder();
	}
	
	/**
	 * Finds the next scope with binders. 
	 * @return The first binder in the next scope or null if none lasts
	 */
	private Binder<V> findNextBinder() {
		while(currentScope != null && currentScope.binders() == null)
			currentScope = computeNextScope();
		return currentScope == null ? null : currentScope.binders();
	}

	/**
	 * Finds the next binder. Is not in the current scope, find the next scope
	 * @return The next binder or null if no more binders
	 */
	private Binder<V> computeNextBinder() {
		// no more binder in the corrent scope? Find the next scope.
		if(currentBinder.next() == null)
			currentScope = computeNextScope(); 
		return currentBinder.next() == null ? findNextBinder() : currentBinder.next();
	}

	/**
	 * Computes the next scope in a preorder transverse of the symbol table tree
	 * @return The next scope in the preorder
	 */
	private Scope<V> computeNextScope() {
		// are we bad boys and are trying to find a scope after the end?
		if (currentScope == null) {
			throw new java.util.NoSuchElementException();
		}
		
		// perform a pre-order traverse in the tree
		
		// try to go left. Push the node for backtracking... 
		if (currentScope.children() != null) {
			stack.push(currentScope);
			return currentScope.children();
		} 
		// try to go right 
		if (currentScope.next() != null) { 
			return currentScope.next();
		}
		// the node is a leaf, backtracks ...
		
		// no more nodes to visit? The end!
		if (stack.empty()) {
			return null;
		}
		
		// backtracks to a node with a right branch to visit or end by returning null
		Scope<V> temp;
		while( (temp=stack.pop().next()) == null && !stack.empty()) {
			// ok
		}
		
		return stack.empty() ? null :  temp;
	}
		
	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return currentBinder != null;
	}

	/**
	 * @see java.util.Iterator#next()
	 * @pre this.hasNext()
	 */
	public Binder<V> next() {
		Binder<V> returnValue = currentBinder;
		// currentBinder has always the next element to visit, or null is no more elements
		currentBinder = computeNextBinder();
		return returnValue;
	} 

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		// we do not support removals
		throw new UnsupportedOperationException();
	}
}
