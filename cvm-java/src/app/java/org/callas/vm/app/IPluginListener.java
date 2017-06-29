package org.callas.vm.app;

/**
 * The plugin listener is invoked after loading all plugins so that a plugin may
 * connect itself with its peers.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @since Feb 2, 2011
 */
public interface IPluginListener {
	/**
	 * Invoked after all plugins are loaded.
	 * 
	 * @param ctx
	 *            Have access to all the available plugins.
	 * @throws PluginRegistryException
	 *             Invoked when this plugin cannot start because of some
	 *             dependency.
	 */
	void handlePluginContext(IPluginContext ctx) throws PluginRegistryException;
}
