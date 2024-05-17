package org.opennars.operator.misc;

import org.opennars.language.*;
import org.opennars.operator.FunctionOperator;
import org.opennars.storage.Memory;

/**
 * Produces canonical "Reflective-Narsese" representation of a parameter term
 * 
 * @author me
 */
public class Reflect extends FunctionOperator {

    /*
     * <(*,<(*,good,property) --> inheritance>,(&&,<(*,human,good) -->
     * product>,<(*,(*,human,good),inheritance) --> inheritance>)) --> conjunction>.
     */

    public Reflect() {
        super("^reflect");
    }

    @Override
    protected Term function(final Memory memory, final Term[] x) {

        if (x.length != 1) {
            throw new IllegalStateException("Requires 1 Term argument");
        }

        final Term content = x[0];

        return getMetaTerm(content);
    }

    /**
     * &lt;(*,subject,object) --&gt; predicate&gt;
     * 
     * @param subject   the subject of the relation
     * @param object    the object for the relation
     * @param predicate the predicate of the relation
     */
    public static Term sop(final Term subject, final Term object, final Term predicate) {
        return Inheritance.make(Product.make(getMetaTerm(subject), getMetaTerm(object)), predicate);
    }

    public static Term sop(final Statement s, final String operatorName) {
        return Inheritance.make(Product.make(getMetaTerm(s.getSubject()), getMetaTerm(s.getPredicate())),
                Term.get(operatorName));
    }

    public static Term sop(final Statement s, final Term predicate) {
        return Inheritance.make(Product.make(getMetaTerm(s.getSubject()), getMetaTerm(s.getPredicate())), predicate);
    }

    public static Term sop(final String operatorName, final Term... t) {
        final Term[] m = new Term[t.length];
        int i = 0;
        for (final Term x : t)
            m[i++] = getMetaTerm(x);

        return Inheritance.make(Product.make(m), Term.get(operatorName));
    }

    public static Term getMetaTerm(final Term node) {
        if (!(node instanceof CompoundTerm)) {
            return node;
        }
        final CompoundTerm t = (CompoundTerm) node;
        switch (t.operator()) {
            case INHERITANCE:
                return sop((Inheritance) t, "inheritance");
            case SIMILARITY:
                return sop((Similarity) t, "similarity");
            default:
                return sop(t.operator().toString(), t.term);
        }

    }

    @Override
    protected Term getRange() {
        return Term.get("reflect");
    }

}
