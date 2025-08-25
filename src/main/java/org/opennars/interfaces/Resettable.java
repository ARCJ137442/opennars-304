package org.opennars.interfaces;

/**
 * Implementations can be reseted - that is to flush all content and restore the
 * state to some default state
 *
 * @author Robert Wünsche
 */
public interface Resettable {
    /**
     * reset
     */
    void reset();
}
