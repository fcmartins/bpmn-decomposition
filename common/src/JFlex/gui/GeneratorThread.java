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

package JFlex.gui;

import JFlex.*;

import java.awt.TextArea;
import java.io.File;


/**
 * Low priority thread for code generation (low priority 
 * that gui has time for screen updates)
 *
 * @author Gerwin Klein
 * @version JFlex 1.3.5, $Revision: 1.1 $, $Date: 2004/01/13 11:16:57 $
 */
public class GeneratorThread extends Thread {

  TextArea  messages;
  String    outputDir;
  String    inputFile;
  MainFrame parent;

  public GeneratorThread(MainFrame parent, String inputFile, 
                         TextArea messages, String outputDir) {
    this.parent    = parent;
    this.inputFile = inputFile;
    this.messages  = messages;
    this.outputDir = outputDir;
  }

  public void run() {
    setPriority(MIN_PRIORITY);    
    Out.setGUIMode(messages);
    try {
      Main.setDir(outputDir);
      Main.generate(new File(inputFile));
      Out.statistics();
      parent.generationFinished(true);
    }
    catch (GeneratorException e) {
      Out.statistics();
      parent.generationFinished(false);
    }
  }

}
