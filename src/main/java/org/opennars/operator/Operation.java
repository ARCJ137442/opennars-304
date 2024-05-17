package org.opennars.operator;

import org.opennars.entity.Task;
import org.opennars.io.Symbols;
import org.opennars.language.Inheritance;
import org.opennars.language.Product;
import org.opennars.language.Term;

import static org.opennars.io.Symbols.NativeOperator.COMPOUND_TERM_CLOSER;
import static org.opennars.io.Symbols.NativeOperator.COMPOUND_TERM_OPENER;

/**
 * An operation is interpreted as an Inheritance relation.
 */
public class Operation extends Inheritance {
    private Task task;
    public final static Term[] SELF_TERM_ARRAY = new Term[] { SELF };

    /**
     * Constructor with partial values, called by make
     *
     */
    protected Operation(final Term argProduct, final Term operator) {
        super(argProduct, operator);
    }

    protected Operation(final Term[] t) {
        super(t);
    }

    /**
     * Clone an object
     *
     * @return A new object, to be casted into a SetExt
     */
    @Override
    public Operation clone() {
        return new Operation(term);
    }

    /**
     * Try to make a new compound from two components. Called by the inference
     * rules.
     *
     * @param addSelf include SELF term at end of product terms
     * @return A compound generated or null
     */
    public static Operation make(final Operator oper, final Term[] arg, final boolean addSelf) {
        return new Operation(new Product(arg), oper);
    }

    public Operator getOperator() {
        return (Operator) getPredicate();
    }

    @Override
    protected CharSequence makeName() {
        if (getSubject() instanceof Product && getPredicate() instanceof Operator)
            return makeName(getPredicate().name(), ((Product) getSubject()).term);
        return makeStatementName(getSubject(), Symbols.NativeOperator.INHERITANCE, getPredicate());
    }

    public static CharSequence makeName(final CharSequence op, final Term[] arg) {
        final StringBuilder nameBuilder = new StringBuilder(16) // estimate
                .append(COMPOUND_TERM_OPENER.ch).append(op);

        int n = 0;
        for (final Term t : arg) {
            nameBuilder.append(Symbols.ARGUMENT_SEPARATOR);
            nameBuilder.append(t.name());
            n++;
        }

        nameBuilder.append(COMPOUND_TERM_CLOSER.ch);
        return nameBuilder.toString();
    }

    /**
     * stores the currently executed task, which can be accessed by Operator
     * execution
     */
    public void setTask(final Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public Product getArguments() {
        return (Product) getSubject();
    }

}
