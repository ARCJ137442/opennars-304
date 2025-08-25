package org.opennars.plugin;

import org.opennars.main.Nar;

import java.io.Serializable;

/**
 * Nar plugin interface
 */
public interface Plugin extends Serializable {

    /**
     * called when plugin is activated (enabled = true) / deactivated
     * (enabled=false)
     */
    default boolean setEnabled(Nar n, boolean enabled) {
        return true;
    }

    default CharSequence name() {
        return this.getClass().getSimpleName();
    }
}
