package org.opennars.operator;

import com.google.common.collect.Lists;
import org.opennars.entity.*;
import org.opennars.interfaces.Timable;
import org.opennars.io.Symbols;
import org.opennars.language.*;
import org.opennars.storage.Memory;

import java.util.List;

import static org.opennars.inference.BudgetFunctions.truthToQuality;

/**
 * Superclass of functions that execute synchronously (blocking, in thread) and
 * take
 * N input parameters and one variable argument (as the final argument),
 * generating a new task
 * with the result of the function substituted in the variable's place.
 */
public abstract class FunctionOperator extends Operator {

    protected FunctionOperator(final String name) {
        super(name);
    }

    /**
     * y = function(x)
     * 
     * @return y, or null if unsuccessful
     */
    abstract protected Term function(Memory memory, Term[] x);

    /**
     * the term that the output will inherit from; analogous to the 'Range' of a
     * function in mathematical terminology
     */
    @Deprecated
    abstract protected Term getRange();

    // abstract protected int getMinArity();
    // abstract protected int getMaxArity();

    @Override
    protected List<Task> execute(Operation operation, final Term[] args, final Memory m, final Timable time) {
        // TODO make memory access optional by constructor argument
        // TODO allow access to Nar instance?
        final int numArgs = args.length - 1;

        if (numArgs < 1) {
            throw new IllegalStateException("Requires at least 1 arguments");
        }

        if (numArgs < 2 /* && !(this instanceof Javascript) */) {
            throw new IllegalStateException("Requires at least 2 arguments");
        }

        // last argument a variable?
        // final Term lastTerm = args[numArgs];
        // final boolean variable = lastTerm instanceof Variable;

        final int numParam = numArgs - 1;

        /*
         * if(this instanceof Javascript && !variable) {
         * numParam++;
         * }
         */

        final Term[] x = new Term[numParam];
        System.arraycopy(args, 1, x, 0, numParam);

        final Term y;
        // try {
        y = function(m, x);
        if (y == null) {
            return null;
        }
        /*
         * if(!variable && this instanceof Javascript) {
         * return null;
         * }
         */
        // m.emit(SynchronousFunctionOperator.class, Arrays.toString(x) + " | " + y);
        /*
         * }
         * catch (Exception e) {
         * throw e;
         * }
         */

        // final Variable var = new Variable("$1");
        // Term actual_part = Similarity.make(var, y);
        // Variable vardep=new Variable("#1");
        // Term actual_dep_part = Similarity.make(vardep, y);
        operation = (Operation) operation.setComponent(0,
                ((CompoundTerm) operation.getSubject()).setComponent(
                        numArgs, y, m),
                m);
        final float confidence = m.narParameters.DEFAULT_JUDGMENT_CONFIDENCE;
        final Sentence s = new Sentence(operation,
                Symbols.JUDGMENT_MARK,
                new TruthValue(1.0f, confidence, m.narParameters),
                new Stamp(time, m));
        final BudgetValue budgetForNewTask = new BudgetValue(m.narParameters.DEFAULT_JUDGMENT_PRIORITY,
                m.narParameters.DEFAULT_FEEDBACK_DURABILITY,
                truthToQuality(s.getTruth()), m.narParameters);
        final Task newTask = new Task(s, budgetForNewTask, Task.EnumType.INPUT);
        return Lists.newArrayList(newTask);
    }

    /**
     * (can be overridden in subclasses) the extent to which it is truth
     * that the 2 given terms are equal. in other words, a distance metric
     */
    public float equals(final Term a, final Term b) {
        // default: Term equality
        return a.equals(b) ? 1.0f : 0.0f;
    }
}
