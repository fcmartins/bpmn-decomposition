package org.callas.vm.app;

/**
 * Implement this interface when you want your plugin to react to the
 * interpreter plugin. You can inspect its properties via
 * <code>handleInterpreterContext</code>, execute something when the interpreter
 * starts, and execute something when the interpreter shuts down.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @since Feb 2, 2011
 */
public interface IInterpreterListener {
	/**
	 * Inspect the interpreter's properties.
	 * @param ctx
	 */
	void handleInterpreterContext(IInterpreter ctx);
	/**
	 * Invoked before the interpreter starts.
	 */
	void onInterpreterStart();
	/**
	 * Invoked when the interpreter terminates.
	 */
	void onInterpreterShutdown();
}
