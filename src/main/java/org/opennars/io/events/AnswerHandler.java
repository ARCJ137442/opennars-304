package org.opennars.io.events;

import org.opennars.entity.Sentence;
import org.opennars.entity.Task;
import org.opennars.io.events.EventEmitter.EventObserver;
import org.opennars.io.events.Events.Answer;
import org.opennars.main.Nar;

/**
 *
 */
public abstract class AnswerHandler implements EventObserver {

    private Task question;
    private Nar nar;

    final static Class[] events = new Class[] {
            Answer.class
    };

    public void start(final Task question, final Nar n) {
        this.nar = n;
        this.question = question;

        nar.event(this, true, events);
    }

    public void off() {
        nar.event(this, false, events);
    }

    @Override
    public void event(final Class event, final Object[] args) {

        if (event == Answer.class) {
            final Task task = (Task) args[0];
            final Sentence belief = (Sentence) args[1];
            if (task.equals(question)) {
                onSolution(belief);
            }
        }
    }

    /** called when the question task has been solved directly */
    abstract public void onSolution(Sentence belief);
}
