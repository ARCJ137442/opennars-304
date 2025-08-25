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

/**
 * Tests for interval handling integrity
 *
 * @author Patrick Hammer
 */
public class ReplaceIntervalTest {
    // <(*,{SELF},<{(*,fragmentC,fragmentD)} --> compare>,TRUE) =\>
    // (*,{SELF},(&/,<{fragmentC} --> mutate>,+12),TRUE)>. %1.00;0.25%
    @Test
    public void replaceIvalTest() throws Narsese.InvalidInputException, IOException, InstantiationException,
            InvocationTargetException, NoSuchMethodException, ParserConfigurationException,
            IllegalAccessException,
            SAXException, ClassNotFoundException, ParseException {
        Nar nar = new Nar();
        Narsese parser = new Narsese(nar);
        Term ret = parser.parseTerm(
                "<(*,{SELF},<{(*,fragmentC,fragmentD)} --> compare>,TRUE) =\\> (*,{SELF},(&/,<{fragmentC} --> mutate>,+12),TRUE)>");
        CompoundTerm ct = (CompoundTerm) CompoundTerm.replaceIntervals(ret);
        assert (ct.toString().equals(
                "<(*,{SELF},<{(*,fragmentC,fragmentD)} --> compare>,TRUE) =\\> (*,{SELF},(&/,<{fragmentC} --> mutate>,+1),TRUE)>"));
    }
}
