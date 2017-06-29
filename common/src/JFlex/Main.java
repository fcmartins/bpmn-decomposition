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
import java.util.*;
import JFlex.gui.MainFrame;


/**
 * This is the main class of JFlex controlling the scanner generation process. 
 * It is responsible for parsing the commandline, getting input files,
 * starting up the GUI if necessary, etc. 
 *
 * @author Gerwin Klein
 * @version JFlex 1.3.5, $Revision: 1.1 $, $Date: 2004/01/13 11:16:57 $
 */
public class Main implements ErrorMessages {
  
  /** JFlex version */
  final public static String version = "1.3.5";

  /** code generation method: maximum packing */
  final public static int PACK   = 0;
  /** code generation method: traditional */
  final public static int TABLE  = 1;
  /** code generation method: switch statement */
  final public static int SWITCH = 2;


  /* global switches: */
    
  /** don't run minimization algorithm if this is true */
  public static boolean no_minimize = false;
  
  /** don't write backup files if this is true */
  public static boolean no_backup = false;
     
  /** default code generation method */
  public static int gen_method = PACK;


  /**
   * Generates a scanner for the specified input file.
   *
   * @param inputFile  a file containing a lexical specification
   *                   to generate a scanner for.
   */
  public static void generate(File inputFile) {

    Out.resetCounters();

    Timer totalTime = new Timer();
    Timer time      = new Timer();
      
    LexScan scanner = null;
    LexParse parser = null;
    FileReader inputReader = null;
    
    totalTime.start();      

    try {  
      Out.println("Reading \""+inputFile+"\"");
      inputReader = new FileReader(inputFile);
      scanner = new LexScan(inputReader);
      scanner.setFile(inputFile);
      parser = new LexParse(scanner);
    }
    catch (FileNotFoundException e) {
      Out.error("Sorry, couldn't find the file \""+inputFile+"\"");
      throw new GeneratorException();
    }
    catch (IOException e) {
      Out.error("Sorry, error opening input file \""+inputFile+"\"");
      throw new GeneratorException();
    }
      
    try {  
      NFA nfa = (NFA) parser.parse().value;      

      Out.checkErrors();

      if (Out.DUMP) Out.dump("NFA is"+Out.NL+nfa+Out.NL);
      
      if (Out.DOT) 
        nfa.writeDot(Emitter.normalize("nfa.dot", null, null));      

      Out.println(nfa.numStates+" states in NFA");
      
      time.start();
      DFA dfa = nfa.getDFA();
      time.stop();
      Out.time("DFA construction took "+time);

      dfa.checkActions(scanner, parser);

      nfa = null;

      if (Out.DUMP) Out.dump("DFA is"+Out.NL+dfa+Out.NL);      

      if (Out.DOT) 
        dfa.writeDot(Emitter.normalize("dfa-big.dot", null, null));

      time.start();
      dfa.minimize();
      time.stop();

      Out.time("Minimization took "+time);
      
      if (Out.DUMP) Out.dump("Miniminal DFA is"+Out.NL+dfa);

      if (Out.DOT) 
        dfa.writeDot(Emitter.normalize("dfa-min.dot", null, null));

      time.start();
      
      Emitter e = new Emitter(inputFile, parser, dfa);
      e.emit();

      time.stop();

      Out.time("Writing took "+time);
      
      totalTime.stop();
      
      Out.time("Overall scanner generation time : "+totalTime);
    }
    catch (ScannerException e) {
      Out.error(e.file, e.message, e.line, e.column);
      throw new GeneratorException();
    }
    catch (MacroException e) {
      Out.error(e.getMessage());
      throw new GeneratorException();
    }
    catch (IOException e) {
      Out.error("An I/O-Error occured : "+e);
      throw new GeneratorException();
    }
    catch (OutOfMemoryError e) {
      Out.error(OUT_OF_MEMORY);
      throw new GeneratorException();
    }
    catch (GeneratorException e) {
      throw new GeneratorException();
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new GeneratorException();
    }

  }

  public static void setDir(String dirName) {
    File d = new File(dirName);

    if ( d.isFile() ) {
      Out.error("Error: \""+d+"\" is not a directory.");
      throw new GeneratorException();
    }
    
    if ( !d.isDirectory() && !d.mkdirs() ) {
      Out.error("Error: couldn't create directory \""+d+"\"");
      throw new GeneratorException();
    }

    Emitter.directory = d;
  }

