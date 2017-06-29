package org.callas.vm.app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PluginLoader implements IPluginContext {
	private Collection<Object> plugins;
	
	/**
	 * @param plugins
	 */
	public PluginLoader(Collection<Object> plugins) {
		this.plugins = plugins;
	}

	public <T> Collection<T> getPluginsFor(Class<T> cls) {
		List<T> result = new ArrayList<T>();
		for (Object plugin : plugins) {
			if (cls.isInstance(plugin)) {
				result.add(cls.cast(plugin));
			}
		}
		return result;
	}
	
	public <T> T getSingleton(Class<T> cls) throws PluginRegistryException {
		Collection<T> result = getPluginsFor(cls);
		if (result.size() > 1) {
			List<String> found = new ArrayList<String>(result.size());
			for (Object obj : result) {
				found.add(obj.getClass().getCanonicalName());
			}
			throw new PluginRegistryException("Expected only one plugin registered, but found " + found);
		}
		for (T plugin : result) {
			return plugin;
		}
		throw new PluginRegistryException("No plugin is registered.");
	}
}
