package org.opennars.interfaces;

import org.opennars.main.Nar;
import org.opennars.plugin.Plugin;

import java.util.List;

/**
 * Implementation can have plugins
 *
 * @author Robert Wünsche
 */
public interface Pluggable {
    /**
     * adds/registers a plugin
     * 
     * @param plugin plugin to be registered
     */
    void addPlugin(final Plugin plugin);

    /**
     * removes a plugin
     * 
     * @param pluginState plugin to be removed
     */
    void removePlugin(final Nar.PluginState pluginState);

    /**
     * returns all plugins which were added
     * 
     * @return plugins
     */
    List getPlugins();
}
