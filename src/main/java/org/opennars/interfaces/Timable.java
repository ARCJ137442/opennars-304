package org.opennars.interfaces;

/**
 * Used to dispatch the deferred retrieval of time
 *
 * @author Robert Wünsche
 */
public interface Timable {
    /**
     * return the current time from the clock
     *
     * @return The current time
     */
    long time();
}
