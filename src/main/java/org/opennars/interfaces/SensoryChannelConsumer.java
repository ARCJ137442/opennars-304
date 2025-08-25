package org.opennars.interfaces;

import org.opennars.plugin.perception.SensoryChannel;

/**
 * Implementations have sensory channels
 *
 * @author Robert Wünsche
 */
public interface SensoryChannelConsumer {
    /**
     * registers a sensory channel by/for the term
     *
     * @param term    term in narsese
     * @param channel the channel to be registered
     */
    void addSensoryChannel(final String term, final SensoryChannel channel);
}
