package org.opennars.operator.misc;

import org.apache.commons.lang3.StringUtils;
import org.opennars.language.Term;
import org.opennars.operator.FunctionOperator;
import org.opennars.storage.Memory;

/**
 * Count the number of elements in a set
 */
public class Add extends FunctionOperator {

    public Add() {
        super("^add");
    }

    @Override
    protected Term function(final Memory memory, final Term[] x) {
        if (x.length != 2) {
            throw new IllegalStateException("Requires 2 arguments");
        }

        final int n1;
        final int n2;

        if (StringUtils.isNumeric(x[0].name())) {
            n1 = Integer.parseInt(String.valueOf(x[0].name()));
        } else {
            throw new IllegalArgumentException("1st parameter not an integer");
        }

        if (StringUtils.isNumeric((x[1].name()))) {
            n2 = Integer.parseInt(String.valueOf(x[1].name()));
        } else {
            throw new IllegalArgumentException("2nd parameter not an integer");
        }

        return new Term(String.valueOf(n1 + n2));
    }

    @Override
    protected Term getRange() {
        return Term.get("added");
    }

}
