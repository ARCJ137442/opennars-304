package org.opennars.core;

import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;
import org.opennars.io.Narsese;
import org.opennars.io.Symbols;
import org.opennars.language.CompoundTerm;
import org.opennars.language.Term;
import org.opennars.language.Variables;
import org.opennars.main.Nar;

/**
 * Tests the correct functionality of the unifier
 *
 * @author Patrick Hammer
 */
public class UnificationTest {
    @Test
    public void testUnificationTermination() {
        try {
            Nar nar = new Nar();
            Narsese parser = new Narsese(nar);
            CompoundTerm t1 = (CompoundTerm) parser.parseTerm(
                    "<(&&,$1#1$,$2,$4$3$,(#,$2,$1#1$,$4$3$)) ==> <(*,$1#1$,(*,(/,REPRESENT,$2,_),(/,REPRESENT,$4$3$,_))) --> REPRESENT>>");
            CompoundTerm t2 = (CompoundTerm) parser.parseTerm(
                    "<(&&,#1,$2,$3,(#,$2,#1,$3)) ==> <(*,#1,(*,(/,REPRESENT,$2,_),(/,REPRESENT,$3,_))) --> REPRESENT>>");
            Map<Term, Term>[] unifier = new LinkedHashMap[] { new LinkedHashMap<Term, Term>(),
                    new LinkedHashMap<Term, Term>() };
            Variables.findSubstitute(nar.memory.randomNumber, Symbols.VAR_DEPENDENT, t1, t2, unifier);
            // Variables.unify(0, t1, t2, compound)
            // findSubstitute(final char type, final Term term1, final Term term2, final
            // Map<Term, Term>[] map, final boolean allowPartial)
            Variables.findSubstitute(nar.memory.randomNumber, Symbols.VAR_INDEPENDENT, t1, t2, unifier, true);
        } catch (Exception ex) {
            assert (false); // test failed, no matter what happened
        }
    }
}
