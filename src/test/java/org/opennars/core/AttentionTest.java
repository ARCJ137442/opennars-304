package org.opennars.core;

import com.google.common.collect.Iterables;
import org.junit.Test;
import org.opennars.entity.Concept;
import org.opennars.main.Nar;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Checks for invariants of Concepts
 */
// TODO run this for each different kind of attention/bag etc
public class AttentionTest {

    @Test
    public void testSampleNextConcept()
            throws IOException, InstantiationException, InvocationTargetException, NoSuchMethodException,
            ParserConfigurationException, IllegalAccessException, SAXException, ClassNotFoundException, ParseException {

        final int numConcepts = 32;
        final Nar n = new Nar();
        for (int i = 0; i < numConcepts; i++)
            n.addInput("<x" + i + " <-> x" + (i + 1) + ">.");

        n.cycles(100);

        final int c = Iterables.size(n.memory.concepts);
        assertTrue(c > 32);

        final Set<Concept> uniqueconcepts = new LinkedHashSet();

        for (int i = 0; i < numConcepts; i++) {
            final Concept s = n.memory.concepts.takeOut();
            n.memory.concepts.putIn(s);
            uniqueconcepts.add(s);
        }

        assertTrue(uniqueconcepts.size() > 1);

        final int c2 = Iterables.size(n.memory.concepts);
        assertEquals("does not affect # of concepts", c, c2);
    }

}
