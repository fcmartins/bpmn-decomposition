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
import java.awt.TextArea;


/**
 * In this class all output to the java console is filtered.
 *
 * Use the switches VERBOSE, TIME and DUMP at compile time to determine
 * the verbosity of JFlex output. There is no switch for
 * suppressing error messages. VERBOSE and TIME can be overridden 
 * by command line paramters.
 *
 * Redirects output to a TextArea in GUI mode.
 *
 * Counts error and warning messages.
 *
 * <UL>
 * <LI>
 * VERBOSE should be switched on in all normal cases. 
 *         It is used for standard progress messages to the user.
 * </LI><LI>
 * TIME    can be set to true for performance measurements if 
 *         time statistics for the different stages of generation
 *         are to be printed.
 * </LI><LI>       
 * DUMP    this one gives you all the information you want (or not).
 *         BUT: prepare to wait.
 *         If only dump-information from specific classes is
 *         needed, compile all classes with DUMP=false first,
 *         then recompile this class and all the classes that
 *         are to dump their information with DUMP=true.
 * </LI>
 * </UL>
 *
 * @author Gerwin Klein
 * @version JFlex 1.3.5, $Revision: 1.1 $, $Date: 2004/01/13 11:16:57 $
 */
public final class Out implements ErrorMessages {

  /** platform dependent newline sequence */
  public static final String NL = System.getProperty("line.separator");
  
  /**
   * If VERBOSE is false, no progress output will be generated
   */
  public static boolean VERBOSE     = true;

  /**
   * If TIME is true, jflex will print time statistics about the generation process
   */
  public static boolean TIME        = false;

  /**
   * If DUMP is true, you will be flooded with information (e.g. dfa tables).
   */
  public static boolean DUMP        = false;

  /**
   * If DOT is true, jflex will write graphviz .dot files for generated automata
   */
  public static boolean DOT         = false;
    
  /**
   * If DEBUG is true, additional verbose debug information is produced
   */
  public final static boolean DEBUG = false;

  /** count total warnings */
  public static int warnings;

  /** count total errors */
  public static int errors;

  /** output device */
  private static StdOutWriter out = new StdOutWriter();


  /**
   * Switches to GUI mode if <code>text</code> is not <code>null</code>
   *
   * @param text  the message TextArea of the JFlex GUI
   */
  public static void setGUIMode(TextArea text) {
    out.setGUIMode(text);
  }

  /**
   * All parts of JFlex, that want to report something about 
   * time statistic should use this method for their output.
   *
   * @param message  the message to be printed
   */
  public static void time(String message) {
    if (TIME) out.println(message);
  }
  

  /**
   * All parts of JFlex, that want to report generation progress
   * should use this method for their output.
   *
   * @param message  the message to be printed
   */
  public static void println(String message) {
    if (VERBOSE) out.println(message);
  }


  /**
   * All parts of JFlex, that want to report generation progress
   * should use this method for their output.
   *
   * @param message  the message to be printed
   */
  public static void print(String message) {
    if (VERBOSE) out.print(message);
  }

  /**
   * Dump debug information to System.out
   *
   * Use like this 
   *
   * <code>if (Out.DEBUG) Out.debug(message)</code>
   *
   * to save performance during normal operation (when DEBUG
   * is turned off).
   */
  public static void debug(String message) {
    if (DEBUG) System.out.println(message); 
  }


  /**
   * All parts of JFlex, that want to provide dump information
   * should use this method for their output.
   *
   * @message the message to be printed 
   */
  public static void dump(String message) {
    if (DUMP) out.println(message);
  }

  
  /**
   * All parts of JFlex, that want to report error messages
   * should use this method for their output.
   *
   * @message  the message to be printed
   */
  private static void err(String message) {
    out.println(message);
  }
  
  
  /**
   * throws a GeneratorException if there are any errors recorded
   */
  public static void checkErrors() {
    if (errors > 0) throw new GeneratorException();
  }
  

  /**
   * print error and warning statistics
   */
  public static void statistics() {    
    StringBuffer line = new StringBuffer(errors+" error");
    if (errors != 1) line.append("s");

    line.append(", "+warnings+" warning");
    if (warnings != 1) line.append("s");

    line.append(".");
    err(line.toString());
  }


  /**
   * reset error and warning counters
   */
  public static void resetCounters() {
    errors = 0;
    warnings = 0;
  }

  
  /**
   * print a warning without position information
   *
   * @param message   the warning message
   */  
  public static void warning(String message) {
    warnings++;

    err(NL+"Warning : "+message);
  }


  /**
   * print a warning with line information
   *
   * @param message  code of the warning message
   * @param line     the line information
   *
   * @see ErrorMessages
   */
  public static void warning(int message, int line) {
    warnings++;

    String msg = NL+"Warning";
    if (line > 0) msg = msg+" in line "+(line+1);

    err(msg+": "+messages[message]);
  }


