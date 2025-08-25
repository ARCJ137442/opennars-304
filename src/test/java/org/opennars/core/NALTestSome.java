package org.opennars.core;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * runs a subset of the test cases, selected by the boolean include(filename)
 * function
 */
@RunWith(Parameterized.class)
public class NALTestSome extends NALTest {

    static {
        showOutput = true;
        showSuccess = showOutput;
    }

    public static boolean include(final String filename) {
        // return true; //filename.startsWith("nal6.8.nal");
        return filename.startsWith("nal4");
    }

    @Parameterized.Parameters
    public static Collection params() {
        final List l = new LinkedList();

        File folder = null;
        try {
            folder = new File(NALTestSome.class.getResource("/nal/multi_step").toURI());
        } catch (final URISyntaxException e) {
            throw new IllegalStateException("Could not parse URI to nal test files.", e);
        }

        for (final File file : folder.listFiles()) {
            if (file.getName().equals("README.txt") || file.getName().contains(".png"))
                continue;
            if (include(file.getName()))
                l.add(new Object[] { file.getAbsolutePath() });
        }

        return l;
    }

    public static void main(final String[] args) {
        org.junit.runner.JUnitCore.runClasses(NALTestSome.class);
    }

    public NALTestSome(final String scriptPath) {
        super(scriptPath);// , true);

    }

}
