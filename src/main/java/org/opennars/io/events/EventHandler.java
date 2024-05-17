package org.opennars.io.events;

import org.opennars.main.Nar;

/**
 */
public abstract class EventHandler implements EventEmitter.EventObserver {
    protected final EventEmitter source;
    protected boolean active = false;
    private final Class[] events;

    public EventHandler(final Nar n, final boolean active, final Class... events) {
        this(n.memory.event, active, events);
    }

    public EventHandler(final EventEmitter source, final boolean active, final Class... events) {
        this.source = source;
        this.events = events;
        setActive(active);
    }

    public void setActive(final boolean b) {
        if (this.active == b)
            return;

        this.active = b;
        source.set(this, b, events);
    }

    public boolean isActive() {
        return active;
    }
}
