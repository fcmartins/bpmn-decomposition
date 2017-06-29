package org.tyco.common.symbol;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * A class to produce symbols from strings, similar to
 * java.util.Dictionary, except that each key must be a org.tyco.tycoc.symbol and
 * there is a scope mechanism.
 *
 * Works in two modes: Build (using <code>beginScope</code> and
 * <code>endScope</code>) and Visiting (using <code>downScope</code>
 * and <code>upScope</code>).
 *
 * @author Andrew Appel, Vasco T Vasconcelos, Andre Goncalves
 * @version $Id$
*/
public class Table<V> {
//implements java.util.Iterator {

    // SECTION I.  Attributes and Constructors

    /**
     * The dictionary for the entries.
     */
    private java.util.Dictionary<Symbol, Entry<V>> dict;
    
    /**
     * The root of the scope tree.
     */
    private Scope<V> root;

    /**
     * The current node in the scope tree.
     */
    private Scope<V> current;

    /**
     * Construct a Table.
     */
    public Table() {
        dict = new java.util.Hashtable<Symbol, Entry<V>>();
        root = new Scope<V>(dict);
        reset();
    }

	// SECTION II.  Put in, get from the table

    /**
     * The object associated with the specified symbol.
     * @param key Key to the object
     */
    public V get(Symbol key) {
        Entry<V> e = dict.get(key);
        return (e == null) ? null : e.value;
    }

    /**
     * If the specified symbol is not in the table in the current
     * scope, then put the value into the table, bound to the symbol.
     * Otherwise make no change.
     * @return false if the symbol was already in the table, true otherwise.
     */
    public boolean put(Symbol key, V value, int pos) {
        Entry<V> b = dict.get(key);
        if (b != null) { // see if we can reach b in this scope
            for (Binder<V> c = current.binders(); c != null; c = c.next()) {
                if (b.value == c.value()) {
                    return false;
                }
            }
        }
        dict.put(key, new Entry<V>(value, b));
        current.add(key, value, pos);
        return true;
    }

    // SECTION III.  Building the tree

    /**
     * Remembers the current state of the Table.
     */
    public void beginScope() {
        current = current.beginScope();
    }

    /** 
     * Restores the table to what it was at the most recent beginScope
     * that has not already been ended.
     */
    public void endScope() {
        current = current.endScope();
    }

    // SECTION IV.  Visiting an already built tree

	/**
	 * Make the current node root.  This procedure should be called
	 * before each new pass in the abstract syntax tree.
	 */
	public void reset() {
		current = root;
		current.insertBinders();
	}

    /**
     * Descends one level on the scope tree, placing in the Table the
     * bindings in the new scope.
     */
    public void downScope() {
        current = current.downScope();
    }

    /**
     * Climbs one level on the scope tree.
     */
    public void upScope() {
        current = current.upScope();
    }
    
    // SECTION V.  Printing a symbol table

	/**
	 * Print the entries in a symbol table, given an object to print the entries.
	 * @param pr The print object
	 */
	public void print (PrintEntry<Symbol, V> pr) {
		Iterator<Binder<V>> s = iterator ();		
		while (s.hasNext()) {
			Binder<V> b = s.next();
			pr.print (b.pos(), b.key(), b.value());
		}
	}

	/**
	 * Constructs an iterator for the symbol table.
	 * @return an iterator for the symbol table
	 */
	public Iterator<Binder<V>> iterator () {
		return new TableIterator<V>(root);
	}

	/**
	 * Constructs an iterator for the entries of the symbols in the scope.
	 * @return an iterator for the entries of the symbols in the scope
	 */
	public Iterator<V> scopeEntriesIterator () {
		Enumeration<Entry<V>> e = dict.elements(); 
		Set<V> scopeEntries = new TreeSet<V>(
			new org.tyco.common.util.Comparator<V>());
		
		while(e.hasMoreElements()) {
			scopeEntries.add(e.nextElement().value);
		}
		return scopeEntries.iterator();
	}
	
}
