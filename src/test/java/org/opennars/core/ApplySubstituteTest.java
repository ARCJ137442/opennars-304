package org.opennars.core;

import org.junit.Test;
import org.opennars.io.Narsese;
import org.opennars.language.CompoundTerm;
import org.opennars.language.Term;
import org.opennars.main.Nar;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class ApplySubstituteTest {

    final Nar n = new Nar();
    final Narsese np = new Narsese(n);

    public ApplySubstituteTest()
            throws IOException, InstantiationException, InvocationTargetException, NoSuchMethodException,
            ParserConfigurationException, IllegalAccessException, SAXException, ClassNotFoundException, ParseException {
    }

    @Test
    public void testApplySubstitute() throws Narsese.InvalidInputException {

        final String abS = "<a --> b>";
        final CompoundTerm ab = (CompoundTerm) np.parseTerm(abS);
        final int originalComplexity = ab.getComplexity();

        final String xyS = "<x --> y>";
        final Term xy = np.parseTerm(xyS);

        final Map<Term, Term> h = new LinkedHashMap();
        h.put(np.parseTerm("b"), xy);
        final CompoundTerm c = ab.applySubstituteToCompound(h);

        assertTrue(c.getComplexity() > originalComplexity);

        assertTrue(ab.name().toString().equals(abS)); // ab unmodified

        assertTrue(!c.name().equals(abS)); // c is actually different
        assertTrue(!c.equals(ab));

    }

    @Test
    public void test2() throws Narsese.InvalidInputException, IOException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, ParserConfigurationException, IllegalAccessException,
            SAXException, ClassNotFoundException, ParseException {
        // substituting: <(*,$1) --> num>. with $1 ==> 0
        final Nar n = new Nar();

        final Map<Term, Term> h = new LinkedHashMap();
        h.put(np.parseTerm("$1"), np.parseTerm("0"));
        final CompoundTerm c = ((CompoundTerm) np.parseTerm("<(*,$1) --> num>")).applySubstituteToCompound(h);

        assertTrue(c != null);
    }
}
