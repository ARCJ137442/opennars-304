package org.opennars.interfaces;

/**
 * Something which can work with Narsese as a string representation
 *
 * @author Robert Wünsche
 */
public interface NarseseConsumer {
    // TODO< split this and refactor to interface which can be used by the parser
    // too >

    /**
     * feeds narsese input to the consumer
     * 
     * @param narsese the narsese text
     */
    void addInput(String narsese);
}
