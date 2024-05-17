package org.opennars.language;

import org.opennars.io.Symbols.NativeOperator;
import org.opennars.main.Debug;
import org.opennars.operator.Operation;
import org.opennars.operator.Operator;

import java.util.Arrays;

/**
 * A Statement about an Inheritance relation as defined in the NARS-theory
 *
 * @author Pei Wang
 * @author Patrick Hammer
 */
public class Inheritance extends Statement {

    /**
     * Constructor with partial values, called by make
     * 
     * @param arg The component list of the term
     */
    protected Inheritance(final Term[] arg) {
        super(arg);

        init(arg);
    }

    protected Inheritance(final Term subj, final Term pred) {
        this(new Term[] { subj, pred });
    }

    /**
     * Clone an object
     * 
     * @return A new object, to be casted into a SetExt
     */
    @Override
    public Inheritance clone() {
        return make(getSubject(), getPredicate());
    }

    @Override
    public Inheritance clone(final Term[] t) {
        if (t == null) {
            return null;
        }
        if (t.length != 2)
            throw new IllegalArgumentException(
                    "Invalid terms for " + getClass().getSimpleName() + ": " + Arrays.toString(t));

        return make(t[0], t[1]);
    }

    /**
     * alternate version of Inheritance.make that allows equivalent subject and
     * predicate
     * to be reduced to the common term.
     */
    public static Term makeTerm(final Term subject, final Term predicate) {
        return make(subject, predicate);
    }

    /**
     * Try to make a new compound from two term. Called by the inference rules.
     * 
     * @param subject   The first component
     * @param predicate The second component
     * @return A compound generated or null
     */
    public static Inheritance make(final Term subject, final Term predicate) {

        if (subject == null || predicate == null || invalidStatement(subject, predicate)) {
            return null;
        }

        final boolean subjectProduct = subject instanceof Product;
        final boolean predicateOperator = predicate instanceof Operator;

        if (Debug.DETAILED) {
            if (!predicateOperator && predicate.toString().startsWith("^")) {
                throw new IllegalStateException("operator term detected but is not an operator: " + predicate);
            }
        }

        if (subjectProduct && predicateOperator) {
            // name = Operation.makeName(predicate.name(), ((CompoundTerm) subject).term);
            return Operation.make((Operator) predicate, ((CompoundTerm) subject).term, true);
        } else {
            return new Inheritance(subject, predicate);
        }

    }

    /**
     * Get the operator of the term.
     * 
     * @return the operator of the term
     */
    @Override
    public NativeOperator operator() {
        return NativeOperator.INHERITANCE;
    }

}
