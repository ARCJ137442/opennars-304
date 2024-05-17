package org.opennars.interfaces;

/**
 * Something which can consume files
 *
 * @author Robert Wünsche
 */
public interface InputFileConsumer {
    /**
     * consumes a file
     *
     * @param filename optional path followed by filename
     */
    void addInputFile(final String filename);
}
