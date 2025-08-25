package org.opennars.language;

import org.opennars.io.Symbols.NativeOperator;

/**
 * A Statement about a Similarity relation as defined in the NARS-theory
 *
 * @author Pei Wang
 * @author Patrick Hammer
 */
public class Similarity extends Statement {

    /**
     * Constructor with partial values, called by make
     * 
     * @param arg The component list of the term
     */
    public Similarity(final Term[] arg) {
        super(arg);

        init(arg);
    }

    public Similarity(final Term subj, final Term pred) {
        this(new Term[] { subj, pred });
    }

    /**
     * Clone an object
     * 
     * @return A new object, to be casted into a Similarity
     */
    @Override
    public Similarity clone() {
        return new Similarity(term);
    }

    @Override
    public Similarity clone(final Term[] replaced) {
        if (replaced == null) {
            return null;
        }
        if (replaced.length != 2)
            return null;
        return make(replaced[0], replaced[1]);
    }

    /**
     * alternate version of make that allows equivalent subject and predicate
     * to be reduced to the common term.
     */
    public static Term makeTerm(final Term subject, final Term predicate) {
        if (subject.equals(predicate))
            return subject;
        return make(subject, predicate);
    }

    /**
     * Try to make a new compound from two term. Called by the inference rules.
     * 
     * @param subject   The first component
     * @param predicate The second component
     * @return A compound generated or null
     */
    public static Similarity make(final Term subject, final Term predicate) {

        if (invalidStatement(subject, predicate)) {
            return null;
        }
        if (subject.compareTo(predicate) > 0) {
            return make(predicate, subject);
        }

        return new Similarity(subject, predicate);
    }

    /**
     * Get the operator of the term.
     * 
     * @return the operator of the term
     */
    @Override
    public NativeOperator operator() {
        return NativeOperator.SIMILARITY;
    }

    /**
     * Check if the compound is commutative.
     * 
     * @return true for commutative
     */
    @Override
    public boolean isCommutative() {
        return true;
    }
}
