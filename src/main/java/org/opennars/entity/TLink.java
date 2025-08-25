package org.opennars.entity;

/**
 * @author Pei Wang
 * @author Patrick Hammer
 */
public interface TLink<T> {

    short getIndex(final int i);

    T getTarget();

    float getPriority();

}
