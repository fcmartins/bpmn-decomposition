package org.callas.vm.app.cli;

public class ShowUsageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3418246001851627616L;

	/**
	 * 
	 */
	public ShowUsageException() {
		// Ok
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ShowUsageException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ShowUsageException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ShowUsageException(Throwable cause) {
		super(cause);
	}

}
