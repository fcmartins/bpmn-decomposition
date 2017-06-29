package org.tyco.common.symbol;

/**
 * A tree to manage variable scopes in a program
 *
 * @author Vasco T Vasconcelos
 * @version $Id$
 */
class Scope<V> {

    // SECTION I.  Tree Attributes and Constructors

    /**
     * The binders in the node.
     */
    private Binder<V> binders;

    /**
     * The next sibling node.
     */
    private Scope<V> next;

    /**
     * The parent node.
     */
    private Scope<V> parent;

    /**
     * The children nodes.
     */
    private Scope<V> firstChild;

    /**
     * The dictionary for the entries.
     */
    private java.util.Dictionary<Symbol, Entry<V>> dict;

    /**
     * Construct a Scope given the parent scope, and the dictionary
     * for the entries.
     * @param d The dictionary for the entries.
     * @param p Parent scope
     */
    Scope(java.util.Dictionary<Symbol, Entry<V>> d, Scope<V> p) {
        dict = d;
        parent = p;
    }

    /**
     * Construct a scope given the dictionary for the entries.
     * @param d The dictionary for the entries.
     */
    Scope(java.util.Dictionary<Symbol, Entry<V>> d) {
        this(d, null);
    }

    /**
     * The binders in the node.
     */
    Binder<V> binders() {
        return binders;
    }

    /**
     * The next sibling node.
     */
    Scope<V> next() {
        return next;
    }

    /**
     * The children nodes.
     */
    Scope<V> children() {
        return firstChild;
    }

    /**
     * The Parent node.
     */
    Scope<V> parent() {
    	return parent;
    }
    
    // SECTION II.  Building the Tree

    /**
     * Add a binder to the node.
     */
    void add(Symbol key, V value, int pos) {
        binders = new Binder<V>(key, value, pos, binders);
    }

    /**
     * A new subtree for the node.
     */
    Scope<V> beginScope() {
        Scope<V> child = new Scope<V>(dict, this);
        if (firstChild == null) {
            firstChild = child;
        } else { // look for the last child
            Scope<V> aChild = firstChild;
            while (aChild.next != null) {
                aChild = aChild.next;
            }
            aChild.next = child;
        }
        return child;
    }

    /**
     * The parent of the node.
     */
    Scope<V> endScope() {
        removeBinders();
        return parent;
    }

    // SECTION III.  Visiting an Already Built Tree

    /**
     * The last child visited.
     */
    private Scope<V> lastChildVisited;

    /**
     * The next subtree of the node.
     */
    Scope<V> downScope() {
        lastChildVisited =
            lastChildVisited == null ? firstChild : lastChildVisited.next;
        lastChildVisited.insertBinders();
        return lastChildVisited;
    }

    /**
     * The parent of the node.
     */
    Scope<V> upScope() {
        removeBinders();
        return parent;
    }

    // SECTION IV.  Inserting and removing binders in the tree.

    /**
     * Removes from the dictionary the binders in a given list of
     * binders.
     */
    private void removeBinders() {
        for (Binder<V> b = binders; b != null; b = b.next()) {
            Entry<V> e = dict.remove(b.key());
            if (e.next != null) {
                dict.put(b.key(), e.next);
            }
        }
    }

    /**
     * Inserts in the dictionary the binders in a given list of
     * binders.
     */
    void insertBinders() {
        for (Binder<V> b = binders; b != null; b = b.next()) {
            dict.put(b.key(), new Entry<V>(b.value(), dict.remove(b.key())));
        }
    }
}
