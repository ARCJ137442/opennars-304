package org.opennars.operator.mental;

import com.google.common.collect.Lists;
import org.opennars.entity.BudgetValue;
import org.opennars.entity.Sentence;
import org.opennars.entity.Stamp;
import org.opennars.entity.Task;
import org.opennars.interfaces.Timable;
import org.opennars.io.Symbols;
import org.opennars.language.Term;
import org.opennars.operator.Operation;
import org.opennars.operator.Operator;
import org.opennars.storage.Memory;

import java.util.List;

/**
 * Operator that creates a question with a given statement
 */
public class Wonder extends Operator {

    public Wonder() {
        super("^wonder");
    }

    /**
     * To create a question with a given statement
     * 
     * @param args   Arguments, a Statement followed by an optional tense
     * @param memory The memory in which the operation is executed
     * @return Immediate results as Tasks
     */
    @Override
    protected List<Task> execute(final Operation operation, final Term[] args, final Memory memory,
            final Timable time) {
        final Term content = args[1];

        final Sentence sentence = new Sentence(
                content,
                Symbols.QUESTION_MARK,
                null,
                new Stamp(time, memory));

        final BudgetValue budget = new BudgetValue(memory.narParameters.DEFAULT_QUESTION_PRIORITY,
                memory.narParameters.DEFAULT_QUESTION_DURABILITY, 1, memory.narParameters);

        final Task newTask = new Task(sentence, budget, Task.EnumType.INPUT);
        return Lists.newArrayList(newTask);
    }

}
