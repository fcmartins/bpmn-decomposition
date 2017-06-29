/**
 * 
 */
package org.callas.vm.app;

/**
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 *
 */
public class PluginRegistryException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7339979303487688469L;

	/**
	 * 
	 */
	public PluginRegistryException() {
		// OK
	}

	/**
	 * @param message
	 */
	public PluginRegistryException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PluginRegistryException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PluginRegistryException(String message, Throwable cause) {
		super(message, cause);
	}

}
