package org.tyco.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Removes all the paths from an output file. (to be used in the automatic
 * tests)
 * 
 * @author Paulo Rafael
 */
public class CleanPath {

	/**
	 * Removes all the paths from an output file. Searches the file line by line
	 * and removes everything that's back from the last "/" or "\", this only
	 * if the line contains .tyc.
	 * 
	 * @param args
	 *            The file to clean.
	 */
	public static void main(String[] args) {

		if (args.length < 1)
			System.out.println("Please supply a file.");
		else
			try {
				FileInputStream fInStream = new FileInputStream(args[0]);
				BufferedReader in
		          = new BufferedReader(new InputStreamReader(fInStream));

				String result = "";
				String currentLine = in.readLine();
				while (currentLine != null) {
					currentLine = currentLine.replace('\\', '/');
					
					int dottyc = currentLine.lastIndexOf(".tyc");
					int lastSlash = currentLine.lastIndexOf("/", dottyc);

					if (lastSlash != -1)
						result += currentLine.substring(lastSlash + 1);
					else
						result += currentLine;
					result += "\n";
					currentLine = in.readLine();
				}
				in.close();

				// write the new contents to file.
				FileOutputStream fOutstream = new FileOutputStream(args[0]);
				fOutstream.write(result.getBytes());

			} catch (FileNotFoundException e1) {
				System.err.println("Please supply an existing file.");
			} catch (IOException e) {
				System.err.println("Error getting file contents.");
			}
	}
}