  /**
   * print warning message with location information
   *
   * @param file     the file the warning is issued for
   * @param message  the code of the message to print
   * @param line     the line number of the position
   * @param column   the column of the position
   */
  public static void warning(File file, int message, int line, int column) {

    String msg = NL+"Warning";
    if (file != null) msg += " in file \""+file+"\"";
    if (line >= 0) msg = msg+" (line "+(line+1)+")";

    try {
      err(msg+": "+NL+messages[message]);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      err(msg);
    }

    warnings++;

    if (line >= 0) {
      if (column >= 0)
        showPosition(file, line, column);
      else 
        showPosition(file, line);
    }
  }

  
  /**
   * print error message (string)
   *
   * @param message  the message to print
   */
  public static void error(String message) {
    errors++;
    err(NL+message);
  }


  /**
   * print error message (code)
   *  
   * @param message  the code of the error message
   *
   * @see ErrorMessages   
   */ 
  public static void error(int message) {
    errors++;
    err(NL+"Error: "+messages[message] );
  }


  /**
   * IO error message for a file (displays file 
   * name in parentheses).
   *
   * @param message  the code of the error message
   * @param file     the file it occurred for
   */
  public static void error(int message, File file) {
    errors++;
    err(NL+"Error: "+messages[message]+" ("+file+")");
  }


  /**
   * print error message with location information
   *
   * @param file     the file the error occurred for
   * @param message  the code of the error message to print
   * @param line     the line number of error position
   * @param column   the column of error position
   */
  public static void error(File file, int message, int line, int column) {

    String msg = NL+"Error";
    if (file != null) msg += " in file \""+file+"\"";
    if (line >= 0) msg = msg+" (line "+(line+1)+")";

    try {
      err(msg+": "+NL+messages[message]);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      err(msg);
    }

    errors++;

    if (line >= 0) {
      if (column >= 0)
        showPosition(file, line, column);
      else 
        showPosition(file, line);
    }
  }


  /**
   * prints a line of a file with marked position.
   *
   * @param file    the file of which to show the line
   * @param line    the line to show
   * @param column  the column in which to show the marker
   */
  public static void showPosition(File file, int line, int column) {
    try {
      String ln = getLine(file, line);
      if (ln != null) {
        err( ln );

        if (column < 0) return;
        
        String t = "^";  
        for (int i = 0; i < column; i++) t = " "+t;  
        
        err(t);
      }
    }
    catch (IOException e) {
      /* silently ignore IO errors, don't show anything */
    }
  }


  /**
   * print a line of a file
   *
   * @param file  the file to show
   * @param line  the line number 
   */
  public static void showPosition(File file, int line) {
    try {
      String ln = getLine(file, line);
      if (ln != null) err(ln);
    }
    catch (IOException e) {
      /* silently ignore IO errors, don't show anything */
    }
  }


  /**
   * get one line from a file 
   *
   * @param file   the file to read
   * @param line   the line number to get
   *
   * @throw IOException  if any error occurs
   */
  private static String getLine(File file, int line) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));

    String msg = "";

    for (int i = 0; i <= line; i++)
      msg = reader.readLine();

    reader.close();
    
    return msg;
  }


  /**
   * Print system information (e.g. in case of unexpected exceptions)
   */
  public static void printSystemInfo() {
    err("Java version:  "+System.getProperty("java.version"));
    err("Runtime name:  "+System.getProperty("java.runtime.name"));
    err("Vendor:        "+System.getProperty("java.vendor")); 
    err("VM version:    "+System.getProperty("java.vm.version")); 
    err("VM vendor:     "+System.getProperty("java.vm.vendor"));
    err("VM name:       "+System.getProperty("java.vm.name"));
    err("VM info:       "+System.getProperty("java.vm.info"));
    err("OS name:       "+System.getProperty("os.name"));
    err("OS arch:       "+System.getProperty("os.arch"));
    err("OS version:    "+System.getProperty("os.version"));
    err("Encoding:      "+System.getProperty("file.encoding"));
    err("JFlex version: "+Main.version);
  }


  /**
   * Request a bug report for an unexpected Exception/Error.
   */
  public static void requestBugReport(Error e) {
    err("An unexpected error occurred. Please send a report of this to");
    err("<bugs@jflex.de> and include the following information:");
    err("");
    printSystemInfo();
    err("Exception:");
    e.printStackTrace(out);
    err("");
    err("Please also include a specification (as small as possible)");
    err("that triggered this error. You may also want to check at");
    err("http://www.jflex.de if there is a newer version available");
    err("that doesn't have this problem");
    err("");
    err("Thanks for your support.");
  }
}
