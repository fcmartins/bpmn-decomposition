package org.tyco.common.symbol;
 /**
  *  A map from <code>Symbols</code> into <code>Objects</code>
  *
  * @author  Vasco T. Vasconcelos
  * @version $Id$
  */
public class Map extends org.tyco.common.util.Map {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1445026590878386640L;

	/**
	 * Construct a map using the Symbol comparator.
	 */
	public Map() {
		super(new Comparator ());
	}
}
