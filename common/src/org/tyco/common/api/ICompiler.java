package org.tyco.common.api;


/**
 * Compile terms of type F into terms of type T.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Oct 9, 2009
 * 
 * @param <F>
 *            The type of the source language.
 * @param <T>
 *            The type of the target language.
 */
public interface ICompiler<F, T> {
	/**
	 * Translate object <code>from</code> into an object of type <code>F</code>.
	 * 
	 * @param from
	 *            The source object to translate.
	 * @return The translated object.
	 */
	T compile(F from);
}
