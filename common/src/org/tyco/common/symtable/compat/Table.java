package org.tyco.common.symtable.compat;

import java.util.Map.Entry;

import org.tyco.common.symbol.PrintEntry;
import org.tyco.common.symbol.Symbol;
import org.tyco.common.symtable.AlreadyInScopeException;
import org.tyco.common.symtable.KeyNotFoundException;
import org.tyco.common.symtable.LinkedScope;
import org.tyco.common.symtable.Scope;
import org.tyco.common.symtable.ScopeBuilder;
import org.tyco.common.symtable.ScopeNavigator;

/**
 * Compatibility class, that conforms to the API of
 * {@link org.tyco.common.symbol.Table} where possible.
 * 
 * @author Tiago Cogumbreiro
 * @version $Id: Table.java,v 1.7 2008/10/21 21:18:17 tyco Exp $
 * @param <V>
 */
@Deprecated
public class Table<V> {

	/**
	 * The root scope.
	 */
	private LinkedScope<Symbol, V> root;

	/**
	 * The navigator of scopes.
	 */
	private ScopeNavigator<Symbol, V> navigator;

	/**
	 * The scope's builder.
	 */
	private ScopeBuilder<Symbol, V> builder;

	/**
	 * Creates a new from a given scope.
	 * 
	 * @param root
	 *            the root scope.
	 */
	public Table(LinkedScope<Symbol, V> root) {
		this.root = root;
		this.builder = new ScopeBuilder<Symbol, V>(root);
	}

	/**
	 * Creates a table from an empty scope.
	 */
	public Table() {
		this(new LinkedScope<Symbol, V>());
	}

	/**
	 * Remembers the current state of the Table.
	 */
	public void beginScope() {
		builder.beginScope();
		navigator = null;
	}

	/**
	 * Restores the table to what it was at the most recent beginScope that has
	 * not already been ended.
	 */
	public void endScope() {
		builder.endScope();
		navigator = null;
	}

	/**
	 * Descends one level on the scope tree, placing in the Table the bindings
	 * in the new scope.
	 */
	public void downScope() {
		navigator.downScope();
	}

	/**
	 * Climbs one level on the scope tree.
	 */
	public void upScope() {
		navigator.upScope();
	}

	/**
	 * The object associated with the specified symbol.
	 * 
	 * @param key
	 *            Key to the object
	 * @return null if not found.
	 */
	public V get(Symbol key) {
		if (navigator != null) {
			try {
				return navigator.getCurrent().get(key);
			} catch (KeyNotFoundException e) {
				return null;
			}
		}
		try {
			return builder.getCurrent().get(key);
		} catch (KeyNotFoundException e) {
			return null;
		}
	}

	/**
	 * If the specified symbol is not in the table in the current scope, then
	 * put the value into the table, bound to the symbol. Otherwise make no
	 * change.
	 * 
	 * @return false if the symbol was already in the table, true otherwise.
	 */
	public boolean put(Symbol key, V value, int pos) {
		try {
			builder.getCurrent().put(key, value);
		} catch (AlreadyInScopeException e) {
			return false;
		}
		return true;
	}

	/**
	 * Make the current node root. This procedure should be called before each
	 * new pass in the abstract syntax tree.
	 */
	public void reset() {
		navigator = new ScopeNavigator<Symbol, V>(root);
	}

	/**
	 * Print the entries in a symbol table, given an object to print the
	 * entries.
	 * 
	 * @param pr
	 *            The print object
	 */
	public void print(PrintEntry<Symbol, V> pr) {
		for (Scope<Symbol, V> scope : root.getChildren()) {
			for (Entry<Symbol, V> entry : scope.getPartialView().entrySet()) {
				pr.print(-1, entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Accesses the root scope.
	 * 
	 * @return
	 */
	public LinkedScope<Symbol, V> getRoot() {
		return root;
	}
}
