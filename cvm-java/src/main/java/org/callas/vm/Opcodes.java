package org.callas.vm;

/**
 * The interface to all the opcodes.
 * 
 * @author Pedro Gomes (prg@dcc.fc.up.pt)
 * @date Mar 18, 2009
 * 
 */
public interface Opcodes {
	final byte LADD = 0x00; //0
	final byte DADD = 0x01; //0
	final byte LSUB = 0x02; //0
	final byte DSUB = 0x03; //0
	final byte LMUL = 0x04; //0
	final byte DMUL = 0x05; //0
	final byte LDIV = 0x06; //0
	final byte DDIV = 0x07; //0
	final byte LREM = 0x08; //0
	final byte LSHR = 0x09; //0
	final byte LSHL = 0x0a; //0
	final byte LNEG = 0x0b; //0
	final byte DNEG = 0x0c; //0
	final byte BNOT = 0x0d; //0
	final byte LNOT = 0x0e; //0
	final byte LAND = 0x0f; //0
	final byte BAND = 0x10; //0
	final byte LOR  = 0x11; //0
	final byte BOR  = 0x12; //0
	final byte LXOR = 0x13; //0
	final byte BXOR = 0x14; //0
	final byte POP  = 0x15; //0
	final byte DUP  = 0x16; //0
	final byte SWAP = 0x17; //0
	final byte CALL = 0x1a; //0
	final byte SYS  = 0x1c; //0
	final byte TMR  = 0x1d; //0
	final byte LE   = 0x1f; //0
	final byte LLT  = 0x20; //0
	final byte LGT  = 0x21; //0
	final byte DE   = 0x22; //0
	final byte DLT  = 0x23; //0
	final byte DGT  = 0x24; //0
	final byte UPD  = 0x26; //0
	final byte RCV  = 0x27; //0
	final byte RET  = 0x28; //0
	final byte SND  = 0x29; //0
	final byte LDB  = 0x2b; //0
	final byte STB  = 0x2c; //0
	final byte KILL = 0x2d; //0
	final byte OPEN = 0x2e; //0
	final byte CLS  = 0x2f; //0
	final byte LD   = 0x18; //1
	final byte LDC  = 0x19; //1
	final byte ST   = 0x1b; //1
	final byte LDM  = 0x2a; //1
	final byte IFT  = 0x1e; //4
	final byte GOTO = 0x25; //4
}
