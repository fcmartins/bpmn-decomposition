package org.callas.vm;

/**
 * The interface to all the tags that occurs in the byte-code.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @author Pedro Gomes (prg@dcc.fc.up.pt)
 * @date May 24, 2009
 * 
 */
public interface Tags {
	final byte BOOL = 0x01;
	final byte BYTE = 0x02;
	final byte INTEGER = 0x03;
	final byte LONG = 0x04;
	final byte STRING = 0x05;
	final byte DOUBLE = 0x06;
	final byte MODULE = 0x07;
	final byte MODULE_DECLARATION = 0x08;
}