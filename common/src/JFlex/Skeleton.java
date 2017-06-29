/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.3.5                                                             *
 * Copyright (C) 1998-2001  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package JFlex;

import java.io.*;
import java.util.Vector;


/**
 * This class stores the skeleton of an generated scanner.
 *
 * The skeleton consists of several parts that can be emitted to
 * a file. Usually there belongs a portion of generated
 * code (which is produced in class Emitter) to every two 
 * parts of skeleton code.
 *
 * @see JFlex.Emitter
 *
 * @author Gerwin Klein
 * @version JFlex 1.3.5, $Revision: 1.1 $, $Date: 2004/01/13 11:16:57 $
 */
public class Skeleton {

  static final private String NL = System.getProperty("line.separator");

  /**
   * The skeleton of a scanner
   */
  public static String line[] = {
    NL+
    "  /** This character denotes the end of file */"+NL+
    "  final public static int YYEOF = -1;"+NL+
    NL+
    "  /** initial size of the lookahead buffer */"+NL
    ,  // final private static int YY_BUFFERSIZE = ...;
    NL+
    "  /** lexical states */"+NL
    ,  // lexical states, charmap
    NL+
    "  /* error codes */"+NL+
    "  final private static int YY_UNKNOWN_ERROR = 0;"+NL+
    "  final private static int YY_ILLEGAL_STATE = 1;"+NL+
    "  final private static int YY_NO_MATCH = 2;"+NL+
    "  final private static int YY_PUSHBACK_2BIG = 3;"+NL+
    NL+
    "  /* error messages for the codes above */"+NL+
    "  final private static String YY_ERROR_MSG[] = {"+NL+
    "    \"Unkown internal scanner error\","+NL+
    "    \"Internal error: unknown state\","+NL+
    "    \"Error: could not match input\","+NL+
    "    \"Error: pushback value was too large\""+NL+
    "  };"+NL+
    NL
    ,  // isFinal list
    "  /** the input device */"+NL+
    "  private java.io.Reader yy_reader;"+NL+
    NL+
    "  /** the current state of the DFA */"+NL+
    "  private int yy_state;"+NL+
    NL+
    "  /** the current lexical state */"+NL+
    "  private int yy_lexical_state = YYINITIAL;"+NL+
    NL+
    "  /** this buffer contains the current text to be matched and is"+NL+
    "      the source of the yytext() string */"+NL+
    "  private char yy_buffer[] = new char[YY_BUFFERSIZE];"+NL+
    NL+
    "  /** the textposition at the last accepting state */"+NL+
    "  private int yy_markedPos;"+NL+
    NL+
    "  /** the textposition at the last state to be included in yytext */"+NL+
    "  private int yy_pushbackPos;"+NL+
    NL+
    "  /** the current text position in the buffer */"+NL+
    "  private int yy_currentPos;"+NL+
    NL+
    "  /** startRead marks the beginning of the yytext() string in the buffer */"+NL+
    "  private int yy_startRead;"+NL+
    NL+
    "  /** endRead marks the last character in the buffer, that has been read"+NL+
    "      from input */"+NL+
    "  private int yy_endRead;"+NL+
    NL+
    "  /** number of newlines encountered up to the start of the matched text */"+NL+
    "  private int yyline;"+NL+
    NL+
    "  /** the number of characters up to the start of the matched text */"+NL+
    "  private int yychar;"+NL+
    NL+
    "  /**"+NL+
    "   * the number of characters from the last newline up to the start of the "+NL+
    "   * matched text"+NL+
    "   */"+NL+
    "  private int yycolumn; "+NL+
    NL+
    "  /** "+NL+
    "   * yy_atBOL == true <=> the scanner is currently at the beginning of a line"+NL+
    "   */"+NL+
    "  private boolean yy_atBOL = true;"+NL+
    NL+
    "  /** yy_atEOF == true <=> the scanner is at the EOF */"+NL+
    "  private boolean yy_atEOF;"+NL+
    NL
    ,  // user class code
    NL+
    "  /**"+NL+
    "   * Creates a new scanner"+NL+
    "   * There is also a java.io.InputStream version of this constructor."+NL+
    "   *"+NL+
    "   * @param   in  the java.io.Reader to read input from."+NL+
    "   */"+NL
    ,  // constructor declaration
    NL+
    NL+
    "  /**"+NL+
    "   * Refills the input buffer."+NL+
    "   *"+NL+
    "   * @return      <code>false</code>, iff there was new input."+NL+
    "   * "+NL+
    "   * @exception   IOException  if any I/O-Error occurs"+NL+
    "   */"+NL+
    "  private boolean yy_refill() throws java.io.IOException {"+NL+
    NL+
    "    /* first: make room (if you can) */"+NL+
    "    if (yy_startRead > 0) {"+NL+
    "      System.arraycopy(yy_buffer, yy_startRead, "+NL+
    "                       yy_buffer, 0, "+NL+
    "                       yy_endRead-yy_startRead);"+NL+
    NL+
    "      /* translate stored positions */"+NL+
    "      yy_endRead-= yy_startRead;"+NL+
    "      yy_currentPos-= yy_startRead;"+NL+
    "      yy_markedPos-= yy_startRead;"+NL+
    "      yy_pushbackPos-= yy_startRead;"+NL+
    "      yy_startRead = 0;"+NL+
    "    }"+NL+
    NL+
    "    /* is the buffer big enough? */"+NL+
    "    if (yy_currentPos >= yy_buffer.length) {"+NL+
    "      /* if not: blow it up */"+NL+
    "      char newBuffer[] = new char[yy_currentPos*2];"+NL+
    "      System.arraycopy(yy_buffer, 0, newBuffer, 0, yy_buffer.length);"+NL+
    "      yy_buffer = newBuffer;"+NL+
    "    }"+NL+
    NL+
    "    /* finally: fill the buffer with new input */"+NL+
    "    int numRead = yy_reader.read(yy_buffer, yy_endRead, "+NL+
    "                                            yy_buffer.length-yy_endRead);"+NL+
    NL+
    "    if (numRead < 0) {"+NL+
    "      return true;"+NL+
    "    }"+NL+
    "    else {"+NL+
    "      yy_endRead+= numRead;  "+NL+
    "      return false;"+NL+
    "    }"+NL+
    "  }"+NL+
    NL+
    NL+
    "  /**"+NL+
    "   * Closes the input stream."+NL+
    "   */"+NL+
    "  final public void yyclose() throws java.io.IOException {"+NL+
    "    yy_atEOF = true;            /* indicate end of file */"+NL+
    "    yy_endRead = yy_startRead;  /* invalidate buffer    */"+NL+
    NL+
    "    if (yy_reader != null)"+NL+
    "      yy_reader.close();"+NL+
    "  }"+NL+
    NL+
    NL+
    "  /**"+NL+
    "   * Closes the current stream, and resets the"+NL+
    "   * scanner to read from a new input stream."+NL+
    "   *"+NL+
    "   * All internal variables are reset, the old input stream "+NL+
    "   * <b>cannot</b> be reused (internal buffer is discarded and lost)."+NL+
    "   * Lexical state is set to <tt>YY_INITIAL</tt>."+NL+
    "   *"+NL+
    "   * @param reader   the new input stream "+NL+
    "   */"+NL+
    "  final public void yyreset(java.io.Reader reader) throws java.io.IOException {"+NL+
    "    yyclose();"+NL+
    "    yy_reader = reader;"+NL+
    "    yy_atBOL  = true;"+NL+
    "    yy_atEOF  = false;"+NL+
    "    yy_endRead = yy_startRead = 0;"+NL+
    "    yy_currentPos = yy_markedPos = yy_pushbackPos = 0;"+NL+
    "    yyline = yychar = yycolumn = 0;"+NL+
    "    yy_lexical_state = YYINITIAL;"+NL+
    "  }"+NL+
    NL+
    NL+
    "  /**"+NL+
    "   * Returns the current lexical state."+NL+
    "   */"+NL+
    "  final public int yystate() {"+NL+
    "    return yy_lexical_state;"+NL+
    "  }"+NL+
    NL+
    NL+
    "  /**"+NL+
    "   * Enters a new lexical state"+NL+
    "   *"+NL+
    "   * @param newState the new lexical state"+NL+
    "   */"+NL+
    "  final public void yybegin(int newState) {"+NL+
    "    yy_lexical_state = newState;"+NL+
    "  }"+NL+
    NL+
    NL+
    "  /**"+NL+
    "   * Returns the text matched by the current regular expression."+NL+
    "   */"+NL+
    "  final public String yytext() {"+NL+
    "    return new String( yy_buffer, yy_startRead, yy_markedPos-yy_startRead );"+NL+
    "  }"+NL+
    NL+
    NL+
    "  /**"+NL+
    "   * Returns the character at position <tt>pos</tt> from the "+NL+
    "   * matched text. "+NL+
    "   * "+NL+
    "   * It is equivalent to yytext().charAt(pos), but faster"+NL+
    "   *"+NL+
    "   * @param pos the position of the character to fetch. "+NL+
    "   *            A value from 0 to yylength()-1."+NL+
    "   *"+NL+
    "   * @return the character at position pos"+NL+
    "   */"+NL+
    "  final public char yycharat(int pos) {"+NL+
    "    return yy_buffer[yy_startRead+pos];"+NL+
    "  }"+NL+
    NL+
    NL+
    "  /**"+NL+
    "   * Returns the length of the matched text region."+NL+
    "   */"+NL+
    "  final public int yylength() {"+NL+
    "    return yy_markedPos-yy_startRead;"+NL+
    "  }"+NL+
    NL+
    NL+
    "  /**"+NL+
    "   * Reports an error that occured while scanning."+NL+
    "   *"+NL+
    "   * In a wellformed scanner (no or only correct usage of "+NL+
    "   * yypushback(int) and a match-all fallback rule) this method "+NL+
    "   * will only be called with things that \"Can't Possibly Happen\"."+NL+
    "   * If this method is called, something is seriously wrong"+NL+
    "   * (e.g. a JFlex bug producing a faulty scanner etc.)."+NL+
    "   *"+NL+
    "   * Usual syntax/scanner level error handling should be done"+NL+
    "   * in error fallback rules."+NL+
    "   *"+NL+
    "   * @param   errorCode  the code of the errormessage to display"+NL+
    "   */"+NL
    ,  // yy_ScanError declaration
    "    String message;"+NL+
    "    try {"+NL+
    "      message = YY_ERROR_MSG[errorCode];"+NL+
    "    }"+NL+
    "    catch (ArrayIndexOutOfBoundsException e) {"+NL+
    "      message = YY_ERROR_MSG[YY_UNKNOWN_ERROR];"+NL+
    "    }"+NL+
    NL
    ,  // throws clause
    "  } "+NL+
    NL+
    NL+
    "  /**"+NL+
    "   * Pushes the specified amount of characters back into the input stream."+NL+
    "   *"+NL+
    "   * They will be read again by then next call of the scanning method"+NL+
    "   *"+NL+
    "   * @param number  the number of characters to be read again."+NL+
    "   *                This number must not be greater than yylength()!"+NL+
    "   */"+NL
    ,  // yypushback decl (contains yy_ScanError exception)
    "    if ( number > yylength() )"+NL+
    "      yy_ScanError(YY_PUSHBACK_2BIG);"+NL+
    NL+
    "    yy_markedPos -= number;"+NL+
    "  }"+NL+
    NL+
    NL
    ,  // yy_doEof
    "  /**"+NL+
    "   * Resumes scanning until the next regular expression is matched,"+NL+
    "   * the end of input is encountered or an I/O-Error occurs."+NL+
    "   *"+NL+
    "   * @return      the next token"+NL+
    "   * @exception   IOException  if any I/O-Error occurs"+NL+
    "   */"+NL
    ,  // yylex declaration
    "    int yy_input;"+NL+
    "    int yy_action;"+NL+
    NL+
    "    // cached fields:"+NL+
    "    int yy_currentPos_l;"+NL+
    "    int yy_startRead_l;"+NL+
    "    int yy_markedPos_l;"+NL+
    "    int yy_endRead_l = yy_endRead;"+NL+
    "    char [] yy_buffer_l = yy_buffer;"+NL+
    "    char [] yycmap_l = yycmap;"+NL+
    NL
    ,  // local declarations
    NL+
    "    while (true) {"+NL+
    "      yy_markedPos_l = yy_markedPos;"+NL+
    NL
    ,  // start admin (line, char, col count)
    "      yy_action = -1;"+NL+
    NL+
    "      yy_startRead_l = yy_currentPos_l = yy_currentPos = "+NL+
    "                       yy_startRead = yy_markedPos_l;"+NL+
    NL
    ,  // start admin (lexstate etc)
    NL+
    "      yy_forAction: {"+NL+
    "        while (true) {"+NL+
    NL
    ,  // next input, line, col, char count, next transition, isFinal action
    "            yy_action = yy_state; "+NL+
    "            yy_markedPos_l = yy_currentPos_l; "+NL
    ,  // line count update
    "          }"+NL+
    NL+
    "        }"+NL+
    "      }"+NL+
    NL+
    "      // store back cached position"+NL+
    "      yy_markedPos = yy_markedPos_l;"+NL
    ,  // char count update
    NL+
    "      switch (yy_action) {"+NL+
    NL
    ,  // actions
    "        default: "+NL+
    "          if (yy_input == YYEOF && yy_startRead == yy_currentPos) {"+NL+
    "            yy_atEOF = true;"+NL
    ,  // eofvalue
    "          } "+NL+
    "          else {"+NL
    ,  // no match
    "          }"+NL+
    "      }"+NL+
    "    }"+NL+
    "  }"+NL+
    NL
    ,  // main
    NL+
    "}"+NL
  };


