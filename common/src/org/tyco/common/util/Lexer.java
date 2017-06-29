package org.tyco.common.util;
/**
 * What the lexical analyser provides (to the parser).
 *
 * @author  Andrew Appel
 * @version $Id$
 */
public interface Lexer {
    public java_cup.runtime.Symbol nextToken () throws java.io.IOException;
}
