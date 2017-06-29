 package org.callas.callas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.callas.absyn.NetworkFile;
import org.tyco.common.api.IParser;
import org.tyco.common.errorMsg.*;

/**
 * This is the default implementation of the IValidator.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 17, 2008
 *
 */
public class ValidatorImpl implements IValidator {

	public Result validateDefault(String filename) throws FileNotFoundException {
		CallasAPI api = new CallasAPI();
		IParser<NetworkFile> parser = api.createNetworkFileParser();
		// the generated messages
		List<SourceMessage> msgs = new LinkedList<SourceMessage>();
		
		try {
			NetworkFile netFile = parser.parse(filename, new FileInputStream(filename));
			api.createFullTypechecker().typecheck(netFile);
		} catch (ErrorMessagesException e) {
			for (ErrorMessage msg : e.getErrors()) {
				String msgFilename = filename;
				// get the title
				String title = msg.toString();
				
				// Capture the message details
				StringPrinter stringPrinter = new StringPrinter();
				IPrinter<Object> printer = api.createPrinter(stringPrinter);
				// generate the summary
				msg.printDetails(printer);
				String summary = stringPrinter.toString();

				SourceLocation where = null;
				if (msg.what instanceof SourceLocation) {
					where = (SourceLocation) msg.what;
				} else if (msg.what instanceof ISourceLocationHolder) {
					where = ((ISourceLocationHolder) msg.what).getSourceLocation();
				}
				
				int col, line;
				if (where != null) {
					col = where.col;
					line = where.line;
					msgFilename = where.filename;
				} else {
					col = line = -1;
				}
				
				// construct and append the message
				msgs.add(new SourceMessage(title, summary, msgFilename, col, line, Level.SEVERE));
			}
		}
		return new Result(msgs.isEmpty(), msgs.toArray(new SourceMessage[0]));
	}

}
