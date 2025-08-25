package org.opennars.operator.mental;

import org.opennars.entity.Task;
import org.opennars.interfaces.Timable;
import org.opennars.language.Term;
import org.opennars.operator.NullOperator;
import org.opennars.operator.Operation;
import org.opennars.operator.Operator;
import org.opennars.storage.Memory;

import java.util.List;

/**
 * Register a new operator when the system is running
 */
public class Register extends Operator {

    public Register() {
        super("^register");
    }

    /**
     * To register a new operator
     *
     * @param args   Arguments, a Statement followed by an optional tense
     * @param memory The memory in which the operation is executed
     * @return Immediate results as Tasks
     */
    @Override
    protected List<Task> execute(final Operation operation, final Term[] args, final Memory memory,
            final Timable time) {
        final Operator op = new NullOperator(args[1].toString());
        memory.addOperator(op); // add error checking
        return null;
    }

}
