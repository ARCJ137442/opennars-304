package org.opennars.language;

import org.opennars.io.Symbols.NativeOperator;
import org.opennars.main.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 * A disjunction of Statements as defined in the NARS-theory
 *
 * @author Pei Wang
 * @author Patrick Hammer
 */
public class Disjunction extends CompoundTerm {

    /**
     * Constructor with partial values, called by make
     *
     * @param arg The component list of the term
     */
    private Disjunction(final Term[] arg) {
        super(arg);

        if (Debug.DETAILED) {
            Terms.verifySortedAndUnique(arg, false);
        }

        init(arg);
    }

    /**
     * Clone an object
     *
     * @return A new object
     */
    @Override
    public Disjunction clone() {
        return new Disjunction(term);
    }

    @Override
    public Term clone(final Term[] x) {
        if (x == null) {
            return null;
        }
        return make(x);
    }

    /**
     * Try to make a new Disjunction from two term. Called by the inference rules.
     *
     * @param term1 The first component
     * @param term2 The first component
     * @return A Disjunction generated or a Term it reduced to
     */
    public static Term make(final Term term1, final Term term2) {
        final List<Term> set = new ArrayList<>();
        if (term1 instanceof Disjunction) {
            set.addAll(((CompoundTerm) term1).asTermList());
            if (term2 instanceof Disjunction) {
                // (&,(&,P,Q),(&,R,S)) = (&,P,Q,R,S)
                set.addAll(((CompoundTerm) term2).asTermList());
            } else {
                // (&,(&,P,Q),R) = (&,P,Q,R)
                set.add(term2);
            }
        } else if (term2 instanceof Disjunction) {
            // (&,R,(&,P,Q)) = (&,P,Q,R)
            set.addAll(((CompoundTerm) term2).asTermList());
            set.add(term1);
        } else {
            set.add(term1);
            set.add(term2);
        }
        return make(set.toArray(new Term[0]));
    }

    public static Term make(Term[] t) {
        t = Term.toSortedSetArray(t);

        if (t.length == 0)
            return null;
        if (t.length == 1) {
            // special case: single component
            return t[0];
        }

        return new Disjunction(t);
    }

    /**
     * Get the operator of the term.
     *
     * @return the operator of the term
     */
    @Override
    public NativeOperator operator() {
        return NativeOperator.DISJUNCTION;
    }

    /**
     * Disjunction is commutative.
     *
     * @return true for commutative
     */
    @Override
    public boolean isCommutative() {
        return true;
    }
}
