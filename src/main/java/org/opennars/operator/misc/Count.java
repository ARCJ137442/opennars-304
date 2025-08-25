package org.opennars.operator.misc;

import org.opennars.language.CompoundTerm;
import org.opennars.language.SetExt;
import org.opennars.language.SetInt;
import org.opennars.language.Term;
import org.opennars.operator.FunctionOperator;
import org.opennars.storage.Memory;

/**
 * Count the number of elements in a set
 * 
 * 
 * 'INVALID
 * (^count,a)!
 * (^count,a,b)!
 * (^count,a,#b)!
 * 
 * 'VALID:
 * (^count,[a,b],#b)!
 * 
 * 
 */
public class Count extends FunctionOperator {

    public Count() {
        super("^count");
    }

    final static String requireMessage = "Requires 1 SetExt or SetInt argument";

    final static Term counted = Term.get("counted");

    @Override
    protected Term function(final Memory memory, final Term[] x) {
        if (x.length != 1) {
            throw new IllegalStateException(requireMessage);
        }

        final Term content = x[0];
        if (!(content instanceof SetExt) && !(content instanceof SetInt)) {
            throw new IllegalStateException(requireMessage);
        }

        final int n = ((CompoundTerm) content).size();
        return Term.get(n);
    }

    @Override
    protected Term getRange() {
        return counted;
    }

}
