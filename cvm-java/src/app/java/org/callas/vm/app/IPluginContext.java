package org.callas.vm.app;

import java.util.Collection;

/**
 * The plugin context has methods to select available plugins.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @since Feb 2, 2011
 */
public interface IPluginContext {
	/**
	 * Get all the plugins of a given class.
	 * 
	 * @param <T>
	 *            The type of the plugin we are looking for.
	 * @param cls
	 *            The class of the plugin we are looking for.
	 * @return A list of all plugins that are instance of the given class.
	 */
	<T> Collection<T> getPluginsFor(Class<T> cls);

	/**
	 * Utility method for when the user specifically wants only a singleton
	 * class.
	 * 
	 * @param <T>
	 *            There must be exactly one instance of the given class.
	 * @param cls
	 *            There must be exactly one instance of the given class.
	 * @return The plugin that is an instance of the given class.
	 * @throws PluginRegistryException
	 *             No plugins were found or there is more than one.
	 */
	<T> T getSingleton(Class<T> cls) throws PluginRegistryException;
}
