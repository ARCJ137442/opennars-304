package org.opennars.language;

import org.opennars.io.Symbols.NativeOperator;

import java.util.Collection;

import static org.opennars.io.Symbols.NativeOperator.SET_EXT_CLOSER;
import static org.opennars.io.Symbols.NativeOperator.SET_EXT_OPENER;

/**
 * An extensionally defined set, which contains one or more instances as defined
 * in the NARS-theory
 *
 * @author Pei Wang
 * @author Patrick Hammer
 */
public class SetExt extends SetTensional {

    /**
     * Constructor with partial values, called by make
     * 
     * @param arg The component list of the term - args must be unique and sorted
     */
    public SetExt(final Term... arg) {
        super(arg);
    }

    /**
     * Clone a SetExt
     * 
     * @return A new object, to be casted into a SetExt
     */
    @Override
    public SetExt clone() {
        return new SetExt(term);
    }

    @Override
    public SetExt clone(final Term[] replaced) {
        if (replaced == null) {
            return null;
        }
        return make(replaced);
    }

    public static SetExt make(Term... t) {
        t = Term.toSortedSetArray(t);
        if (t.length == 0)
            return null;
        return new SetExt(t);
    }

    public static SetExt make(final Collection<Term> l) {
        return make(l.toArray(new Term[0]));
    }

    /**
     * Get the operator of the term.
     * 
     * @return the operator of the term
     */
    @Override
    public NativeOperator operator() {
        return NativeOperator.SET_EXT_OPENER;
    }

    /**
     * Make a String representation of the set, override the default.
     * 
     * @return true for communitative
     */
    @Override
    public CharSequence makeName() {
        return makeSetName(SET_EXT_OPENER.ch, term, SET_EXT_CLOSER.ch);
    }
}
