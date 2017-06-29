package org.tyco.common.symbol;

import java.io.IOException;
import java.io.Serializable;

/**
 * A class to produce symbols from strings.
 * The class differs from its original due to
 *  the implementation of the java.util.Serializable interface.
 *
 * @author Andrew Appel, Andre Goncalves, Hervé Paulino
 * @version $Id$
 */
public class Symbol implements Serializable, Comparable<Symbol> {

    /** 
     * The unique symbol associated with a string.
     * Repeated calls to <code>symbol("abc")</code> return the same org.tyco.tycoc.symbol.
     * @param n Name of the symbol
     * @return An unique instance associated with the given string.
     */
    public static Symbol symbol(String n) {
        String u = n.intern();
        Symbol s = dict.get(u);
        if (s == null) {
            s = new Symbol(u);
            dict.put(u, s);
        }
        return s;
    }

    /**
     * Textual representation
     */
    @Override
	public String toString() {
        return name;
    }

    /**
     * Textual representation
     */
    private String name;

    /**
     * Construct given a string
     */
    private Symbol(String n) {
        name = n;
    }

    /**
     * The map from Strings to Symbols
     */
	private static java.util.Dictionary<String, Symbol> dict = new java.util.Hashtable<String, Symbol>(); 

	// The java.io.Serializable methods
	
	/**
	 * Writing the object to a stream
	 * @param out The output stream 
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out)
		throws java.io.IOException {
		out.writeObject(this.name);
		out.writeObject(dict);
	}

	/**
	 * Read object from stream.
	 * @param in The input stream
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void readObject(java.io.ObjectInputStream in)
		throws java.io.IOException {
		try {
			this.name = (String) in.readObject();
			java.util.Hashtable<String, Symbol> d = (java.util.Hashtable<String, Symbol>) in.readObject();
			String obj;
			for (java.util.Enumeration<String> e = d.keys(); e.hasMoreElements(); ) {
				obj = e.nextElement();
				dict.put(obj, d.get(obj));
			}
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int compareTo(Symbol o) {
		return this.name.compareTo(o.name);
	}
}
