package org.opennars.io.events;

import org.opennars.io.events.Events.Answer;
import org.opennars.main.Nar;
import org.opennars.storage.Memory;

/**
 * Output Channel: Implements this and Nar.addOutput(..) to receive output
 * signals on various channels
 *
 */
public abstract class OutputHandler extends EventHandler {

    /** implicitly repeated input (a repetition of all input) */
    public interface IN {
    }

    /** conversational (judgments, questions, etc...) output */
    public interface OUT {
    }

    /** warnings, errors &amp; exceptions */
    public interface ERR {
    }

    /**
     * explicitly repeated input (repetition of the content of input ECHO commands)
     */
    public interface ECHO {
    }

    /** debug statements, enabled through Debug.java */
    public interface DEBUG {
    }

    /** operation execution */
    public interface EXE {
    }

    public static class ANTICIPATE {
    }

    public static class CONFIRM {
    }

    public static class DISAPPOINT {
    }

    public static final Class[] DefaultOutputEvents = new Class[] { IN.class, EXE.class, OUT.class, ERR.class,
            ECHO.class, Answer.class, ANTICIPATE.class, CONFIRM.class, DISAPPOINT.class, DEBUG.class };

    public OutputHandler(final EventEmitter source, final boolean active) {
        super(source, active, DefaultOutputEvents);
    }

    public OutputHandler(final Memory m, final boolean active) {
        this(m.event, active);
    }

    public OutputHandler(final Nar n, final boolean active) {
        this(n.memory.event, active);
    }

    public OutputHandler(final Nar n) {
        this(n, true);
    }

}
