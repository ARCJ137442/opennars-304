package org.opennars.perf;

import org.opennars.core.NALTest;
import org.opennars.interfaces.pub.Reasoner;
import org.opennars.main.Nar;
import org.opennars.main.Debug;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Collection;

import static org.opennars.perf.NALStressMeasure.perfNAL;

/**
 * Runs NALTestPerf continuously, for profiling
 */
public class NALPerfLoop {

    public static void main(final String[] args)
            throws IOException, InstantiationException, InvocationTargetException, NoSuchMethodException,
            ParserConfigurationException, IllegalAccessException, SAXException, ClassNotFoundException, ParseException {

        final int repeats = 2;
        final int warmups = 1;
        final int extraCycles = 2048;
        final int randomExtraCycles = 512;

        final Reasoner n = new Nar();

        final Collection<?> c = NALTest.params();
        while (true) {
            for (final Object o : c) {
                final String examplePath = (String) ((Object[]) o)[0];
                Debug.DETAILED = false;

                perfNAL(n, examplePath, extraCycles + (int) (Math.random() * randomExtraCycles), repeats, warmups,
                        true);
            }
        }
    }
}
