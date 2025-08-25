package org.opennars.interfaces;

/**
 * Implementation can work with cycles and stopped
 *
 * @author Robert Wünsche
 */
public interface Multistepable {
    void start(final long minCyclePeriodMS);

    void start();

    void stop();

    void cycles(final int cycles);

    void cycle();
}
