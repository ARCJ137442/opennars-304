package org.opennars.interfaces;

import org.opennars.io.events.EventEmitter;

/**
 * Implementation can observe events
 *
 * @author Robert Wünsche
 */
public interface Eventable {
    void on(final Class c, final EventEmitter.EventObserver o);

    void off(final Class c, final EventEmitter.EventObserver o);

    void event(final EventEmitter.EventObserver e, final boolean enabled, final Class... events);

    void emit(final Class c, final Object... o);
}