  public static Vector parseOptions(String argv[]) {
    Vector files = new Vector();

    for (int i = 0; i < argv.length; i++) {

      if ( argv[i].equals("-d") ) {
        if ( ++i >= argv.length ) {
          Out.error(NO_DIRECTORY); 
          System.exit(1);
        }
        setDir(argv[i]);
        continue;
      }

      if ( argv[i].equals("--skel") || argv[i].equals("-skel") ) {
        if ( ++i >= argv.length ) {
          Out.error(NO_SKEL_FILE);
          System.exit(1);
        }

        File skel = new File(argv[i]);
        if ( !skel.isFile() || !skel.canRead() ) {
          Out.error("Error: couldn't open \""+skel+"\".");
          System.exit(1);
        }
        
        Skeleton.readSkelFile(skel);

        continue;
      }

      if ( argv[i].equals("-v") || argv[i].equals("--verbose") || argv[i].equals("-verbose") ) {
        Out.VERBOSE = true;
        continue;
      }

      if ( argv[i].equals("-q") || argv[i].equals("--quiet") || argv[i].equals("-quiet") ) {
        Out.VERBOSE = false;
        continue;
      }

      if ( argv[i].equals("--dump") || argv[i].equals("-dump") ) {
        Out.DUMP = true;
        continue;
      }

      if ( argv[i].equals("--time") || argv[i].equals("-time") ) {
        Out.TIME = true;
        continue;
      }

      if ( argv[i].equals("--version") || argv[i].equals("-version") ) {
        Out.println("This is JFlex "+version);
        System.exit(0);
      }

      if ( argv[i].equals("--dot") || argv[i].equals("-dot") ) {
        Out.DOT = true;
        continue;
      }

      if ( argv[i].equals("--help") || argv[i].equals("-h") || argv[i].equals("/h") ) {
        printUsage();
        System.exit(0);
      }

      if ( argv[i].equals("--info") || argv[i].equals("-info") ) {
        Out.printSystemInfo();
        System.exit(0);
      }
      
      if ( argv[i].equals("--nomin") || argv[i].equals("-nomin") ) {
        no_minimize = true;
        continue;
      }

      if ( argv[i].equals("--pack") || argv[i].equals("-pack") ) {
        gen_method = PACK;
        continue;
      }

      if ( argv[i].equals("--table") || argv[i].equals("-table") ) {
        gen_method = TABLE;
        continue;
      }

      if ( argv[i].equals("--switch") || argv[i].equals("-switch") ) {
        gen_method = SWITCH;
        continue;
      }
      
      if ( argv[i].equals("--nobak") || argv[i].equals("-nobak") ) {
        no_backup = true;
        continue;
      }
      
      if ( argv[i].startsWith("-") ) {
        Out.error("Error: unknown option \""+argv[i]+"\"");
        printUsage();
        System.exit(1);
      }

      // if argv[i] is not an option, try to read it as file 
      File f = new File(argv[i]);
      if ( f.isFile() && f.canRead() ) 
        files.addElement(f);      
      else {
        Out.error("Sorry, couldn't open \""+f+"\"");
        throw new GeneratorException();
      }
    }

    return files;
  }


  public static void printUsage() {
    Out.println("");
    Out.println("Usage: jflex <options> <input-files>");
    Out.println("");
    Out.println("Where <options> can be one or more of");
    Out.println("-d <directory>   write generated file to <directory>");
    Out.println("--skel <file>    use external skeleton <file>");
    Out.println("--switch");
    Out.println("--table");
    Out.println("--pack           set default code generation method");
    Out.println("--nomin          skip minimization step");
    Out.println("--nobak          don't create backup files");
    Out.println("--dump           display transition tables"); 
    Out.println("--dot            write graphviz .dot files for the generated automata (alpha)");
    Out.println("--verbose");
    Out.println("-v               display generation progress messages (default)");
    Out.println("--quiet");
    Out.println("-q               display errors only");
    Out.println("--time           display generation time statistics");
    Out.println("--version        print the version number of this copy of jflex");
    Out.println("--info           print system + JDK information");
    Out.println("--help");
    Out.println("-h               print this message");
    Out.println("");
    Out.println("This is JFlex "+version);
    Out.println("Have a nice day!");
  }


  public static void generate(String argv[]) {
    Vector files = parseOptions(argv);

    if (files.size() > 0) {
      for (int i = 0; i < files.size(); i++) 
        generate((File) files.elementAt(i));          
    }
    else {
      new MainFrame();
    }    
  }


  /**
   * Starts the generation process with the files in <code>argv</code> or
   * pops up a window to choose a file, when <code>argv</code> doesn't have
   * any file entries.
   *
   * @param argv the commandline.
   */
  public static void main(String argv[]) {
    try {
      generate(argv);
    }
    catch (GeneratorException e) {
      Out.statistics();
      System.exit(1);
    }
  }
}
