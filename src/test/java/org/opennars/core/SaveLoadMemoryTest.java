package org.opennars.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.opennars.entity.Concept;
import org.opennars.io.Narsese;
import org.opennars.main.Nar;
import org.xml.sax.SAXException;

/**
 * Tests for the correct functionality of the serialization fo the state
 *
 * @author Patrick Hammer
 */
public class SaveLoadMemoryTest {
    @Test
    public void testloadSaveMem()
            throws IOException, InstantiationException, NoSuchMethodException, SAXException, ParseException,
            IllegalAccessException, InvocationTargetException, ParserConfigurationException, ClassNotFoundException,
            Narsese.InvalidInputException, Exception {
        Nar nar = new Nar();
        nar.addInput("<a --> b>.");
        nar.cycles(1);
        String fname = "test1.nars";
        nar.SaveToFile(fname);
        Nar nar2 = Nar.LoadFromFile(fname);
        Concept c2 = nar2.concept("<a --> b>");
        assert (c2 != null);
    }
}
