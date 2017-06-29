package org.tyco.common.util;

/**
 * This is an element that is present in the source code.
 * @author Tiago Cogumbreiro
 * @version $Id: SourceElement.java,v 1.1 2008/01/25 12:39:51 tyco Exp $
 */
public interface SourceElement {
	/**
	 * The location (offset of bytes ) on the source code stream.
	 * @return
	 */
	int getLocation();
}
