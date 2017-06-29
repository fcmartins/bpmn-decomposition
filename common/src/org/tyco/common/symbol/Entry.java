package org.tyco.common.symbol;

/**
 * An entry in the dictionary.
 *
 * @author Vasco T. Vasconcelos
 * @version $Id$
 */
class Entry<V> {
    V value;
    Entry<V> next;
    Entry(V v, Entry<V> t) {
        value = v;
        next = t;
    }
}
