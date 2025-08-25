package org.opennars.operator;

import org.opennars.entity.Task;
import org.opennars.interfaces.Timable;
import org.opennars.language.Term;
import org.opennars.main.Debug;
import org.opennars.storage.Memory;

import java.util.List;

/**
 * A class used as a template for Operator definition.
 */
public class NullOperator extends Operator {

    public NullOperator() {
        this("^sample");
    }

    public NullOperator(final String name) {
        super(name);
    }

    /** called from Operator */
    @Override
    protected List<Task> execute(final Operation operation, final Term[] args, final Memory memory,
            final Timable time) {
        if (Debug.DETAILED) {
            memory.emit(getClass(), (Object[]) args);
        }
        return null;
    }

}
