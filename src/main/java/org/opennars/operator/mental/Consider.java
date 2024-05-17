package org.opennars.operator.mental;

import org.opennars.control.DerivationContext;
import org.opennars.control.InferenceControl;
import org.opennars.entity.BudgetValue;
import org.opennars.entity.Concept;
import org.opennars.entity.Task;
import org.opennars.interfaces.Timable;
import org.opennars.language.Term;
import org.opennars.operator.Operation;
import org.opennars.operator.Operator;
import org.opennars.storage.Memory;

import java.util.List;

/**
 * Operator that activates a concept
 */
public class Consider extends Operator {

    public static BudgetValue budgetMentalConcept(final Operation o) {
        return o.getTask().budget.clone();
    }

    public Consider() {
        super("^consider");
    }

    /**
     * To activate a concept as if a question has been asked about it
     *
     * @param args   Arguments, a Statement followed by an optional tense
     * @param memory The memory in which the operation is executed
     * @return Immediate results as Tasks
     */
    @Override
    protected List<Task> execute(final Operation operation, final Term[] args, final Memory memory,
            final Timable time) {
        final Term term = args[1];

        final Concept concept = memory.conceptualize(Consider.budgetMentalConcept(operation), term);

        final DerivationContext cont = new DerivationContext(memory, memory.narParameters, time);
        cont.setCurrentConcept(concept);
        InferenceControl.fireConcept(cont, 1);

        return null;
    }

}
