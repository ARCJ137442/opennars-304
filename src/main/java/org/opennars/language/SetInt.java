package org.opennars.language;

import org.opennars.io.Symbols.NativeOperator;

import java.util.Collection;

import static org.opennars.io.Symbols.NativeOperator.SET_INT_CLOSER;
import static org.opennars.io.Symbols.NativeOperator.SET_INT_OPENER;

/**
 * An intensionally defined set, which contains one or more instances defining
 * the Term.
 *
 * @author Pei Wang
 * @author Patrick Hammer
 */
public class SetInt extends SetTensional {

    /**
     * Constructor with partial values, called by make
     * 
     * @param arg The component list of the term - args must be unique and sorted
     */
    public SetInt(final Term... arg) {
        super(arg);
    }

    /**
     * Clone a SetInt
     * 
     * @return A new object, to be casted into a SetInt
     */
    @Override
    public SetInt clone() {
        return new SetInt(term);
    }

    @Override
    public SetInt clone(final Term[] replaced) {
        if (replaced == null) {
            return null;
        }
        return make(replaced);
    }

    public static SetInt make(final Collection<Term> l) {
        return make(l.toArray(new Term[0]));
    }

    public static SetInt make(Term... t) {
        t = Term.toSortedSetArray(t);
        if (t.length == 0)
            return null;
        return new SetInt(t);
    }

    /**
     * Get the operator of the term.
     * 
     * @return the operator of the term
     */
    @Override
    public NativeOperator operator() {
        return NativeOperator.SET_INT_OPENER;
    }

    /**
     * Make a String representation of the set, override the default.
     * 
     * @return true for communitative
     */
    @Override
    public CharSequence makeName() {
        return makeSetName(SET_INT_OPENER.ch, term, SET_INT_CLOSER.ch);
    }

}
