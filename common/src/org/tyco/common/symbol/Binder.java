package org.tyco.common.symbol;

/**
 * A list of key-value-position triples.
 *
 * @author Vasco T. Vasconcelos
 * @version $Id$
 */
class Binder<V> {
	
	// SECTION I.  Binder Attributes and Constructors
	
	/**
	 * The value held in the binder.
	 */
    private V value;
    
	/**
	 * The key for the binder.
	 */    
    private Symbol key;
    
	/**
	 * The position of the symbol in the source code.
	 */   
    private int pos;
    
	/**
	 * The next binder in the list.
	 */
    private Binder<V> next;
    
    /**
     * Constructs a new binder giving a symbol as a key, a value, a position in the source
     * code, and the reference to the next binder.
	 * @param k 	The key of the binder
	 * @param v	The value of the binder
	 * @param p	The position where the symbol appeared at the source code
	 * @param t 	The next binder in the list
	 */
	Binder(Symbol k, V v, int p, Binder<V> t) {
        value = v;
        key = k;
        pos = p;
        next = t;
    }
    
	/**
	 * The value of the binder.
	 */
	V value() {
		return value;
	}

	/**
	 * The next binder in the list.
	 */
	Binder<V> next() {
		return next;
	}

	/**
	 * The key of the binder.
	 */
	Symbol key() {
		return key;
	}

	/**
	 * The position of the binder.
	 */
	int pos() {
		return pos;
	}
}