  /**
   * The current part of the skeleton (an index of nextStop[])
   */
  private int pos;
  
  
  /**
   * The writer to write the skeleton-parts to
   */
  private PrintWriter out;


  /**
   * Creates a new skeleton instance. 
   *
   * @param   out  the writer to write the skeleton-parts to
   */
  public Skeleton(PrintWriter out) {
    this.out = out;
  }


  /**
   * Emits the next part of the skeleton
   */
  public void emitNext() {
    out.print( line[pos++] );
  }


  /**
   * Reads an external skeleton file for later use with this class.
   */
  public static void readSkelFile(File skeletonFile) {

    int size = line.length;

    try {
      BufferedReader reader = new BufferedReader(new FileReader(skeletonFile));

      Out.println("Reading skeleton file \""+skeletonFile+"\"");

      Vector lines = new Vector();
      StringBuffer section = new StringBuffer();
      
      String ln;
      while ( (ln = reader.readLine()) != null ) {
        if ( ln.startsWith("---") ) {
          lines.addElement( section.toString() );
          section.setLength(0);
        }
        else {
          section.append(ln);  
          section.append(NL); 
        }
      }

      if (section.length() > 0) 
        lines.addElement( section.toString() );

      line = new String[lines.size()];
      for (int i = 0; i < lines.size(); i++)
        line[i] = (String) lines.elementAt(i);
      
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    if ( line.length != size ) {
      Out.error(ErrorMessages.WRONG_SKELETON);
      throw new GeneratorException();
    }
  }
}
