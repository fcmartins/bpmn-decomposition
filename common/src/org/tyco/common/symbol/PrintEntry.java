package org.tyco.common.symbol;

/**
 * An interface for printing an entry in a symbol table.
 *
 * @author  Vasco T. Vasconcelos
 * @version $Id$
 */
public interface PrintEntry<K, V> {

	/**
	 * Print an entry in a symbol table.
     * @param pos The position of the identifier associated to the entry,
     * within the source file.
	 * @param key The key to the entry.
	 * @param value The value of the entry.
	 */
	public void print (int pos, K key, V value);
}
