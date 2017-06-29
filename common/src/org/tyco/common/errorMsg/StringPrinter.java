package org.tyco.common.errorMsg;

/**
 * Prints the contents into a string, which may be retreived by calling
 * toString.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 31, 2008
 * 
 */
public class StringPrinter implements IPrinter<String> {

	/**
	 * The repository that will hold the printed data.
	 */
	private StringBuilder builder = new StringBuilder();

	public void print(String obj) {
		builder.append(obj);
	}

	/**
	 * Clears the contents.
	 */
	public void reset() {
		builder = new StringBuilder();
	}

	/**
	 * Gets the printed data.
	 */
	@Override
	public String toString() {
		return builder.toString();
	}
}
