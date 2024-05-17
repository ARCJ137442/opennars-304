package org.opennars.operator.mental;

import com.google.common.collect.Lists;
import org.opennars.entity.*;
import org.opennars.inference.BudgetFunctions;
import org.opennars.interfaces.Timable;
import org.opennars.io.Symbols;
import org.opennars.language.Term;
import org.opennars.operator.Operation;
import org.opennars.operator.Operator;
import org.opennars.storage.Memory;

import java.util.List;

/**
 * Operator that creates a judgment with a given statement
 * Causes the system to belief things it has no evidence for
 */
public class Believe extends Operator {

    public Believe() {
        super("^believe");
    }

    /**
     * To create a judgment with a given statement
     * 
     * @param args   Arguments, a Statement followed by an optional tense
     * @param memory The memory in which the operation is executed
     *               + * @return Immediate results as Tasks
     */
    @Override
    protected List<Task> execute(final Operation operation, final Term[] args, final Memory memory,
            final Timable time) {

        final Term content = args[1];

        final TruthValue truth = TruthValue.fromWordTerm(memory.narParameters, args[2]);
        final Sentence sentence = new Sentence(
                content,
                Symbols.JUDGMENT_MARK,
                truth,
                new Stamp(time, memory));

        final float quality = BudgetFunctions.truthToQuality(truth);
        final BudgetValue budget = new BudgetValue(memory.narParameters.DEFAULT_JUDGMENT_PRIORITY,
                memory.narParameters.DEFAULT_JUDGMENT_DURABILITY, quality, memory.narParameters);

        final Task newTask = new Task(sentence, budget, Task.EnumType.INPUT);

        return Lists.newArrayList(newTask);

    }
}
