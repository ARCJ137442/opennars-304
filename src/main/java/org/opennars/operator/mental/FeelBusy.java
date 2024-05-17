package org.opennars.operator.mental;

import org.opennars.entity.Task;
import org.opennars.interfaces.Timable;
import org.opennars.language.Term;
import org.opennars.operator.Operation;
import org.opennars.storage.Memory;

import java.util.List;

/**
 * Feeling busy value
 */
public class FeelBusy extends Feel {

    public FeelBusy() {
        super("^feelBusy");
    }

    /**
     * To get the current value of an internal sensor
     * 
     * @param args   Arguments, a set and a variable
     * @param memory The memory in which the operation is executed
     * @return Immediate results as Tasks
     */
    @Override
    protected List<Task> execute(final Operation operation, final Term[] args, final Memory memory,
            final Timable time) {
        if (memory.emotion == null) {
            return null;
        }
        return feeling(memory.emotion.busy(), memory, time);
    }
}
